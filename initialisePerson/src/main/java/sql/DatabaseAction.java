package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAction {
        private static String url = "jdbc:mysql://localhost:3306/TrainingSet?useSSL=false";
        private static String user = "root";
        private static String password = "&HFX6y#U.Sn-Ws%2";

        public static Boolean addNewPerson(String name){
                boolean result = true;
                try (Connection con = DriverManager.getConnection(url, user, password)){
                        String query = "INSERT INTO People(name) VALUES ('"+name+"');";
                        Statement st = con.createStatement();
                        result = st.execute(query);

                        query = "CREATE TABLE IF NOT EXISTS `"+name+"` ( `id` int(50) NOT NULL AUTO_INCREMENT, `h_json` JSON NOT NULL, PRIMARY KEY (`id`));";
                        st = con.createStatement();
                        result = st.execute(query);

                        con.close();
                }catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return result;
        }

        public static boolean saveHistograms(String name, String data){
                boolean result = false;

                try (Connection con = DriverManager.getConnection(url, user, password)){
                        String query = "INSERT INTO "+name+" (h_json) VALUES ('"+data+"');";
                        Statement st = con.createStatement();
                        result = st.execute(query);
                        con.close();
                } catch (SQLException ex) {
                        ex.printStackTrace();
                } 

                return result;
        }
}
