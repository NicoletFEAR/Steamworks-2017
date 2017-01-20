package org.usfirst.frc.team4786.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arduino extends Subsystem {
	private static I2C wire;
	public Arduino(){
		wire = new I2C(Port.kOnboard, 4);
	}



    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    //method to send data over i2c to arduino
    public void writeStringData(String writeable){
    	String WriteString = writeable;
    	char[] CharArray = WriteString.toCharArray();
    	byte[] WriteData = new byte[CharArray.length];
    	for (int i = 0; i < CharArray.length; i++) {
    		WriteData[i] = (byte) CharArray[i];
    	}
    	wire.transaction(WriteData, WriteData.length, null, 0);
    }
    
    public String readStringData(){
    	byte[] byteLength = new byte[2];
    	wire.transaction(null, 0, byteLength, 2);
    	char[] lengthCharArray = new char[2];
    	for(int i = 0; i < lengthCharArray.length; i++){
    		lengthCharArray[i] = (char) byteLength[i];
    	}
    	String lenString = String.valueOf(lengthCharArray);
    	int length = Integer.parseInt(lenString);
    	byte[] data = new byte[length];
    	char[] charArray = new char[length];
    	wire.transaction(null, 0, data, length);
    	for(int i = 0; i < length; i++){
    		charArray[i] = (char) data[i];
    	}
    	String dataString = String.valueOf(charArray);
    	return dataString;
    }
}

