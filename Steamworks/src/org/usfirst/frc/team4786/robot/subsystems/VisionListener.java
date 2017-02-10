package org.usfirst.frc.team4786.robot.subsystems;

import org.usfirst.frc.team4786.robot.Robot;

import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;

public class VisionListener implements Listener<VisionImage> {

	@Override
	public void copyPipelineOutputs(VisionImage pipeline) {
		/*Robot.frameData.targetAcquired = Robot.visionImage.getContoursFound();
		Robot.frameData.pixelWidthOfTarget = Robot.visionImage.getWidthOfTarget();
		Robot.frameData.pixelFieldOfViewWidth = Robot.visionImage.getFieldOfViewWidth();
		Robot.frameData.leftTargetArea = Robot.visionImage.getLeftContourArea();
		Robot.frameData.rightTargetArea = Robot.visionImage.getRightContourArea();*/
	}
}