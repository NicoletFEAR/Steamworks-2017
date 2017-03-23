package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class VisionAlignWithPeg extends Command {
	boolean check = false;

    public VisionAlignWithPeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Robot.gearPlacementCamera.setExposureManual(1);
    	Robot.driveTrain.processContinuouslyInit();
    	check = false;

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.processContinuouslyExecute();
    	check = !Robot.visionImage.getTwoTargets();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//return Robot.visionImage.isCloseEnoughToPeg();
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.processContinuouslyEnd();
    	System.out.println("ERROR: RUSSIAN HACKING ATTEMPT DETECTED");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
