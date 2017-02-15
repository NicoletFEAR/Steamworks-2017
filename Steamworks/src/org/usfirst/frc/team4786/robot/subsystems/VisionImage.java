package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.cscore.CvSource;
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
import org.usfirst.frc.team4786.robot.subsystems.MatRapper;

public class VisionImage {	
	
	static boolean twoTargets = false;
	static int numOfTargets = 0;
	static double largestRectArea = 0;
	static double smallestRectArea = 1000;
	static double distanceToLeft = 0;
	static double distanceToRight = 0;
	static double centerX = 0;
	static double matHeight = 0;
	static Rect leftRect = null;
	static Rect rightRect = null;
	static Mat hierarchy = null;
	static Mat mat = null;
	static List<Rect> filteredContoursRect = null;
	static List<MatOfPoint> filteredContours = null;
	public static enum locationOfTargets{
		Right, Left, Ahead, NoTargetVisible
	}
	
	public static double getDistanceLeft(){//distance to left target
		return distanceToLeft;
	}
	public static double getDistanceRight(){//distance to left target
		return distanceToRight;
	}
	public static int getNumOfTargets(){
		return numOfTargets;
	}
	public static boolean getTwoTargets(){
		return twoTargets;
	}
	public static locationOfTargets getLocationOfTarget(){//returns if targets are right, left, ahead, or not visible
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
	public static void processImage(MatRapper image, CvSource cameraStream){
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
		cameraStream.putFrame(mat);
	}
	public static void analysis(){				
		matHeight = mat.rows();	//.getHeight();
		//(widthOfTarget*pixelFieldOfViewWidth)/(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
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
		}
	}
	public static void putValuesToSmartDashboard(){	//this is for testing
		if(twoTargets && filteredContoursRect.size() >= 2){
			SmartDashboard.putNumber("Left Height", leftRect.height);
			SmartDashboard.putNumber("Right Height", rightRect.height);
			SmartDashboard.putNumber("Left Width", leftRect.width);
			SmartDashboard.putNumber("Right Width", rightRect.width);
			SmartDashboard.putNumber("Distance to left Rect", distanceToLeft);
			SmartDashboard.putNumber("Distance to right Rect", distanceToRight);
		}
		SmartDashboard.putBoolean("Rectangles detected?", twoTargets);
		SmartDashboard.putNumber("Number of targets", numOfTargets);
		SmartDashboard.putNumber("Area of largest Rect", largestRectArea);
		SmartDashboard.putNumber("Area of smallest Rect", smallestRectArea);
	}
}