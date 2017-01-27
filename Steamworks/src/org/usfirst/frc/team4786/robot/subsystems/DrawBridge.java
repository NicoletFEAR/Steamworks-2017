package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DrawBridge extends Subsystem {

	Servo gateServo = new Servo(RobotMap.bridgeServoChannel);
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    	//Probably not needed
    }
    
    public void openThyBridge(){
    	
    }
    
    public void closeThyBridge(){
    	
    }
}

