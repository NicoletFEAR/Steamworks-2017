package org.usfirst.frc.team4786.robot.subsystems;

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
	//everything is static
	static int numOfTargets = 0;
	static double largestRectArea = 0;
	static double smallestRectArea = 100;
	static Rect leftRect;
	static Rect rightRect;
	static double distanceToLeft = 0;
	static double distanceToRight = 0;
	
	public static void processImage(MatRapper mat){
		Core.inRange(mat.getMat(), new Scalar(RobotMap.lowBlueValue,RobotMap.lowGreenValue,RobotMap.lowRedValue), new Scalar(RobotMap.highBlueValue,RobotMap.highGreenValue,RobotMap.highRedValue), mat.getMat());
		
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<>();
		
		//filtered camera feed won't show filtered objects after this - until draw contours
		findContours(mat.getMat(), contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);
		
		List<MatOfPoint> filteredContours = new ArrayList<>();	//List of filtered contours
		List<Rect> filteredContoursRect = new ArrayList<>();
		
		for (MatOfPoint contour : contours)	{ //for every contour(Matrix of points) in contours
			Rect boundingRect = boundingRect(contour);		//creates a rectangle around the contour
			//double rectRatio = (boundingRect.height*1.0)/boundingRect.width;
			//double ratioOfTape = 5/2;	//it will be 5/2 in competition
			//double errorMargin = .25;
			//filtering out false positives - contour must be taller than it is wide 
			// & the ratio of the target's height over width must be = to the tape's height over width with a margin of error
			if(boundingRect.width < boundingRect.height && boundingRect.area() >= RobotMap.minPixelCount) {//&& (((1 - errorMargin)*(ratioOfTape)) < rectRatio && rectRatio < (1 + errorMargin)*(ratioOfTape))) 
				filteredContours.add(contour); 	//adds contours that fit requirements to list of contours
				filteredContoursRect.add(boundingRect);
				//contoursFound = true;
				//draws filtered contours on video display
				drawContours(mat.getMat(), filteredContours, -1, new Scalar(0xFF, 0xFF, 0xFF), FILLED);
				if(boundingRect.area() > largestRectArea)
					largestRectArea = boundingRect.area();
				if(boundingRect.area() < smallestRectArea)
					smallestRectArea = boundingRect.area();
			}
		}
		//double centerY = (leftRect.y + leftRect.height * .5);
		//Imgproc.circle(mat.getMat(), new Point(centerX,centerY), 20, new Scalar(255,255,255), 2);
		//Imgproc.rectangle(mat.getMat(), new Point(leftRect.x,leftRect.y), new Point(rightRect.x,rightRect.y), new Scalar(255,255,255),2);
		
		// Give the output stream a new image to display
		//outputStream.putFrame(mat.getMat());
		boolean twoTargets = false;
		
		double matHeight = mat.getHeight();
		//(widthOfTarget*pixelFieldOfViewWidth)/(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
		if(filteredContours.size() >= 2){
			twoTargets = true;
		}
		numOfTargets = filteredContours.size();
		if(twoTargets){
			
			if(filteredContoursRect.get(0).x < filteredContoursRect.get(1).x){
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
    		double centerX = .5 * ((leftRect.x + leftRect.width) + rightRect.x);
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
		//SmartDashboard.putNumber("X", filteredContoursRect.get(0).x);	un-commenting this results in too many simultaneous client streams
		//SmartDashboard.putNumber("Y", filteredContoursRect.get(0).y);	un-commenting this results in too many simultaneous client streams
	}
	public static double getDistanceLeft(){
		return distanceToLeft;
	}
	public static double getDistanceRight(){
		return distanceToRight;
	}
	public static int getNumOfTargets(){
		return numOfTargets;
	}
}