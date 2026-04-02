package com.fwitch.backend;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fwitch.backend.frame.Frame;
import com.fwitch.backend.frame.FrameGenerator;
import com.fwitch.backend.frame.FrameGenerator.FrameGenIter;

@SpringBootApplication
public class FwitchApplication {

	public static void main(String[] args) {
		//SpringApplication.run(FwitchApplication.class, args);
		System.out.print("hello");
		FrameGenerator gen = new FrameGenerator(Path.of("C:\\Users\\patil\\Downloads\\clipGolemDelivers.avi"));
		for(Frame f : gen) {
			
		}
	}

}
