package vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import converters.Converter;
import entity.Person;
import sql.DatabaseAction;

public class RecogniseController {
        private static final float PERCENTAGE_THRESHOLD = 80;
        private static final int AMOUNT_OF_SAMPLES = 20;

        private List<Person> training_set;
        private Mat grayscale_image;
        private List<String> names_recognised;

        /*
         * Not used atm, but can be used to save the RGB bufferedImage for taking
         * pictures and what not */
        private BufferedImage buf_image;

        public RecogniseController() {
                training_set = loadTrainingSet();
                names_recognised = new LinkedList<String>();
        }

        /*
         * Sets a BufferedImage object by converting the incoming byte data to an Image
         * initialise a RGB Matrix and a grayscale matrix. The grayscale matrix is used
         * for further processing RGB Image is channel 3, meaning it contains the RGB
         * values of every pixel. We need to convert it to a grayscale image for image
         * processing which contains 1 gray pixel value
         */
        public void setImageAndMakeGrayScale(byte[] data) {
                buf_image = Converter.bytesToImage(data);
                Mat rgb_image = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);

                grayscale_image = new Mat();
                Imgproc.cvtColor(rgb_image, grayscale_image, Imgproc.COLOR_RGB2GRAY);
        }

        /*
         * LBPH Implementation process LBP: 
         * init the grayscale matrix, the training dataset and the LBP mathematical operations class 
         * start the LBP process, which will return a boolean indicating success 
         * The LBP process converts the grayscale matrix into a 2D array with 0 values around the border for making 3x3 matrices  easier - For every pixel value within the 2D array, ignoring the border values, it calculates the LBP value of the middle pixel of the 3x3 matrix. It does this by: 
         *      - get the surrounding neighbours of every pixel
         *      - rearranging the neighbours to make the binary string. 
         *              - middle_value => neighbour = add 1 to string
         *              - middle_value < neighbour = add 0 to string
         *      - convert the binary string to a decimal value which is the LBP value. 
         *      - store the LBP value into a global array 
         *      - make the output LBP matrix, which we can save as a BufferedImage to show the success of the conversion 
         * 
         * MAKING THE HISTOGRAM 
         * from the LBP global array, we make a histogram in the form a hashmap which contains the frequency of every LBP value in the array. 
         * calculate the euclidean distance for every histogram in the training_set for every person compared with the histogram we made from the LBP values 
         * save the list of euclidean distances and sort the list from small to big 
         * get the first 'x' values and check if:
         *      - the values are below a certain threshold
         *              if not, return an empty list that indicates failure 
         *      - all the values are from the same person 
         *              - if so, return that name 
         *              - we have found a match */
        public void recognise(int i) {
                LBPHAlgorithm lbph = new LBPHAlgorithm(grayscale_image, training_set);

                names_recognised.add(lbph.startLBPProcess());

                // saveLBPImage(lbph.getLBPMat(), "roderick", i);
                // saveGrayScaleImage(lbph.getGrayScaleMat(), "roderick", i);
                
                String final_name = null;
                Set<String> distinct = new HashSet<>(names_recognised);
                if (names_recognised.size() == AMOUNT_OF_SAMPLES) {
                        final_name = getResult(distinct);
                        // System.out.println("HELLO: " + final_name);
                        clearList();         
                }
        }

        public List<Person> loadTrainingSet(){
                List<Person> training_set = DatabaseAction.loadTrainingSet(); 
                return training_set;
        }

        /* calculate the percentage of the people recognised in the set. the set consists potential recognized names per frame if 80% of the names in the set are the same, thats the person recognised */
        public String getResult(Set<String> distinct){
                String result = null;
                float old_percentage = -1;
                for (String s : distinct) {
                        float percentage = (Collections.frequency(names_recognised, s) * 100) / names_recognised.size();
                        if (percentage > old_percentage && percentage >= PERCENTAGE_THRESHOLD) {
                                result = s;
                        }
                        old_percentage = percentage;
                        System.out.println(percentage + "% " + s);
                }
                return result;
        }

        public void clearList(){
                System.out.println("\n----------NEW SET----------\n");
                names_recognised.clear();
        }

        public void saveLBPImage(Mat mat, String name, int i){
                String path = "resources/trainingSet/"+name+"/lbpImages/lbp_image_"+i+".jpg";
                try {
                        BufferedImage img = Converter.Mat2BufferedImage(mat);
                        File f = new File(path);
                        ImageIO.write(img, "JPG", f);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void saveGrayScaleImage(Mat mat, String name, int i){
                String path = "resources/trainingSet/"+name+"/grayScaleImages/gray_image"+i+".jpg";
                try {
                        BufferedImage img = Converter.Mat2BufferedImage(mat);
                        File f = new File(path);
                        ImageIO.write(img, "JPG", f);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public BufferedImage getBufImage(){
                return buf_image;
        }

        public Mat getGrayImage(){
                return grayscale_image;
        }
}
