
package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.commands.OpenBridge;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4786.robot.subsystems.Intake;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.GreenLight;
import org.usfirst.frc.team4786.robot.commands.RedLight;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Gear;

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
	public static final DriveTrain driveTrain = new DriveTrain();
	public static final Intake intake = new Intake();
	public static final DrawBridge drawBridge = new DrawBridge();
	public static final Climber climber = new Climber();

	public static OI oi;
	public static Arduino arduino;

	Command autonomousCommand;
	Command teleopCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		arduino = new Arduino(RobotMap.ledArduinoPort);
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
