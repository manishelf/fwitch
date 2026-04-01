package com.fwitch.backend.frame;

import java.io.Serializable;

public class Frame implements Serializable {	
	byte[] video;
	byte[] audio;
	long frameNo = 0;
	
	public Frame(long frameNo, byte[] video, byte[] audio) {
		this.frameNo = frameNo;
    	this.video = video;
		this.audio = audio;
	}
}
