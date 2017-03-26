package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SwitchFrontSide extends Command {

    public SwitchFrontSide() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	this.setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(Robot.oi == null){ end(); }
    	
    	if(Robot.frontSide.equals("Gear")){
    		Robot.frontSide = "Ball";
    	}else{
    		Robot.frontSide = "Gear";
    	}
		SmartDashboard.putString("Front Side:", Robot.frontSide);

    	//Flip direction of travel
    	Robot.driveTrain.switchFront();
    	//Flip left and right
    	Robot.oi.switchJoystickIDs();
    	

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
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
