package vision.maths;

public class BasicMath {

        public BasicMath(){}

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
