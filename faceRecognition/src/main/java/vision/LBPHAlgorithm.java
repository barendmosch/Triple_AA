package vision;

import sql.DatabaseAction;
import vision.maths.LBP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.opencv.core.Mat;

import converters.Converter;
import entity.Person;
{0=2210, 1=2207, 2=177, 3=869, 4=1011, 5=94, 6=785, 7=2594, 8=216, 9=121, 10=9, 11=33, 12=736, 13=148, 14=1550, 15=5961, 16=2151, 17=1684, 18=190, 19=322, 20=112, 21=16, 22=197, 23=361, 24=911, 25=332, 26=39, 27=140, 28=2885, 29=296, 30=9251, 31=8375, 32=168, 33=130, 34=10, 35=71, 36=64, 37=9, 38=51, 39=193, 40=20, 41=11, 42=1, 43=5, 44=15, 45=7, 46=38, 47=148, 48=785, 49=281, 50=77, 51=102, 52=106, 53=6, 54=134, 55=157, 56=1146, 57=116, 58=38, 59=55, 60=4812, 61=73, 62=3685, 63=2302, 64=945, 65=107, 66=67, 67=109, 68=313, 69=6, 70=103, 71=132, 72=67, 73=9, 74=5, 75=9, 76=112, 77=6, 78=100, 79=80, 80=92, 81=16, 82=11, 83=2, 84=8, 85=1, 86=8, 87=11, 88=88, 89=10, 90=7, 91=6, 92=133, 93=7, 94=99, 95=92, 96=653, 97=172, 98=59, 99=125, 100=83, 101=5, 102=49, 103=116, 104=23, 105=4, 106=1, 107=4, 108=43, 109=15, 110=65, 111=134, 112=2542, 113=317, 114=154, 115=158, 116=111, 117=8, 118=140, 119=133, 120=3870, 121=66, 122=85, 123=108, 124=3730, 125=70, 126=2022, 127=1229, 128=137, 129=703, 130=13, 131=876, 132=69, 133=83, 134=31, 135=3044, 136=10, 137=48, 138=2, 139=35, 140=60, 141=94, 142=46, 143=3070, 144=94, 145=216, 146=10, 147=152, 148=4, 149=7, 150=4, 151=83, 152=51, 153=61, 154=4, 155=54, 156=167, 157=130, 158=249, 159=2437, 160=18, 161=26, 162=1, 163=36, 164=2, 165=8, 166=3, 167=112, 168=0, 169=5, 170=0, 171=3, 172=2, 173=0, 174=8, 175=65, 176=22, 177=67, 178=2, 179=69, 180=6, 181=8, 182=1, 183=81, 184=34, 185=46, 186=2, 187=94, 188=125, 189=115, 190=88, 191=1692, 192=608, 193=2419, 194=28, 195=3326, 196=104, 197=136, 198=64, 199=2677, 200=63, 201=149, 202=0, 203=121, 204=59, 205=106, 206=96, 207=1859, 208=123, 209=203, 210=7, 211=70, 212=5, 213=5, 214=8, 215=90, 216=103, 217=151, 218=6, 219=99, 220=126, 221=116, 222=221, 223=1321, 224=1197, 225=8237, 226=20, 227=2868, 228=91, 229=74, 230=73, 231=1773, 232=39, 233=246, 234=2, 235=77, 236=95, 237=193, 238=161, 239=2984, 240=5355, 241=6479, 242=153, 243=2126, 244=64, 245=94, 246=142, 247=1112, 248=3370, 249=2073, 250=60, 251=1532, 252=2011, 253=1321, 254=3102}
public class LBPHAlgorithm {
        public static final int NUMBER_OF_REGIONS = 256;
        private static final int HISTOGRAM_SIZE = 255;
        private static final int AMOUNT_OF_HISTOGRAMS = 50;
        private static final int CORRECT_SAMPLES = AMOUNT_OF_HISTOGRAMS / 2;

        /* mat is considered the grayscale mat */
        private Mat mat;
        /* output mat filled with lbp values */
        private Mat lbp_mat;
        private List<Person> training_set = new LinkedList<>();

        private LBP lbp;
        private int[] lbp_values;
        private Map<Integer, Integer> h_recognise;
        private List<Double> e_distances_all = new ArrayList<>();
        
        public LBPHAlgorithm(Mat mat, List<Person> training_set) {
                this.mat = mat;
                this.training_set = training_set;
                lbp = new LBP(this.mat);
        }

        public String startLBPProcess() {
                /*  Here we convert the input image into a 2D array with borders around the
                 * border line values this is for making the neighbours around a pixel easier */
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);

                lbp_values = getLBPValues(mat_data_with_borders);
                h_recognise = createHistogram();
                printHistogram(h_recognise);
                String[] names = getTrainingSetNames();
                setDistances(names);
                String recognised_person = compareDistances(names);
                lbp_mat = makeLBPMatrix();

                return recognised_person;
        }

        public int[] getLBPValues(int[][] lpb_values){
                return lbp.getLBPValues(lpb_values);
        }

        public Map<Integer, Integer> createHistogram(){
                Map<Integer, Integer> histogram = new HashMap<>();
                int n = lbp_values.length;

                /* Initialise the histograms keys so that the keys which arent present in the histogram get a value of 0 */
                for(int i=0; i<HISTOGRAM_SIZE; i++){
                        histogram.put(i, 0);
                }

                for(int i=0; i<n; i++){
                        if (histogram.containsKey(lbp_values[i])){
                                histogram.put(lbp_values[i], histogram.get(lbp_values[i]) + 1);
                        }
                }

                return histogram;
        }

        public String[] getTrainingSetNames(){
                String[] result = new String[training_set.size()];
                for(int i=0; i<result.length; i++){
                        result[i] = training_set.get(i).getName();
                }
                return result;
        }

         /* Gets the data from the local mysql database, which contains the people histogram data from the trainingset
                Calculates the euclidean distance from the data and saves the distance in an ArrayList */
        public void setDistances(String[] names) {
                /* Get the names from the people in the training set */
                for(int i=0; i<names.length; i++){
                        int amount_of_histograms = training_set.get(i).getHistograms().size();
                        for (int y=0; y<amount_of_histograms; y++) {
                                /* get current histogram map and convert to int array to get only the values */
                                Map<Integer, Integer> values_hashmap = training_set.get(i).getHistograms().get(y).getMap();
                                int[] values = Converter.HashMapToIntArray(values_hashmap);

                                /* Calculate the euclidean distances from the values and add the result to a global list */
                                double d = euclideanDistance(values);
                                e_distances_all.add(d);
                                // System.out.println("Distance of histogram: " + i + ": " + d);
                        }
                }
        }

        /* Find the {CORRECT_SAMPLES} (correct_samples if the amount of histograms / 2) lowest values return an empty list */
        public String compareDistances(String[] names){
                List<Double> lowest = findLowestValues();

                /* For every person in the training set, loop through the e_distances of that person. */
                String n = null;
                for(int y=0; y<names.length; y++){
                        List<Boolean> matches = new LinkedList<Boolean>();

                        for(int i=AMOUNT_OF_HISTOGRAMS * y; i<AMOUNT_OF_HISTOGRAMS * (y + 1); i++){
                                boolean r = lowest.contains(e_distances_all.get(i));
                                if(r) matches.add(true);
                        }

                        if(matches.size() == CORRECT_SAMPLES){
                                n = names[y];
                        }
                }

                return n;
        }

        public List<Double> findLowestValues(){
                List<Object> sorted_list = e_distances_all.stream().sorted().collect(Collectors.toList());
                List<Double> result = new LinkedList<>();

                /* Loop through the first 20 values of the sorted list of distances. Add the value to the result list */
                for(int i=0; i<CORRECT_SAMPLES; i++){
                        double value = (double)sorted_list.get(i);
                        result.add(value);
                        // System.out.println("lowest:" + result.get(i));
                }

                return result;
        }

        /* Calculate the euclidean distance between the histogram values from every person, every histogram 
                and the histogram generated from the frame we want to recognise */
        public double euclideanDistance(int[] values_train){
                int[] h_t_arr = values_train;
                int[] h_r_arr = Converter.HashMapToIntArray(h_recognise);

                long summation = 0;
                
                /* See report results section Euclidean distance for mathematical explanation */
                for(int i=0; i<h_r_arr.length; i++){
                        double diff = Math.pow((h_t_arr[i] - h_r_arr[i]), 2);
                        summation += diff;
                }

                double distance = Math.sqrt(summation);
                return distance;
        }

        private int[][] convertMat2DIntArrayWithBorders(Mat mat){
                return lbp.convertMat2DIntArrayWithBorders(mat);
        }

        private Mat makeLBPMatrix(){
                return lbp.makeLBPMatrix(lbp_values);
        }

        public void initPersonsData(String name){
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);
                lbp_values = getLBPValues(mat_data_with_borders);
                h_recognise = createHistogram();

                String json_string = Converter.buildJSONStringFromMap(h_recognise);

                DatabaseAction.saveHistograms(name, json_string);

                lbp_mat = makeLBPMatrix();
        }

        public Mat getGrayScaleMat(){
                return mat;
        }

        public Mat getLBPMat(){
                return lbp_mat;
        }


        public void printHistogram(Map<Integer, Integer> map){
                System.out.println(map);
        }
}
