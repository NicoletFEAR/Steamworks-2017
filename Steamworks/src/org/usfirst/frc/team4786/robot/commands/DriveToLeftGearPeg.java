package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveToLeftGearPeg extends CommandGroup {

    public DriveToLeftGearPeg() {
    	/*
    	addSequential(new DriveToPosition(3));
    	addSequential(new TurnToAngle(60));
    	addSequential(new DriveToPosition(1));*/
    	
    	//initial drive straight in feet
    	addSequential(new DriveToPosition(9.417- RobotMap.professorElementalRobotLength));
    	//turning degrees via .22 power ratio on inside of turn for .75 seconds
    	addSequential(new TurnToAngle(-60));
    	//final drive straight to target
    	addSequential(new DriveToPosition(1.5));
    	
        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
