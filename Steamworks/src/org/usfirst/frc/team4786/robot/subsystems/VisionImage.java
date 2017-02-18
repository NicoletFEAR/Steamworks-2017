package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Core.*;
import org.opencv.videoio.VideoCapture;
import org.usfirst.frc.team4786.robot.Robot;
import org.usfirst.frc.team4786.robot.RobotMap;
import org.usfirst.frc.team4786.robot.commands.DriveToPosition;
import org.usfirst.frc.team4786.robot.commands.TurnToAngle;
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;

public class VisionImage extends Subsystem{	
	double d3 = .7083;
	boolean twoTargets = false;
	int numOfTargets = 0;
	double largestRectArea = 0;
	double smallestRectArea = 1000;
	double distanceToLeft = 0;
	double distanceToRight = 0;
	double centerX = 0;
	double matHeight = 0;
	Rect leftRect = null;
	Rect rightRect = null;
	Mat hierarchy = null;
	Mat mat = null;
	List<Rect> filteredContoursRect = null;
	List<MatOfPoint> filteredContours = null;
	public enum locationOfTargets{
		Right, Left, Ahead, NoTargetVisible
	}
	
	public double getDistanceLeft(){//distance to left target
		return distanceToLeft;
	}
	public double getDistanceRight(){//distance to left target
		return distanceToRight;
	}
	public int getNumOfTargets(){
		return numOfTargets;
	}
	public  boolean getTwoTargets(){
		return twoTargets;
	}
	public double getFirstAngleToBePerpendicular(){
		double d3 = .7083;
		double x;
		double theta;
		double angle;
		double dm;
		double dl = Robot.visionImage.getDistanceLeft();
		double dr = Robot.visionImage.getDistanceRight();
		x = Math.acos(((dl*dl) - (dr*dr) - (d3*d3)) / (-2.0*dr*d3));
		dm = Math.sqrt((.5*d3)*(.5*d3) + dr*dr - d3*dr*Math.cos(x));
		theta = Math.asin(.5*d3*Math.sin(x)/dm);
		angle = theta + x;

    	return Math.toDegrees(angle);
	}
	public double getFirstDistanceToBePerpendicular(){
		double d3 = .7083;
		double x;
		double dm;
		double dl = Robot.visionImage.getDistanceLeft();
		double dr = Robot.visionImage.getDistanceRight();
		x = Math.acos(((dl*dl) - (dr*dr) - (d3*d3)) / (-2.0*dr*d3));
		dm = Math.sqrt((.5*d3)*(.5*d3) + dr*dr - d3*dr*Math.cos(x));


    	return dm * .5;
	}
	public locationOfTargets getLocationOfTarget(){//returns if targets are right, left, ahead, or not visible
		if(leftRect != null && rightRect != null){	//if targets have the same area they are ahead
			if(leftRect.area() == rightRect.area()){
				return locationOfTargets.Ahead;
			}else if(leftRect.area() > rightRect.area()){	//if the left target is bigger than the right they are to the right
				return locationOfTargets.Right;
			}else{
				return locationOfTargets.Left;
			}	
		}else{
			return locationOfTargets.NoTargetVisible;
		}
	}
	public void processImage(MatRapper image){
		mat = image.getMat();
		
		Core.inRange(mat, new Scalar(RobotMap.lowBlueValue,RobotMap.lowGreenValue,RobotMap.lowRedValue), 
				new Scalar(RobotMap.highBlueValue,RobotMap.highGreenValue,RobotMap.highRedValue), mat);
		
		hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<>();
		
		//filtered camera feed won't show filtered objects after this - until draw contours
		findContours(mat, contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);
		
		filteredContours = new ArrayList<>();	//List of filtered contours
		filteredContoursRect = new ArrayList<>();	//List of filtered contours as Rect objects
		
		for (MatOfPoint contour : contours)	{ //for every contour(Matrix of points) in contours
			Rect boundingRect = boundingRect(contour);		//creates a rectangle around the contour
			//filtering out false positives - contour must be taller than it is wide 
			if(boundingRect.width < boundingRect.height && boundingRect.area() >= RobotMap.minPixelCount) { 
				filteredContours.add(contour); 	//adds contours that fit requirements to list of contours
				filteredContoursRect.add(boundingRect);
				//draws filtered contours on video display
				drawContours(mat, filteredContours, -1, new Scalar(0xFF, 0xFF, 0xFF), FILLED);	//adds contours back to Mat
				if(boundingRect.area() > largestRectArea)
					largestRectArea = boundingRect.area();
				if(boundingRect.area() < smallestRectArea)
					smallestRectArea = boundingRect.area();
			}
		}
		//cameraStream.putFrame(mat);
	}
	public void analysis(){				
		matHeight = mat.rows();
		if(filteredContours.size() >= 2){
			twoTargets = true;
		}
		numOfTargets = filteredContours.size();
		if(twoTargets && filteredContoursRect.size() >= 2){
			if(filteredContoursRect.get(0).x < filteredContoursRect.get(1).x){	//sets left & right rect based off of x coordinates
				leftRect = filteredContoursRect.get(0);
				rightRect = filteredContoursRect.get(1);
			}else{
				leftRect = filteredContoursRect.get(1);
				rightRect = filteredContoursRect.get(0);
			}
			distanceToLeft = RobotMap.heightOfTargetInFeet * matHeight;
			distanceToLeft /= (leftRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration);
			distanceToLeft -= RobotMap.distanceOfCamFromFrontOfBot;
			distanceToRight = ((RobotMap.heightOfTargetInFeet*matHeight)/
					(rightRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration))-RobotMap.distanceOfCamFromFrontOfBot;
			//Distance calculations, may need to be tuned
			distanceToLeft += .088;
			distanceToRight += .088;
			distanceToLeft /= 1.886;
			distanceToRight /= 1.886;
			//subtract distance of camera to front of robot to final calculations
    		centerX = .5 * ((leftRect.x + leftRect.width) + rightRect.x);
    		
    		//angle calculations
        	/*temp = (distanceToLeft * distanceToLeft - distanceToRight * distanceToRight - d3 * d3);
        	temp /= (-2.0 * distanceToRight * d3);

        	x = Math.acos(temp);
        	
        	dm2 = Math.pow(d3 / 2.0, 2.0) + distanceToRight*distanceToRight - 2.0 * (d3/2.0) * distanceToRight * Math.cos(x);

        	dm = Math.sqrt(dm2);
        	theta = Math.asin(((d3 / 2.0) * (Math.sin(x)) / dm));
        	
        	angle = Math.toDegrees(theta + x);*/
        	
		}
	}
	public void putValuesToSmartDashboard(){	//this is for testing
		if(twoTargets && filteredContoursRect.size() >= 2){
			SmartDashboard.putNumber("Left Height", leftRect.height);
			SmartDashboard.putNumber("Right Height", rightRect.height);
			SmartDashboard.putNumber("Left Width", leftRect.width);
			SmartDashboard.putNumber("Right Width", rightRect.width);
			SmartDashboard.putNumber("Distance to left Rect", distanceToLeft);
			SmartDashboard.putNumber("Distance to right Rect", distanceToRight);
			SmartDashboard.putNumber("Angle To Turn", Robot.visionImage.getFirstAngleToBePerpendicular());
			SmartDashboard.putNumber("First Distance", Robot.visionImage.getFirstDistanceToBePerpendicular());

		}
		SmartDashboard.putBoolean("Rectangles detected?", twoTargets);
		SmartDashboard.putNumber("Number of targets", numOfTargets);
		SmartDashboard.putNumber("Area of largest Rect", largestRectArea);
		SmartDashboard.putNumber("Area of smallest Rect", smallestRectArea);
	}
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}