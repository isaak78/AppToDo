package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

/*
 * Created by pc on 11/10/16.
 */

public class FXMLDocumentController implements Initializable {

    /* declaradas as variaveis com a tag @FXML */

    @FXML private Label label;
    @FXML private AnchorPane home_page;
    @FXML private TextField username_box;
    @FXML private TextField password_box;
    @FXML private Label invalid_label;
    @FXML private Label isConnected;
    @FXML private CheckBox checkbox;

    private MysqlConnect dc;
    int control =1;
    private boolean tickEnabled;

    public boolean isTickEnabled() {
        return tickEnabled;
    }




    /* Botão de acção do Login (ActionEvent) */
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        System.out.println("----------------[  OK Stage ]----------------");
        /* segundo FMXL para ser chamado, quando as credenciais sao true */
        invalid_label.setVisible(false);
        Parent home_page_parent =  FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        invalid_label.setText("Não-autorizado: Acesso negado\ncredenciais inválidas ");



        Properties prop = new Properties();




        if (isValidCredentials()) {
            if(checkbox.isSelected()){
                System.out.println(" CheckBoX ON");
                tickEnabled = true;
                OutputStream output = new FileOutputStream("config.properties");
                // write login-username to config file
                prop.setProperty("username", username_box.getText());
                prop.setProperty("password", password_box.getText());
                prop.store(output, null);
            }

            control = 1;
            app_stage.hide();
            app_stage.setScene(home_page_scene);
            app_stage.show();



        }
            /** se for false é porque na na tabela users da DB não há
             * correspondencia entre o username e a passaword
             * os campos sao limpos e entao é devoldida para o operador
             * uma menssagem de Não-autorizado: Acesso negado  */

        if(control ==3){
            control = 0;
            invalid_label.setVisible(true);
            //UtilsForm.alertMsg(Alert.AlertType.ERROR,"Não-autorizado:\nExcedeu as "+control+" tentativas de acesso");
            invalid_label.setText("Blocked User "
                    +username_box.getText()+"\nContactar o suporte técnico");
            lockAccount(username_box.getText());
            username_box.clear();
            password_box.clear();
            //System.exit(0);
        }else {
         //username_box.clear();
            //password_box.clear();
        invalid_label.setVisible(true);
        username_box.clear();
        password_box.clear();
        control++;
        }
    }


    private void lockAccount(String block) throws SQLException {
        FXMLHomePageController.isUsernameTaken(block);
        String query = "UPDATE form2.pessoa SET pessoa.estado='1'  WHERE (pessoa.username ='"+block+"' And cc <>'0');";
        if(FXMLHomePageController.isUsernameTaken(block)){
            System.out.println(query);
            FXMLHomePageController.insertStatement(query);
            System.out.println("Já foste!");

        }


    }


    private boolean isValidCredentials() {

        boolean let_in = false;
        System.out.println( "SELECT * FROM pessoa WHERE username LIKE " + "'" + username_box.getText() + "'"
            + " AND password LIKE " + "'" + password_box.getText() + "'" );
    
        Statement stmt = null;
        try {
            Connection conn = dc.ConnectDb();

            conn.setAutoCommit(false);

            System.out.println("STATUS ---> Conectado com sucesso!");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM pessoa WHERE username LIKE " + "'" + username_box.getText() + "'"
                    + " AND password LIKE " + "'" + password_box.getText() + "' AND estado = 0" );


            while ( rs.next() ) {
                 if (!(rs.getString("username").isEmpty()) || !(rs.getString("password").isEmpty())) {
                     String  username = rs.getString("username");
                     System.out.println( "Username = " + username );
                     String password = rs.getString("password");
                     System.out.println("Password = " + password);
                     let_in = true;
                 }
            }
            rs.close();
            stmt.close();
            conn.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                UtilsForm.alertMsg(Alert.AlertType.ERROR,e.toString());

            // System.exit(0);
            }
            System.out.println("STATUS ---> operação realizada com sucesso!");
            return let_in;
        
    }

    protected void controlChek(){
        Properties prop = new Properties();


        System.out.println(checkbox.isSelected());
            try (InputStream input = new FileInputStream("config.properties")) {
                // load a properties file
                prop.load(input);
                username_box.setText(prop.getProperty("username"));
                password_box.setText(prop.getProperty("password"));
                // get the property value and print it out
                System.out.println("Username - " + prop.getProperty("username"));
                System.out.println("Password - " + prop.getProperty("password"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }


    }


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // username_box.clear();
        if(isTickEnabled()){
            controlChek();
            System.out.println(isTickEnabled());
        }
        System.out.println(isTickEnabled());



    }
    
}
