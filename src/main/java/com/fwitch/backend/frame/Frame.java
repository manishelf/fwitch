package com.fwitch.backend.frame;

import java.io.Serializable;

public class Frame implements Serializable {	
	Image[] video;
	Sample[] audio;
	long frameNo = 0;
	
	public Frame(long frameNo, Image[] video, Sample[] audio) {
		this.frameNo = frameNo;
    	this.video = video;
		this.audio = audio;
	}

	public Image[] getVideo() {
		return video;
	}

	public void setVideo(Image[] video) {
		this.video = video;
	}

	public Sample[] getAudio() {
		return audio;
	}

	public void setAudio(Sample[] audio) {
		this.audio = audio;
	}

	public long getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(long frameNo) {
		this.frameNo = frameNo;
	}
	
	
}
