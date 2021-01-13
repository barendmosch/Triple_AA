package vision;

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

/* this class contains the methods that make up the LBPH process, it consists of:
        - conversion of the input matrix to a 2d array with border values
        - calculating the LBP operator of every 3x3 matrix of the 2d array and save it in an array
        - creating the histogram of the LBP values
        - calculate the euclidean distances of the histogram and every histogram of the training set and saving the value in a global list
        - compare the distances by sorting the list and safe the 25 lowest values. Then check if the distance values of every name in the training set includes the 25 lowest values, if so, we have a match 
        - make the LBP Matrix, which is not used currently, but made anyway for report reasons 
        - this class also contains some getters and methods to illustrate important data */
public class LBPHAlgorithm {
        /* highest gray pixel value in the matrix, therefore in the histogram as well */
        private static final int HISTOGRAM_MAX_PIXEL = 255;
        /* amount of histograms in the database per person */
        private static final int AMOUNT_OF_HISTOGRAMS = 50;
        /* amount of samples required to get a match */
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

        /* contains the entire LBPH process, returns the name recognised in that particular frame */
        public String startLBPProcess() {
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);

                lbp_values = getLBPValues(mat_data_with_borders);
                h_recognise = createHistogram();

                String[] names = getTrainingSetNames();
                setDistances(names);
                String recognised_person = compareDistances(names);
                lbp_mat = makeLBPMatrix();

                return recognised_person;
        }

        private int[][] convertMat2DIntArrayWithBorders(Mat mat){
                return lbp.convertMat2DIntArrayWithBorders(mat);
        }

        public int[] getLBPValues(int[][] lpb_values){
                return lbp.getLBPValues(lpb_values);
        }

        public Map<Integer, Integer> createHistogram(){
                Map<Integer, Integer> histogram = new HashMap<>();
                int n = lbp_values.length;

                /* Initialise the histograms keys so that the keys which arent present in the histogram get a value of 0, so that looping through the histogram in generic */
                for(int i=0; i<HISTOGRAM_MAX_PIXEL; i++){
                        histogram.put(i, 0);
                }

                /* fill the histogram with the lbp values */
                for(int i=0; i<n; i++){
                        if (histogram.containsKey(lbp_values[i])){
                                histogram.put(lbp_values[i], histogram.get(lbp_values[i]) + 1);
                        }
                }

                return histogram;
        }

        /* get all names of the people in the database */
        public String[] getTrainingSetNames(){
                String[] result = new String[training_set.size()];
                for(int i=0; i<result.length; i++){
                        result[i] = training_set.get(i).getName();
                }
                return result;
        }

        /* Gets the data from the local mysql database, which contains the histogram data from the training set
                calculates the euclidean distance from the data and saves the distance in a list
                - for every person, get the amount of histograms,
                - for every histogram of that person, get the HashMap and convert it to an int array
                - calculate the euclidean distance between the histogram values of that person and the histogram value we want to recognise 
                - add the distance value to a list */
        public void setDistances(String[] names) {
                for(int i=0; i<names.length; i++){
                        int amount_of_histograms = training_set.get(i).getHistograms().size();
                        for (int y=0; y<amount_of_histograms; y++) {
                                Map<Integer, Integer> values_hashmap = training_set.get(i).getHistograms().get(y).getMap();
                                int[] values = Converter.hashMapToIntArray(values_hashmap);

                                double d = euclideanDistance(values);
                                e_distances_all.add(d);
                        }
                }
        }

        /* the euclidean distance is the square root of the summation of the difference between both histogram values to the power of 2
                see the thesis Section 5.1.2 for elaboration
                - h_t_arr = array of training histogram values
                - h_r_arr = array of recognition histogram values */
        public double euclideanDistance(int[] values_train){
                int[] h_t_arr = values_train;
                int[] h_r_arr = Converter.hashMapToIntArray(h_recognise);

                long summation = 0;
                
                for(int i=0; i<h_r_arr.length; i++){
                        double diff = Math.pow((h_t_arr[i] - h_r_arr[i]), 2);
                        summation += diff;
                }

                double distance = Math.sqrt(summation);
                return distance; 
        }

        /* loop through the training set and for every name loop through their euclidean distance data
                for each euclidean distance per training set it checks if the value is the same in the lowest e_distances list
                if so, add true to a boolean list
                if size is 25, than all 25 lowest distances are from the same person, set n */
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
                                return n;
                        }
                }

                return "Empty";
        }

        /* sort the euclidean distance list from low to high and save only the lowest 25 */
        public List<Double> findLowestValues(){
                List<Object> sorted_list = e_distances_all.stream().sorted().collect(Collectors.toList());
                List<Double> result = new LinkedList<>();

                for(int i=0; i<CORRECT_SAMPLES; i++){
                        double value = (double)sorted_list.get(i);
                        result.add(value);
                }

                return result;
        }

        private Mat makeLBPMatrix(){
                return lbp.makeLBPMatrix(lbp_values);
        }

        /* print the histogram, not used at the moment, but can be called whenever */
        public void printHistogram(Map<Integer, Integer> map){
                System.out.println(map);
        }

        /* some getters */
        public Mat getGrayScaleMat(){
                return mat;
        }

        public Mat getLBPMat(){
                return lbp_mat;
        }
}
