package org.usfirst.frc.team4786.robot.subsystems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
    
    public int readIntData(){
    	byte[] data = new byte[4];
    	wire.transaction(null, 0, data, 4);
    	ByteBuffer buffer = ByteBuffer.wrap(data);
    	buffer.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
    	int result = buffer.getInt();
    	return result;
    }
}

