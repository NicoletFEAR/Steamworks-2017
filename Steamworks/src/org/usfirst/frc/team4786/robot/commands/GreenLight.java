package org.usfirst.frc.team4786.robot.commands;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GreenLight extends Command {

    public GreenLight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.arduino);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.arduino.writeStringData("redlight");
    	WaitCommand w = new WaitCommand(1000);
    	w.start();
    	Robot.arduino.writeStringData("redlight");
    	SmartDashboard.putString("Arduino String", Robot.arduino.readStringData());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
