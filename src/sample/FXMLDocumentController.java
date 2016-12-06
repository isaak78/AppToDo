package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.IOException;
import java.net.URL;
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

    private MysqlConnect dc;

    /* Botão de acção do Login (ActionEvent) */
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        
        
            System.out.println("----------------[  OK Stage ]----------------");
        /* segundo FMXL para ser chamado, quando as credenciais sao true */
            Parent home_page_parent =  FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        /* se isValidCredentials devolver true o login é valido e é chamado o FXMLHomePage.fxml */




        if (isValidCredentials()) {
                app_stage.hide();
                app_stage.setScene(home_page_scene);
                app_stage.show();
            }
            /** se for false é porque na na tabela users da DB não há
             * correspondencia entre o username e a passaword
             * os campos sao limpos e entao é devoldida para o operador
             * uma menssagem de Não-autorizado: Acesso negado  */

            else {
                username_box.clear();
                password_box.clear();
                invalid_label.setText("Não-autorizado: Acesso negado\ncredenciais inválidas");
            }
    }
    
    private boolean isValidCredentials()
    {
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
            + " AND password LIKE " + "'" + password_box.getText() + "'" );

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

             System.exit(0);
            }
            System.out.println("STATUS ---> operação realizada com sucesso!");
            return let_in;
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
}
