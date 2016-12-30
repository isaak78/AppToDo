package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
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
    @FXML private TableColumn<UserFalta, String> Just;
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
        LocalDate today = LocalDate.now();
        LocalDate start;
        LocalDate stop;
        if(date_start.getValue() == null ){
            start = today.withDayOfMonth(1);
            date_start.setValue(start);
        }
        if(date_stop.getValue() == null ){
            stop = today.withDayOfMonth(today.lengthOfMonth());
            date_stop.setValue(stop);
        }
        start = date_start.getValue();
        stop = date_stop.getValue();

        Instant iStart = start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant iStop = stop.atStartOfDay(ZoneId.systemDefault()).toInstant();
        java.util.Date dateOut = Date.from(Instant.from(iStop));
        java.util.Date dateIn = Date.from(Instant.from(iStart));
        if(!FXMLHomePageController.compareTo0(dateIn,dateOut)){
            stop = date_start.getValue().withDayOfMonth(today.lengthOfMonth());
            date_stop.setValue(stop);
        }


        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd");

        String sStart = sdf.format(dateIn);
        String sStop = sdf.format(dateOut);

        String sQlQuery = "";
        System.out.println("Data Start = "+sStart);
        System.out.println("Data Stop = "+sStop);


        try {
            Connection conn = dc.ConnectDb();
            data = FXCollections.observableArrayList();
            if(chkTodos.isSelected()){
                System.out.println(" QUERY -> TODOS");
                sQlQuery = "select id_formador, formador.nome ,faltaformador.data ,horas ,email, cc from formador" +
                        ",faltaformador where faltaformador.fk_formador=formador.id_formador " +
                        "and (data between '"+sStart+"' AND '"+sStop+"')";
            }

            if (searchBox1.getValue() != null){
                String searchNome = searchBox1.getValue();
                sQlQuery = "select id_formador, formador.nome ,faltaformador.data ,horas ,email, cc " +
                        "from formador,faltaformador where faltaformador.fk_formador = formador.id_formador " +
                        "and formador.nome='"+searchNome+"' and (data between '"+sStart+"' AND '"+sStop+"')";
            }
                System.out.println(searchBox1.getValue());
                System.out.println(sQlQuery);

                ResultSet rs = conn.createStatement().executeQuery(sQlQuery);
                while (rs.next()) {
                    data.add(new UserFalta(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(3),rs.getString(5)));
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
        Just.setCellValueFactory(new PropertyValueFactory<>("cc"));


        tableFDFalta.setItems(null);
        tableFDFalta.setItems(data);

    }



    @FXML
    private void buildListaFormadores(){
        Connection conn = dc.ConnectDb();
        System.out.println("----------------[ Lista Formadores ]----------------");
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