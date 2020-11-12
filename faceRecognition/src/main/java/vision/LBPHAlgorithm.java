package vision;

import converters.Converter;
import vision.maths.LBP;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
        
public class LBPHAlgorithm {
        public static final int NUMBER_OF_REGIONS = 256;

        /* mat is considered the grayscale mat */
        private Mat mat;
        /* output mat filled with lbp values */
        private Mat lbp_mat;
        private LBP lbp;

        public LBPHAlgorithm(Mat mat){
                this.mat = mat;

                lbp = new LBP(this.mat);
        }

        public void startLBPProcess(){
                /* Here we will convert the input image into a 2D array with borders around the border line values 
                        this is for making the neighbours around a pixel easier */
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);

                /* This will loop through the 2D array to get all the values and store them in a 1D array called the middle values
                        it then stores the neighbours surrounding the middle_values in a separate array, since we added the borders, this will be always 8 neighbours
                        After that it will calculate the 8 bit value in decimal from the 8 neighbours, going clockwise.
                                See: https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ for more information about the bit_string 
                        It will return all the decimal values needed to create the output LBP matrix */
                int[] lpb_values = getLBPValues(mat_data_with_borders);

                /* Convert the 1D array to 2D for easier insertion. Then make the LBP matrix out of the 2D LBP values */
                /* Add a failsafe in case making the LBP isnt working and therefore the lbp_mat object is NULL */
                lbp_mat = makeLBPMatrix(lpb_values);
        }    

        public void createHistograms(){
                
        }

        private int[][] convertMat2DIntArrayWithBorders(Mat mat){
                return lbp.convertMat2DIntArrayWithBorders(mat);
        }

        private int[] getLBPValues(int[][] lpb_values){
                return lbp.getLBPValues(lpb_values);
        }

        private Mat makeLBPMatrix(int[] lbp_values){
                return lbp.makeLBPMatrix(lbp_values);
        }

        public Mat getGrayScaleMat(){
                return mat;
        }

        public Mat getLBPMat(){
                return lbp_mat;
        }
}
