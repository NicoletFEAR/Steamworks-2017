package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 * 
 */
public class GearFromOffset extends CommandGroup {
		
    public GearFromOffset() {    
    	addSequential(new VisionSetup());
    	addSequential(new TurnToAngle(Robot.visionImage.getFirstAngleToBePerpendicular()));
    	addSequential(new DriveToPosition(Robot.visionImage.getFirstDistanceToBePerpendicular()));
    	//addSequential(new TurnToAngle(-90));
    }
}