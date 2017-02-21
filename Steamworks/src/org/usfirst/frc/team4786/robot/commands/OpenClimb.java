package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenClimb extends Command {

    public OpenClimb() {
    	//We need the only people who can climb on ice(A rope)!
    	//The ICE CLIMBERS!
        requires(Robot.climber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    //Called repeatedly when this Command is scheduled to run
    //We just start climbing!
    protected void execute() {
    	double speed = Robot.oi.getXbox().getTriggerAxis(GenericHID.Hand.kLeft);
    	Robot.climber.startOpenClimbing(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.climber.isFinishedClimbing()/* || Timer.getMatchTime() < 5.0*/;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climber.stopClimbing();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
