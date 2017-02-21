package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveArc extends Command {

	private double horizontalDist, theta;
	
	/**
	 * @param horizontalDist - Distance between start and end points measured along a line perpendicular to the starting orientation. Negative turns left, positive turns right.
	 * @param theta - Angle in degrees between start and end orientations.
	 */
    public DriveArc(double horizontalDist, double theta) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	this.horizontalDist = horizontalDist;
    	this.theta = theta;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.driveArcInit(horizontalDist, theta);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//This is called instead of a special "driveArcIsFinished()" because it will do the same thing
    	return Robot.driveTrain.driveToPositionIsFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	//This is called instead of a special "driveArcEnd()" because it will do the same thing
    	Robot.driveTrain.driveToPositionEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
