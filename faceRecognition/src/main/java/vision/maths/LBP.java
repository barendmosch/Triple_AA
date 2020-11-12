package vision.maths;

import java.util.LinkedList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import converters.Converter;

public class LBP {

        private Converter conv;
        private BasicMath basicMath;
        private Mat main_image_mat;

        private static final int BORDER_VALUE = -1;

        public LBP(Mat mat){
                main_image_mat = mat;
                conv = new Converter();
                basicMath = new BasicMath();
        }

        public int[][] convertMat2DIntArrayWithBorders(Mat mat){
                /* we add 2 to the rows and cols because we want to make a border around the values
                        so that its easier to create arrays of the neighbours of every value */
                int[][] values = new int[mat.rows() + 2][mat.cols() + 2];

                /* Loop through the rows and cols of the matrix,
                        if row or col is the border, add -1 to indicate the border
                        if not, fill the 2D array with the corresponding value, minus -1 because of the border */
                for (int i=0; i<mat.rows() + 2; i++){
                        for(int y=0; y<mat.cols() + 2; y++){
                                /* Add bounderies surrounding the values so that making 3x3 matrices is easier (neighbours are always 8) */
                                if (i == 0 || i == mat.rows() + 1 || y == 0 || y == mat.cols() + 1){
                                        values[i][y] = BORDER_VALUE;
                                }else{
                                        values[i][y] = (int)mat.get(i - 1, y - 1)[0];
                                }
                        }
                }

                return values;
        }

        public int[] getLBPValues(int[][] values){
                /* hold every neighbour array for every value */
                int[][] all_the_neighbours = new int[(int)main_image_mat.total()][8];
                int[] middle_values = new int[(int)main_image_mat.total()];

                /* Loop through the 2D array with borders
                        Ignoring the border values, 
                                for every pixel value fill the neighbour array with the pixel neighbours 
                        For corners and edges, the array can contain border values. These will be handled later 
                        After that, add the neighbour array to the 2D neighbour array, also save the pixel value, called middle_value, for later */
                int x = 0;
                for(int i=0; i<values.length; i++){
                        for(int y=0; y<values[i].length; y++){
                                int[] neighbours = new int[8];
                                if (values[i][y] != BORDER_VALUE){
                                        neighbours[0] = values[i-1][y-1];
                                        neighbours[1] = values[i-1][y];
                                        neighbours[2] = values[i-1][y+1];

                                        neighbours[3] = values[i][y-1];
                                        neighbours[4] = values[i][y+1];

                                        neighbours[5] = values[i+1][y-1];
                                        neighbours[6] = values[i+1][y];
                                        neighbours[7] = values[i+1][y+1];

                                        all_the_neighbours[x] = neighbours;
                                        middle_values[x] = values[i][y];

                                        x++;
                                }
                        }
                }

                int[] lbp_values = new int[(int)main_image_mat.total()];

                /* Loop through all the neighbours to rearrange the array so that we can determine the decimal value correctly */
                for(int i=0; i<all_the_neighbours.length; i++){
                        int[] new_array = basicMath.reArrangeArray(all_the_neighbours[i]);
                        int middle_value = middle_values[i];
                        StringBuilder bit_string = new StringBuilder();

                        for(int y=0; y<new_array.length; y++){
                                /* if the value isnt a border value. Check if the neighbour value is greater or smaller then the middle value
                                        append 1 if greater or the same, 0 if not */
                                if(new_array[y] != BORDER_VALUE){
                                        if(new_array[y] >= middle_value){
                                                bit_string.append(1);
                                        }else{
                                                bit_string.append(0);
                                        }
                                }
                        }
                        /* Convert the bit_string to decimal and add the decimal value to the lbp_values array */
                        int decimal_value = Integer.parseInt(bit_string.toString(), 2);
                        lbp_values[i] = decimal_value;
                }
                
                return lbp_values;
        }

        public Mat makeLBPMatrix(int[] decimals){
                /* Convert the 1D array to 2D so it is easier to fill the matrix */
                int[][] lbp_values = new int[main_image_mat.rows()][main_image_mat.cols()];
                lbp_values = basicMath.arrayTo2DArray(decimals, lbp_values);
                
                /* Define the LBP_OUTPUT matrix with the same dimensions as the input grayscale image */
                Mat region_in_LBP_values = new Mat(main_image_mat.rows(), main_image_mat.cols(), CvType.CV_32F);

                /* Loop through the dimensions and fill the matrix row by row */
                for(int i=0; i<region_in_LBP_values.rows(); i++){
                        int size = lbp_values[i].length;

                        float[] row_data = new float[size];
                        for(int y=0; y<size; y++){
                                row_data[y] = lbp_values[i][y];
                        }
                        region_in_LBP_values.put(i, 0, row_data);
                }

                return region_in_LBP_values;
        }

}