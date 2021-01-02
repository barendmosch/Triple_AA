package vision;

import org.opencv.videoio.VideoCapture;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.Videoio;

public class Camera {

        VideoCapture video;
        Mat2Image mat2Img = new Mat2Image();

        BufferedImage img;

        Camera(){
                video = new VideoCapture();
                /* PTZ Camera via the rtsp stream */
                // video.open(VIDEO_STREAM);
                /* PTZ Camera via USB not via the vid stream */
                // video.open(1);
                video.open("rtsp://192.168.1.188:554/stream/main");
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
