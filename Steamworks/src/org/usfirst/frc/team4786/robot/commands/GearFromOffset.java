package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.subsystems.VisionImage;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearFromOffset extends CommandGroup {

    public GearFromOffset() {
    	double d3 = 8.25;
    	double dl = VisionImage.getDistanceLeft();
    	double dr = VisionImage.getDistanceRight();
    	double x = Math.acos((dl*dl - dr*dr -d3*d3)/(-2*dr*d3));
    	double dm2 = Math.pow(d3 / 2, 2) + dr*dr - 2 * (d3/2) * dr * Math.cos(x);
    	double dm = Math.sqrt(dm2);
    	double theta = Math.asin(((d3 / 2) * Math.sin(x)) / dm);
    	
    	double angle = theta + x;
    	
    	TurnToAngle turn1 = new TurnToAngle(angle);
    	DriveToPosition drive1 = new DriveToPosition(dm);
    	TurnToAngle turn2 = new TurnToAngle(-90);
    	addSequential(turn1);
    	addSequential(drive1);
    	addSequential(turn2);
    }
}
