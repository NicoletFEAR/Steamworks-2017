 
package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.commands.OpenBridge;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4786.robot.subsystems.FrameData;
import org.usfirst.frc.team4786.robot.subsystems.Intake;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;
import org.usfirst.frc.team4786.robot.subsystems.SwitchState;
import org.usfirst.frc.team4786.robot.subsystems.VisionImage;
import org.opencv.core.Mat;
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

	//We setup our subsystem objects here
	public static DriveTrain driveTrain = new DriveTrain();
	public static Intake intake = new Intake();
	public static DrawBridge drawBridge = new DrawBridge();
	public static Climber climber = new Climber();
	public static VisionImage visionImage = new VisionImage();
	public static FrameData frameData;
	public static CvSink cvSink;
	public static CvSource outputStream;
	int count = 0;

	public static OI oi;
	public static Arduino arduino;
	public static Mat output;

	Command autonomousCommand;
	Command teleopCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		frameData = new FrameData();
		arduino = new Arduino(RobotMap.ledArduinoPort);
		
		new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            //CameraServer.getInstance().removeCamera("USB Camera 0");
            int width = 320;//320
            int height = 240;//240
            camera.setResolution(width, height);
            //camera.setFPS(1);
            cvSink = CameraServer.getInstance().getVideo();
            outputStream = CameraServer.getInstance().putVideo("Blur", width, height);
            
            MatRapper source = new MatRapper(new Mat());
            output = new Mat();
            //Mat output2= new Mat();
            
            while(/*!Thread.interrupted()*/ true) {
            	count++;
                //SmartDashboard.putString("Is it null?", Boolean.toString(visionImage.filtered.equals(null)));
            	SmartDashboard.putNumber("Thread while loop count: ",count);
                cvSink.grabFrame(source.getMat());
                //output = source.getMat();
                Imgproc.cvtColor(source.getMat(), output, Imgproc.COLOR_BGR2RGB);
                visionImage.process(output);
                //Imgproc.cvtColor(output, output2, Imgproc.COLOR_RGB2BGR);
                //outputStream.putFrame(source.getMat());
                //source.getMat().setTo(visionImage.returnFilteredImage());
                outputStream.putFrame(source.getMat());
            }
        }).start();

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
		
		SmartDashboard.putBoolean("Found strips?", visionImage.getContoursFound());
		SmartDashboard.putNumber("Left area", visionImage.getLeftContourArea());
		SmartDashboard.putNumber("Right area", visionImage.getRightContourArea());
		SmartDashboard.putNumber("Number of targets found", visionImage.getNumberOfTargets());
		SmartDashboard.putNumber("New State", SwitchState.newState);  //These two lines are dependent of the SwitchState method
		SmartDashboard.putNumber("Old State", SwitchState.oldState);  // get rid of them if we don't have this method
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
