package vision;

import converters.Converter;
import javafx.scene.control.Label;
import people.TrainingSet;
import vision.maths.LBP;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

public class LBPHAlgorithm {
        public static final int NUMBER_OF_REGIONS = 256;

        /* mat is considered the grayscale mat */
        private Mat mat;
        /* output mat filled with lbp values */
        private Mat lbp_mat;
        private LBP lbp;
        private int[] lbp_values;
        private TrainingSet current_training_set;
        private List<Double> e_distances = new ArrayList<Double>();
        private String[] labels = {"barend", "Theo", "yvonne"};
        private int TRAINING_SET = labels.length;
        private Map<Integer, Integer> h_rec;

        public LBPHAlgorithm(Mat mat) {
                this.mat = mat;

                lbp = new LBP(this.mat);
        }

        public void startLBPProcess(TrainingSet barend) {
                current_training_set = barend;
                /*
                 * Here we will convert the input image into a 2D array with borders around the
                 * border line values this is for making the neighbours around a pixel easier
                 */
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);

                /*
                 * This will loop through the 2D array to get all the values and store them in a
                 * 1D array called the middle values it then stores the neighbours surrounding
                 * the middle_values in a separate array, since we added the borders, this will
                 * be always 8 neighbours After that it will calculate the 8 bit value in
                 * decimal from the 8 neighbours, going clockwise. See:
                 * https://iq.opengenus.org/lbph-algorithm-for-face-recognition/ for more
                 * information about the bit_string It will return all the decimal values needed
                 * to create the output LBP matrix
                 */
                lbp_values = getLBPValues(mat_data_with_borders);
                /* CREATING THE TRAINING HISTOGRAMS IS ONLY ONCE, THIS SHOULD BE IN A DATABASE */
                // createTrainDingHistograms();
                
                h_rec = createHistogram();

                compareHistograms();

                /* WHAT DOES THIS DO?
                        It makes 3 different lists per person, filters their euclidean distance
                        Sorts the whole list to get the 20 lowest values
                        checks if the lowest values arent above a certain threshold to ensure reliability
                        checks of all 20 lowest values are from 1 label
                        if so, recognision succesful */
                /* The 20 lowest values need to be from 1 label and under a certain threshold to correctly identify the person */
                /* TODO, make it cleaner, make classes and loops generic */
                List<Double> e_distances_barend = new ArrayList<Double>();
                List<Double> e_distances_theo = new ArrayList<Double>();
                List<Double> e_distances_yvonne = new ArrayList<Double>();
                List<Double> lowest = new ArrayList<Double>();

                for(int i=0; i<e_distances.size(); i++){
                        if(i < 50){
                                e_distances_barend.add(e_distances.get(i));
                        }else if(i >= 50 && i < 100){
                                e_distances_theo.add(e_distances.get(i));
                        }else{
                                e_distances_yvonne.add(e_distances.get(i));
                        }
                }

                List<Object> sorted_list = e_distances.stream().sorted().collect(Collectors.toList());

                for(int i=0; i<20; i++){
                        double value = (double)sorted_list.get(i);
                        if(value > 5000.0){
                                System.out.println(i + "|| Value is too high, cant have a valid match");
                                break;
                        }
                        
                        lowest.add((double)sorted_list.get(i));
                        System.out.println("lowest:" + lowest.get(i));
                }

                int j = 0;
                for(int i=0; i<lowest.size(); i++){
                        boolean ans = e_distances_barend.contains(lowest.get(i));
                        if(!ans){
                                System.out.println(i + " || NOT BAREND");
                                break;
                        }else{
                                j++;
                        }
                        if(j == lowest.size()){
                                System.out.println("HELLO BAREND");
                        }
                }
                
                j = 0;
                for(int i=0; i<lowest.size(); i++){
                        boolean ans = e_distances_theo.contains(lowest.get(i));
                        if(!ans){
                                System.out.println(i + " || NOT THEO");
                                break;
                        }else{
                                j++;
                        }
                        if(j == lowest.size()){
                                System.out.println("HELLO THEO");
                        }
                }

                j = 0;
                for(int i=0; i<lowest.size(); i++){
                        boolean ans = e_distances_yvonne.contains(lowest.get(i));
                        if(!ans){
                                System.out.println(i + " || NOT YVONNE");
                                break;
                        }else{
                                j++;
                        }
                        if(j == lowest.size()){
                                System.out.println("HELLO YVONNE");
                        }
                }

                /*
                 * Convert the 1D array to 2D for easier insertion. Then make the LBP matrix out
                 * of the 2D LBP values
                 */
                /*
                 * Add a failsafe in case making the LBP isnt working and therefore the lbp_mat
                 * object is NULL
                 */
                lbp_mat = makeLBPMatrix(lbp_values);
        }

        public void compareHistograms(){
                for(int y=0; y<TRAINING_SET; y++){
                        System.out.println("Training set of: " + labels[y]);
                        for(int i=0; i<50; i++){
                                Map<Integer, Integer> h = ReadFile(i, labels[y]);
                                double d = EuclideanDistance(h);
                                e_distances.add(EuclideanDistance(h));
                                // System.out.println("Distance of histogram: " + i + ": " + d);
                        }
                        System.out.println("\n");
                }
        }

        public double EuclideanDistance(Map<Integer, Integer> h_train){
                Iterator it_t = h_train.entrySet().iterator();
                Iterator it_r = h_rec.entrySet().iterator();

                int[] h_t_arr = new int[h_train.size()];
                int[] h_r_arr = new int[h_rec.size()];

                int i = 0;
                while(it_t.hasNext()){
                        Map.Entry pair = (Map.Entry)it_t.next();
                        h_t_arr[i] = (int)pair.getValue();
                        i++;
                }

                i = 0;
                while(it_r.hasNext()){
                        Map.Entry pair = (Map.Entry)it_r.next();
                        h_r_arr[i] = (int)pair.getValue();
                        i++;
                }


                long summation = 0;
                
                for(i=0; i<h_r_arr.length; i++){
                        double diff = Math.pow((h_t_arr[i] - h_r_arr[i]), 2);
                        summation += diff;
                }

                double distance = Math.sqrt(summation);
                return distance;
        }

        public Map<Integer, Integer> ReadFile(int i, String name){
                Map<Integer, Integer> h = new HashMap<>();

                String file_path = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceRecognition/resources/trainingSet/"+name+"/histograms/histo" + i + ".txt";

                File h_file = new File(file_path);
                Scanner myReader;
                try {
                        myReader = new Scanner(h_file);
                      
                        while(myReader.hasNextLine()){
                                String data = myReader.nextLine();
                                String[] strArr = data.split(",");

                                h.put(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]));
                        }
        
                        myReader.close();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }

                return h;
        }

        public Map<Integer, Integer> createHistogram(){
                Map<Integer, Integer> histogram = new HashMap<>();
                int n = lbp_values.length;

                /* Initialise the histograms keys so that the keys which arent present in the histogram get a value of 0 */
                for(int i=0; i<255; i++){
                        histogram.put(i, 0);
                }

                for(int i=0; i<n; i++){
                        if (histogram.containsKey(lbp_values[i])){
                                // System.out.println(lbp_values[i]);
                                histogram.put(lbp_values[i], histogram.get(lbp_values[i]) + 1);
                        }
                }

                return histogram;
        }

        /* Make histgrams in the form of a hashmap for reduced complecity: O(n) */
        public void createTrainingHistograms() {
                current_training_set.addHistogram(createHistogram());
        }

        public void safeHistograms() {
                String file_path = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceRecognition/resources/trainingSet/theo/histograms/";
                for (int i = 0; i < current_training_set.training_histograms.size(); i++) {
                        String file_name = file_path + "histo" + i + ".txt";
                        try {
                                PrintWriter out = new PrintWriter(file_name);
                                for(Map.Entry<Integer, Integer> entry : current_training_set.getHistograms().get(i).entrySet()){
                                        String text = entry.getKey() + "," + entry.getValue();
                                        out.println(text);
                                }
                                out.close();
                        } catch (FileNotFoundException e) {
                                e.printStackTrace();
                        }
                }
        }

        private int[][] convertMat2DIntArrayWithBorders(Mat mat){
                return lbp.convertMat2DIntArrayWithBorders(mat);
        }

        private int[] getLBPValues(int[][] lpb_values){
                return lbp.getLBPValues(lpb_values);
        }

        private Mat makeLBPMatrix(int[] lbp_values){
                return lbp.makeLBPMatrix(lbp_values);
        }

        public Mat getGrayScaleMat(){
                return mat;
        }

        public Mat getLBPMat(){
                return lbp_mat;
        }
}
