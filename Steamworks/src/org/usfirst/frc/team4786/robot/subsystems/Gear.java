package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.commands.ArduinoLimitSwitchMonitor;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Gear extends Subsystem {

	DigitalInput limitSwitchGear;
	DigitalInput limitSwitchPeg;

	public Gear() {
	}
    public void initDefaultCommand() {
        setDefaultCommand(new ArduinoLimitSwitchMonitor());
    }
    
    public boolean gearLimitSwitchPressed(){
    	return !Robot.oi.getGearLimitSwitch().get();
    }
    //returns true when pressed; must invert logic
    
    public boolean pegLimitSwitchPressed(){
    	return !Robot.oi.getPegLimitSwitch().get();

    //returns true when pressed; must invert logic
    }	
}    




