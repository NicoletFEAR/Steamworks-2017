package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;

public class VisionListener implements Listener<VisionImage> {

	@Override
	public void copyPipelineOutputs(VisionImage pipeline) {
		FrameData.targetAcquired = VisionImage.contoursFound;
		FrameData.pixelWidthOfTarget = VisionImage.widthOfTarget;
		FrameData.pixelFieldOfViewWidth = VisionImage.fieldOfViewWidth;
		FrameData.leftTargetArea = VisionImage.leftContourArea;
		FrameData.rightTargetArea = VisionImage.rightContourArea;	
	}
}