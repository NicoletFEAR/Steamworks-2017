package org.usfirst.frc.team4786.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//instantiate buttons, sensors, joysticks, and Xbox controllers here
	private final Joystick leftDriveJoy;
    private final Joystick rightDriveJoy;
	private final DigitalInput limitSwitchGear;
	private final DigitalInput limitSwitchPeg;
	private final XboxController xbox;
	public static GenericHID.Hand kLeft;
	public static GenericHID.Hand kRight;
	
    public OI(){
    	//Init the objects for all the buttons, sensors, joysticks, and Xbox controllers
    	leftDriveJoy = new Joystick(0);
    	rightDriveJoy = new Joystick(1);
    	xbox = new XboxController(2);
    	
    	//Tie our many buttons, sensors, joysticks, and Xbox controllers to robot commands
    	    	
    	limitSwitchGear = new DigitalInput(RobotMap.limitSwitchGearPort);
    	limitSwitchPeg = new DigitalInput(RobotMap.limitSwitchPegPort);
    	
    	/*if (xbox.getAButton() == true) {
    		//would be automated gear
    	} */
    	//all buttons are only while held
    	if (xbox.getBButton() == true) {
        	Robot.climber.startOpenClimbing();
    	}
    	
    	/*if (xbox.get?Button() == true) {
        	//stop climbing, or if drivers want to hold, it doesn't matter
    	} */
    	
    	if (xbox.getBumper(kLeft) == true) {
    		Robot.drawBridge.openThyBridge();
    	} else if (xbox.getBumper(kLeft) == false) {
    		Robot.drawBridge.closeThyBridge();
    	}
    	//need a button to switch cameras
    	
    	//drawbridge closes when false
    	
    	if (xbox.getBumper(kRight) == true) {
    		Robot.intake.collectBalls();
    		//might not be correct, we kinda just put whatever we found
    	}
    }
	
	public Joystick getLeftDriveJoy() {
	    return leftDriveJoy;
	}
	
	public Joystick getRightDriveJoy() {
	    return rightDriveJoy;
	}
	public XboxController getXbox() {
		return xbox;
	}
	// return DigitalInput value of limit switches
	public DigitalInput getGearLimitSwitch(){
		return limitSwitchGear;
	}

	public DigitalInput getPegLimitSwitch(){
		return limitSwitchPeg;
	}

}