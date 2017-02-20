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
import org.usfirst.frc.team4786.robot.subsystems.SwitchState;

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
import org.usfirst.frc.team4786.robot.commands.VisionSetup;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Gear;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
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

    public static SwitchState switchState = new SwitchState();


	public static OI oi;
	public static Arduino arduino;
	public static Timer timer;
	//public static Mat output;

	Command autonomousCommand;
	Command teleopCommand;

	SendableChooser<Command> sendableChooser;
	
	DriverStation.Alliance alliance;
	
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
		sendableChooser.addObject("Drive to Baseline", new DriveToPosition(6));
		sendableChooser.addObject("Drive to Center Gear Peg", new DriveToPosition(4));
		sendableChooser.addObject("Drive to Left Gear Peg", new DriveToLeftGearPeg());
		sendableChooser.addObject("Drive to Right Gear Peg", new DriveToRightGearPeg());
		//sendableChooser.addObject("GetToGearTest", new GearFromOffset());
		SmartDashboard.putData("Autonomous Selector", sendableChooser);
		

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		alliance = DriverStation.getInstance().getAlliance();

		//send correct alliance data to arduino
		String allianceColorVal;
		if(alliance.toString().equalsIgnoreCase("blue")){
			allianceColorVal = "bluelight";
			arduino.writeStringData(allianceColorVal);
		}else if(alliance.toString().equalsIgnoreCase("red")){
			allianceColorVal = "redlight";
			arduino.writeStringData(allianceColorVal);
		}else{
			allianceColorVal = "purplelight";
			arduino.writeStringData(allianceColorVal);
		}
		SmartDashboard.putString("Alliance", allianceColorVal);


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
    switchState.switchChange();
		Scheduler.getInstance().run();
		timer = new Timer();

		SmartDashboard.putNumber("Left Encoder Positon", driveTrain.getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Positon", driveTrain.getRightEncoderPosition());
		SmartDashboard.putNumber("Left Motor Output", driveTrain.motorOutputLeft);
		SmartDashboard.putNumber("Right Motor Output", driveTrain.motorOutputRight);
		SmartDashboard.putBoolean("Gear Present", gear.gearLimitSwitchPressed());
		SmartDashboard.putBoolean("Peg Present", gear.pegLimitSwitchPressed());
    SmartDashboard.putNumber("New State", SwitchState.newState);  //These two lines are dependent of the SwitchState method
		SmartDashboard.putNumber("Old State", SwitchState.oldState);  // get rid of them if we don't have this method

	}

	@Override
	public void testInit(){
		//runs motors and output
		Robot.driveTrain.openLoopDrive(1, 1);
		//Robot.intake.collectBalls();
		Robot.arduino.writeStringData("rainbowlight");
	}
	

	@Override
	public void testPeriodic() {
		LiveWindow.run();
		

		
		//shows smartdashboard values
		/*Command visionTest = new VisionSetup();
		visionTest.start();*/
		

	}
}
