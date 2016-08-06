package com.keenbrace.util;

public class ByteHelp {

	public static int toUnsigned(short s) {
		return s & 0x0FFFF;
	}

	public static int byteArrayToInt(byte[] b) {
		return toUnsigned(ByteArrayToShort(b));
	}
	/**
	 * short数组转byte
	 * */
	public byte[] shortToByteArray(short x){
		byte[] data = new byte[2];
		data[0] = (byte) (x >> 0);  
		data[1] = (byte) (x >> 8);  
		return data;
	}
	
	/**
	 * float数组转byte
	 * */
	public byte[] floatToByteArray(float x){
		byte[] data = new byte[4];
        int l = Float.floatToIntBits(x);  
        for (int i = 0; i < 4; i++) {  
        	data[i] = new Integer(l).byteValue();  
            l = l >> 8;  
        }  
        return data;
	}
	
	
	/**
	 * int数组转byte
	 * */
	public byte[] intToByteArray(int x){
		byte[] data = new byte[4];
		data[0] = (byte) (x >> 0);  
		data[1] = (byte) (x >> 8);  
		data[2] = (byte) (x >> 16);  
		data[3] = (byte) (x >> 24);  
		return data;
	}
	
	/**
	 * long数组转byte
	 * */
	public byte[] longToByteArray(long x){
		byte[] data = new byte[8];
		data[0] = (byte) (x >> 0);  
		data[1] = (byte) (x >> 8);  
		data[2] = (byte) (x >> 16);  
		data[3] = (byte) (x >> 24);  
		data[4] = (byte) (x >> 32);  
		data[5] = (byte) (x >> 40);  
		data[6] = (byte) (x >> 48);
		data[7] = (byte) (x >> 56);  
		return data;
	}
	
	/**
	 * Short数组转byte
	 * */
	public byte[] shortArrayToByteArray(short[] s) {
		  byte[] data = new byte[s.length*2];
		  for(int j=0;j<s.length;j++){
			  for(int i=0;i<2;i++) {
			     int offset = (1 -i)*8;
			     data[i+j*2] = (byte)((s[j]>>>offset)&0xff);
			  }
		  }
		  return data;
	}
	
	/**
	 * byte数组转Short
	 * */
	public static short ByteArrayToShort(byte[] b)
	{
		return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff)); 
	}
}
