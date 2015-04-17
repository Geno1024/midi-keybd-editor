package com.geno.midikeybdeditor;

public class Progress
{
	public static final int[] metaEventStatus =
	{
		0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
	//	0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,

	//	0x10,
	//	0x18,

		0x20,
	/*	0x28,*/	                           0x2F,

	//	0x30,
	//	0x38,

	//	0x40,
	//	0x48,

		     0x51,          0x54,
		0x58,0x59,

	//	0x60,
	//	0x68,

	//	0x70,
	/*	0x78,*/                            0x7F,
	};

	public static final int eventCount = 7;

	public static final int instFamilyCount = 16;

	public static final int instCountPerFami = 8;

	public static final byte[] midiHeader = 
	new byte[]
	{
		0x4D,0x54,0x68,0x64,0x00,0x00,//MThd
		0x00,0x06,//6 bytes
	};

	public static String[] notedefinedname = 
	{
		"C",
			"C#",
		"D",
			"D#",
		"E",
		"F",
			"F#",
		"G",
			"G#",
		"A",
			"A#",
		"B",
	};

	public static String[] trackno = 
	{
		"0","1","2","3","4","5","6","7",
		"8","9","10","11","12","13","14","15"
	};

	public static String[] note12 = 
	{
		"8x -5","8x -4","8x -3","8x -2","8x -1",
		"8x ±0",
		"8x +1","8x +2","8x +3","8x +4","8x +5",
	};

}
