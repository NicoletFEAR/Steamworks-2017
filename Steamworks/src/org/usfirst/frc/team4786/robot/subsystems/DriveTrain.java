package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;

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
	
	//Set Important PID Variables
	public int rawCodesPerRev = RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV;
	public double gearBoxRatio = RobotMap.DRIVETRAIN_GEARBOX_RATIO;
	public int error = (int) (rawCodesPerRev * gearBoxRatio / RobotMap.ERROR_CONSTANT);
	
	public DriveTrain(){
		//Enable the Talons!
		frontLeft.enable();
		frontRight.enable();
		backLeft.enable();
		backRight.enable();
		
		//follower code
		backLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
		backRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		backLeft.set(RobotMap.frontLeftPort);
		backRight.set(RobotMap.frontRightPort);
		//this inverts the cantalons on the right side
		frontLeft.setInverted(true);
		
		//Beginning of the world of PID!
		frontLeft.setProfile(0);
		frontRight.setProfile(0);
		frontLeft.setPID(RobotMap.LeftP, RobotMap.LeftI, RobotMap.LeftD, RobotMap.LeftF, 0, 0, 0);		
		frontRight.setPID(RobotMap.RightP, RobotMap.RightI, RobotMap.RightD, RobotMap.RightF, 0, 0, 0);
		frontLeft.setCloseLoopRampRate(0);
		frontRight.setCloseLoopRampRate(0);
		frontLeft.setIZone(0);
		frontRight.setIZone(0);
		
		//Set Up the Encoder Revolutions!
		frontLeft.configEncoderCodesPerRev((int) (rawCodesPerRev * gearBoxRatio));
		frontRight.configEncoderCodesPerRev((int) (rawCodesPerRev * gearBoxRatio));
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		
		//Set how far the robot can be from where we want it to be without going past that amount
		frontLeft.setAllowableClosedLoopErr(error);
		frontRight.setAllowableClosedLoopErr(error);
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    	// Set the default command for a subsystem here.
        setDefaultCommand(new OpenLoopDrive());
    }
    
	public void brake(){
		//zero is to brake
    	frontRight.set(0);
    	frontLeft.set(0);
    }

	//Drive Command for Open Loop System;
	//Should be obsolete once PID is Implemented
	public void openLoopDrive(double leftInput, double rightInput) {
		double leftOutput = leftInput * RobotMap.openLoopSpeedScaling;
		double rightOutput = rightInput * RobotMap.openLoopSpeedScaling;
		frontLeft.set(leftOutput);
		frontRight.set(rightOutput);
	}
}