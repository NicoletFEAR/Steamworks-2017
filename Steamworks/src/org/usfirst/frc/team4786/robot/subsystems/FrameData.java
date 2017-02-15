package org.usfirst.frc.team4786.robot.subsystems;

public class FrameData{

	boolean targetAcquired;
	private int targetNum;
	
	/*information needed for distance calculations/getDistance method
	  all measurements are in feet or pixels*/
	private double distanceAtCalibration = 0.4199475; //12.8 cm
	private double fieldOfViewWidth = .134514; //4.1 cm
	private double widthOfTarget = 2/12; //2 in
	double pixelFieldOfViewWidth; //depends on resolution, set in VisionListener
	double pixelWidthOfTarget;	//set in VisionListener
	private double distanceOfCamFromFrontOfBot;
	
	/*fields needed for getLocationOfTarget()
	  tells if target is left, right, or ahead of target */
	public enum targetLocation {
		LEFT, AHEAD, RIGHT, NoTargetVisible;
	}
	
	double rightTargetArea;
	double leftTargetArea;
	
	//methods
	public boolean isTargetVisible() {
		return targetAcquired;
	}
	public int numOfTargets() {	
		return targetNum;
	}
	//returns the distance to the target in feet;
	public double getDistance() {
		return (widthOfTarget*pixelFieldOfViewWidth)/
				(pixelWidthOfTarget*(.5*fieldOfViewWidth)/distanceAtCalibration)-distanceOfCamFromFrontOfBot;
	}
	//if the right contour is bigger than the other the robot is on the right of the target/must turn left & vice versa
	public targetLocation getLocationOfTarget() {
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
