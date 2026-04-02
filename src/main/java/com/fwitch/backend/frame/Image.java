package com.fwitch.backend.frame;

import java.io.Serializable;

public class Image implements Serializable{
	public Pixel[][] pixels;
	public int width;
	public int height;
			
	public class Pixel{
		public byte RED;
		public byte GREEN;
		public byte BLUE;
		public byte ALPHA;
		public Pixel(byte rED, byte gREEN, byte bLUE, byte aLPHA) {
			RED = rED;
			GREEN = gREEN;
			BLUE = bLUE;
			ALPHA = aLPHA;
		}
	}
}
