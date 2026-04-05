package com.fwitch.backend.media;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public abstract class MediaDecoderAbstract  implements MediaDecoder{
	
	Path source;

    int pointer = 0;
    byte[] raw;
    
    int fileSize = 0;

    // video format
    int width;
    int height;
    int bitCount;
    String compression;

    @Override
    public void setSource(Path source) {
        this.source = source;
    }
    
   

    public void expect(String expected) throws IOException {
        String actual = grabString(expected.length());
        if (!expected.equals(actual)) {
            throw new IOException("Expected " + expected + " but got " + actual);
        }
    }

    int grabInt() {
		// LITTLE_ENDIAN
        return ((raw[pointer++] & 0xFF)) |
                ((raw[pointer++] & 0xFF) << 8) |
                ((raw[pointer++] & 0xFF) << 16) |
                ((raw[pointer++] & 0xFF) << 24);
    }

    int grabShort() {
		// LITTLE_ENDIAN
        return ((raw[pointer++] & 0xFF)) |
                ((raw[pointer++] & 0xFF) << 8);
    }

    String grabString(int len) {
        String s = new String(raw, pointer, len, Charset.defaultCharset());
        pointer += len;
        return s;
    }

    byte[] grabBytes(int len) {
        byte[] out = new byte[len];
        System.arraycopy(raw, pointer, out, 0, len);
        pointer += len;
        return out;
    }

    void skip(int len) {
        pointer += len;
    }

    void align() {
        if ((pointer & 1) != 0) pointer++; // align with word
    }

}
