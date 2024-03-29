package vision.maths;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/* this class contains the more mathematical operations of the LBP algorithm. It contains the following:
        - the conversion of the input Matrix to a 2D array and adding the border values for easier making the 3x3 matrices
        - calculating the LBP values by finding the neighbourhood of the central pixel and looping clockwise through the 
        neighbourhood and creating a bitstring based on the difference between the cental pixel and neighbourhood
        - making the lbp matrix containing the lbp operators, which atm is not needed for the recognition, 
        but still can be used to create the image */
public class LBP {
        /* indicating the border of the matrix, makes it easier to create the neighbourhood of the central pixel */
        private static final int BORDER_VALUE = -1;
        /* amount of neighbours of the central pixel */
        private static final int RADIUS = 8;

        private Mat mat;
        
        public LBP(Mat mat){
                this.mat = mat;
        }

        /* convert the input matrix to 2D int array with borders */
        public int[][] convertMat2DIntArrayWithBorders(Mat mat){
                /* adding 2 to the rows and cols for making the border around the values
                        so that its easier to create arrays of the neighbours */
                int borders = 2;
                int[][] values = new int[mat.rows() + borders][mat.cols() + borders];

                /* Loop through the rows and cols of the matrix,
                        if row or col is the border, add -1 to indicate the border
                        if not, fill the 2D array with the corresponding value, minus -1 because of the border */
                for (int i=0; i<mat.rows() + borders; i++){
                        for(int y=0; y<mat.cols() + borders; y++){
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

        /* for every pixel in the 2D array save the central pixels and the neighbourhood of that central pixel
                it builds a bit string based on the difference between the central pixel and the neighbourhood pixels
                convert the 8 bit string to decimal that makes the LBP operator
                add the LBP operator to an array
                return the array */
        public int[] getLBPValues(int[][] values){
                int[][] all_the_neighbours = new int[(int)mat.total()][RADIUS];
                int[] middle_values = new int[(int)mat.total()];

                /* Loop through the 2D array with borders
                        Ignoring the border values, 
                                for every pixel value fill the neighbour array with the pixel neighbours 
                        For corners and edges, the array can contain border values. These will be handled later 
                        After that, add the neighbour array to the 2D neighbour array, also save the pixel value, called middle_value, for later */
                int x = 0;
                for(int i=0; i<values.length; i++){
                        for(int y=0; y<values[i].length; y++){
                                int[] neighbours = new int[RADIUS];
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

                int[] lbp_values = new int[(int)mat.total()];

                /* Loop through all the neighbours to rearrange the array so that we can determine the decimal value correctly */
                for(int i=0; i<all_the_neighbours.length; i++){
                        int[] new_array = BasicMath.reArrangeArray(all_the_neighbours[i]);
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

        /* convert an array of LBP operators to a Matrix */
        public Mat makeLBPMatrix(int[] decimals){
                /* Convert the 1D array to 2D so it is easier to fill the matrix */
                int[][] lbp_values = new int[mat.rows()][mat.cols()];
                lbp_values = BasicMath.arrayTo2DArray(decimals, lbp_values);
                
                /* Define the LBP matrix with the same dimensions as the input grayscale image */
                Mat lbp_mat = new Mat(mat.rows(), mat.cols(), CvType.CV_32F);

                /* Loop through the dimensions and fill the matrix row by row */
                for(int i=0; i<lbp_mat.rows(); i++){
                        int size = lbp_values[i].length;
                        float[] row_data = new float[size];
                        for(int y=0; y<size; y++){
                                row_data[y] = lbp_values[i][y];
                        }
                        lbp_mat.put(i, 0, row_data);
                }

                return lbp_mat;
        }

}
