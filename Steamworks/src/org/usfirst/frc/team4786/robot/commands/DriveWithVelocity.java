package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveWithVelocity extends Command {

    public DriveWithVelocity() {  
    	requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//We require the driveTrain to drive, obviously!!!
    	Robot.driveTrain.driveWithVelocityInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Get Joystick Y Axis
    	double leftYStick = Robot.oi.getLeftDriveJoy().getAxis(AxisType.kY);
    	double rightYStick = Robot.oi.getRightDriveJoy().getAxis(AxisType.kY);
    	
    	double targetSpeedLeft = leftYStick * RobotMap.MAXIMUM_SPEED_VELOCITY_PID;
    	double targetSpeedRight = rightYStick * RobotMap.MAXIMUM_SPEED_VELOCITY_PID;
    	
    	Robot.driveTrain.driveWithVelocityControl(targetSpeedLeft, targetSpeedRight);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
