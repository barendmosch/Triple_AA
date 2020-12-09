package people;

import java.util.*;

public class TrainingSet {

        public List<Map<Integer, Integer>> training_histograms;
        public List<Image> training_images;

        public TrainingSet(){
                training_histograms = new ArrayList<>();
                training_images = new ArrayList<Image>();
        }

        public void addHistogram(Map<Integer, Integer> h){
                training_histograms.add(new HashMap<>(h));
        }

        public List<Map<Integer, Integer>> getHistograms(){
                return training_histograms;
        }

        public List<Image> getImages(){
                return training_images;
        }
}
