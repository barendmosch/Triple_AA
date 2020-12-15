package vision.maths;

/* This class will contain all the basic mathematical function that every object can call. 
        These function are different than the matrix specific funtions */
public class BasicMath {

        public BasicMath(){}

        public static int[][] arrayTo2DArray(int[] input, int[][] output){
                int[][] output_decimal_region = output;

                /* Convert the 1D decimal array to a 2D for an easier transition to an output matrix */
                int z = 0;
                for(int i=0; i<output_decimal_region.length; i++){
                        for(int y=0; y<output_decimal_region[i].length; y++){
                                output_decimal_region[i][y] = input[z];
                                z++;
                        }
                }
                return output_decimal_region;
        }

        /* rearrange the array to make it available to bit conversion in a clockwise way.
                It is extremely ugly, but it works, look here https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ */
        public static int[] reArrangeArray(int[] a){
                int[] r = new int[a.length];
                for(int i=0; i<a.length; i++){
                        if (i < 3){
                                r[i] = a[i];
                        }else if(i == 3){
                                r[i] = a[4];
                        }else if(i == 4){
                                r[i] = a[7];

                        }else if(i == 5){
                                r[i] = a[6];
                        }
                        else if(i == 6){
                                r[i] = a[5];
                        }
                        else if(i == 7){
                                r[i] = a[3];
                        }
                        else{
                                continue;
                        }
                }
                return r;
        }
}
