package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {

    CANTalon climbTalon = new CANTalon(RobotMap.climbMotorPort);
    
    public Climber() {
    	//Enable the Climber to Climb!
    	climbTalon.enable();
    	
    	//Set all of the PID Info, geese that is a lot!
    	climbTalon.setPID(RobotMap.ClimbingP, RobotMap.ClimbingI, RobotMap.ClimbingD, RobotMap.ClimbingF, RobotMap.IZONE_CLIMBER, RobotMap.CLOSED_LOOP_RAMP_RATE_CLIMBER, RobotMap.CLIMBER_PROFILE);
    	
    	//Reset our Encoder Positions
    	climbTalon.setEncPosition(0);
    }

    public void initDefaultCommand() {
        //Not needed
    }
    
    public void startOpenClimbing() {
    	//Want to make sure if we are normally climbing, we don't climb precisely
    	climbTalon.changeControlMode(TalonControlMode.PercentVbus);
    	
    	//Don't stop climbing till you fall to your doom!
    	climbTalon.set(RobotMap.OPEN_LOOP_CLIMBING_SPEED);
    }
    
    public void startPIDClimbing() {
    	//Want to make sure we are actually in a PID compatible mode for the CANTalon
    	climbTalon.changeControlMode(TalonControlMode.Position);
    	
    	//Climb distance comes directly from RobotMap in Native Units
    	//Assuming this value won't need to change if our rope stays the same
    	climbTalon.set(RobotMap.CLIMBING_DISTANCE_NATIVE_UNITS);
    	
    	//For testing purposes, we put the encoder position to SmartDashboard
    	SmartDashboard.putNumber("ClimbTalon Position", climbTalon.getPosition());
    }
    
    public void stopClimbing() {
    	climbTalon.set(0.0);
    }
}

