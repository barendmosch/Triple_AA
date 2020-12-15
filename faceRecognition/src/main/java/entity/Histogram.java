package entity;

import java.util.Map;

public class Histogram {
        private Map<Integer, Integer> map;
        
        public Histogram(Map<Integer, Integer> map){
                this.map = map;
        }

        public Map<Integer,Integer> getMap(){
                return map;
        }
        
}
