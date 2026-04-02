package com.fwitch.backend.media;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fwitch.backend.frame.Frame;

public class AVIDecoder implements MediaDecoder {
	Path source;
	@Override
	public void setSource(Path source) {
		this.source = source;
	}

	@Override
	public void fill(List<Frame> frames) throws IOException {
		byte[]  raw = Files.readAllBytes(source);
		
		int i = 0;
		if (raw[i++] != 'R' || raw[i++] != 'I' || raw[i++] != 'F' || raw[i++] != 'F') {
		    throw new IOException("Invalid RIFF header");
		}
		/* cant do asserts normally in java FU JAVA
		int i = 0;
		assert(raw.length > 0);
		assert(raw[i++] == 'R');
		assert(raw[i++] == 'I');
		assert(raw[i++] == 'F');
		assert(raw[i++] == 'F');
		*/ 
		int fileSize = grabInt(raw, i);
		i+=4;
		if (raw[i++] != 'A' || raw[i++] != 'V' || raw[i++] != 'I' || raw[i++] != ' ') {
		    throw new IOException("FOURCC fileType");
		}
		
		while(raw[i++] == 'L' && raw[i++] == 'I' && raw[i++] == 'S' && raw[i++] == 'T') {
			int listSize = grabInt(raw, i); 
			i+=4;
			if(raw[i++] == 'h' && raw[i++] == 'd' && raw[i++] == 'r' && raw[i++] == 'l') {
				
			}
			System.out.println("---------------------");
			System.out.println(fileSize);
			System.out.println("---------------------");
			System.out.println(listSize);
		}
	}
	
	int grabInt(byte[] raw, int offset) {
		int i = offset;

		// LITTLE_ENDIAN
		return  ((raw[i++] & 0xFF) << 0   ) |
			    ((raw[i++] & 0xFF) << 8   ) |
			    ((raw[i++] & 0xFF) << 16  ) |
			    ((raw[i++] & 0xFF) << 24  )  ;
	}

}
