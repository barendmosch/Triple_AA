package vision;

import converters.Converter;
import vision.entity.ListOfMatrices;
import vision.entity.PixelMatrix;
import vision.maths.MatrixMath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

/* Checklist: https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ */

public class LBPHAlgorithm {
        /* Currently not in use, but they might be used to get additional information that a MAT cant provide */
        private BufferedImage img;

        public static final int NUMBER_OF_REGIONS = 256;

        private Mat mat;
        private Converter conv;
        private ListOfMatrices list_of_pixel_matrices;
        private int[][] pixel_LBP_values;
        private MatrixMath matrixMath;

        public LBPHAlgorithm(Mat mat, BufferedImage img){
                this.mat = mat;
                this.img = img;
                list_of_pixel_matrices = new ListOfMatrices();

                conv = new Converter();
                matrixMath = new MatrixMath(this.mat);
        }

        public void startLBPHProcess(){
                /* Start the region dividing process. NUMBER_OF_REGIONS indicates the number of output regions in total
                        so that means there will be 16 regions per row, so 256 regions in total
                        Every region consists of rows containing the RGB value of the corresponding pixel, these are the first 3 values
                        example row_1 = [5, 255, 7, 9, 254, 20, 7, 255, 6] 
                        This the the first row of the first region containing 3 sets of RGB values
                        regions.cols() sees every set of 3 values as 1 column. 
                        Thats the reason why regions.cols() and regions.rows() are the same, but there are more values in each row  */
                LinkedList<Mat> regions = makeRGBRegions();

                /*  Make a PixelMatrix object for every region containing:
                         the region with RGB values
                         corresponding gray value 3x3 matrices for that region */
                for (Mat reg: regions){
                        PixelMatrix pixelMatrix = new PixelMatrix(reg, makeMatrices(reg));
                        list_of_pixel_matrices.addMatrixToList(pixelMatrix);
                }

                /* Loop through every RGB value matrices for every region and calculate binary value of the 3x3 matrix
                        The binary value is calculated by taking the middle value if the 3x3 matrix and comparing the 8 neighbours
                        to the value in a clockwise fashion. If the neighbour is greater than the middle value then we add a 1 to the binary string. Once completed, convert the binary string to a decimal number and put it into the array */
                pixel_LBP_values = convertGrayPixelMatrixToDecimalValues();

                /* Convert the array with all the grayScale intensity values of every region to the output matrix */
                makeOutputMatrix();

                /* Print all regions */
                // list_of_pixel_matrices.printAllRegions();
                /* Print all 3x3 gray pixel matrices */
                // list_of_pixel_matrices.printAllGrayPixelMatrices();
        }    

        public LinkedList<Mat> makeRGBRegions(){
                return matrixMath.makeRGBRegions();
        }

        public LinkedList<Mat> makeMatrices(Mat reg){
                return matrixMath.makeMatrices(reg);
        }

        public int[][] convertGrayPixelMatrixToDecimalValues(){
                return matrixMath.convertGrayPixelMatrixToDecimalValues(list_of_pixel_matrices);
        }

        public void makeOutputMatrix(){
                matrixMath.makeOutputMatrix(list_of_pixel_matrices, pixel_LBP_values);

        }

        public void saveOneImage(Mat mat, int i){
                try {
                        BufferedImage img = conv.Mat2BufferedImage(mat);
                        File f = new File("resources/final_image" + i + ".jpg");
                        ImageIO.write(img, "JPG", f);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
