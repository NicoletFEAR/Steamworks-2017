package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    CANTalon climbTalon = new CANTalon(RobotMap.climbMotorPort);

    public void initDefaultCommand() {
        //Not needed
    }
    
    public void startClimbing() {
    	climbTalon.set(RobotMap.OPEN_LOOP_CLIMBING_SPEED);
    }
    
    public void stopClimbing() {
    	climbTalon.set(0.0);
    }
}

