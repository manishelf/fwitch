package com.fwitch.backend.frame;

import java.nio.file.Path;

import com.fwitch.backend.media.AVIDecoder;
import com.fwitch.backend.media.MediaDecoder;

public class MediaDecoderFactory {
	
	public enum MEDIA_DECODER {
		WAV,
		AVI
	}
	
	public static MEDIA_DECODER getDecoderFor(Path source) {
		return MEDIA_DECODER.AVI;
	}

	public static MediaDecoder newDecoderFor(MEDIA_DECODER type) {
		switch(type) {
			case AVI: return new AVIDecoder();
			default : return null;
		}
	}

}
