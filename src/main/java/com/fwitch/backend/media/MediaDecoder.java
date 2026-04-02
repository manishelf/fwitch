package com.fwitch.backend.media;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.fwitch.backend.frame.Frame;

public interface MediaDecoder {

	public void setSource(Path source);
	public void fill(List<Frame> frames) throws IOException;

}
