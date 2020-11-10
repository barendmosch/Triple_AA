package vision.maths;

import java.util.LinkedList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import converters.Converter;
import vision.LBPHAlgorithm;
import vision.entity.ListOfMatrices;

public class MatrixMath {

        private Converter conv;
        private BasicMath basicMath;
        private Mat main_image_mat;

        public MatrixMath(Mat mat){
                main_image_mat = mat;
                conv = new Converter();
                basicMath = new BasicMath();
        }

        public int[][] convertGrayPixelMatrixToDecimalValues(ListOfMatrices list_of_pixel_matrices){
                int number_of_regions = list_of_pixel_matrices.getAllPixelMatricesObjects().size(); // 256
                int region_gray_pixel_mats_size = list_of_pixel_matrices.getAllGrayPixelMatsPerRegion(0).size(); // 36

                /* Define an array that holds all the gray pixel values (average of every RGB value) for all the regions         */
                int[][] pixel_LBP_values = new int[number_of_regions][region_gray_pixel_mats_size];

                /* Fill the array by looping through the regions mats */
                for(int i=0; i<number_of_regions; i++){
                        LinkedList<Mat> region_gray_pixel_mats = list_of_pixel_matrices.getAllGrayPixelMatsPerRegion(i);
                        for (int y=0; y<region_gray_pixel_mats_size; y++){
                                Mat mat = region_gray_pixel_mats.get(y);

                                int[] mat_array = fillArray(mat);
                                pixel_LBP_values[i][y] = basicMath.getDecimalValue(mat_array);
                
                                // System.out.println(" Decimal vlaue of the values greater than middle: " + decimal_value_from_binary_array);
                        }
                }

                return pixel_LBP_values;
        }

        /* Useful website for setting matrices values:
                - https://www.tutorialspoint.com/how-to-declare-opencv-mat-object-using-java */
        
                /* Make a list of 3x3 matrices in the region. The 3x3 matrices are used get the grayValue intensities */
        public LinkedList<Mat> makeMatrices(Mat region){
                /* Define the 3 by 3 Matrix to perform the intensity algorithm on */
                Mat gray_pixel_mat = new Mat(3, 3, CvType.CV_16S);
                LinkedList<Mat> result = new LinkedList<Mat>();

                /* Row dump shows 3 times more numbers than the number of column. Example. cols() = 19, numbers in row = 57.
                        This is because regions.get(i).dump() shows the RGB value, which is a 1x3 matrix, considered as 1 column
                        That explains why making submats require the range from 0 - 1 instead of 0 - 3 regarding the columns */

                /* Loop through the rows and colums to set each index in the matrix with the corresponding value from the grayscale value in the array 
                       Indices explained:
                                x: Indicates the rows of the regions matrix. Make the 3x3 matrix of the first row, then go down
                                z: Indicates the columns of the regions matrix, make the 3x3 of the first row first column, go col right, row down
                                i: Indicates the traversal of the 3x3 matrix rows to get the correct RGB values 
                                y: Indicates the traversal of the 3x3 matrix cols to get the correct RGB values 

                       Failsafes are added to ensure the indices arent going out of bounds, because of the += 3 

                       Loops explained:
                                Loop through the rows and columns (x & z ) of the region matrix, increasing by 3 because we want to make
                                3x3 matrices. Loop through the rows of the 3x3 matrix to gain access to the first row. 
                                Loop through the columns of the rows and get the first column of the row (which are the RGB values (3))
                                Get the average value of the 3 (which I think is the grayValue). And set the first value in the 3x3 matrix
                                to the grayValue. After putting every grayValue to the 3x3 matrix, transpose the matrix because I might did something wrong with the indices. And add the 3x3 matrix to the list.

                       Submat indices examples:
                                                          rows | cols
                         Mat test = regions.get(0).submat(0, 1, 0, 1);
                         Mat test2 = regions.get(0).submat(1, 2, 0, 1);
                         Mat test4 = regions.get(0).submat(2, 3, 2, 3);
                         Mat test5 = regions.get(0).submat(0, 1, 3, 4); */
                for (int x=0; x<region.rows(); x+=gray_pixel_mat.rows()){
                        for (int z=0; z<region.cols(); z+=gray_pixel_mat.cols()){
                                for (int i=z; i<gray_pixel_mat.rows() + z; i++){
                                        if (i >= region.cols()){
                                                break;
                                        }

                                        /* i - z because gray_pixel_mat.rows() = 3, so we cant go over 3 */
                                        Mat row = gray_pixel_mat.row(i - z);
                                        for (int y=x; y<gray_pixel_mat.cols() + x; y++){
                                                if (y >= region.rows()){
                                                        break;
                                                }

                                                // System.out.println("x: " + x +" || z: " + z + " || i: " + i + " || y: " + y);
                                                /* y - x because gray_pixel_mat.cols() = 3, so we cant go over 3 */
                                                Mat indices = row.col(y - x);

                                                Mat m = region.submat(y, y+1, i, i+1);

                                                Scalar scalar = new Scalar(getGrayValue(m));
                                                indices.setTo(scalar);
                                        }
                                }
                                /* Transpose the matrix to get the columns the correct way, Scalar works in a mysterious ways */
                                gray_pixel_mat = gray_pixel_mat.t();
                                result.add(gray_pixel_mat);
                        }
                }
                return result;
        }

        public LinkedList<Mat> makeRGBRegions(){
                int line_end = (int)Math.sqrt(main_image_mat.total()) / (int)(Math.sqrt(LBPHAlgorithm.NUMBER_OF_REGIONS)); 
                LinkedList<Mat> rgb_regions = new LinkedList<Mat>();
                
                /* line_end is the end of the last row and column in the matrix 
                        This loop will keep making submatrices, starting from first row to the end of the columns 
                        then go down till it reaches the end of rows 
                        Example of submat method:
                                line_end = 16
                                i = 0
                                line_end + i = 16
                                y = 16
                                line_end + y = 32
                                Make a matrix of the first 16 rows and the second set of 16 columns of the main matrix 
                                This will be the second region */
                for(int i=0; i<main_image_mat.rows() - line_end; i+=line_end){
                        for(int y=0; y<main_image_mat.cols() - line_end; y+=line_end){
                                Mat rgb_mat = main_image_mat.submat(i, line_end + i, y, line_end + y);
                                rgb_regions.add(rgb_mat);
                        }
                }

                return rgb_regions;
        }

        public void makeOutputMatrix(ListOfMatrices list_of_pixel_matrices, int[][] pixel_LBP_values){
                /* dimensions = 256 same as the number of regions || same as original image */
                int number_of_regions = list_of_pixel_matrices.getAllRegions().size();
                int sqr_number_of_regions = (int) Math.sqrt(number_of_regions);
                LinkedList<Mat> allRegionOutputMatrices = new LinkedList<Mat>();

                /* Get all 6x6 or 5x5 matrices with grayscale intensities values for every region */
                for(int i=0; i<number_of_regions; i++){
                        int size = pixel_LBP_values[i].length;
                        // System.out.print("\nNumber of values in regions: " + i + " with size: " + size + "\n\n"); // i = index of region (0 .. 256) size = number of values. 36 or 49 (6x6) (7x7) MAT
                        float[] dataa = new float[size];
                        int dims = (int) Math.sqrt(size);
                        Mat region_mat = new Mat(dims, dims, CvType.CV_32F);

                        for(int y=0; y<size; y++){
                                dataa[y] = pixel_LBP_values[i][y];
                        }

                        region_mat.put(0, 0, dataa);
                        allRegionOutputMatrices.add(region_mat);

                        /* print these for testing to print the regions filled with gray pixel intensities (0 is the top left region) */
                        // if(i == 0 || i == 1 || i == 14 || i == 15 || i == 16 || i == 31){
                        //         System.out.println("i: "+ i + "\n");
                        //         System.out.println(region_mat.dump());
                        // }
                        // saveOneImage(region_mat, y);
                }

                /* This is gonna be the output grayScale matrix */
                /* This is gonna be a 96x96 matrix. Because 
                       there are 256 (16x16) region, each region is a 6x6 matrix.
                       16 regions each row each col, means 6x16 values in each row and col */
                int region_matrix_dims = (int) Math.sqrt(allRegionOutputMatrices.get(0).total());
                /*      (96)          =         6          *         16            */
                int values_in_row_col = region_matrix_dims * sqr_number_of_regions;
                Mat big_mat = new Mat(values_in_row_col, values_in_row_col, CvType.CV_32F);
                float[] row = new float[values_in_row_col];
               
                /* Loop through every column */
                for(int l=0; l<sqr_number_of_regions; l++){
                        /* Loop through the dimensions of the output matrix (96) to set every row of every region */
                        for(int k=0; k<region_matrix_dims; k++){
                                int i = 0; // indicating the place in the array. 0-6 indices equals for row of the region, next 6-12 equals next row 
                                int z = (l * sqr_number_of_regions) - 1; // indicating the region. First 16 regions are the top 6x6 matrices in the out_put matrix
                                                                         // l indicates when the first regions rows are done, and when we need to go down the next set region rows

                                /* Loop through all the values in the array */
                                for(int y=0; y<values_in_row_col; y++){
                                        /* If we reach the end of the row of each region matrix (6)
                                                reset i to 0 if we're at the first row of the region, else reset i by multiples of 6 for the next row in the region
                                                also z + 1 for indicating to get data from the next region */
                                        if(y % region_matrix_dims == 0){
                                                i = 0 + (k * region_matrix_dims);
                                                z++;
                                        }

                                        row[y] = pixel_LBP_values[z][i];

                                        // System.out.print(row[y] + "\n");
                                        // System.out.print("i: " + i + " | z: " + z + " | y: " + y + " | k: " + k + " | l: " + l + "\n");

                                        /* i + 1 for getting data out of the next index in the row array */
                                        i++;
                                }
                                // System.out.print("i: " + i + " | z: " + z + " k: " + k + " | l: " + l + "\n");
                                /* index is the amount of columns of the output matrix, form 0 to 95 | region_matrix_dims = 6. l = {0, 1, 2, 3, 4, 5} and k = {0, 1, 2, 3, 4, 5} in every l */
                                int index = k + (l * region_matrix_dims);
                                big_mat.put(index, 0, row);
                                // System.out.print("\n----------------------------------------\n");
                        }       
                }

                // System.out.print("OUTPUT MATRIX DUMP: \n");
                // System.out.print(big_mat.dump());
                // saveOneImage(big_mat, 10);
        }

        public int[] fillArray(Mat mat){
                int[] result = new int[(int)mat.total()];
                int x = 0;
                for(int i=0; i<mat.rows(); i++){
                        for(int y=0; y<mat.cols(); y++){
                                result[x] = (int)mat.get(i, y)[0];
                                x++;
                        }
                }
                return result;
        }

        public int getGrayValue(Mat m){
                /* Using string converter here instead of fillArray() method cause:
                        one value in matrix consists of 3 RGB values, which the matrix counts as 1 row,
                        Thats why mat.total() in fillArray() cant find all 3 values
                        The values are needed to get the average */
                int[] RGB_values = conv.stringToIntArray(m.dump());
                int avg = basicMath.getAverageFromArray(RGB_values);

                return avg;
        }
}
