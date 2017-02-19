
package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.DoNothing;
import org.usfirst.frc.team4786.robot.commands.DriveToLeftGearPeg;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.DriveToRightGearPeg;
import org.usfirst.frc.team4786.robot.commands.GearFromOffset;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
//import org.usfirst.frc.team4786.robot.subsystems.FrameData;
import org.usfirst.frc.team4786.robot.subsystems.Intake;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;
//import org.usfirst.frc.team4786.robot.subsystems.Test;
//import org.usfirst.frc.team4786.robot.subsystems.VisionImage;
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
import java.util.concurrent.TimeUnit;

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
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {

	//Thread visionThread;
	
	//We setup our subsystem objects here

	public static DriveTrain driveTrain = new DriveTrain();
	public static Intake intake = new Intake();
	public static DrawBridge drawBridge = new DrawBridge();
	public static Gear gear = new Gear();
	public static Climber climber = new Climber();
	public static VisionImage visionImage = new VisionImage();
	public static CvSink cvSink;
	public static CvSource outputStream;
	public static CvSource regStream;
	public static UsbCamera camera;
	public static OI oi;
	public static Arduino arduino;
	public static Timer timer;
	//public static Mat output;

	Command autonomousCommand;
	Command teleopCommand;


	SendableChooser<Command> sendableChooser;
	
	@Override
	public void robotInit() {
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(RobotMap.cameraFOVWidth,RobotMap.cameraFOVHeight);
		cvSink = CameraServer.getInstance().getVideo();
		oi = new OI();
		arduino = new Arduino(RobotMap.ledArduinoPort);
		//VisionImage.putValuesToSmartDashboard();
		
		sendableChooser = new SendableChooser<Command>();
		sendableChooser.addDefault("Do Nothing!", new DoNothing());
		sendableChooser.addObject("Drive to Baseline", new DriveToPosition(10));
		sendableChooser.addObject("Drive to Center Gear Peg", new DriveToPosition(8));
		sendableChooser.addObject("Drive to Left Gear Peg", new DriveToLeftGearPeg());
		sendableChooser.addObject("Drive to Right Gear Peg", new DriveToRightGearPeg());
		sendableChooser.addObject("GetToGearTest", new GearFromOffset());
		SmartDashboard.putData("Autonomous Selector", sendableChooser);
		
		// Get the UsbCamera from CameraServer
		// Set the resolution
		
		//camera.setExposureManual(RobotMap.exposure);
					
		// Get a CvSink. This will capture Mats from the camera
		// Setup a CvSource. This will send images back to the Dashboard
		
		// Mats are very memory expensive. Lets reuse this Mat.
		/*try(MatRapper mat = new MatRapper(new Mat());){
			if (cvSink.grabFrame(mat.getMat()) == 0) {
				// Send the output the error.
				outputStream.notifyError(cvSink.getError());
				regStream.notifyError(cvSink.getError());
				// skip the rest of the current iteration
				//continue;
			}
			regStream.putFrame(mat.getMat());
			VisionImage.processImage(mat, outputStream);
			VisionImage.analysis();
			VisionImage.putValuesToSmartDashboard();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/*visionThread = new Thread(() -> {
			try(MatRapper mat = new MatRapper(new Mat());){
				// This cannot be 'true'. The program will never exit if it is. This
				// lets the robot stop this thread when restarting robot code or
				// deploying.
				while (!Thread.interrupted()) {
					//TimeUnit.SECONDS.sleep(1);
					// Tell the CvSink to grab a frame from the camera and put it
					// in the source mat.  If there is an error notify the output.
					//outputStream.putFrame(mat.getMat());
					// Put a rectangle on the image
					//Imgproc.rectangle(mat.getMat(), new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
				}
			} catch (Exception e) {
			e.printStackTrace();
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();*/
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
	//camera.setExposureManual(RobotMap.exposure);
    autonomousCommand = (Command) sendableChooser.getSelected();
    
	//autonomousCommand = new GearFromOffset();

		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("Left Encoder Positon", driveTrain.getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Positon", driveTrain.getRightEncoderPosition());
		SmartDashboard.putNumber("Servo Angle", drawBridge.getServoAngle());
	}

	@Override
	public void teleopInit() {
		camera.setExposureAuto();
		
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		//visionThread.yield();
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		teleopCommand = new OpenLoopDrive();
		
		if(teleopCommand != null)
			teleopCommand.start();
	}


	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		timer = new Timer();

		SmartDashboard.putNumber("Left Encoder Positon", driveTrain.getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Positon", driveTrain.getRightEncoderPosition());
		SmartDashboard.putNumber("Left Motor Output", driveTrain.motorOutputLeft);
		SmartDashboard.putNumber("Right Motor Output", driveTrain.motorOutputRight);
		SmartDashboard.putBoolean("Gear Present", Gear.gearLimitSwitchPressed());
		SmartDashboard.putBoolean("Peg Present", Gear.pegLimitSwitchPressed());

	}

	@Override
	public void testInit(){
	}
	

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
