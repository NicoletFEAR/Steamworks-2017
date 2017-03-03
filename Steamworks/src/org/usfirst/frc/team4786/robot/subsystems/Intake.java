package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.CollectBalls;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

    CANTalon intakeMotor = new CANTalon(RobotMap.intakePort);

    public void initDefaultCommand() {
    	//setDefaultCommand(new CollectBalls());
    }
    
    //Set the motor on the intake to take-in/collect balls
    public void collectBalls(double speed) {
    	intakeMotor.changeControlMode(TalonControlMode.PercentVbus);
    	intakeMotor.set(speed * RobotMap.INTAKE_COLLECT_SPEED_SCALING);
    }
    
    //Set the motor on the intake to eject/spit out balls
    public void spitBalls() {
    	intakeMotor.changeControlMode(TalonControlMode.PercentVbus);
    	intakeMotor.set(RobotMap.INTAKE_TALON_SPIT_SPEED);
    }
    
    public boolean isFinishedIntaking(){
    	return false;
    }
    
    //Just stops the motor
    public void stopIntaking() {
    	intakeMotor.set(0.0);
    }
}

