package org.usfirst.frc.team4786.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//instantiate buttons, sensors and joysticks here
	private final Joystick leftDriveJoy;
    private final Joystick rightDriveJoy;
	private final DigitalInput limitSwitchGear;
	private final DigitalInput limitSwitchPeg;
	
    public OI(){
    	leftDriveJoy = new Joystick(0);
    	rightDriveJoy = new Joystick(1);
    	limitSwitchGear = new DigitalInput(RobotMap.limitSwitchGearPort);
    	limitSwitchPeg = new DigitalInput(RobotMap.limitSwitchPegPort);
    }
	
	public Joystick getLeftDriveJoy() {
	    return leftDriveJoy;
	}
	
	public Joystick getRightDriveJoy() {
	    return rightDriveJoy;
	}
	// return DigitalInput value of limit switches
	public DigitalInput getGearLimitSwitch(){
		return limitSwitchGear;
	}

	public DigitalInput getPegLimitSwitch(){
		return limitSwitchPeg;
	}

}