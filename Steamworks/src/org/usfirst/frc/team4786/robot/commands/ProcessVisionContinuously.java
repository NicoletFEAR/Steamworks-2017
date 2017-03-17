package org.usfirst.frc.team4786.robot.commands;

import org.opencv.core.Mat;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ProcessVisionContinuously extends Command {

    public ProcessVisionContinuously() {
        requires(Robot.visionImage);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearPlacementCamera.setExposureManual(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Mat frame = new Mat();
    	
    	//Robot.outputStream.putFrame(frame);
    	Robot.cvSink.grabFrame(frame);
    	MatRapper rapperNamedMat = new MatRapper(frame);
    	Robot.visionImage.processImage(rapperNamedMat);
    	Robot.visionImage.analysis();
    	SmartDashboard.putBoolean("Rectangles detected?", Robot.visionImage.getTwoTargets());
    	SmartDashboard.putNumber("Right", Robot.visionImage.getDistanceRight());
    	SmartDashboard.putNumber("Left", Robot.visionImage.getDistanceLeft());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//it only runs in autonomous right now
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
