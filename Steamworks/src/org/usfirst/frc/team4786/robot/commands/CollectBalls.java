package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CollectBalls extends Command {

    public CollectBalls() {
        //We require the Intake mech so we can take in these balls
    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = Robot.oi.getXbox().getTriggerAxis(GenericHID.Hand.kRight);
    	Robot.intake.collectBalls(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.intake.isFinishedIntaking();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.stopIntaking();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
