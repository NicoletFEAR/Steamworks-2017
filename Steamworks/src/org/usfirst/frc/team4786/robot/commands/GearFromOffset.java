package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.subsystems.VisionImage;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearFromOffset extends CommandGroup {

    public GearFromOffset() {
    	double a = VisionImage.getAngle();
    	double d = VisionImage.getFirstDistance();
    	double n = -90;
    	
    	addSequential(new TurnToAngle(a));
    	addSequential(new DriveToPosition(d));
    	addSequential(new TurnToAngle(n));
    }
}
