package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
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
	static double smallestRectArea = 100;
	static double distanceToLeft = 0;
	static double distanceToRight = 0;
	static double centerX;
	static double matHeight;
	static Rect leftRect;
	static Rect rightRect;
	//static Mat filteredMat = new Mat();
	static Mat hierarchy;
	static Mat mat;
	/*public static enum locationOfTargets{
		Right, Left, Ahead, NoTargetVisible
	}
	static targetLocation = NoTargetVisible;*/
	
	public static double getDistanceLeft(){//distance to left target
		return distanceToLeft;
	}
	public static double getDistanceRight(){//distance to left target
		return distanceToRight;
	}
	public static int getNumOfTargets(){
		return numOfTargets;
	}
	/*public static Mat getFilteredMat(){
		return filteredMat;
	}*/
	public static boolean getTwoTargets(){
		return twoTargets;
	}
	/*public static locationOfTargets getLocationOfTarget{
		
	}*/
	public static void processImage(MatRapper image, CvSource cameraStream){
		mat = image.getMat();
		
		Core.inRange(mat, new Scalar(RobotMap.lowBlueValue,RobotMap.lowGreenValue,RobotMap.lowRedValue), 
				new Scalar(RobotMap.highBlueValue,RobotMap.highGreenValue,RobotMap.highRedValue), mat);
		
		hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<>();
		
		//filtered camera feed won't show filtered objects after this - until draw contours
		findContours(mat, contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);
		
		List<MatOfPoint> filteredContours = new ArrayList<>();	//List of filtered contours
		List<Rect> filteredContoursRect = new ArrayList<>();	//List of filtered contours as Rect objects
		
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
		//double centerY = (leftRect.y + leftRect.height * .5);
		//Imgproc.circle(mat.getMat(), new Point(centerX,centerY), 20, new Scalar(255,255,255), 2);
		//Imgproc.rectangle(mat.getMat(), new Point(leftRect.x,leftRect.y), new Point(rightRect.x,rightRect.y), new Scalar(255,255,255),2);
				
		matHeight = mat.rows();	//.getHeight();
		//(widthOfTarget*pixelFieldOfViewWidth)/(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
		if(filteredContours.size() >= 2){
			twoTargets = true;
		}
		numOfTargets = filteredContours.size();
		if(twoTargets){
			
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
			/*distanceToLeft = ((RobotMap.heightOfTargetInFeet*matHeight)/
					(leftRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration))-RobotMap.distanceOfCamFromFrontOfBot;*/
			distanceToRight = ((RobotMap.heightOfTargetInFeet*matHeight)/
					(rightRect.height*(.5*RobotMap.cameraFOVHeightInFeet)/RobotMap.distanceAtCalibration))-RobotMap.distanceOfCamFromFrontOfBot;
			distanceToLeft += .088;
			distanceToRight += .088;
			distanceToLeft /= 1.886;
			distanceToRight /= 1.886;
    		centerX = .5 * ((leftRect.x + leftRect.width) + rightRect.x);
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
		
		cameraStream.putFrame(mat);
		//filteredMat = mat;
	}
}