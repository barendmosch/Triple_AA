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
public class LBPHAlgorithm {
        private static final int HISTOGRAM_MAX_PIXEL = 255;
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
                for(int i=0; i<HISTOGRAM_MAX_PIXEL; i++){
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
                /* h_t = histograms training set || h_r = histogram recognise (current frame) */
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
