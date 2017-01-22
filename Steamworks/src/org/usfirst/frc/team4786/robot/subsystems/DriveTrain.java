package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI.Port;
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

		//Beginning of the world of PID!!!!!!!!
		
		//Make sure the CANTalons are looking at the right stored PID values
		frontLeft.setProfile(RobotMap.DRIVEBASE_PROFILE);
		frontRight.setProfile(RobotMap.DRIVEBASE_PROFILE);
		//Set our PID Values
		frontLeft.setPID(RobotMap.LeftP, RobotMap.LeftI, RobotMap.LeftD, RobotMap.LeftF, 0, 0, 0);		
		frontRight.setPID(RobotMap.RightP, RobotMap.RightI, RobotMap.RightD, RobotMap.RightF, 0, 0, 0);
		
		/* Set how fast of a rate the robot will accelerate
		   Do not remove or you get a fabulous prize of a
		   Flipping robot */
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
	
	public void driveWithVelocityInit(){
		//Set our type of feedback device/encoder type
		frontLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		//Make sure our sensors for some reason aren't reversed
		frontLeft.reverseSensor(false);
		frontRight.reverseSensor(false);
		
		//Define ticks per rev the encoders goes by
		frontLeft.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV);
		frontRight.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV);
		
		//Set Our Mode to Speed!
		frontLeft.changeControlMode(TalonControlMode.Speed);
		frontRight.changeControlMode(TalonControlMode.Speed);
		
		/* set the peak and nominal outputs, 12V means full */
		frontLeft.configNominalOutputVoltage(+0.0f, -0.0f);
		frontRight.configNominalOutputVoltage(+0.0f, -0.0f);
		
		frontLeft.configPeakOutputVoltage(+12.0f, 0.0f);
		frontRight.configPeakOutputVoltage(+12.0f, 0.0f);
		
		//Set the all famous PID Values into profile 1 on encoders	
		frontLeft.setPID(RobotMap.LeftVelocityP, RobotMap.LeftVelocityI, RobotMap.LeftVelocityD, RobotMap.LeftVelocityF, RobotMap.IZONE, RobotMap.CLOSED_LOOP_RAMP_RATE, RobotMap.DRIVEBASE_PROFILE);
		frontRight.setPID(RobotMap.RightVelocityP, RobotMap.RightVelocityI, RobotMap.RightVelocityD, RobotMap.RightVelocityF, RobotMap.IZONE, RobotMap.CLOSED_LOOP_RAMP_RATE, RobotMap.DRIVEBASE_PROFILE);
	}
	
	//Connects to a Function within the DriveWithVelocity command
	public void driveWithVelocityControl(double targetSpeedLeft, double targetSpeedRight){
		//Set the front motors' speed
		frontLeft.set(targetSpeedLeft);
		frontRight.set(targetSpeedRight);
		
		//Get both motor outputs and display them on SmartDashboard
		double motorOutputLeft = frontLeft.getOutputVoltage() / frontLeft.getBusVoltage();
		double motorOutputRight = frontRight.getOutputVoltage() / frontRight.getBusVoltage();
		
		SmartDashboard.putNumber("Left Motor Output", motorOutputLeft);
		SmartDashboard.putNumber("Right Motor Output", motorOutputRight);
	}
	
	//Take a distance in feet and convert to
	//rotations that CANTalons can take as input
	private double convertToRotations(double distanceInFeet){
		return (distanceInFeet)/(Math.PI * (RobotMap.WHEEL_RADIUS * 2));
	}
	
	//Take a speed in feet per second, converts to
	//native units per 100 milliseconds
	//Possibly a non-needed method
	private double convertToVelocity(double feetPerSecond){
		double rotationsPerSecond = convertToRotations(feetPerSecond);
		double temp = rotationsPerSecond / (RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV * 4);
		return temp / 10;
	}
}