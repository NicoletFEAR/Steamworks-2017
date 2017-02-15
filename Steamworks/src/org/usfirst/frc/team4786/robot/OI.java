package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.SwitchFrontSide;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//instantiate buttons, sensors, joysticks, and Xbox controllers here
	private Joystick leftDriveJoy;
    private Joystick rightDriveJoy;
	private final DigitalInput limitSwitchGear;
	private final DigitalInput limitSwitchPeg;
	private final XboxController xbox;
	private static Button kLeftJoy6Button;
	private static Button kRightJoy6Button;
	private static Button kLeftJoy7Button;
	private static Button kRightJoy7Button;
	private static Button kLeftJoy10Button;
	private static Button kRightJoy10Button;
	private static Button kLeftJoy11Button;
	private static Button kRightJoy11Button;
	
    public OI(){
    	//Init the objects for all the buttons, sensors, joysticks, and Xbox controllers
    	leftDriveJoy = new Joystick(0);
    	rightDriveJoy = new Joystick(1);
    	xbox = new XboxController(2);
    	kLeftJoy6Button = new JoystickButton(leftDriveJoy, 6);
    	kRightJoy6Button = new JoystickButton(rightDriveJoy, 6);
    	kLeftJoy7Button = new JoystickButton(leftDriveJoy, 7);
    	kRightJoy7Button = new JoystickButton(rightDriveJoy, 7);
    	kLeftJoy10Button = new JoystickButton(leftDriveJoy, 10);
    	kRightJoy10Button = new JoystickButton(rightDriveJoy, 10);
    	kLeftJoy11Button = new JoystickButton(leftDriveJoy, 11);
    	kRightJoy11Button = new JoystickButton(rightDriveJoy, 11);
    	
    	//Tie our many buttons, sensors, joysticks, and Xbox controllers to robot commands
    	    	
    	limitSwitchGear = new DigitalInput(RobotMap.limitSwitchGearPort);
    	limitSwitchPeg = new DigitalInput(RobotMap.limitSwitchPegPort);
    	
    	/*if (xbox.getAButton() == true) {
    		//would be automated gear placement command in teleop, and if gotten to of course :)
    	} */
    	//all buttons are only while held
    	/*if (xbox.getBButton() == true) {
    		SmartDashboard.putNumber("B Button", 1);
        	Robot.climber.startOpenClimbing();
    	} else if (xbox.getBButton() == false) {
    		SmartDashboard.putNumber("B Button", 0);
    		Robot.climber.stopClimbing();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kLeft) == true) {
    		Robot.drawBridge.openThyBridge();
    	} else if (xbox.getBumper(GenericHID.Hand.kLeft) == false) {
    		Robot.drawBridge.closeThyBridge();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kRight) == true) {
    		Robot.intake.collectBalls();
    	} else if (xbox.getBumper(GenericHID.Hand.kRight) == false) {
    		Robot.intake.stopIntaking();
    	} */
    	
    	//We map a bunch of buttons to switch the front side for Andy's convience ;)
    	kLeftJoy6Button.whileHeld(new SwitchFrontSide());
    	kRightJoy6Button.whileHeld(new SwitchFrontSide());
    	kLeftJoy7Button.whileHeld(new SwitchFrontSide());
    	kRightJoy7Button.whileHeld(new SwitchFrontSide());
    	kLeftJoy10Button.whileHeld(new SwitchFrontSide());
    	kRightJoy10Button.whileHeld(new SwitchFrontSide());
    	kLeftJoy11Button.whileHeld(new SwitchFrontSide());
    	kRightJoy11Button.whileHeld(new SwitchFrontSide());
    	
    	//need a button to switch cameras
    	
    	//drawbridge closes when false
    }
	
    //We have this since checking the button states in the OI constructor doesn't work
    //We instead run this command within teleopPeriodic() for it to work properly
    public void checkXboxButtonStates() {
    	if (xbox.getBButton() == true) {
        	Robot.climber.startOpenClimbing();
    	} else if (xbox.getBButton() == false) {
    		Robot.climber.stopClimbing();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kLeft) == true) {
    		Robot.drawBridge.openThyBridge();
    	} else if (xbox.getBumper(GenericHID.Hand.kLeft) == false) {
    		Robot.drawBridge.closeThyBridge();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kRight) == true) {
    		Robot.intake.collectBalls();
    	} else if (xbox.getBumper(GenericHID.Hand.kRight) == false) {
    		Robot.intake.stopIntaking();
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
	
	public void switchJoystickIDs(){ 
		int temp = leftDriveJoy.getPort(); 
		leftDriveJoy = new Joystick(rightDriveJoy.getPort()); 
		rightDriveJoy = new Joystick(temp); 
	} 
}