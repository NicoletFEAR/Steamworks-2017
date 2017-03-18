package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.DoNothing;
import org.usfirst.frc.team4786.robot.commands.DriveToLeftGearPeg;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.DriveToRightGearPeg;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.commands.ProcessVisionContinuously;
import org.usfirst.frc.team4786.robot.commands.VisionAlignWithPeg;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4786.robot.subsystems.Gear;
//import org.usfirst.frc.team4786.robot.subsystems.FrameData;
import org.usfirst.frc.team4786.robot.subsystems.Intake;
import org.usfirst.frc.team4786.robot.subsystems.SwitchState;
//import org.usfirst.frc.team4786.robot.subsystems.Test;
//import org.usfirst.frc.team4786.robot.subsystems.VisionImage;
import org.usfirst.frc.team4786.robot.subsystems.VisionImage;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {

	//Thread visionThread;
	
	//We setup our subsystem objects here

	public static String frontSide;
	
	public static DriveTrain driveTrain = new DriveTrain();
	public static Intake intake = new Intake();
	public static DrawBridge drawBridge = new DrawBridge();
	public static Gear gear = new Gear();
	public static Climber climber = new Climber();

	public static VisionImage visionImage = new VisionImage();
	public static CvSink cvSink;
	public static CvSource outputStream;
	public static CvSource regStream;
	public static CvSource visionStream;
	public static UsbCamera gearPlacementCamera;
	public static UsbCamera ballPlacementCamera;

    public static SwitchState switchState = new SwitchState();


	public static OI oi;
	public static Arduino arduino;
	public static Timer timer;
	//public static Mat output;

	Command autonomousCommand;
	Command teleopCommand;

	SendableChooser<Command> sendableChooser;
	
	public static DriverStation.Alliance alliance;
	public static String allianceColorVal = "";

	
	@Override
	public void robotInit() {
    	System.out.println("ERROR: RUSSIAN HACKING ATTEMPT DETECTED");
		gearPlacementCamera = CameraServer.getInstance().startAutomaticCapture("Gear", 0);
		ballPlacementCamera = CameraServer.getInstance().startAutomaticCapture("Ball Camera", 1);
		gearPlacementCamera.setFPS(15);		
		ballPlacementCamera.setFPS(15);

		gearPlacementCamera.setResolution(RobotMap.cameraFOVWidth,RobotMap.cameraFOVHeight);
		ballPlacementCamera.setResolution(RobotMap.cameraFOVWidth, RobotMap.cameraFOVHeight);
		gearPlacementCamera.setExposureManual(1);
		
		cvSink = CameraServer.getInstance().getVideo("Gear");
		//outputStream = CameraServer.getInstance().putVideo("Gear Camera", RobotMap.cameraFOVWidth, RobotMap.cameraFOVHeight);
		//= new CvSource("GearCamera", VideoMode.PixelFormat.kRGB565, RobotMap.cameraFOVWidth, RobotMap.cameraFOVHeight, 15);
		
		
		frontSide = "Gear";

		oi = new OI();
		arduino = new Arduino(RobotMap.ledArduinoPort);
		


		sendableChooser = new SendableChooser<Command>();
		sendableChooser.addDefault("Drive to Center Gear Peg", new DriveToPosition(4.83));
		sendableChooser.addObject("Do Nothing!", new DoNothing());
		sendableChooser.addObject("Drive to Baseline", new DriveToPosition(10));
		sendableChooser.addObject("Drive to Left Gear Peg", new DriveToLeftGearPeg());
		sendableChooser.addObject("Drive to Right Gear Peg", new DriveToRightGearPeg());
		sendableChooser.addObject("Vision align with peg", new VisionAlignWithPeg());
		SmartDashboard.putData("Autonomous Selector", sendableChooser);
		
	}
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		/*alliance = DriverStation.getInstance().getAlliance();
		//send correct alliance data to arduino
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
		SmartDashboard.putString("Alliance", allianceColorVal);*/
		
		

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		
		alliance = DriverStation.getInstance().getAlliance();

		//send correct alliance data to arduino
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
	    Command visionProcess = new ProcessVisionContinuously();
	    //visionProcess.start();
	    
		if (autonomousCommand != null)
			autonomousCommand.start();
	}
    
	//autonomousCommand = new GearFromOffset();

    //autonomousCommand = new DriveToLeftGearPeg();
	//	if (autonomousCommand != null)
	//		autonomousCommand.start();
	//}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
		SmartDashboard.putNumber("Left Encoder Positon", driveTrain.getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Positon", driveTrain.getRightEncoderPosition());
	}

	@Override
	public void teleopInit() {

    	System.out.println("ERROR: RUSSIAN HACKING ATTEMPT DETECTED");
    	
		gearPlacementCamera.setExposureAuto();
		
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		//visionThread.yield();
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		Robot.driveTrain.switchFront();
    	Robot.oi.switchJoystickIDs();
		
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
		SmartDashboard.putBoolean("Peg Present", gear.pegLimitSwitchPressed());
		SmartDashboard.putNumber("New State", SwitchState.newState);  //These two lines are dependent of the SwitchState method
		SmartDashboard.putNumber("Old State", SwitchState.oldState);  // get rid of them if we don't have this method
		SmartDashboard.putBoolean("Reversed", driveTrain.isReversed());
	}

	@Override
	public void testInit(){
		//everything is awesome code
		//runs motors and output
		Robot.driveTrain.openLoopDrive(1, 1);
		Robot.intake.collectBalls(1);
		Robot.climber.startOpenClimbing(1);
		Robot.arduino.writeStringData("rainbowlight");
	}
	

	@Override
	public void testPeriodic() {
		LiveWindow.run();
		/*Robot.driveTrain.openLoopDrive(1, 1);
		Robot.intake.collectBalls(1);
		Robot.climber.startOpenClimbing(1);*/

		
		/*Command visionTest = new VisionSetup();
		visionTest.start();*/
		

	}
}
