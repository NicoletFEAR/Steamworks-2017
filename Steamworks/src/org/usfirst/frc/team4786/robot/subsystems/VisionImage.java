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

public class VisionImage implements VisionPipeline {	
	
	//these fields are for methods in FrameData
	private boolean contoursFound = false;	//have contours been found
	private int numOfTargets;
	private double widthOfTarget;	//in pixels
	private int fieldOfViewWidth;	//in pixels
	private double rightContourArea;
	private double leftContourArea;
	
	public Mat filtered = new Mat();
	
	/*	put in robot class - robotInnit()
	Mat image = new Mat();
	VideoCapture camera = new VideoCapture(0);
	camera.read(image);
	*/
	
	public boolean getContoursFound(){
		boolean f = contoursFound;
		return f;
	}
	
	public int getNumberOfTargets(){
		int n = numOfTargets;
		return n;
	}
	
	public double getWidthOfTarget(){
		double w = widthOfTarget;
		return w;
	}
	
	public int getFieldOfViewWidth(){
		int w = fieldOfViewWidth;
		return w;
	}
	
	public double getRightContourArea(){
		double a = rightContourArea;
		return a;
	}
	
	public double getLeftContourArea(){
		double a = leftContourArea;
		return a;
	}
	
	public void process(Mat image) {
		
		
		//try statements ensure the MatRapper class releases the memory upon each iteration
        try(MatRapper blurred = new MatRapper(new Mat())) {
        	//blurs image to smooth out false positives
        	GaussianBlur(image, blurred.getMat(), new Size(320, 240), 0);
        	try(MatRapper processLocalFiltered = new MatRapper(image)) {
        		//for getDistance() in FrameData
        		fieldOfViewWidth = processLocalFiltered.getWidth();
        		
        		double lowB = RobotMap.lowBlueValue;
        		double lowR = RobotMap.lowRedValue;
        		double lowG = RobotMap.lowGreenValue;
        		double highB = RobotMap.highBlueValue;
        		double highR = RobotMap.highRedValue;
        		double highG = RobotMap.highGreenValue;
        		//values from image, R:251 G:252 B:250
        		
        		//filters out contours not within color range
        		inRange(blurred.getMat(), new Scalar(lowR, lowG, lowB), new Scalar(highR, highG, highB), processLocalFiltered.getMat());	
        		
        		Mat hierarchy = new Mat();
        		List<MatOfPoint> contours = new ArrayList<>();
        		
        		findContours(processLocalFiltered.getMat(), contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);
        		
        		List<MatOfPoint> filteredContours = new ArrayList<>();	//List of filtered contours
        		List<Rect> filteredContoursRect = new ArrayList<>();	//List of x value of filtered contours
        		
        		for (MatOfPoint contour : contours)	{ //for every contour(Matrix of points) in contours
        			Rect boundingRect = boundingRect(contour);		//creates a rectangle around the contour
        			//double rectRatio = (boundingRect.height*1.0)/boundingRect.width;
        			//double ratioOfTape = 5/2;	//it will be 5/2 in competition
        			//double errorMargin = .25;
        			//filtering out false positives - contour must be taller than it is wide 
        			// & the ratio of the target's height over width must be = to the tape's height over width with a margin of error
        			if(boundingRect.width < boundingRect.height ) {//&& (((1 - errorMargin)*(ratioOfTape)) < rectRatio && rectRatio < (1 + errorMargin)*(ratioOfTape))) 
        				filteredContours.add(contour); 	//adds contours that fit requirements to list of contours
        				filteredContoursRect.add(boundingRect);
        				contoursFound = true;
        				//draws filtered contours on video display
        				drawContours(processLocalFiltered.getMat(), filteredContours, -1, new Scalar(0xFF, 0xFF, 0xFF), FILLED);
        			}
        		}
        		if(filteredContoursRect.get(0).br().x < filteredContoursRect.get(1).br().x) {
        			//if the x value of first filtered contour < second filtered contour
        			leftContourArea = filteredContoursRect.get(0).area();
        			rightContourArea = filteredContoursRect.get(1).area();
        			widthOfTarget = filteredContoursRect.get(0).width;
        		}else{
        			leftContourArea = filteredContoursRect.get(1).area();
        			rightContourArea = filteredContoursRect.get(0).area();
        			widthOfTarget = filteredContoursRect.get(1).width;
        		}
        		
        		//For Testing
        		//SmartDashboard.putNumber("Distance", Robot.frameData.getDistance());
        		//SmartDashboard.putString("Location of Target", Robot.frameData.getLocationOfTarget().name());
        		filtered = blurred.getMat();
        }catch (Exception e) {
        	e.printStackTrace();
        }
        }catch (Exception e1) {
        	e1.printStackTrace();
        }
	}
	public Mat returnFilteredImage() {	//returns filtered image
		return filtered;
	}
	/*public int getWidth() {
		return processLocalFiltered.getWidth();
	}
	public int getHeight() {
		//return filtered.getHeight();
	}*/
}