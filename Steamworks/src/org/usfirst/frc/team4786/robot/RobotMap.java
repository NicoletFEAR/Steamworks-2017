package org.usfirst.frc.team4786.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//CANTalon ports, use instead of random numbers
	//Not Final at ALL!!!
	public static final int backLeftPort = 14;
	public static final int backRightPort = 13;
	public static final int frontLeftPort = 15;
	public static final int frontRightPort = 12;
	
	//Scaling used only for open loop drive system
	public static final double openLoopSpeedScaling = .6;
	
	//General PID Constants
	//public static final double ERROR_CONSTANT = 45;
	public static final int DRIVETRAIN_ENCODER_CODES_PER_REV = 360;
	//Wheel Radius measured in feet
	public static final double WHEEL_RADIUS = 0.25;
	//Left GearBox PID Constants
	public static final double LeftP = 0.355 / 7; //35% throttle within 5 rotations of target
	public static final double LeftI = RobotMap.LeftP / 100000;
	public static final double LeftD = 0.0;
	public static final double LeftF = 0.0;
	//Right GearBox PID Constants
	public static final double RightP = 0.355 / 7;
	public static final double RightI = RobotMap.RightP / 100000;
	public static final double RightD = 0.0;
	public static final double RightF = 0.0;
	
    /* For example to map the left and right motors, you could define the
     following variables to use with your drivetrain subsystem.
     If you are using multiple modules, make sure to define both the port
     number and the module. 
     
     */
}
