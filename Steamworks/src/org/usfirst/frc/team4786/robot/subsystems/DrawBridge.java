package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DrawBridge extends Subsystem {
	/*  Init our Servo on PWM Port 4 on the RoboRio */
	
	/*  Note: The Port on PWM can be change by changing the "bridgeServoChannel" 
	    value in the RobotMap Class                                                 */
	
	/*  Our Servo Connector is plugged in with the black cable lined up with the ground symbol on RoboRio
	    and the yellow cable lined up with the "S" on the RoboRio  */
	private Servo gateServo = new Servo(RobotMap.bridgeServoChannel);

    public void initDefaultCommand() {
    	//Not needed, called from elsewhere
    }
    
    public void openThyBridge() {
    	gateServo.setAngle(90);
    }
    
    public void closeThyBridge() {
    	gateServo.setAngle(-90);
    }
    
    public double getServoAngle(){
    	return gateServo.getAngle();
    }
}

