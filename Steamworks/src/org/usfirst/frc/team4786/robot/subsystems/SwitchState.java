package org.usfirst.frc.team4786.robot.subsystems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Tracks current and last button state. And will take actions based on the
 * transition from one state to another The code will be setup for 4
 * buttons/limit switches and be stored in a single integer (0 - 15) that will
 * then be combined with the stored integer (also 0 - 15) of the previous state
 * to make a combined "transitiony state thingy" the integer for 4 buttons will
 * range from 0 (none pressed) to 15 (all 4 four pressed) Each button being
 * pressed or released will alter the state value by its number (1 to 4) to the
 * power of 2 i.e
 * b4, b3, b2, b1  = state 
 * f   f   f   f   =   0
 * f   f   f   T   =   1
 * f   f   T   f   =   2
 * f   f   T   T   =   3 
 * f   T   f   f   =   4
 * f   T   f   T   =   5
 * f   T   T   f   =   6 
 * f   T   T   T   =   7 
 * T   f   f   f   =   8 
 * T   f   f   T   =   9 
 * T   f   T   f   =   10 
 * T   f   T   T   =   11 
 * T   T   f   f   =   12 
 * T   T   f   T   =   13 
 * T   T   T   f   =   14 
 * T   T   T   T   =   15
 * The code will support specific actions based on either the new state alone or the combined "transitiony state thingy"
 * 
 */
public class SwitchState extends Subsystem {

	public static int newState = 0;
	public static int oldState = 0;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void switchChange() {
		oldState = newState;
		newState = setNewState();
		
		if (oldState == 1 && newState == 3){
	    	Robot.arduino.writeStringData("teallight");
	    	return;			
		}
		if (oldState == 2 && newState == 3){ // pressing just 2 then 2 + 1
	    	Robot.arduino.writeStringData("heartbeat");
	    	return;			
		}
        if (newState == 15){
	    	Robot.arduino.writeStringData("rainbowlight");
	    	return;
		}
		if (newState == 1 | newState == 3 | newState == 5 | newState == 7 | newState == 9 | newState == 11 | newState == 13 | newState == 15) {
	    	Robot.arduino.writeStringData("bluelight");
	    	return;
		}
        if (newState > 7){
	    	Robot.arduino.writeStringData("redlight");
	    	return;
		}
        if (newState == 2 | newState == 6 | newState == 10){
	    	Robot.arduino.writeStringData("purplelight");
	    	return;
		}

	}

	public int setNewState() {
		int calcState = 0;
		if (Robot.oi.getLimit1Switch().get()) {
			calcState = calcState +1;
		}
		if (Robot.oi.getLimit2Switch().get()) {
			calcState = calcState +2;
		}
		if (Robot.oi.getLimit3Switch().get()) {
			calcState = calcState +4;
		}
		if (Robot.oi.getLimit4Switch().get()) {
			calcState = calcState +8;
		}
		return calcState;
	}
}
