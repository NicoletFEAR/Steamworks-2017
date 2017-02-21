package org.usfirst.frc.team4786.robot.commands;


import org.opencv.core.Mat;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;
import edu.wpi.first.wpilibj.command.Command;

public class VisionSetup extends Command {
	boolean b;
	
    public VisionSetup() {
    	requires(Robot.visionImage);
    }
    // Called just before this Command runs the first time
    protected void initialize() {
    	b = false;
    	Robot.gearPlacementCamera.setExposureManual(RobotMap.exposure);
    	try(MatRapper mat = new MatRapper(new Mat());){
			
			Robot.cvSink.grabFrame(mat.getMat());	//sets mat to an image from the camera
			Robot.visionImage.processImage(mat);
			Robot.visionImage.analysis();
			Robot.visionImage.putValuesToSmartDashboard();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	b = true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return b;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
}