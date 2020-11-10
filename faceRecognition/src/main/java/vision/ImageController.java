package vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import converters.Converter;

public class ImageController {

        public Converter converter;
        private BufferedImage buf_image;
        private byte[] data_image;
        private Mat mat_image;

        private static String IMAGE_PATH = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceRecognition/resources/trainingImages/barend/output";

        public ImageController(){
                converter = new Converter();
        }

        /* FACE RECOGNITION SOURCE: DESKRESEARCH: https://www.javatpoint.com/face-recognition-and-face-detection-using-opencv
                                                - https://docs.opencv.org/2.4/modules/contrib/doc/facerec/facerec_tutorial.html#fisherfaces-in-opencv


        LBPH Implementation
                - Convert the greyscale image to a 16x16 cell image
                        
                Link: https://medium.com/dev-genius/face-recognition-based-on-lbph-algorithm-17acd65ca5f7
                        https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ 
        */
        public void startRecognition() {
                /* I can change the incoming parameters later */
                LBPHAlgorithm lbph = new LBPHAlgorithm(mat_image, buf_image);
                
                /* start the recognition process */
                lbph.startLBPHProcess();
        }

        public void saveImage(BufferedImage img, int i, String name) {
                File f = new File("resources/" + name + i + ".jpg");
                try {
                        ImageIO.write(img, "JPG", f);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public BufferedImage getBufImage(){
                return buf_image;
        }

        public Mat getImageMat(){
                return mat_image;
        }

        public void setImage(byte[] data){
                data_image = data;
                buf_image = converter.bytesToImage(data);
                mat_image = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);
                System.out.print("mat_image.channels(): " + mat_image.channels());
        }
}
