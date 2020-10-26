package zmq;

import org.opencv.videoio.VideoCapture;
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

        BufferedImage getOneFrame() {
                /* Grabs, decodes and returns the next video frame */
                video.read(mat2Img.mat);
                return mat2Img.getImage(mat2Img.mat);
        }

        public BufferedImage returnImageFromMat(Mat frame){
                return mat2Img.getImage(frame);
        }

        public void saveFrame(BufferedImage image){
                img = image;
        }

        public BufferedImage getFrame(){
                return img;
        }
}