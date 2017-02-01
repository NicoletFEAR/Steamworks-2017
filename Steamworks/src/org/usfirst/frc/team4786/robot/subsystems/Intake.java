package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

    CANTalon intakeMotor = new CANTalon(RobotMap.intakePort);

    public void initDefaultCommand() {
    	//No Default Command Needed for This
    }
    
    //Set the motor on the intake to take-in/collect balls
    public void collectBalls() {
    	intakeMotor.set(RobotMap.INTAKE_TALON_COLLECT_SPEED);
    }
    
    //Set the motor on the intake to eject/spit out balls
    public void spitBalls() {
    	intakeMotor.set(RobotMap.INTAKE_TALON_SPIT_SPEED);
    }
    
    //Just stops the motor
    public void stopIntaking() {
    	intakeMotor.set(0.0);
    }
}

