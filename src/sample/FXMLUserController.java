package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by pc on 11/20/16.
 */

public class FXMLUserController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Label invalid_label;
    @FXML
    ComboBox<String> accaoCombo;
    @FXML
    ComboBox<String> formadorCombo;
    @FXML
    private TableView<UserDetails> tableUser;
    @FXML
    private TableColumn<UserDetails, Integer> columnId;
    @FXML
    private TableColumn<UserDetails, String> columnName;
    @FXML
    private TableColumn<UserDetails, String> columnApelido;
    @FXML
    private TableColumn<UserDetails, String> columnCc;
    @FXML
    private MenuButton turmas;
    @FXML
    private Button btnLoad;
        // observable list para receber os dados da BD
    private ObservableList<UserDetails> data;
    private ObservableList<String>  listaAccoes = FXCollections.observableArrayList(); //Lista de Accoes
    private ObservableList<String>  listaFormador = FXCollections.observableArrayList(); //Lista de Formadores

    private MysqlConnect dc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            // TODO
    }

    @FXML
    private void connectToDatabase (ActionEvent actionEvent) {
        try {
            Connection conn = MysqlConnect.ConnectDb();
            data = FXCollections.observableArrayList();
                // Executa a query e guarda o resultado em resultset
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM formador");
            while (rs.next()) {
                    //get string da BD + UserDetails
                    data.add(new UserDetails(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    listaFormador.add(rs.getString("id_formador")); //add String curso na list
            }
            formadorCombo.setItems(listaFormador);
        } catch (SQLException ex) {
            System.err.println("Erro ---> "+ex);
        }

            // constroi a tableview com os gets/sets do UserDetails
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnApelido.setCellValueFactory(new PropertyValueFactory<>("apelido"));
        columnCc.setCellValueFactory(new PropertyValueFactory<>("cc"));

        tableUser.setItems(null);
        tableUser.setItems(data);
        buildAccaoData();



    }

    private void buildAccaoData(){
        Connection conn = MysqlConnect.ConnectDb();
        System.out.println("----------------[  OK Acções ]----------------");
        try{
            String SQL = "SELECT * FROM accao;";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                listaAccoes.add(rs.getString("cod_accao")); //add String curso na list
            }
            accaoCombo.setItems(listaAccoes); //Carrega a lista de acçoes/turmas na combobox
        }
        catch(Exception e){
            e.printStackTrace();
            UtilsForm.alertMsg(Alert.AlertType.ERROR,("Error! Building Data" +e.getMessage()));
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (accaoCombo.getValue() == null || formadorCombo.getValue() == null) {

            UtilsForm.alertMsg(Alert.AlertType.INFORMATION,("Escolha: Formador / Acção!"));

        } else {
            Connection conn = MysqlConnect.ConnectDb();
            System.out.println("----------------[  OK Acções/Formador ]----------------");

            String accaoBox = accaoCombo.getSelectionModel().getSelectedItem().toString();
            String formBox = formadorCombo.getSelectionModel().getSelectedItem().toString();
            String query = "INSERT INTO formadorturma (fk_accao,fk_formador) VALUES (" + "'" + accaoBox +
                    "'," + "'" + formBox + "');";
            System.out.println("QUERY : " + query);

            conn = null;
            Statement stmt = null;
            try {
                conn = MysqlConnect.ConnectDb();
                conn.setAutoCommit(false);
                System.out.println("STATUS ---> Conectado com sucesso!");
                stmt = conn.createStatement();
                System.out.println("QUERY ---> " + query);
                stmt.executeUpdate(query);
                stmt.close();
                conn.commit();
                conn.close();
                UtilsForm.alertMsg(Alert.AlertType.INFORMATION,(" Dados Validados!"));

            }catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                UtilsForm.alertMsg(Alert.AlertType.INFORMATION,(" Dados inválidos! "+e.getMessage()));
            }
        }

    }
}


