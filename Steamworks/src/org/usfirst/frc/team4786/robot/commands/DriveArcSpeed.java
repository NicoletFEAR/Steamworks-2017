package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveArcSpeed extends Command {

	private double leftSpeed, rightSpeed, time;
	
    public DriveArcSpeed(double leftSpeed, double rightSpeed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	this.leftSpeed = leftSpeed;
    	this.rightSpeed = rightSpeed;
    	this.time = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.driveArcSpeedInit(leftSpeed, rightSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timeSinceInitialized() > time;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.driveArcSpeedEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
