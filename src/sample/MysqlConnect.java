package sample;

import javafx.scene.control.Alert.AlertType;
import java.sql.*;


/**
 * Created by pc on 11/19/16.
 *
 * https://lowendbox.com/blog/getting-started-with-mysql-over-ssl/
 */

public class MysqlConnect {
    Connection conn = null;
    public static Connection ConnectDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://172.17.0.2/form2?useSSL=false","root","xxxxxx");
            System.out.println("Connection success!");//j"root",("jdbc:mysql://192.168.1.216/form2","isaak","xxxxxx"
            return conn;
        } catch(Exception e){
            System.err.println(e);
            UtilsForm.alertMsg(AlertType.ERROR,e.toString());
            return null;
        }
    }


}

