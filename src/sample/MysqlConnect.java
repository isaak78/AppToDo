package sample;

import javafx.scene.control.Alert.AlertType;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * Created by pc on 11/19/16.
 *
 * https://lowendbox.com/blog/getting-started-with-mysql-over-ssl/
 */

public class MysqlConnect {
    Connection conn = null;
    public static Connection ConnectDb() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("jdbcproperties.xml");

            //Ficheiro properites no formato .XML
            props.loadFromXML(fis);

            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String dbaddress = props.getProperty("jdbc.dbaddress");
            String database = props.getProperty("jdbc.database");

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+dbaddress+"/"+database+
                    "?useSSL=false",username,password);
            System.out.println("Connection success!");
            return conn;
        } catch(Exception e){
            System.err.println(e);
            UtilsForm.alertMsg(AlertType.ERROR,e.toString());
            return null;
        }
    }




}

