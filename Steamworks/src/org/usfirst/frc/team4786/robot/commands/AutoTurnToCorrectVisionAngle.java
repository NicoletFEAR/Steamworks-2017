package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurnToCorrectVisionAngle extends Command {

    public AutoTurnToCorrectVisionAngle() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double d3 = 8.25;
    	double dl = Robot.visionImage.getDistanceLeft();
    	double dr = Robot.visionImage.getDistanceRight();
    	double x = Math.acos((dl*dl - dr*dr -d3*d3)/(-2*dr*d3));
    	double dm2 = Math.pow(d3 / 2, 2) + dr*dr - 2 * (d3/2) * dr * Math.cos(x);
    	double dm = Math.sqrt(dm2);
    	double theta = Math.asin(((d3 / 2) * Math.sin(x)) / dm);
    	
    	double angle = theta + x;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
    }
}
