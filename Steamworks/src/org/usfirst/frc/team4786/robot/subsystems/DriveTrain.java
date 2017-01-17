package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	//CANTalon objects, there is a reason why they are private
	private CANTalon backLeft = new CANTalon(RobotMap.backLeftPort);
	private CANTalon backRight = new CANTalon(RobotMap.backRightPort);
	private CANTalon frontLeft = new CANTalon(RobotMap.frontLeftPort);
	private CANTalon frontRight = new CANTalon(RobotMap.frontRightPort);
	
	//Set Important PID Variables
	//public int error = (int) (rawCodesPerRev * gearBoxRatio / RobotMap.ERROR_CONSTANT);
	
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
		
		//Make sure the CANTalons are looking at the right stored PID values
		frontLeft.setProfile(RobotMap.DRIVEBASE_PROFILE);
		frontRight.setProfile(RobotMap.DRIVEBASE_PROFILE);
		//Set our PID Values
		frontLeft.setPID(RobotMap.LeftP, RobotMap.LeftI, RobotMap.LeftD, RobotMap.LeftF, 0, 0, 0);		
		frontRight.setPID(RobotMap.RightP, RobotMap.RightI, RobotMap.RightD, RobotMap.RightF, 0, 0, 0);
		/*
		  Set how fast of a rate the robot will accelerate
		  Do not remove or you get a fabulous prize of a
		  Flipping robot
		*/
		frontLeft.setCloseLoopRampRate(RobotMap.CLOSED_LOOP_RAMP_RATE);
		frontRight.setCloseLoopRampRate(RobotMap.CLOSED_LOOP_RAMP_RATE);
		frontLeft.setIZone(RobotMap.IZONE);
		frontRight.setIZone(RobotMap.IZONE);
		//Set Up the Encoder Revolutions!
		frontLeft.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV);
		frontRight.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV);
		//Set Encoder Position to 0
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
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
		//Change Talon modes to PercentVbus
		frontLeft.changeControlMode(TalonControlMode.PercentVbus);
		frontRight.changeControlMode(TalonControlMode.PercentVbus);
		
		double leftOutput = leftInput * RobotMap.openLoopSpeedScaling;
		double rightOutput = rightInput * RobotMap.openLoopSpeedScaling;
		frontLeft.set(leftOutput);
		frontRight.set(rightOutput);
	}
	
	//Begin PID Functions
	
	public void driveToPosition(double distanceToDrive){
		//Run convertToRotations function
		double rot = convertToRotations(distanceToDrive);
		
		//Change Talon modes to "position" just in case
		//they were in another mode before
		frontLeft.changeControlMode(TalonControlMode.Position);
		frontRight.changeControlMode(TalonControlMode.Position);
		
		//Make motors drive number of rotations
		//calculated before by convertToRotations()
		frontLeft.set(rot);
		//Make sure we inverse this right side,
		//otherwise, you have a spinning robot on your hands
		frontRight.set(-rot);
		
		SmartDashboard.putNumber("Position", frontLeft.getPosition());
	}
	
	//Take a distance in feet and convert to
	//rotations that CANTalons can take as input
	private double convertToRotations(double distanceInFeet){
		return (distanceInFeet)/(Math.PI * (RobotMap.WHEEL_RADIUS * 2));
	}
}