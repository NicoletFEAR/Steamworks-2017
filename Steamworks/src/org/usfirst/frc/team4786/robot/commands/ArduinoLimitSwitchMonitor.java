package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArduinoLimitSwitchMonitor extends Command {
	boolean pressedLast = false;
	boolean defaulLast = true;
	public ArduinoLimitSwitchMonitor() {
        requires(Robot.gear);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.gear.pegLimitSwitchPressed() == true && pressedLast == false){
    		//SmartDashboard.putString("Working", "test");
    		Robot.oi.getXbox().setRumble(RumbleType.kLeftRumble, 1);
    		Robot.oi.getXbox().setRumble(RumbleType.kRightRumble, 1);
    		pressedLast = true;
    		//red.start();
        	Robot.arduino.writeStringData("redblue");
    		defaulLast = false;
    	}else if(pressedLast && Robot.gear.pegLimitSwitchPressed() == true){
    		defaulLast = false;
    	}else if(!defaulLast){
    		pressedLast = false;
    		Robot.oi.getXbox().setRumble(RumbleType.kLeftRumble, 0);
    		Robot.oi.getXbox().setRumble(RumbleType.kRightRumble, 0);
    		//send default color here'
    		defaulLast = true;
    		Robot.arduino.writeStringData(Robot.allianceColorVal);
    	}

    	
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
