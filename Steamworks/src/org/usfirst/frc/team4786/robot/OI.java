package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.SwitchFrontSide;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;



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
	public static Button kLeftJoy6Button;
	public static Button kRightJoy6Button;
	public static Button kLeftJoy11Button;
	public static Button kRightJoy11Button;
	
    public OI(){
    	//Init the objects for all the buttons, sensors, joysticks, and Xbox controllers
    	leftDriveJoy = new Joystick(0);
    	rightDriveJoy = new Joystick(1);
    	xbox = new XboxController(2);
    	kLeftJoy6Button = new JoystickButton(leftDriveJoy, 6);
    	kRightJoy6Button = new JoystickButton(rightDriveJoy, 6);
    	kLeftJoy11Button = new JoystickButton(leftDriveJoy, 11);
    	kRightJoy11Button = new JoystickButton(rightDriveJoy, 11);
    	
    	//Tie our many buttons, sensors, joysticks, and Xbox controllers to robot commands
    	    	
    	limitSwitchGear = new DigitalInput(RobotMap.limitSwitchGearPort);
    	limitSwitchPeg = new DigitalInput(RobotMap.limitSwitchPegPort);
    	
    	/*if (xbox.getAButton() == true) {
    		//would be automated gear
    	} */
    	//all buttons are only while held
    	if (xbox.getBButton() == true) {
        	Robot.climber.startOpenClimbing();
    	} else if (xbox.getBButton() == false) {
    		Robot.climber.stopClimbing();
    	}
    	
    	if (xbox.getBumper(kLeft) == true) {
    		Robot.drawBridge.openThyBridge();
    	} else if (xbox.getBumper(kLeft) == false) {
    		Robot.drawBridge.closeThyBridge();
    	}
    	
    	if (xbox.getBumper(kRight) == true) {
    		Robot.intake.collectBalls();
    	} else if (xbox.getBumper(kRight) == false) {
    		Robot.intake.stopIntaking();
    	} 
    	
    	//We map a bunch of buttons to switch the front side for Andy's convience ;)
    	kLeftJoy6Button.whileHeld(new SwitchFrontSide());
    	kRightJoy6Button.whileHeld(new SwitchFrontSide());
    	kLeftJoy11Button.whileHeld(new SwitchFrontSide());
    	kRightJoy11Button.whileHeld(new SwitchFrontSide());
    	
    	//need a button to switch cameras
    	
    	//drawbridge closes when false
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