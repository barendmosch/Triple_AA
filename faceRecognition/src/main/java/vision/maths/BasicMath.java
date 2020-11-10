package vision.maths;

/* This class will contain all the basic mathematical function that every object can call. 
        These function are different than the matrix specific funtions */
public class BasicMath {

        public BasicMath(){}

        public int getDecimalValue(int[] a){
                int[] re_arranged_array = new int[a.length - 1];
                int result = 0;
                int middel_value = a[(a.length / 2)];

                re_arranged_array = reArrangeArray(a);

                StringBuilder bit_string = new StringBuilder();
                for(int i=0; i<re_arranged_array.length; i++){
                        if (re_arranged_array[i] < middel_value){
                                bit_string.append(0);
                        }else{
                                bit_string.append(1);
                        }
                }

                result = Integer.parseInt(bit_string.toString(), 2);
                return result;
        }

        /* Extremely ugly, but its the only way I believe
                Look at this webiste: https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ */
        public int[] reArrangeArray(int[] a){
                int[] r = new int[a.length - 1];
                for(int i=0; i<a.length; i++){
                        if (i < 3){
                                r[i] = a[i];
                        }else if(i == 3){
                                r[i] = a[5];
                        }else if(i == 4){
                                r[i] = a[8];

                        }else if(i == 5){
                                r[i] = a[7];
                        }
                        else if(i == 6){
                                r[i] = a[6];
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

        public int getAverageFromArray(int[] data){
                int sommation = 0;
                for(int i=0; i<data.length; i++){
                        sommation += data[i];
                }
                return (sommation / data.length);
        }
}
