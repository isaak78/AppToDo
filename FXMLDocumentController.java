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
 *
 * https://www.dailycred.com/article/bcrypt-calculator
 *
 */

import java.io.IOException;
import java.io.InputStream;


public class FXMLDocumentController implements Initializable {

    /* declaradas as variaveis com a tag @FXML */

    @FXML private Label label;
    @FXML private AnchorPane home_page;
    @FXML private TextField username_box;
    @FXML private TextField password_box;
    @FXML private Label invalid_label;
    @FXML private CheckBox checkbox;

    private MysqlConnect dc;
    int control =1;





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







        if (isValidCredentials()) {
            Properties defaultProps = new Properties();
            FileInputStream in = new FileInputStream("config.properties");
            Properties prop = new Properties(defaultProps);

            prop.load(in);

            if(checkbox.isSelected()){
                OutputStream output = new FileOutputStream("config.properties");

                System.out.println(" CheckBoX ON");
                prop.setProperty("chkbx", "_ON");
                prop.setProperty("username", username_box.getText());
                prop.setProperty("password", password_box.getText());
                prop.store(output, null);
            }
           if(!(checkbox.isSelected())){
               OutputStream output = new FileOutputStream("config.properties");

                System.out.println(" CheckBoX OFF");
                prop.setProperty("username", " ");
                prop.setProperty("password", " ");
                prop.setProperty("chkbx", "_OFF");
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
        invalid_label.setVisible(true);
        username_box.clear();
        password_box.clear();
        control++;
        }
    }


    private void lockAccount(String block) throws SQLException {
        FXMLHomePageController.isUsernameTaken(block);
        String query = "UPDATE form2.users SET users.enabled=true WHERE (username='"+block+"' and userid <> 0);";
        if(FXMLHomePageController.isUsernameTaken(block)){
            System.out.println(query);
            FXMLHomePageController.insertStatement(query);
            System.out.println("STATUS ---> User blocked: "+block);
        }
    }


    private boolean isValidCredentials() {

        boolean let_in = false;
        System.out.println( "SELECT password FROM users WHERE username LIKE '" + username_box.getText()+  "' AND enabled = true" );

        Statement stmt = null;
        try {
            Connection conn = dc.ConnectDb();

            conn.setAutoCommit(false);
            String passc = password_box.getText();

            System.out.println("STATUS ---> Conectado com sucesso na DB!");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT password FROM users WHERE username LIKE '" + username_box.getText()+  "' AND enabled = true" );


            while ( rs.next() ) {
                 if (!(rs.getString("password").isEmpty())) {

                     String password = rs.getString("password");
                     System.out.println("Password = " + password);
                     if(SecurityKey.autox(passc,password))
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
        try ( FileInputStream input = new FileInputStream("config.properties")) {
            // load a properties file
            prop.load(input);
            username_box.setText(prop.getProperty("username"));
            password_box.setText(prop.getProperty("password"));
            // get the property value and print it out
            System.out.println("Username - " + prop.getProperty("username"));
            System.out.println("Password - " + prop.getProperty("password"));
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // username_box.clear();
        checkbox.setSelected(false);


        try {
            Properties defaultProps = new Properties();
            FileInputStream in = new FileInputStream("config.properties");
            Properties prop = new Properties(defaultProps);

            prop.load(in);
            String foo = prop.getProperty("chkbx");
            System.out.println(foo);
            if(prop.getProperty("chkbx").contains("ON")){
                System.out.println("FOO -> "+foo);
                checkbox.setSelected(true);
                controlChek();

            }
            else if(prop.getProperty("chkbx").contains("OFF")) {
                checkbox.setSelected(false);
            }
                } catch (IOException e) {
                e.printStackTrace();
            }
        }



    
}
