package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import entity.Histogram;
import entity.Person;

/* A local MySQL database is set up with a training set of 10 people with each 50 histograms */
public class DatabaseAction {
        private static String url = "jdbc:mysql://localhost:3306/TrainingSet?useSSL=false";
        private static String user = "root";
        private static String password = "&HFX6y#U.Sn-Ws%2";
        private static String DATABASE_NAME = "TrainingSet";

        /* Load all data needed for the LBPH algorithm. returns a list of persons which contains the name and a list of histgram data in the form hashmaps */
        public static List<Person> loadTrainingSet(){
                List<Person> list_of_persons = new LinkedList<Person>();
                String[] names = getAllNames();

                try (Connection con = DriverManager.getConnection(url, user, password)){
                        for(int i=0; i<names.length; i++){
                                List<Histogram> histograms = new ArrayList<>();
                                String query = "SELECT h_json FROM "+DATABASE_NAME+"."+names[i]+";";
                        
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery(query);

                                while (rs.next()) {
                                        Map<Integer, Integer> map = new HashMap<>();
                                        JSONObject obje = new JSONObject(rs.getString(1));
                                        for(int y=0; y<255; y++){
                                                map.put(y, (int)obje.get(""+y));
                                        }
                                        histograms.add(new Histogram(map));
                                }

                                list_of_persons.add(new Person(names[i], histograms));
                        }
                        con.close();
                } catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return list_of_persons;
        }

        /* Return the amount of people in the training_set database */
        public static int getCountofPeople(){
                int training_set = 0;
                
                try (Connection con = DriverManager.getConnection(url, user, password)){
                        String query = "SELECT count(*) FROM "+DATABASE_NAME+".People;";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        if (rs.next()) {
                                training_set = rs.getInt(1);
                        }
                        con.close();
                }catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return training_set;
        }

        /* Get all names of people */
        public static String[] getAllNames(){
                int size = getCountofPeople();
                String[] names = new String[size];

                try (Connection con = DriverManager.getConnection(url, user, password)){
                        String query = "SELECT name FROM "+DATABASE_NAME+".People;";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        int i = 0;

                        while (rs.next()) {
                                names[i] = rs.getString(1);
                                i++;
                        }
                        
                        con.close();
                }catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return names;
        }

        /* Get all histogram values for a given person */
        /* CURRENTLY NOT IN USE */
        public static int[] getAllHistogramValues(int size, String name, int index){
                int[] result = new int[size];

                try (Connection con = DriverManager.getConnection(url, user, password)){
                        String query = "SELECT h_json FROM "+DATABASE_NAME+"."+name+" WHERE id = "+(index+1)+";";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        if (rs.next()) {
                                JSONObject obje = new JSONObject(rs.getString(1));
                                for(int i=0; i<result.length; i++){
                                        result[i] = (int)obje.get(""+i);
                                }
                        }

                        con.close();
                } catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return result;
        }
}
