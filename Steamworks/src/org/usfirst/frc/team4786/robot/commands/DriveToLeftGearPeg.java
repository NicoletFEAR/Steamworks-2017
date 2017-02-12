package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveToLeftGearPeg extends CommandGroup {

    public DriveToLeftGearPeg() {
    	/*addSequential(new DriveToPosition(7.75 + (RobotMap.MESHBOT_ROBOT_LENGTH / 2)));
    	addSequential(new TurnToAngle(60));
    	addSequential(new DriveToPosition(4.208333));*/
    	
    	//Test code
    	addSequential(new DriveToPosition(1));
    	addSequential(new TurnToAngle(60));
    	addSequential(new DriveToPosition(1));
    	
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

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
