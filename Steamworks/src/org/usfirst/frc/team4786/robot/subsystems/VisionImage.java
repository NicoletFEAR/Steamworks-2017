package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import org.opencv.core.*;
import java.util.ArrayList;
import java.util.List;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Core.*;
import org.opencv.videoio.VideoCapture;

public class VisionImage implements VisionPipeline {	
	
	//these fields are for methods in FrameData
	static boolean contoursFound = false;	//have contours been found
	static int numOfTargets;
	static double widthOfTarget;	//in pixels
	static int fieldOfViewWidth;	//in pixels
	static double rightContourArea;
	static double leftContourArea;
	
	MatRapper filtered;
	
	/*	put in robot class - robotInnit()
	Mat image = new Mat();
	VideoCapture camera = new VideoCapture(0);
	camera.read(image);
	*/
	
	public void process(Mat image) 
	{
		//try statements ensure the MatRapper class releases the memory upon each iteration
        try(MatRapper blurred = new MatRapper(new Mat()))
        {
        //blurs image to smooth out false positives
        GaussianBlur(image, blurred.getMat(), new Size(15, 15), 0);		
        try(MatRapper filtered = new MatRapper(new Mat()))
        {
        //for getDistance() in FrameData
        fieldOfViewWidth = filtered.getWidth();
        
        double lowB = 240, lowR = 240, lowG = 240, highB = 260, highR = 260, highG = 260;//values from image, R:251 G:252 B:250
        //filters out contours not within color range
        inRange(blurred.getMat(), new Scalar(lowR, lowG, lowB), new Scalar(highR, highG, highB), filtered.getMat());	
        
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        findContours(filtered.getMat(), contours, hierarchy, RETR_LIST, CHAIN_APPROX_NONE);

        List<MatOfPoint> filteredContours = new ArrayList<>();	//List of filtered contours
        List<Rect> filteredContoursRect = new ArrayList<>();	//List of x value of filtered contours

        for (MatOfPoint contour : contours)	//for every contour(Matrix of points) in contours
        {
            Rect boundingRect = boundingRect(contour);		//creates a rectangle around the contour
            double rectRatio = (boundingRect.height*1.0)/boundingRect.width;
            double ratioOfTape = 5/2;	//it will be 5/2 in competition
            double errorMargin = .25;
            /*filtering out false positives - contour must be taller than it is wide 
             * & the ratio of the target's height over width must be = to the tape's height over width with a margin of error */
            if(boundingRect.width < boundingRect.height )//&& (((1 - errorMargin)*(ratioOfTape)) < rectRatio && rectRatio < (1 + errorMargin)*(ratioOfTape))) 
            {
                filteredContours.add(contour); 	//adds contours that fit requirements to list of contours
                filteredContoursRect.add(boundingRect);
                contoursFound = true;
                widthOfTarget = boundingRect.width;
                //draws filtered contours on video display
                drawContours(filtered.getMat(), filteredContours, -1, new Scalar(0xFF, 0xFF, 0xFF), FILLED);
            }
        }
        if(filteredContoursRect.get(0).br().x < filteredContoursRect.get(1).br().x)
        //if the x value of first filtered contour < second filtered contour
        {
        	leftContourArea = filteredContoursRect.get(0).area();
        	rightContourArea = filteredContoursRect.get(1).area();
        }
        else
        {
        	leftContourArea = filteredContoursRect.get(1).area();
        	rightContourArea = filteredContoursRect.get(0).area();
        }
            
        //For Testing
        SmartDashboard.putNumber("Distance", FrameData.getDistance());
        SmartDashboard.putString("Location of Target", FrameData.getLocationOfTarget().name());

        } catch (Exception e) 
        {	e.printStackTrace();   }
	} catch (Exception e1) 
      {   e1.printStackTrace();   }
	}
	public Mat returnFilteredImage()	//returns filtered image as Capture object
	{
		return filtered.getMat();
	}
	public int getWidth()
	{
		return filtered.getWidth();
	}
	public int getHeight()
	{
		return filtered.getHeight();
	}
}