package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.OpenClimb;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    private CANTalon climbTalon = new CANTalon(RobotMap.climbMotorPort);
    
    public Climber() {
    	//Enable the Climber to Climb!
    	climbTalon.enable();
    	
    	//Want to make sure if we are normally climbing, we don't climb precisely
    	//Control mode set here - instead of in startOpenClimbing() - to make sure isFinishedClimbing() runs properly
    	climbTalon.changeControlMode(TalonControlMode.PercentVbus);
    }

    public void initDefaultCommand() {
        //Not needed
    	setDefaultCommand(new OpenClimb());
    }
    
    public void startOpenClimbing(double speed) {
    	//Don't stop climbing till you fall to your doom!
    	climbTalon.set(speed * RobotMap.OPEN_LOOP_CLIMBING_SPEED_SCALING);
    }
    
    public boolean isFinishedClimbing(){
    	//The climbing limit switches are ported into DIO 3
    	//return (!Robot.oi.getClimberLimitSwitch().get());
    	return false;
    }
    
    public void stopClimbing() {
    	climbTalon.set(0.0);
    }
}

