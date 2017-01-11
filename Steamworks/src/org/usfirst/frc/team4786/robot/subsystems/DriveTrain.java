package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.Drive;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	//CANTalon objects
	CANTalon backLeft = new CANTalon(RobotMap.backLeftPort);
	CANTalon backRight = new CANTalon(RobotMap.backRightPort);
	CANTalon frontLeft = new CANTalon(RobotMap.frontLeftPort);
	CANTalon frontRight = new CANTalon(RobotMap.frontRightPort);
	
	public DriveTrain(){
		//follower code
		backLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
		backRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		backLeft.set(RobotMap.frontLeftPort);
		backRight.set(RobotMap.frontRightPort);
		frontLeft.setInverted(true);
		//this inverts the cantalons on the right side
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    	// Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }
    
	public void brake(){
		//zero is to brake
    	frontRight.set(0);
    	frontLeft.set(0);
    }


	public void drive(double leftInput, double rightInput) {
		//scales between 1 and 1
		//did this because we don't know what the maximum speed of the motors is
		double leftOutput = leftInput * RobotMap.scaling;
		double rightOutput = rightInput * RobotMap.scaling;
		frontLeft.set(leftOutput);
		frontRight.set(rightOutput);
	}
}