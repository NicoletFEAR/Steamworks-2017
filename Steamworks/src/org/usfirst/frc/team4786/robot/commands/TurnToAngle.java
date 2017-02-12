package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToAngle extends Command {

	double targetAngle;
	
    public TurnToAngle(double angle) {
        requires(Robot.driveTrain);
        
        targetAngle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.turnToAngleInit(targetAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.turnToAngleExecute();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.turnToAngleIsFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.turnToAngleEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
