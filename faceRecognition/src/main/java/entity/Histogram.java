package entity;

import java.util.Map;

/* Histogram entity class containing the HashMap representing the histogram of the LBP values */
public class Histogram {
        private Map<Integer, Integer> map;
        
        public Histogram(Map<Integer, Integer> map){
                this.map = map;
        }

        public Map<Integer,Integer> getMap(){
                return map;
        }
        
}
