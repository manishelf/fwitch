package com.fwitch.backend.frame;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fwitch.backend.frame.MediaDecoderFactory.MEDIA_DECODER;
import com.fwitch.backend.media.MediaDecoder;

public class FrameGenerator implements Iterable<Frame> {
	Path source;
	boolean initialized = false;
	MediaDecoder decoder;
	MEDIA_DECODER decoderType;
	List<Frame> frames = new ArrayList<>();
	
	public FrameGenerator(Path source) {
		this.source = source;
	}
	
	public boolean init() {
		
		if(this.initialized) return true;
		
		try {
			this.decoderType = MediaDecoderFactory.getDecoderFor(source);
			this.decoder = MediaDecoderFactory.newDecoderFor(decoderType);
			this.decoder.setSource(source);
			this.decoder.fill(frames);
			this.initialized = true;
		}catch(IOException e) {
			this.initialized = false;
			e.printStackTrace();
		}

		return this.initialized;
	}
	
	public static class FrameGenIter implements Iterator<Frame>{
		
		FrameGenerator gen;
		boolean isValid;
		Iterator<Frame> listIter;
		
		public FrameGenIter(FrameGenerator gen) {
			this.gen = gen;
			this.isValid = this.gen.init();
			listIter = this.gen.frames.iterator();
		}
		
		@Override
		public boolean hasNext() {
			return this.isValid && this.listIter.hasNext();
		}

		@Override
		public Frame next() {
			return this.listIter.next();
		}
		
	}

	@Override
	public Iterator<Frame> iterator() {
		return new FrameGenIter(this);
	}
}
