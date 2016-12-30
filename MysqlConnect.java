package sample;

import javafx.scene.control.Alert.AlertType;
/*
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * Created by pc on 11/19/16.
 *
 * Config da Ligaçao no ficheiro config.properties
 *
 * default:
 * jdbc.database=form2
 * jdbc.dbaddress=172.17.0.2
 * jdbc.username=root
 * jdbc.password=xxxxxx
 *
 */

public class MysqlConnect {
    Connection conn = null;
    public static Connection ConnectDb() {
        try {
           // Properties props = new Properties();
           // Path propFile = Paths.get("sample/imagens/jdbc.properties");
            //props.load(Files.newBufferedReader(propFile));
            //FileInputStream fis = new FileInputStream("config.properties");
            //FileInputStream fis = new FileInputStream("jdbcproperties.xml");

            //Ficheiro properites no formato .XML
            //props.loadFromXML(fis);

            //props.load(fis);

            //Ficheiro properites no formato .XML
            String username = "root";//props.getProperty("jdbc.username");
            String password = "xxxxxx";//props.getProperty("jdbc.password");
            String dbaddress = "172.17.0.2";//props.getProperty("jdbc.dbaddress");
            String database = "form2";//props.getProperty("jdbc.database");

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

