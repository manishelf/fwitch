package com.fwitch.backend.frame;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FrameSerializer {
	static final long serialVersionUID = 1L;

    public static byte[] serialize(Frame frame) throws IOException {
        int videoLength = frame.video != null ? frame.video.length : 0;
        int audioLength = frame.audio != null ? frame.audio.length : 0;

        ByteBuffer buffer = ByteBuffer.allocate(
                  Long.BYTES        // serialVersionUid
                + Long.BYTES        // frameNo
                + Integer.BYTES     // videoLength 
                + videoLength       // video
                + Integer.BYTES     // audioLength
                + audioLength       // audio
                + 1                 // partity / checksum / crc        
        );

        buffer.putLong(serialVersionUID);
        buffer.putLong(frame.frameNo);
        buffer.putInt(videoLength);
        //if (videoLength > 0) buffer.put(compress(frame.video));
        buffer.putInt(audioLength);
        //if (audioLength > 0) buffer.put(compress(frame.audio));
        buffer.put(xorPartity(buffer.array()));
        return buffer.array();
    }
    
    public static Frame deserialize(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        assert(buffer.getLong() == serialVersionUID);
        
        long frameNo = buffer.getLong();
        int videoLength = buffer.getInt();
        Image[] video = new Image[videoLength];
        //buffer.get(decompress(video));

        int audioLength = buffer.getInt();
        Sample[] audio = new Sample[audioLength];
        //buffer.get(decompress(audio));
        
        // TODO: validate parity
        byte xorParity = buffer.get();
        return new Frame(frameNo, video, audio);
    }
    
    public static byte[] compress(Sample[] raw) {
    	return null;
    }

    public static Sample[] decompress(byte[] compressed) {
    	return null;
    }
    
    public static byte xorPartity(byte[] in) {
    	byte xor = 0;
    	for(int i = 0; i < in.length ; i++) {
    		xor ^= in[i];
    	}
    	return xor;
    }
    
    public static byte checkSum(byte[] in) {
    	byte sum = 0;
    	for(int i = 0; i < in.length ; i++) {
    		sum |= in[i];
    	}
    	return sum;
    }
    
    public static byte crc(byte[] in) {
    	// TODO
    	return in[0];
    }
}