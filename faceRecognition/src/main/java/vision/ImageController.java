package vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import converters.Converter;

public class ImageController {

        public Converter conv;
        private BufferedImage buf_image;
        private byte[] data_image;
        private Mat gray_image;

        private static String IMAGE_PATH = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceRecognition/resources/trainingImages/barend/output";

        public ImageController(){
                conv = new Converter();
        }

        public void setImageAndMakeGrayScale(byte[] data){
                data_image = data;
                buf_image = conv.bytesToImage(data);
                Mat rgb_image = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);

                /* RGB Image is channel 3, meaning it contains the RGB values of every pixel. 
                        We need to convert it to a grayscale image for image processing. */
                gray_image = new Mat();
                Imgproc.cvtColor(rgb_image, gray_image, Imgproc.COLOR_RGB2GRAY);
        }

        /* LBPH Implementation
        - do this process for 'x' trainingImages.
        - save the histogram and maybe images in a DB for comparing
                - Convert the incoming image to grayscale (1 channel, no RGB)
                - Add border values to the matrix 
                - For every pixel value in the matrix, except border values, calculate the surrounding neighbour pixel and save the pixel and the neighbours
                - rearrange the neighbour pixels array like this: https://iq.opengenus.org/lbph-algorithm-for-face-recognition/
                - make the bit_string of the neighbour pixels by comparing every neighbour to the middle_pixel.
                        - if greater or the same, add 1 
                        - if smaller, add 0
                - convert the bit_string to decimal
                - make the output matrix from all the decimal values, this should be the same dimensions as the original image
                - save the image as a LBP image
                - from the image, make a histogram

        - After making the training data, once face data comes through, do the process again
                - Compare the histogram with the training data of every person in the DB
                - If compared and they are kinda the same
                        - Person detected
                - If not, 
                        - Keep searching */
        public void startRecognition(int i) {
                LBPHAlgorithm lbph = new LBPHAlgorithm(gray_image);
                
                /* start the recognition process */
                lbph.startLBPProcess();
                lbph.createHistograms();

                /* Save the imcoming grayscale image and the output LBP Mat as training images in the DB */
                // saveOneImage(lbph.getGrayScaleMat(), "grayScale", i); 
                saveTrainingData(lbph.getLBPMat(), "lbph", i);
        }

        public void saveTrainingData(Mat mat, String name, int i){
                try {
                        BufferedImage img = conv.Mat2BufferedImage(mat);
                        File f = new File("resources/trainingImages/yvonne/" + name + i + ".jpg");
                        ImageIO.write(img, "JPG", f);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public BufferedImage getBufImage(){
                return buf_image;
        }

        public Mat getGrayImage(){
                return gray_image;
        }
}
