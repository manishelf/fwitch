package com.fwitch.backend.frame;

import java.io.Serializable;

public class Sample implements Serializable{
	public byte[] raw;
	public int sampleRate;
	public Sample(byte[] raw, int sampleRate) {
		super();
		this.raw = raw;
		this.sampleRate = sampleRate;
	}
}
