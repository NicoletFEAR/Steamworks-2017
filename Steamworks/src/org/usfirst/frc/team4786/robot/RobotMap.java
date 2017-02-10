package org.usfirst.frc.team4786.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//CANTalon ports, use instead of random numbers
	public static final int frontLeftPort = 13;
	public static final int frontRightPort = 14;
	
	//Our CANTalon game mech ports
	//Change these mech Talon IDs, do not know final CANTalon ids yet
	public static final int intakePort = 18;
	public static final int climbMotorPort = 16;
	public static final int bridgeServoChannel = 4;
	
	//Scaling used only for open loop drive system
	public static final double openLoopSpeedScaling = .6;
	
	//General PID Constants
	public static final int ERROR_CONSTANT = 360; //In native units
	public static final int ALLOWABLE_TURN_ERROR = 1; //In degrees
	public static final int DRIVETRAIN_ENCODER_CODES_PER_REV = 360;
	public static final double CLOSED_LOOP_RAMP_RATE = 0.015625;
	public static final int IZONE = 0;
	public static final int DRIVEBASE_PROFILE = 0;
	public static final double MAXIMUM_SPEED_VELOCITY_PID = 0.6;
	public static final double fudgeFactor = .65;
	//Wheel Radius measured in feet
	public static final double WHEEL_RADIUS = 0.25;
	//Game Mech Costants, Not final
	public static final double INTAKE_TALON_COLLECT_SPEED = 0.25;
	public static final double INTAKE_TALON_SPIT_SPEED = -0.25;
	public static final double OPEN_LOOP_CLIMBING_SPEED = 0.5;
	public static final double OPEN_BRIDGE_ANGLE = 180;
	public static final double CLOSED_BRIDGE_ANGLE = -180;
	//Left GearBox PID Constants
	public static final double LeftP = 0.118 * 2; //25% throttle within 1.5 rotations of target
	public static final double LeftI = 0.002 /*LeftP / 100000*/ ;
	public static final double LeftD = 0.0;
	public static final double LeftF = 0.0;
	//Right GearBox PID Constants
	public static final double RightP = 0.118 * 2;
	public static final double RightI = 0.002 /*RightP / 100000*/ ;
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
	public static final double TurnP = 0.025;
	public static final double TurnI = 0.006;
	public static final double TurnD = 0.04;
	public static final double TurnF = 0.0;
	//Climbing PID Constants
	public static final double ClimbingP = 0.0;
	public static final double ClimbingI = 0.0;
	public static final double ClimbingD = 0.0;
	public static final double ClimbingF = 0.0;
	public static final int ENCODER_CODES_PER_REV_CLIMBER = 360;
	public static final double CLOSED_LOOP_RAMP_RATE_CLIMBER = 0.0;
	public static final int IZONE_CLIMBER = 0;
	public static final int CLIMBER_PROFILE = 1;
	//This converts to climbing 4feet 11inches
	//We assume a quad encoder is being used, not final
	public static final int CLIMBING_DISTANCE_NATIVE_UNITS = 0; 
	
	// limit switch ports on RoboRIO DIO
	public static final int limitSwitchGearPort = 1;
	public static final int limitSwitchPegPort = 2;


	//LED arduino constants and values
	public static final int ledArduinoPort = 8;

	
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
