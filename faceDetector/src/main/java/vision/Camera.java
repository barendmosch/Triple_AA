package vision;

import org.opencv.videoio.VideoCapture;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

/* The camera class is only responsible for:
        - running the camera untill user stops the program
        - returning the current frame as a BufferedImage without face rectangle */
public class Camera {
	
	private static final int CAMERA_INDEX = 1;
        private VideoCapture video;
        public Mat2Image mat2Img;

        BufferedImage img;

        Camera(){
                mat2Img = new Mat2Image();
                video = new VideoCapture();
                //video.open("rtsp://192.168.1.188:554/stream/main");
                
                video.open(CAMERA_INDEX);
                // System.out.print(video.isOpened() + "\n\n");
        }

        public BufferedImage getOneFrame() {
                /* Grabs, decodes and returns the next video frame */
                video.read(mat2Img.mat);
                return mat2Img.getImageFromMat(mat2Img.mat);
        }
}
