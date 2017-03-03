package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenBridge extends Command {

    public OpenBridge() {
    	//We need a "thy bridge" to open "thy bridge"
    	requires(Robot.drawBridge);
    }

    // Called just before this Command runs the first time
    //Open Bridge!!!!
    protected void initialize() {
    	Robot.drawBridge.openThyBridge();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    //Make sure the servo has moved all the way to where it needs to go before finishing
    protected boolean isFinished() {
        return (Robot.drawBridge.getServoAngle() == RobotMap.OPEN_BRIDGE_ANGLE)/* || this.timeSinceInitialized() >= 5*/;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
