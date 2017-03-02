package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.OpenLoopDrive;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem implements PIDOutput {
	
	//CANTalon objects, there is a reason why they are private

	private CANTalon frontLeft = new CANTalon(RobotMap.frontLeftPort);
	private CANTalon frontRight = new CANTalon(RobotMap.frontRightPort);
	
	//NavX and PIDController objects
	private AHRS navX;
	private PIDController turnController;
	private double turnToAngleRate;
	
	//Robot will drive as if the front side is the back when reversed is true
	private boolean reversed;
	
	//Set Important PID Variables
	//public int error = (int) (rawCodesPerRev * gearBoxRatio / RobotMap.ERROR_CONSTANT);

	//Get both motor outputs
	public double motorOutputLeft = frontLeft.getOutputVoltage() / frontLeft.getBusVoltage();
	public double motorOutputRight = frontRight.getOutputVoltage() / frontRight.getBusVoltage();
	
	public DriveTrain(){
		//Enable the Talons!
		frontLeft.enable();
		frontRight.enable();
		
		frontLeft.setInverted(true);


		//Beginning of the world of PID!!!!!!!!
		
		//Set our feedback device, otherwise nothing works
		frontLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		//The encoders need to be reversed
		frontLeft.reverseSensor(true);
		frontRight.reverseSensor(true);
		
		//Beginning of the world of PID!!!!!!!!
		
		//Set Up the Encoder Revolutions!
		frontLeft.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV_LEFT);
		frontRight.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV_RIGHT);
		
		frontLeft.setPosition(0);
		frontRight.setPosition(0);
		
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		
		frontLeft.setAllowableClosedLoopErr(RobotMap.ERROR_CONSTANT_LEFT);
		frontRight.setAllowableClosedLoopErr(RobotMap.ERROR_CONSTANT_RIGHT);
		
		//Make sure the CANTalons are looking at the right stored PID values with the Profile
		//Set our PID Values
		/* Set how fast of a rate the robot will accelerate
		   Do not remove or you get a fabulous prize of a
		   Flipping robot - CLOSED_LOOP_RAMP_RATE */
		frontLeft.setPID(RobotMap.LeftP, RobotMap.LeftI, RobotMap.LeftD, RobotMap.LeftF, RobotMap.IZONE, RobotMap.CLOSED_LOOP_RAMP_RATE, RobotMap.DRIVEBASE_PROFILE);		
		frontRight.setPID(RobotMap.RightP, RobotMap.RightI, RobotMap.RightD, RobotMap.RightF, RobotMap.IZONE, RobotMap.CLOSED_LOOP_RAMP_RATE, RobotMap.DRIVEBASE_PROFILE);
		
		//Initialize NavX and turnController objects
		navX = new AHRS(SPI.Port.kMXP);
		turnController = new PIDController(RobotMap.TurnP, RobotMap.TurnI, RobotMap.TurnD, RobotMap.TurnF, navX, this);
		turnController.setInputRange(-180.0f,  180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(RobotMap.ALLOWABLE_TURN_ERROR);
		turnController.setContinuous(true);
		/* Add the PID Controller to the Test-mode dashboard, allowing manual
		 * tuning of the Turn Controller's P, I and D coefficients.
		 * Typically, only the P value needs to be modified. */
		LiveWindow.addActuator("DriveTrain", "TurnController", turnController);
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
		
		//Smartdashboard values
		SmartDashboard.putString("Front Side:", Robot.frontSide);

	}
	
	//Begin PID Functions
	
	public void driveToPositionInit(double distanceToDrive){
		//Change Talon modes to "position" just in case
		//they were in another mode before
		frontLeft.changeControlMode(TalonControlMode.Position);
		frontRight.changeControlMode(TalonControlMode.Position);
		
		//Set Encoder Position to 0
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		
		//Run convertToRotations function
		double rot = convertToRotations(distanceToDrive);
		
		//Make motors drive number of rotations
		//calculated before by convertToRotations()
		frontLeft.set(-rot);
		//Make sure we inverse this right side,
		//otherwise, you have a spinning robot on your hands
		frontRight.set(rot);
		
		SmartDashboard.putNumber("Rotations Calculated", rot);
	}
	
	public void driveArcInit(double horizontalDist, double theta){
		//Change Talon modes to "position" just in case
		//they were in another mode before
		frontLeft.changeControlMode(TalonControlMode.Position);
		frontRight.changeControlMode(TalonControlMode.Position);
		
		//Set Encoder Position to 0
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		
		//Calculate arc lengths
		theta = Math.toRadians(theta);
		double radius = horizontalDist / (1 - Math.cos(theta));
		double leftArcLength = theta * (radius + RobotMap.WHEEL_SEPARATION / 2);
		double rightArcLength = theta * (radius - RobotMap.WHEEL_SEPARATION / 2);
		if(horizontalDist < 0){
			leftArcLength *= -1;
			rightArcLength *= -1;
		}
		
		//Run convertToRotations functions
		double leftRot = convertToRotations(leftArcLength);
		double rightRot = convertToRotations(rightArcLength);
		
		//Make motors drive number of rotations
		//calculated before by convertToRotations()
		frontLeft.set(leftRot/* * RobotMap.turnFudgeFactor*/);
		//Make sure we inverse this right side,
		//otherwise, you have a spinning robot on your hands
		frontRight.set(-rightRot/* * RobotMap.turnFudgeFactor*/);
	}
	
	public void driveArcSpeedInit(double leftSpeed, double rightSpeed){
		//Change Talon modes to "position" just in case
		//they were in another mode before
		frontLeft.changeControlMode(TalonControlMode.PercentVbus);
		frontRight.changeControlMode(TalonControlMode.PercentVbus);
		//Set Encoder Position to 0
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
		
		frontLeft.set(-leftSpeed);
		frontRight.set(-rightSpeed);
	}
	
	public void driveArcSpeedEnd(){
		frontLeft.set(0);
		frontRight.set(0);
	}
	
	//Some special isFinished() command stuff to not stop before the robot has even moved

	public boolean driveToPositionIsFinished() {
		return Math.abs(frontLeft.getError()) <= RobotMap.ERROR_CONSTANT_LEFT && Math.abs(frontRight.getError()) <= RobotMap.ERROR_CONSTANT_RIGHT;
	}
	
	public void driveToPositionEnd(){
		frontLeft.setEncPosition(0);
		frontRight.setEncPosition(0);
	}
	
	public double getLeftEncoderPosition()
	{
		//Make sure graph isn't upside down (The stocks are going into the toilet!!)
		return -frontLeft.getEncPosition();
	}
	
	public double getRightEncoderPosition()
	{
		return frontRight.getEncPosition();
	}
	
	
	public void driveWithVelocityInit(){
		//Set our type of feedback device/encoder type
		frontLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		//Make sure our sensors for some reason aren't reversed
		frontLeft.reverseSensor(false);
		frontRight.reverseSensor(false);
		
		//Define ticks per rev the encoders goes by
		frontLeft.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV_LEFT);
		frontRight.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV_RIGHT);
		
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
		
		//Get both motor outputs
		motorOutputLeft = frontLeft.getOutputVoltage() / frontLeft.getBusVoltage();
		motorOutputRight = frontRight.getOutputVoltage() / frontRight.getBusVoltage();

	}
	
	//Methods called by TurnToAngle
	public void turnToAngleInit(double targetAngle){
		/* PIDController calculates a rate of motor output,
		 * so the CANTalons need to be in PercentVbus mode */
		frontLeft.changeControlMode(TalonControlMode.PercentVbus);
		frontRight.changeControlMode(TalonControlMode.PercentVbus);
		
		//Initialize turnController and set the target
		navX.reset();
		turnController.enable();
		turnController.setSetpoint(targetAngle);
	}
	
	public void turnToAngleExecute(){
		//Set the CANTalons to the speed calculated by PIDController
		frontLeft.set(-turnToAngleRate);
		frontRight.set(turnToAngleRate);
		
		SmartDashboard.putNumber("NavX Angle", navX.getAngle());
		SmartDashboard.putNumber("NavX Turn Rate", navX.getRate());
	}
	
	//Another weird variable check for if turning should stop
	public boolean turnToAngleIsFinished(){
		return turnController.onTarget();
	}
	
	public void turnToAngleEnd(){
		turnController.disable();
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
		double temp = rotationsPerSecond / (RobotMap.DRIVETRAIN_ENCODER_CODES_PER_REV_LEFT * 4);
		return temp / 10;
	}
	
	public boolean isReversed(){
		return reversed;
	}
	
	public void switchFront(){
		frontLeft.setInverted(!frontLeft.getInverted());
		frontRight.setInverted(!frontRight.getInverted());
		reversed = !reversed;
	}
	
	//Overrides PIDOutput
	@Override
	public void pidWrite(double output) {
		turnToAngleRate = output;
	}
}