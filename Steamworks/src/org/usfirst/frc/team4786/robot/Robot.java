
package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.commands.OpenBridge;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4786.robot.subsystems.FrameData;
import org.usfirst.frc.team4786.robot.subsystems.Intake;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;
import org.usfirst.frc.team4786.robot.subsystems.Test;
import org.usfirst.frc.team4786.robot.subsystems.VisionImage;

import static org.opencv.core.Core.FILLED;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_NONE;
import static org.opencv.imgproc.Imgproc.RETR_LIST;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.findContours;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.GreenLight;
import org.usfirst.frc.team4786.robot.commands.RedLight;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Gear;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Thread visionThread;
	
	//We setup our subsystem objects here
	public static DriveTrain driveTrain = new DriveTrain();
	public static Intake intake = new Intake();
	public static DrawBridge drawBridge = new DrawBridge();
	public static Climber climber = new Climber();
	/*public static VisionImage visionImage = new VisionImage();
	public static FrameData frameData;
	public static CvSink cvSink;
	public static CvSource outputStream;
	int count = 0;*/

	public static OI oi;
	public static Arduino arduino;
	public static Mat output;

	Command autonomousCommand;
	Command teleopCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@SuppressWarnings("unused")
	@Override
	public void robotInit() {
		oi = new OI();
		arduino = new Arduino(RobotMap.ledArduinoPort);
		
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(RobotMap.cameraFOVWidth,RobotMap.cameraFOVHeight);//320, 240
			camera.setExposureManual(RobotMap.exposure);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", RobotMap.cameraFOVWidth, RobotMap.cameraFOVHeight);
			CvSource regStream = CameraServer.getInstance().putVideo("Regular", RobotMap.cameraFOVWidth, RobotMap.cameraFOVHeight);

			// Mats are very memory expensive. Lets reuse this Mat.
			
			try(MatRapper mat = new MatRapper(new Mat());)
			{
			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat.getMat()) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					regStream.notifyError(cvSink.getError());

					// skip the rest of the current iteration
					continue;
				}
				
				// Put a rectangle on the image
				//Imgproc.rectangle(mat.getMat(), new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
				//Mat output = new Mat();
				//Imgproc.GaussianBlur(mat, output, new Size(160,120), 0);
				//Imgproc.blur(mat, output, new Size(320,240));
				regStream.putFrame(mat.getMat());

				//Core.inRange(mat.getMat(), new Scalar(250,250,250), new Scalar(255,255,255), mat.getMat());

				//BGR
				Core.inRange(mat.getMat(), new Scalar(RobotMap.lowBlueValue,RobotMap.lowGreenValue,RobotMap.lowRedValue), new Scalar(RobotMap.highBlueValue,RobotMap.highGreenValue,RobotMap.highRedValue), mat.getMat());
				
				Mat hierarchy = new Mat();
        		List<MatOfPoint> contours = new ArrayList<>();
        		
        		//filtered camera feed won't show filtered objects after this - until draw contours
        		findContours(mat.getMat(), contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);
        		
        		List<MatOfPoint> filteredContours = new ArrayList<>();	//List of filtered contours
        		List<Rect> filteredContoursRect = new ArrayList<>();
        		double largestRectArea = 0;
        		double smallestRectArea = 100;
        		
        		for (MatOfPoint contour : contours)	{ //for every contour(Matrix of points) in contours
        			Rect boundingRect = boundingRect(contour);		//creates a rectangle around the contour
        			//double rectRatio = (boundingRect.height*1.0)/boundingRect.width;
        			//double ratioOfTape = 5/2;	//it will be 5/2 in competition
        			//double errorMargin = .25;
        			//filtering out false positives - contour must be taller than it is wide 
        			// & the ratio of the target's height over width must be = to the tape's height over width with a margin of error
        			if(boundingRect.width < boundingRect.height && boundingRect.area() >= RobotMap.minPixelCount) {//&& (((1 - errorMargin)*(ratioOfTape)) < rectRatio && rectRatio < (1 + errorMargin)*(ratioOfTape))) 
        				filteredContours.add(contour); 	//adds contours that fit requirements to list of contours
        				filteredContoursRect.add(boundingRect);
        				//contoursFound = true;
        				//draws filtered contours on video display
        				drawContours(mat.getMat(), filteredContours, -1, new Scalar(0xFF, 0xFF, 0xFF), FILLED);
        				if(boundingRect.area() > largestRectArea)
        					largestRectArea = boundingRect.area();
        				if(boundingRect.area() < smallestRectArea)
        					smallestRectArea = boundingRect.area();
        			}
        		}
        		//double centerY = (leftRect.y + leftRect.height * .5);
        		//Imgproc.circle(mat.getMat(), new Point(centerX,centerY), 20, new Scalar(255,255,255), 2);
        		//Imgproc.rectangle(mat.getMat(), new Point(leftRect.x,leftRect.y), new Point(rightRect.x,rightRect.y), new Scalar(255,255,255),2);
        		
				// Give the output stream a new image to display
				outputStream.putFrame(mat.getMat());
				boolean twoTargets = false;
				int numOfTargets = 0;
				Rect leftRect;
				Rect rightRect;
				double distanceToLeft;
				double distanceToRight;
				double matHeight = mat.getHeight();
				//(widthOfTarget*pixelFieldOfViewWidth)/(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
				if(filteredContours.size() >= 2){
					twoTargets = true;
				}
				numOfTargets = filteredContours.size();
				if(twoTargets){
					
					if(filteredContoursRect.get(0).x < filteredContoursRect.get(1).x){
						leftRect = filteredContoursRect.get(0);
						rightRect = filteredContoursRect.get(1);
					}else{
						leftRect = filteredContoursRect.get(1);
						rightRect = filteredContoursRect.get(0);
					}
					distanceToLeft = RobotMap.heightOfTargetInFeet * matHeight;
					distanceToLeft /= (leftRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration);
					distanceToLeft -= RobotMap.distanceOfCamFromFrontOfBot;
					/*distanceToLeft = ((RobotMap.heightOfTargetInFeet*matHeight)/
							(leftRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration))-RobotMap.distanceOfCamFromFrontOfBot;*/
					distanceToRight = ((RobotMap.heightOfTargetInFeet*matHeight)/
							(rightRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration))-RobotMap.distanceOfCamFromFrontOfBot;
					distanceToLeft += .088;
					distanceToRight += .088;
					distanceToLeft /= 1.886;
					distanceToRight /= 1.886;
	        		double centerX = .5 * ((leftRect.x + leftRect.width) + rightRect.x);
					SmartDashboard.putNumber("Left Height", leftRect.height);
					SmartDashboard.putNumber("Right Height", rightRect.height);
					SmartDashboard.putNumber("Left Width", leftRect.width);
					SmartDashboard.putNumber("Right Width", rightRect.width);
					SmartDashboard.putNumber("Distance to left Rect", distanceToLeft);
					SmartDashboard.putNumber("Distance to right Rect", distanceToRight);
				}

				SmartDashboard.putBoolean("Rectangles detected?", twoTargets);
				SmartDashboard.putNumber("Number of targets", numOfTargets);
				SmartDashboard.putNumber("Area of largest Rect", largestRectArea);
				SmartDashboard.putNumber("Area of smallest Rect", smallestRectArea);
				//SmartDashboard.putNumber("X", filteredContoursRect.get(0).x);	un-commenting this results in too many simultaneous client streams
				//SmartDashboard.putNumber("Y", filteredContoursRect.get(0).y);	un-commenting this results in too many simultaneous client streams
				
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
		
		

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		teleopCommand = new OpenLoopDrive();
		
		if(teleopCommand != null)
			teleopCommand.start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("Left Motor Output", driveTrain.motorOutputLeft);
		SmartDashboard.putNumber("Right Motor Output", driveTrain.motorOutputRight);
		SmartDashboard.putBoolean("Gear Present", Gear.gearLimitSwitchPressed());
		SmartDashboard.putBoolean("Peg Present", Gear.pegLimitSwitchPressed());
		
		
	}

	@Override
	public void testInit(){
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
