package org.usfirst.frc.team4786.robot.subsystems;

import org.opencv.core.Mat;

//import edu.wpi.first.wpilibj.command.Subsystem;

public class MatRapper implements AutoCloseable {
	private Mat mat;
	
	public MatRapper(Mat matrix) {   
		mat = matrix;  
	}
	
	@Override
	public void close() throws Exception {   
		mat.release();
	}
	
	public Mat getMat() {
		return mat;
	}
	
	public int getWidth() {   
		return mat.cols();   
	}
	
	public int getHeight() {
		return mat.rows();
	}
	
}