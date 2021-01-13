package vision;

import org.opencv.videoio.VideoCapture;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

/* The camera class is only responsible for:
        - running the camera until user stops the program
        - returning the current frame as a BufferedImage */
public class Camera {
	
	private static final int CAMERA_INDEX = 2;
        private VideoCapture video;
        private Mat2Image mat_2_image;

        Camera(){
                mat_2_image = new Mat2Image();
                video = new VideoCapture();
                /* used to read from a rtsp live stream, which is the standard stream of the PTZ camera */
                //video.open("rtsp://192.168.1.188:554/stream/main");
                
                video.open(CAMERA_INDEX);
        }

        public BufferedImage getOneFrame() {
                /* Grabs, decodes and returns the next video frame */
                video.read(mat_2_image.mat);
                return mat_2_image.getImageFromMat(mat_2_image.mat);
        }

        public Mat2Image getMat2Image(){
                return mat_2_image;
        }
}
