package com.fwitch.backend.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;

import com.fwitch.backend.frame.Frame;
import com.fwitch.backend.frame.Image;

/* * RIFF ('AVI ') 
 * ├── LIST ('hdrl') 
 * │ ├── avih (main header) 
 * │ └── LIST ('strl') 
 * │ ├── strh (stream header) 
 * │ └── strf (stream format) 
 * ├── LIST ('movi') 
 * │ ├── 00db / 00dc (video frames) 
 * │ ├── 01wb (audio) 
 * └── idx1 (index) 
 */
//https://learn.microsoft.com/en-us/windows/win32/directshow/avi-riff-file-reference
public class AVIDecoder extends MediaDecoderAbstract {

    @Override
    public void fill(List<Frame> frames) throws IOException {

        raw = Files.readAllBytes(source);

        expect("RIFF");
        fileSize = grabInt();
        expect("AVI ");

        while (pointer < raw.length) {
            String chunkId = grabString(4);
            int chunkSize = grabInt();
            //System.out.println("Chunk - " + chunkId + " of size " + chunkSize);
            switch (chunkId) {
                case "LIST":
                    handleLIST(chunkSize, frames);
                    break;

                case "idx1":
                    skip(chunkSize);
                    break;

                default:
                	//System.out.println("Skipping unknown chunk " + chunkId +" of length "+ chunkSize);
                    skip(chunkSize);
            }

            align();
        }
    }

    private void handleLIST(int size, List<Frame> frames) {
        String listType = grabString(4);
        int end = pointer + size - 4;
        switch (listType) {
            case "hdrl":
                parseHeaderList(end);
                break;

            case "strl":
                parseStreamList(end);
                break;

            case "movi":
                parseMovi(end, frames);
                break;

            default:
            	//System.out.println("unknown LIST FOURCC " + listType);
                pointer = end;
        }
    }

    private void parseHeaderList(int end) {
        while (pointer < end) {
            String id = grabString(4);
            int size = grabInt();
            //System.out.println("HEADER - " + id + " of size "+ size);
            if ("avih".equals(id)) {
                parseAvih(size);
            } else if ("LIST".equals(id)) {
                handleLIST(size, null);
            } else {
            	//System.out.println("Skipping unknown header "+ id + " of size " + size);
                skip(size);
            }

            align();
        }
    }

    private void parseStreamList(int end) {
        while (pointer < end) {
            String id = grabString(4);
            int size = grabInt();

            if ("strf".equals(id)) {
                parseStrf(size);
            } else {
            	//System.out.println("Skipping unkown stream " + id + " of size "+ size);
                skip(size);
            }

            align();
        }
    }

    private void parseAvih(int size) {
        int microSecPerFrame = grabInt();
        int maxBytesPerSec = grabInt(); 
        //TODO: to be included in output frames
        System.out.println("Frame time (µs): " + microSecPerFrame);

        skip(size - 8);
    }

    private void parseStrf(int size) {
    	/*
    	    DWORD biSize          (4)
			LONG  biWidth         (4)
			LONG  biHeight        (4)
			WORD  biPlanes        (2)
			WORD  biBitCount      (2)
			DWORD biCompression   (4)
			DWORD biSizeImage     (4)
			LONG  biXPelsPerMeter (4)
			LONG  biYPelsPerMeter (4)
			DWORD biClrUsed       (4)
			DWORD biClrImportant  (4)
         */
    	
    	int headerSize = grabInt();   
        width = grabInt();
        height = grabInt();

        int planes = grabShort();
        bitCount = grabShort();

        compression = grabString(4);

        int imageSize = grabInt();
        int xppm = grabInt();
        int yppm = grabInt();
        int clrUsed = grabInt();
        int clrImportant = grabInt();
        
        System.out.println("Video: " + width + "x" + height +
                " " + bitCount + "bit codec=" + compression);

        skip(size);
    }

    private void parseMovi(int end, List<Frame> frames) {
    	System.out.println(end);
        while (pointer < end) {
            String chunkId = grabString(4);
            int size = grabInt();

            if (chunkId.endsWith("db") || chunkId.endsWith("dc")) {
                byte[] frameData = grabBytes(size);

                Image img = decodeFrame(frameData);

                Frame f = new Frame();
                f.setVideo(new Image[]{img});
                frames.add(f);
            } else {
                skip(size);
            }

            align();
        }
    }

    private Image decodeFrame(byte[] data) {
        if ("MJPG".equals(compression)) {
            return decodeJPEG(data);
        }

        if (!"DIB ".equals(compression) && !"RGB ".equals(compression)) {
            throw new RuntimeException("Unsupported codec: " + compression);
        }

        return decodeRGB(data);
    }

    private Image decodeRGB(byte[] data) {
        Image img = new Image();
        img.width = width;
        img.height = height;
        img.pixels = new Image.Pixel[height][width];

        int stride = ((width * 3 + 3) / 4) * 4;
        int index = 0;

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                byte b = data[index++];
                byte g = data[index++];
                byte r = data[index++];

                img.pixels[y][x] = img.new Pixel(r, g, b, (byte) 255);
            }

            index += stride - (width * 3);
        }

        return img;
    }

    private Image decodeJPEG(byte[] data) {
        try {
            BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(data));

            Image img = new Image();
            img.width = bimg.getWidth();
            img.height = bimg.getHeight();
            img.pixels = new Image.Pixel[img.height][img.width];

            for (int y = 0; y < img.height; y++) {
                for (int x = 0; x < img.width; x++) {
                    int rgb = bimg.getRGB(x, y);

                    byte r = (byte) ((rgb >> 16) & 0xFF);
                    byte g = (byte) ((rgb >> 8) & 0xFF);
                    byte b = (byte) (rgb & 0xFF);

                    img.pixels[y][x] = img.new Pixel(r, g, b, (byte) 255);
                }
            }

            return img;

        } catch (Exception e) {
            throw new RuntimeException("JPEG decode failed", e);
        }
    }
    
    private Image decodeCVID(byte[] data) {
        try {
            BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(data));

            if (bimg == null) {
                throw new RuntimeException("CVID decode failed (no ImageIO support)");
            }

            Image img = new Image();
            img.width = bimg.getWidth();
            img.height = bimg.getHeight();
            img.pixels = new Image.Pixel[img.height][img.width];

            for (int y = 0; y < img.height; y++) {
                for (int x = 0; x < img.width; x++) {
                    int rgb = bimg.getRGB(x, y);

                    byte r = (byte)((rgb >> 16) & 0xFF);
                    byte g = (byte)((rgb >> 8) & 0xFF);
                    byte b = (byte)(rgb & 0xFF);

                    img.pixels[y][x] = img.new Pixel(r, g, b, (byte)255);
                }
            }

            return img;

        } catch (Exception e) {
            throw new RuntimeException("CVID decode failed", e);
        }
    }
}
