package entity;

import java.util.List;

public class Person {
        private String name;
        private List<Histogram> hist_list;

        public Person(String name, List<Histogram> hist_list){
                this.name = name;
                this.hist_list = hist_list;
        }

        public String getName(){
                return name;
        }

        public List<Histogram> getHistograms(){
                return hist_list;
        }
}
