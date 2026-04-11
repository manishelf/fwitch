package com.fwitch.backend;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App
{
    public static void main( String[] args ) throws Exception
    {
        FrameGrabber grabber = FFmpegFrameGrabber.createDefault("C:\\Users\\patil\\Desktop\\Project_Manish\\fwitch\\clipGolemDelivers.avi");
        grabber.start();

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat grabbedImage = converter.convert(grabber.grab());
        int height = grabbedImage.rows();
        int width = grabbedImage.cols();

        FrameRecorder recorder = FFmpegFrameRecorder.createDefault("output.avi", width, height);
        recorder.start();
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(grabber.getVideoCodec());

        while ((grabbedImage = converter.convert(grabber.grab())) != null) {
            Mat rotatedImage = grabbedImage.clone(); // or apply transformations here

            Frame rotatedFrame = converter.convert(rotatedImage);
            recorder.record(rotatedFrame);
        }

        recorder.stop();
        grabber.stop();
    }
}
