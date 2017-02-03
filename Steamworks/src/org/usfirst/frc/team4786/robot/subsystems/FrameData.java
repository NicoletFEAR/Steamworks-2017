package org.usfirst.frc.team4786.robot.subsystems;

public class FrameData{

	static boolean targetAcquired;
	static int targetNum;
	
	/*information needed for distance calculations/getDistance method
	  all measurements are in feet or pixels*/
	static double distanceAtCalibration = 0.4199475; //12.8 cm
	static double fieldOfViewWidth = .134514; //4.1 cm
	static double widthOfTarget = 2/12; //2 in
	static double pixelFieldOfViewWidth; //depends on resolution, set in VisionListener
	static double pixelWidthOfTarget;	//set in VisionListener
	static double distanceOfCamFromFrontOfBot;
	
	/*fields needed for getLocationOfTarget()
	  tells if target is left, right, or ahead of target */
	public enum targetLocation
	{	LEFT, AHEAD, RIGHT, NoTargetVisible;	  }
	static double rightTargetArea;
	static double leftTargetArea;
	
	//methods
	public static boolean isTargetVisible()
	{
		return targetAcquired;
	}
	public int numOfTargets()
	{	
		return targetNum;
	}
	//returns the distance to the target in feet;
	public static double getDistance()
	{
		return (widthOfTarget*pixelFieldOfViewWidth)/
				(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
	}
	//if the right contour is bigger than the other the robot is on the right of the target/must turn left & vice versa
	public static targetLocation getLocationOfTarget()
	{
		if (targetNum != 1 && targetNum != 2)
			return targetLocation.NoTargetVisible;
		else if (rightTargetArea > leftTargetArea)
			return targetLocation.RIGHT;
		else if (rightTargetArea < leftTargetArea)
			return targetLocation.LEFT;
		else
			return targetLocation.AHEAD;
	}
}
