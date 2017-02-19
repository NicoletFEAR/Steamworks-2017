package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.subsystems.*;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ArduinoLimitSwitchMonitor extends Command {
	String last = "red";
 
	public ArduinoLimitSwitchMonitor() {
        requires(Robot.gear);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//RedLight red = new RedLight();
    	//BlueLight blue = new BlueLight(); 
		
    	if(Robot.gear.pegLimitSwitchPressed() == false && !last.equals("blue")){
    		Robot.oi.getXbox().setRumble(RumbleType.kLeftRumble, 0);
    		Robot.oi.getXbox().setRumble(RumbleType.kRightRumble, 0);
    		//blue.start();
    		last = "blue";
        	Robot.arduino.writeStringData("bluelight");

      		}	
	

    	if(Robot.gear.pegLimitSwitchPressed() == true && !last.equals("red")){
    		//SmartDashboard.putString("Working", "test");
    		Robot.oi.getXbox().setRumble(RumbleType.kLeftRumble, 1);
    		Robot.oi.getXbox().setRumble(RumbleType.kRightRumble, 1);
    		last = "red";
    		//red.start();
        	Robot.arduino.writeStringData("redlight");
    		
    		
    		
        	
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
