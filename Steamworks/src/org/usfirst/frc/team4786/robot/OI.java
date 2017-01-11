package org.usfirst.frc.team4786.robot;

import edu.wpi.first.wpilibj.Joystick;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//instantiate buttons, sensors and joysticks here
	public Joystick left0;
    public Joystick right1;
    public static Joystick gameMech;
    
    public OI(){
    	left0 = new Joystick(0);
    	right1 = new Joystick(1);
    }


public Joystick getLeft() {
    return left0;
}

public Joystick getRight() {
    return right1;
}

}