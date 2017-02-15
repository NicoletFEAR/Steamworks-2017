package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToPosition extends Command {

	private double targetPosition;
	
    public DriveToPosition(double distance) {
    	//We require the driveTrain to drive, obviously!!!!
    	requires(Robot.driveTrain);
    	
    	//So we can use this variable in the execute() function
    	targetPosition = distance * RobotMap.fudgeFactor;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Let's drive!
    	Robot.driveTrain.driveToPositionInit(targetPosition);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.driveToPositionIsFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.driveToPositionEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
