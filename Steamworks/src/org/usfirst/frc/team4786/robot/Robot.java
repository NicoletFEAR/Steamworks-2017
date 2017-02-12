
package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;
import org.usfirst.frc.team4786.robot.subsystems.Arduino;
import org.usfirst.frc.team4786.robot.subsystems.Climber;
import org.usfirst.frc.team4786.robot.subsystems.DrawBridge;
import org.usfirst.frc.team4786.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4786.robot.subsystems.Gear;
import org.usfirst.frc.team4786.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {

	//We setup our subsystem objects here
	public static final DriveTrain driveTrain = new DriveTrain();
	public static final Intake intake = new Intake();
	public static final DrawBridge drawBridge = new DrawBridge();
	public static final Gear gear = new Gear();
	public static final Climber climber = new Climber();


	public static OI oi;
	public static Arduino arduino;

	Command autonomousCommand;
	Command teleopCommand;


	@Override
	public void robotInit() {
		oi = new OI();
		arduino = new Arduino(RobotMap.ledArduinoPort);
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
		autonomousCommand = new DriveToPosition(10);

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


	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

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
