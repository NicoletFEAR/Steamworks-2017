package org.usfirst.frc.team4786.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//CANTalon ports
	// use instead of magic numbers
	//are final ports
	
	public static final int backLeftPort = 15;
	public static final int backRightPort = 12;
	public static final int frontLeftPort = 14;
	public static final int frontRightPort = 13;
	public static final int intakeMotorPort = 2;
	public static final int positionMotorPort = 3;
	public static final int limitSwitchTopPort = 8;
	public static final int limitSwitchBottomPort = 7;
	public static final int limitSwitchIntakePort = 6;
	
	//game mech constants
	//use instead of hard-coded values
	public static final double armSpeed = .5;
	public static final double intakeSpeed = -1;
	public static final double passSpeed = .75;
	public static final double shootSpeed = 1;
	public static final double raisePortcullisSpeed = 1;

	//autonomous constants
	//use instead of hard-coded numbers
	public static final double lowBarSpeed = 0.6;
	public static final double moatSpeed = 0.6;
	public static final double rampartsSpeed = 0.5;
	public static final double rockyTerrainSpeed = 0.7;
	public static final double rockWallSpeed = 0.6;
	public static final double autoSpeed = 0.5;
	
	public static final double autoTimer = 5;
	public static final double moatTimer = 4;
	public static final double rockWallTimer = 3;
	public static final double rockyTerrainTimer = 4;
	public static final double rampartsTimer = 3;
	public static final double lowBarTimer = 4;
	
	
	public static final double scaling = .6;

	
	public static final double defaultArmPosition = 0;

	
	
    /* For example to map the left and right motors, you could define the
     following variables to use with your drivetrain subsystem.
     If you are using multiple modules, make sure to define both the port
     number and the module. 
     
     */
}
