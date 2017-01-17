package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arduino extends Subsystem {
	private static I2C Wire = new I2C(Port.kOnboard, 4);



    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void writeStringData(String writeable){
    	String WriteString = writeable;
    	char[] CharArray = WriteString.toCharArray();
    	byte[] WriteData = new byte[CharArray.length];
    	for (int i = 0; i < CharArray.length; i++) {
    		WriteData[i] = (byte) CharArray[i];
    	}
    	Wire.transaction(WriteData, WriteData.length, null, 0);
    }
}

