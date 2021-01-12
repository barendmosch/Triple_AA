package vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class RecogniseController {
        private Mat grayscale_image;

        public RecogniseController() {}

        /*
         * Sets a BufferedImage object by converting the incoming byte data to an Image
         * initialise a RGB Matrix and a grayscale matrix. The grayscale matrix is used
         * for further processing RGB Image is channel 3, meaning it contains the RGB
         * values of every pixel. We need to convert it to a grayscale image for image
         * processing which contains 1 gray pixel value
         */
        public void setImageAndMakeGrayScale(byte[] data) {
                Mat rgb_image = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);

                grayscale_image = new Mat();
                Imgproc.cvtColor(rgb_image, grayscale_image, Imgproc.COLOR_RGB2GRAY);
        }

        /* the initialisation fase will only happen when the histogram data needs to be saved for a new person
                it does exactly the same as the recognition fase, but it does not perform the data comparison. 
                it makes 50 histograms and saves the name and histogram as a JSON in the database */                
        public void initialisePerson(String name){
                LBPHAlgorithm lbph = new LBPHAlgorithm(grayscale_image);
                lbph.initPersonsData(name);
        }

        public Mat getGrayImage(){
                return grayscale_image;
        }
}
