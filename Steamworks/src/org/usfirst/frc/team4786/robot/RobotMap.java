
package org.usfirst.frc.team4786.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//CANTalon ports, use instead of random numbers

	/*public static final int frontLeftPort = 14;
	public static final int frontRightPort = 13;*/
	public static final int frontLeftPort = 13;
	public static final int frontRightPort = 14;
	
	//Our CANTalon game mech ports
	//Change these mech Talon IDs, do not know final CANTalon ids yet
	public static final int intakePort = 16;
	public static final int climbMotorPort = 17;

	public static final int bridgeServoChannel = 0;
	
	//Scaling used only for open loop drive system
	public static final double openLoopSpeedScaling = 1;  
	
	public static final double MESHBOT_ROBOT_LENGTH = 2.34375; //In feet
	
	//General PID Constants
	public static final int ERROR_CONSTANT_LEFT = 40; //In native units
	public static final int ERROR_CONSTANT_RIGHT = 20;
	public static final int ALLOWABLE_TURN_ERROR = 1; //In degrees
	public static final int DRIVETRAIN_ENCODER_CODES_PER_REV_LEFT = 1024;
	public static final int DRIVETRAIN_ENCODER_CODES_PER_REV_RIGHT = 512;
	/*public static final int DRIVETRAIN_ENCODER_CODES_PER_REV_LEFT = 360;
	public static final int DRIVETRAIN_ENCODER_CODES_PER_REV_RIGHT = 360;*/
	public static final double CLOSED_LOOP_RAMP_RATE = 0.015625;
	public static final int IZONE = 0;
	public static final int DRIVEBASE_PROFILE = 0;
	public static final double MAXIMUM_SPEED_VELOCITY_PID = 0.6;
	public static final double fudgeFactor = 1;
	public static final double turnFudgeFactor = 0.75;
	//Wheel Radius measured in feet
	public static final double WHEEL_RADIUS = 0.25;
	//Distance between wheels measured in feet
	public static final double WHEEL_SEPARATION = 2;
	//Game Mech Costants, Not final
	public static final double INTAKE_TALON_COLLECT_SPEED = 1;
	public static final double INTAKE_TALON_SPIT_SPEED = 1;
	public static final double INTAKE_COLLECT_SPEED_SCALING = -	1;
	public static final double INTAKE_SPIT_SPEED_SCALING = 1;
	//public static final double OPEN_LOOP_CLIMBING_SPEED = 0.5;
	public static final double OPEN_LOOP_CLIMBING_SPEED_SCALING = -.5;
	public static final double OPEN_BRIDGE_ANGLE = 90;
	public static final double CLOSED_BRIDGE_ANGLE = -90;
	//Left GearBox PID Constants
	/*
	 * For PID tuning next time:
	 * Get the P value to perpetually try to reach the target,
	 * just undershooting
	 * Then add a tiny I value to push it up a little bit
	 * If needed, add a D value to smooth it out
	 */
	//public static final double LeftP = 0.1;
	public static final double LeftP = 0.1;
	public static final double LeftI = 0.0001;
	public static final double LeftD = 0.0;
	public static final double LeftF = 0.0;
	//Right GearBox PID Constants
	//public static final double RightP = 0.1;
	public static final double RightP = 0.1;
	public static final double RightI = 0.0001;
	public static final double RightD = 0.0;
	public static final double RightF = 0.0;
	//Left GearBox Velocity PID Constants
	public static final double LeftVelocityP = 0.22;
	public static final double LeftVelocityI = 0.0;
	public static final double LeftVelocityD = 0.0;
	public static final double LeftVelocityF = 0.1097;
	//Right GearBox Velocity PID Constants
	public static final double RightVelocityP = 0.22;
	public static final double RightVelocityI = 0.0;
	public static final double RightVelocityD = 0.0;
	public static final double RightVelocityF = 0.1097;
	//NavX turn PID Constants
/*	public static final double TurnP = 0.025;
	public static final double TurnI = 0.006;
	public static final double TurnD = 0.04;
	public static final double TurnF = 0.0; */
	public static final double TurnP = 0.02;
	public static final double TurnI = 0.0;
	public static final double TurnD = 0.0;
	public static final double TurnF = 0.0;
	
	// limit switch ports on RoboRIO DIO
	public static final int limitSwitchGearPort = 5;
	public static final int limitSwitchPegPort = 2;
    public static final int limitSwitch3Port = 3;
	public static final int limitSwitch2Port = 9;
	public static final int climberLimitSwitchPort = 1;
	public static final int limitSwitch4Port = 4;


	//LED arduino constants and values
	public static final int ledArduinoPort = 8;
	
	//Vision constants
	//for filtering
	//new values: R:0, G: 196. B: 120
	public static final int highRedValue = 50;
	public static final int highGreenValue = 255;
	public static final int highBlueValue = 140;
	
	public static final int lowRedValue = 0;
	public static final int lowGreenValue = 70;
	public static final int lowBlueValue = 20;
	
	public static final int exposure = 1;
	public static final int cameraFOVHeight = 240;//former resolution 320, 240
	public static final int cameraFOVWidth = 320;
	public static final int minPixelCount = 500;
	
	//for calculating distance
	public static final double heightOfTargetInFeet = 5.0/12; 
	public static final double cameraFOVHeightInFeet = .19685;//10.6cm - 4.173228in - 0.19685ft
	public static final double distanceAtCalibration = .262467;//12.8cm - 5.03937in - 0.262467ft
	public static final double distanceOfCamFromFrontOfBot = 0;
	
	//for calculating angles
	public static final double distanceBetweenCentersOfTargets = 8.25/12;
	
    /* For example to map the left and right motors, you could define the
     following variables to use with your drivetrain subsystem.
     If you are using multiple modules, make sure to define both the port
     number and the module. 
     */
	
	/* How to determine starting P value:
	 * Select a number of revolutions before the setpoint at which the robot should begin to slow down.
	 * Choose a percentage of maximum throttle to slow down to.
	 * Find the encoder codes per rev
	 * If the encoder is a quad encoder, the formula for determining the starting P value is the following:
	 * 
	 * P = percentThrottleAsDecimal * 1023 / (4 * revolutionsBeforeSetpoint * encoderCodesPerRev)
	 * 
	 * For example, the P value for 25% throttle, 5 revolutions before the setpoint, with 400 codes per rev,
	 * the equation looks like this:
	 * 
	 * P = .25 * 1023 / (4 * 5 * 400) = 0.03196875
	 */
}
