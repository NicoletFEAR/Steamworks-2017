package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.subsystems.Gear;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArduinoLimitSwitchMonitor extends Command {

    public ArduinoLimitSwitchMonitor() {
        requires(Robot.gear);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		RedLight red = new RedLight();
		BlueLight blue = new BlueLight();


    	if(Gear.pegLimitSwitchPressed()){
    		if(blue.isRunning()){
    			blue.cancel();
    		}
    		red.start();
    	}
    	if(!Gear.pegLimitSwitchPressed()){
    		if(red.isRunning()){
    			red.cancel();
    		}
    		blue.start();
    	}
    	
    	/*if(Gear.gearLimitSwitchPressed()){
    		Robot.oi.getXbox().setRumble(RumbleType.kLeftRumble, 1);
    		Robot.oi.getXbox().setRumble(RumbleType.kRightRumble, 1);
    	}*/
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
