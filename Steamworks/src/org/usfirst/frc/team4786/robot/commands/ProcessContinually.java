package org.usfirst.frc.team4786.robot.commands;

import org.opencv.core.Core.*;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.vision.CameraServer;

/**
 *
 */
public class ProcessContinually extends Command {

    public ProcessContinually() {
    	requires(Robot.visionImage);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearPlacementCamera.setExposureManual(RobotMap.exposure);
    	Robot.driveTrain.processVisionContinuallyInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	try(MatRapper mat = new MatRapper(new Mat());){
			Robot.cvSink.grabFrame(mat.getMat());	//sets mat to an image from the camera
			Robot.visionImage.processImage(mat);
			Robot.visionImage.analysis();
			//Run PID here
			Robot.driveTrain.processVisionContinuallyExecute();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (Robot.oi.getPegLimitSwitch().get()){
        	return true;
        } else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearPlacementCamera.setExposureAuto();
    	Robot.driveTrain.processVisionContinuallyEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
