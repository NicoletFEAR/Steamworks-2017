package org.usfirst.frc.team4786.robot.subsystems;

import org.opencv.core.Mat;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class VisionPIDSource implements PIDSource{

	PIDSourceType type;
	public VisionPIDSource(){
		type = PIDSourceType.kDisplacement;
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		type = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub

    	Mat frame = new Mat();
    	
    	//Robot.outputStream.putFrame(frame);
    	Robot.cvSink.grabFrame(frame);
    	MatRapper rapperNamedMat = new MatRapper(frame);
    	Robot.visionImage.processImage(rapperNamedMat);
    	Robot.visionImage.analysis();
    	//Not certain whether this should be the absolute input or the error
		return Robot.visionImage.centerX;
	}

}
