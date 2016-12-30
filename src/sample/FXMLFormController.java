package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * Created by pc on 12/1/16.
 */
public class FXMLFormController implements Initializable {


    @FXML private TableView<UserFalta> tableFalta;
    @FXML private TableColumn<UserFalta, String> UserId;
    @FXML private TableColumn<UserFalta, String> UserName;
    @FXML private TableColumn<UserFalta, String> DataFalta;
    @FXML private TableColumn<UserFalta, String> Ufcd;
    @FXML private TableColumn<UserFalta, String> FaltaMarca;
    @FXML private TableColumn<UserFalta, String> FaltaJust;
    @FXML private DatePicker date_start;
    @FXML private DatePicker date_stop;
    @FXML private CheckBox chkTodos;
    @FXML ComboBox<String> accaoBox;
    @FXML ComboBox<String> searchBox;



    private MysqlConnect dc;
    ObservableList<String>  listaAccoes = FXCollections.observableArrayList(); //Lista de Accoes
    ObservableList<String>  listaFormandos = FXCollections.observableArrayList(); //Lista de Formadores
    private ObservableList<UserFalta> data;

    public void FXMLFormController() {
        buildAcDataFaltas();
    }

    public void FXMLAllController() {
        if(chkTodos.isSelected()){
            searchBox.setDisable(true);
        }else
            searchBox.setDisable(false);
    }



    public void FXMLSearchController() {
        if(accaoBox.getValue()!= null){
            searchBox.getSelectionModel().clearSelection();
            listaFormandos.clear();
            buildListaFormandos(accaoBox.getValue());
            System.out.println("STATUS ---> Clear Box / New Lista Formandos");
        }

    }

    @FXML
    private void connectToDataFaltas (ActionEvent actionEvent) {
        dc = new MysqlConnect();
        LocalDate ldefault = LocalDate.parse("2014-01-01");

        if(date_start.getValue() == null){
            System.out.println("Data Start = "+ldefault);
            date_start.setValue(ldefault);
        }
        if(date_stop.getValue() == null){
            LocalDate today = LocalDate.now();

            System.out.println("Data Start = "+today);
            date_stop.setValue(today);
        }

        String sQlQuery = "";


        if (accaoBox.getValue() == null &&((searchBox.getValue() == null)||!(chkTodos.isSelected()))){
            UtilsForm.alertMsg(Alert.AlertType.INFORMATION,("Escolha: Acção / Formando!"));
            System.out.println(searchBox.getValue());
        }
        else{
            try {
                Connection conn = dc.ConnectDb();
                data = FXCollections.observableArrayList();
                if(chkTodos.isSelected()){
                    System.out.println(" QUERY -> TODOS");
                    sQlQuery = "select id_formando, formando.nome ,aula.fk_ufcd ,aula.data_h, falta, justif from aula," +
                            " formando ,faltaformando, accaoaluno where accaoaluno.fk_formando = formando.id_formando " +
                            "and formando.id_formando=faltaformando.fk_formando and aula.id_aula = fk_aula and " +
                            "accaoaluno.fk_accao = '"+accaoBox.getValue()+"'; ";

                }

                if (searchBox.getValue() != null){
                    String searchNome = searchBox.getValue();
                    // sQlQuery = "SELECT formando.nome FROM accao WHERE cod_accao LIKE = '"+accaoBox.getValue()+"';";
                    //System.out.println(sQlQuery);
                    sQlQuery = "select id_formando, formando.nome ,aula.fk_ufcd ,aula.data_h, falta ,justif from aula," +
                            " formando ,faltaformando, accaoaluno where accaoaluno.fk_formando= formando.id_formando " +
                            "and  formando.id_formando=faltaformando.fk_formando and aula.id_aula = fk_aula and " +
                            "accaoaluno.fk_accao = '"+accaoBox.getValue()+"' and formando.nome like '"+searchBox.getValue()+"'; ";

                }




                System.out.println(searchBox.getValue());
                System.out.println(sQlQuery);

                ResultSet rs = conn.createStatement().executeQuery(sQlQuery);
                while (rs.next()) {
                    data.add(new UserFalta(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(6)));
                }
            } catch (SQLException ex) {
                System.err.println("Erro ---> "+ex);
                UtilsForm.alertMsg(Alert.AlertType.WARNING,("Error! Building Data" +ex.getMessage()));


            }



        }


        UserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        UserName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        DataFalta.setCellValueFactory(new PropertyValueFactory<>("datas"));
        Ufcd.setCellValueFactory(new PropertyValueFactory<>("ufcd"));
        FaltaMarca.setCellValueFactory(new PropertyValueFactory<>("falta"));
        FaltaJust.setCellValueFactory(new PropertyValueFactory<>("justifi"));

        tableFalta.setItems(null);
        tableFalta.setItems(data);
        accaoBox.setValue(null);
        searchBox.setValue(null);
    }


    private void buildAcDataFaltas(){
        Connection conn = dc.ConnectDb();
        System.out.println("----------------[  OK Lista Acções ]----------------");

        try{
            String SQL = "SELECT * FROM accao;";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                listaAccoes.add(rs.getString("cod_accao"));
            }//todo fix bug
            accaoBox.setItems(listaAccoes);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilsForm.alertMsg(Alert.AlertType.ERROR,("Error! Building Data" +e.getMessage()));
        }
    }

    private void buildListaFormandos(String accao){
        Connection conn = dc.ConnectDb();
        System.out.println("----------------[  OK Lista Formandos ]----------------");
        //searchBox.getSelectionModel().clearSelection();
        try{
            String SQL = "SELECT nome FROM formando, accaoaluno WHERE id_formando=fk_formando and fk_accao like '"+accao+"';";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                listaFormandos.add(rs.getString(1));
            }
            searchBox.setItems(listaFormandos);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilsForm.alertMsg(Alert.AlertType.WARNING,("Error! Building Data" +e.getMessage()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}