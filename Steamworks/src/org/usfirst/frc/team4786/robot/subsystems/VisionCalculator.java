package org.usfirst.frc.team4786.robot.subsystems;
import org.usfirst.frc.team4786.robot.Robot;
public class VisionCalculator {
	public static double getAngle(){
		double d3 = .7083;
		double x;
		double theta;
		double angle;
		double dm;
		double dl = 4.03;
		double dr = 4.608;
		x = Math.acos(((dl*dl) - (dr*dr) - (d3*d3)) / (-2.0*dr*d3));
		dm = Math.sqrt((.5*d3)*(.5*d3) + dr*dr - d3*dr*Math.cos(x));
		theta = Math.asin(.5*d3*Math.sin(x)/dm);
		angle = theta + x;

    	return angle;
	}
	
	public static double getDistance(){
		double d3 = .7083;
		double x;
		double dm;
		double dl = Robot.visionImage.getDistanceLeft();
		double dr = Robot.visionImage.getDistanceRight();
		x = Math.acos(((dl*dl) - (dr*dr) - (d3*d3)) / (-2.0*dr*d3));
		dm = Math.sqrt((.5*d3)*(.5*d3) + dr*dr - d3*dr*Math.cos(x));


    	return dm;
	}
}
