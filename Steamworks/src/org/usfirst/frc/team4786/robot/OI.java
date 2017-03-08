package org.usfirst.frc.team4786.robot;

import org.usfirst.frc.team4786.robot.commands.CloseBridge;
import org.usfirst.frc.team4786.robot.commands.CollectBalls;
import org.usfirst.frc.team4786.robot.commands.OpenBridge;
import org.usfirst.frc.team4786.robot.commands.OpenClimb;
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
	private Joystick leftDriveJoy;
    private Joystick rightDriveJoy;
    private final XboxController xbox;
	private final DigitalInput limitSwitchGear;
	private final DigitalInput limitSwitchPeg;
	private final DigitalInput limitSwitch1;
	private final DigitalInput limitSwitch2;
	private final DigitalInput limitSwitch3;
	private final DigitalInput limitSwitch4;
	private static Button kLeftJoy6Button;
	private static Button kRightJoy6Button;
	private static Button kLeftJoy7Button;
	private static Button kRightJoy7Button;
	private static Button kLeftJoy10Button;
	private static Button kRightJoy10Button;
	private static Button kLeftJoy11Button;
	private static Button kRightJoy11Button;
	private static Button leftJoystickTrigger;
	private static Button rightJoystickTrigger;

	
	private static Button rightBumper;
	private static Button leftBumper;


    public OI(){
    	//Init the objects for all the buttons, sensors, joysticks, and Xbox controllers
    	leftDriveJoy = new Joystick(0);
    	rightDriveJoy = new Joystick(1);
        xbox = new XboxController(2);
        leftJoystickTrigger = new JoystickButton(leftDriveJoy, 1);
    	rightJoystickTrigger = new JoystickButton(rightDriveJoy, 1);
     	kLeftJoy6Button = new JoystickButton(leftDriveJoy, 6);
    	kRightJoy6Button = new JoystickButton(rightDriveJoy, 6);
    	kLeftJoy7Button = new JoystickButton(leftDriveJoy, 7);
    	kRightJoy7Button = new JoystickButton(rightDriveJoy, 7);
    	kLeftJoy10Button = new JoystickButton(leftDriveJoy, 10);
    	kRightJoy10Button = new JoystickButton(rightDriveJoy, 10);
    	kLeftJoy11Button = new JoystickButton(leftDriveJoy, 11);
    	kRightJoy11Button = new JoystickButton(rightDriveJoy, 11);
    	  
    	rightBumper = new JoystickButton(xbox, Buttons.RightBump);
    	leftBumper = new JoystickButton(xbox, Buttons.LeftBump);

      //Tie our many buttons, sensors, joysticks, and Xbox controllers to robot commands
    		    	
    	limitSwitchGear = new DigitalInput(RobotMap.limitSwitchGearPort);
    	limitSwitchPeg = new DigitalInput(RobotMap.limitSwitchPegPort);
    	limitSwitch1 = new DigitalInput(RobotMap.limitSwitch3Port);
    	limitSwitch2 = new DigitalInput(RobotMap.limitSwitch2Port);
    	limitSwitch3 = new DigitalInput(RobotMap.climberLimitSwitchPort);
    	limitSwitch4 = new DigitalInput(RobotMap.limitSwitch4Port);
    
   	//We map a bunch of buttons to switch the front side for Andy's convenience ;)
    	rightJoystickTrigger.whenPressed(new SwitchFrontSide());
    	leftJoystickTrigger.whenPressed(new SwitchFrontSide());
    	kLeftJoy6Button.whenPressed(new SwitchFrontSide());
    	kRightJoy6Button.whenPressed(new SwitchFrontSide());
    	kLeftJoy7Button.whenPressed(new SwitchFrontSide());
    	kRightJoy7Button.whenPressed(new SwitchFrontSide());
    	kLeftJoy10Button.whenPressed(new SwitchFrontSide());
    	kRightJoy10Button.whenPressed(new SwitchFrontSide());
    	kLeftJoy11Button.whenPressed(new SwitchFrontSide());
    	kRightJoy11Button.whenPressed(new SwitchFrontSide());
    	
     
    	//rightBumper.whenPressed(new OpenBridge());
    	//leftBumper.whenPressed(new CloseBridge());
    }
    /*public void checkXboxButtonStates() {
    	if (xbox.getBButton() && Robot.climber.getCurrentCommand() == null) {
        	new OpenClimb().start();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kLeft) && Robot.drawBridge.getCurrentCommand().getName() == "OpenBridge") {
    		new OpenBridge().start();
    	} else if (!xbox.getBumper(GenericHID.Hand.kLeft) && Robot.drawBridge.getCurrentCommand().getName() == "CloseBridge") {
    		new CloseBridge().start();
    	}
    	
    	if (xbox.getBumper(GenericHID.Hand.kRight) && Robot.intake.getCurrentCommand() == null) {
    		new CollectBalls().start();
    	}
    }*/
 
  
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
//	public DigitalInput getLimit1Switch(){
//		return limitSwitch1;
//	}
	public DigitalInput getLimit3Switch(){
		return limitSwitch2;
	}
	public DigitalInput getClimberLimitSwitch(){
		return limitSwitch3;
	}
	public DigitalInput getLimit4Switch(){
		return limitSwitch4;
	}
  	public XboxController getXbox() {
		return xbox;
	}
  
    public void switchJoystickIDs(){
		int temp = leftDriveJoy.getPort();
		leftDriveJoy = new Joystick(rightDriveJoy.getPort());
		rightDriveJoy = new Joystick(temp);
	}

}