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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * Created by pc on 12/10/16.
 */
public class FXMLFrmdController implements Initializable {


    @FXML private TableView<UserFalta> tableFDFalta;
    @FXML private TableColumn<UserFalta, String> UserId;
    @FXML private TableColumn<UserFalta, String> UserName;
    @FXML private TableColumn<UserFalta, String> DataFalta;
    @FXML private TableColumn<UserFalta, String> FaltaMarca;
    @FXML private TableColumn<UserFalta, String> Contacto;
    @FXML private DatePicker date_start;
    @FXML private DatePicker date_stop;
    @FXML private CheckBox chkTodos;
    @FXML ComboBox<String> accaoBox;
    @FXML ComboBox<String> searchBox1;



    private MysqlConnect dc;
    ObservableList<String>  listaFormadores = FXCollections.observableArrayList(); //Lista de Formadores
    private ObservableList<UserFalta> data;


    public void FXMLAllController() {
        if(chkTodos.isSelected()){
            searchBox1.setDisable(true);
        }else
            searchBox1.setDisable(false);
    }



    public void FXMLSearchController() {
        if(chkTodos.isSelected()){
            searchBox1.getSelectionModel().clearSelection();
            listaFormadores.clear();
            buildListaFormadores();
            System.out.println("STATUS ---> FIX Clear Box / New Lista Formadores");
        }

    }

    @FXML
    private void connectToDataFaltas (ActionEvent actionEvent) {
        dc = new MysqlConnect();
        LocalDate ldefault = LocalDate.parse("2015-01-01");
        Instant fimc = ldefault.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        LocalDate today = LocalDate.now();
        fimc = today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date dateIn = Date.from(Instant.from(fimc));

        if(date_start.getValue() != null ){
            LocalDate ldi = date_start.getValue();
            System.out.println("Data Start = "+ldi);
        }
        if(date_stop.getValue() != null ){
            LocalDate ldf = date_stop.getValue();
            System.out.println("Data Stop = "+ldf);
        }

        java.util.Date dateOut = Date.from(Instant.from(fimc));

        String sQlQuery = "";

        System.out.println("Data Start default = "+ldefault);
        System.out.println("Data Hoje = "+today);


        try {
            Connection conn = dc.ConnectDb();
            data = FXCollections.observableArrayList();
            if(chkTodos.isSelected()){
                System.out.println(" QUERY -> TODOS");
                sQlQuery = "select id_formador, formador.nome ,faltaformador.data ,formador.email from formador" +
                        " ,faltaformador where faltaformador.fk_formador = formador.id_formador ";

            }
            if(date_start.getValue() == null || (!FXMLHomePageController.compareTo0(dateIn,dateOut)) ){

                fimc = ldefault.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                dateIn = Date.from(Instant.from(fimc));
                Date sqlDateIn = new Date(dateIn.getTime());
                date_start.setValue(sqlDateIn.toLocalDate());
                System.out.println("Data Start = "+ldefault);

            }//todo testar melhor
            if(date_stop.getValue() == null){
                System.out.println("Data Start = "+ldefault);
                System.out.println("Data Stop = "+today);
                sQlQuery = "select '"+accaoBox.getValue()+"' and (data_h BETWEEN '"+ldefault+"' AND '"
                        +today+"')";

            }

            if (searchBox1.getValue() != null){
                String searchNome = searchBox1.getValue();
                // sQlQuery = "SELECT formando.nome FROM accao WHERE cod_accao LIKE = '"+accaoBox.getValue()+"';";
                //System.out.println(sQlQuery);
                sQlQuery = "select id ";
            }
                System.out.println(searchBox1.getValue());
                System.out.println(sQlQuery);

                ResultSet rs = conn.createStatement().executeQuery(sQlQuery);
                while (rs.next()) {
                    data.add(new UserFalta(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(6)));
                }
            } catch (SQLException ex) {
                System.err.println("Erro ---> "+ex);
                UtilsForm.alertMsg(Alert.AlertType.WARNING,("Error! Building Data" +ex.getMessage()));


            }






        UserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        UserName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        DataFalta.setCellValueFactory(new PropertyValueFactory<>("datas"));
        FaltaMarca.setCellValueFactory(new PropertyValueFactory<>("falta"));
        Contacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));

        tableFDFalta.setItems(null);
        tableFDFalta.setItems(data);

    }



    @FXML
    private void buildListaFormadores(){
        Connection conn = dc.ConnectDb();
        System.out.println("----------------[  FIX OK Lista Formadores ]----------------");
        searchBox1.getSelectionModel().clearSelection();
        listaFormadores.clear();
        try{
            String SQL = "SELECT nome FROM formador;";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                listaFormadores.add(rs.getString(1));
            }
            searchBox1.setItems(listaFormadores);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilsForm.alertMsg(Alert.AlertType.WARNING,("Error! Building Data" +e.getMessage()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO fix bugs
    }

}