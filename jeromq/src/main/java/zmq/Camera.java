package zmq;

import org.opencv.videoio.VideoCapture;
import java.awt.image.BufferedImage;

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

                saveFrame(mat2Img.getImage(mat2Img.mat));

                /* I think somewhere here I might need to perform face detection and recognition algorithms */

                return mat2Img.getImage(mat2Img.mat);
        }

        public void saveFrame(BufferedImage image){
                img = image;
        }

        public BufferedImage getFrame(){
                return img;
        }
}