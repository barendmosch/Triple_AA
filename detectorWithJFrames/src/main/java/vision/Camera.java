package vision;

import org.opencv.videoio.VideoCapture;

import converters.Mat2Image;

import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

public class Camera {

        VideoCapture video;
        Mat2Image mat2Img = new Mat2Image();

        BufferedImage img;

        Camera(){
                video = new VideoCapture();
                video.open(0);
        }

        public BufferedImage getOneFrame() {
                /* Grabs, decodes and returns the next video frame */
                video.read(mat2Img.mat);
                return mat2Img.getImage(mat2Img.mat);
        }

        public BufferedImage returnImageFromMat(Mat frame){
                return mat2Img.getImage(frame);
        }
}