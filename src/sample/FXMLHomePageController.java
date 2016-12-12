package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.io.IOException;


/**
 * FXML Controller class
 *
 * Created by pc on 11/10/16.
 *
 */

public class FXMLHomePageController implements Initializable {

    @FXML private TextField user;
    @FXML private TextField passcheck;
    @FXML private TextField pass;
    @FXML private DatePicker date_start;
    @FXML private DatePicker date_stop;
    @FXML private Label invalid_label;
    @FXML private Label invalid_cc;
    @FXML private TextField nome_formador;
    @FXML private TextField apelido_formador;
    @FXML private TextField nif;
    @FXML private TextField cc;
    @FXML private TextField accao;
    @FXML private TextField accao_desc;
    @FXML private TextField curso;
    @FXML private TextArea curso_desc;
    @FXML private Button btn1;
    @FXML private Button btn3;
    @FXML private Button btnF;
    @FXML ComboBox<String> tipooBox;
    @FXML ComboBox<String> featureCombo; //lista de Cursos
    @FXML ComboBox<String> combooBox; // Horario enum 8-14 14-20
    @FXML ComboBox<String> accaoBox;


    @FXML
    private TextField nome_formando;

    @FXML
    private TextField email_formando;

    @FXML
    private TextField cc1;

    @FXML
    private TextField nif1;



    private MysqlConnect dc;
    private String encode;
    @FXML
    private void handleBtn1Action(ActionEvent event) throws IOException {

        System.out.println("----------------[ Associar Formador ]----------------\n");
        Stage stage;
        Parent root;
        stage = new Stage();
        buildData();
        root = FXMLLoader.load(getClass().getResource("FXMLUser.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle(" TODOIS -> Associar Formador");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btn1.getScene().getWindow());

        stage.showAndWait();
        System.out.println("yiuuuuuupY");

    }

    @FXML
    private void handleBtnFFAction(ActionEvent event) throws IOException {

        System.out.println("----------------[ Faltas Formandos ]----------------\n");
        Stage stage;
        Parent root;
        stage = new Stage();
        root = FXMLLoader.load(getClass().getResource("FXMLForm.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TODOIS -> QUERY por NOME / ACÇÃO / DATE WHERE (date BETWEEN x AND x)");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnF.getScene().getWindow());
        stage.showAndWait();
        System.out.println("yiuuuuuupY");
    }


    @FXML
    private void handleBtnFRAction(ActionEvent event) throws IOException {

        System.out.println("----------------[ Faltas Formadores ]----------------\n");
        Stage stage;
        Parent root;
        stage = new Stage();
        root = FXMLLoader.load(getClass().getResource("FXMLFrmd.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TODOIS -> QUERY por NOME / ACÇÃO / DATE WHERE (date BETWEEN x AND x)");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnF.getScene().getWindow());
        stage.showAndWait();
        System.out.println("FiNiSH HiM");
    }


    @FXML
    private void handleBt2Action(ActionEvent event) throws IOException { //SQLException ,

        System.out.println("----------------[  OK Edit  ]----------------\n");
        if (accaoBox.getValue() == null){

            UtilsForm.alertMsg(Alert.AlertType.INFORMATION,("Escolha: Acção a Editar!"));

        }else {
            String ac_edit = accaoBox.getSelectionModel().getSelectedItem().toString();
            System.out.println(" Editar ---> "+ac_edit);
            String query = "SELECT * FROM accao WHERE cod_accao LIKE '"+ac_edit+"';";
            System.out.println("QUERY : " + query);
            try {
                Connection conn = dc.ConnectDb();
                ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    accao.setText(rs.getString(1));
                    accao_desc.setText(rs.getString(2));
                    featureCombo.setValue(rs.getString(3));
                    date_start.setValue(rs.getDate(4).toLocalDate());
                    date_stop.setValue(rs.getDate(5).toLocalDate());
                    //combooBox.setVisibleRowCount(1);
                    combooBox.setValue(rs.getString(6));
                };
            } catch (SQLException ex) {
                System.err.println("Erro ---> "+ex);
            }
            btn3.setDisable(false);
            btn1.setDisable(true);

        }
    }


    @FXML
    private void handleBtnFrAction(ActionEvent event) throws SQLException ,IOException {

        System.out.println("----------------[ Associar Formando ]----------------\n");
        Stage stage;
        Parent root;
        stage = new Stage();
        buildData();
        root = FXMLLoader.load(getClass().getResource("FXMLStudent.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle(" TODOIS -> accaoaluno fk_formando como PK");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btn1.getScene().getWindow());

        stage.showAndWait();
        System.out.println("yiuuuuuupY 2");

        ArrayList <String[]> result = new ArrayList<String[]>();
        Connection conn = dc.ConnectDb();

        ResultSet rs = conn.createStatement().executeQuery( "SELECT * FROM curso;" );
        int columnCount = rs.getMetaData().getColumnCount();
        while(rs.next())
        {
            String[] row = new String[columnCount];
            for (int i=0; i <columnCount ; i++)
            {
                row[i] = rs.getString(i + 1);
                System.out.println("ROW "+i +" "+row[i]);

            }
            result.add(row);
        }
        UtilsForm.alertMsg(Alert.AlertType.WARNING,("IiiiuUUPPYy!"));

      //  insertStatement(query);
        System.out.println(" ArrayList dos Cursos");
        for (String[] res : result)
            System.out.println(res);

    }

    @FXML
    private void handleBtn3Action(ActionEvent event) throws SQLException, IOException {

        System.out.println("----------------[  OK Acçoes  ]----------------\n");
        String tDesc = accao_desc.getText();
        String turma = accao.getText();
        LocalDate ld = date_start.getValue();
        LocalDate ldf = date_stop.getValue();

        if ((date_start.getValue() == null) || (date_stop.getValue() == null)){
            System.out.println(" Insira data  ");
            invalid_label.setText("Insira Data\nData inválida");
        }
        if ((turma.length() <= 3) || (tDesc.length() <= 3)){
            System.out.println(" Insira curso  ");
            invalid_label.setText("Insira curso\ncurso inválido");
        }
        if (featureCombo.getValue() == null || combooBox.getValue() == null){
            System.out.println(" Escolha o Curso");
            invalid_label.setText("Escolha Curso\nHorário");
        }
        else if ((date_start.getValue() != null) && (date_stop.getValue() != null)){
            //   DatePicker date_start = new DatePicker();
            //       String datain ="0";
            Instant inicioc = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            java.util.Date dateIn = Date.from(Instant.from(inicioc));
            java.sql.Date sqlDateIn = new java.sql.Date(dateIn.getTime());
            System.out.println("\t ---[ Data Inicio Curso ]---");
            System.out.println(" utilDate ---> " + dateIn);
            System.out.println(" sqlDate ---> " + sqlDateIn);

            Instant fimc = ldf.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            java.util.Date dateOu = Date.from(Instant.from(fimc));
            java.sql.Date sqlDateOut = new java.sql.Date(dateOu.getTime());
            System.out.println("\t ---[ Data Fim Curso ]---");
            System.out.println(" utilDate ---> " + dateOu);
            System.out.println(" sqlDate ---> " + sqlDateOut);

            if (compareTo0(dateIn,dateOu)){
                /*  Compara se a data inicial do curso é anterior à data final */
                String obs = " ";
                String cursoEsc = featureCombo.getSelectionModel().getSelectedItem().toString();
                System.out.println(" Curso ---> "+cursoEsc);
                String am_pm = combooBox.getSelectionModel().getSelectedItem().toString();
                System.out.println(" Horario ---> "+am_pm);
                String query = "INSERT INTO accao(cod_accao, desc_accao, fk_curso, data_inicio, data_fim, horario, obs)"
                        + " VALUES (" + "'" + turma + "'," + "'" + tDesc + "'," + "'" + cursoEsc +  "'," + "'"
                        + sqlDateIn + "'," + "'" + sqlDateOut + "'," + "'" + am_pm + "'," + "'" + obs +"');";
                System.out.println("QUERY : " + query);
                insertStatement(query);
                accao.clear();
                accao_desc.clear();

            }
        }else {
            UtilsForm.alertMsg(Alert.AlertType.WARNING,(" Dados inválidos!"));
        }
    }


    @FXML
    private void handleButtonAction(ActionEvent event) {

        String query = "INSERT INTO curso (id_curso,ofi_curso) VALUES (" + "'" + curso.getText() +
                "'," + "'" + curso_desc.getText() + "');";

        System.out.println("----------------[  OK  ]----------------");
        System.out.println("QUERY : " + query);
        insertStatement(query);

        curso.clear();
        curso_desc.clear();
        System.out.println("yaYAHha Clear OK!");

    }




    @FXML
    private void doneButtonAction(ActionEvent event) throws IOException, SQLException {
        String teste = "";
        /**
         *      testar o inserir turmas + sql
         */
        System.out.println("----------------[  ADD Formador  ]----------------");
        ArrayList<String> validaErrors = new ArrayList<>();
        String cartaoc = cc.getText();
        if(!UtilsForm.luhnCheck(cartaoc)){
            cc.clear();
            invalid_cc.setText("CC inválido");
        }

        if (!nif.getText().matches("[0-9]{9}")) {
            nif.clear();
            invalid_cc.setText("NiF inválido");
        }


        if ((isRegFormadOk()) && (nif.getText().matches("[0-9]{9}")) && (UtilsForm.luhnCheck(cartaoc))){
            UtilsForm.isValidNif(Integer.parseInt(nif.getText()));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirma que os dados estão corretos?");
            alert.setContentText(" Criar Novo Registo de Formador");
            String query2 = "INSERT INTO formador (nome,email,fk_cc) VALUES (" + "'" + nome_formador.getText() +
                    "'," + "'" + apelido_formador.getText() + "'," + "'" + cc.getText() + "');";
            Optional<ButtonType> result = alert.showAndWait();
            String query1 = "INSERT INTO pessoa (username,password,nome,cc,tipo) VALUES (" + "'" + user.getText() +
                    "'," + "'" + encode + "'," + "'" + nome_formador.getText() + "','" + cc.getText() + "' ,'Formador');";



            if (result.get() == ButtonType.OK){
                System.out.println("QUERY : " + query1);
                insertStatement(query1);
                insertStatement("unlock tables");

                System.out.println("QUERY : " + query2);
                insertStatement(query2);

                System.out.println("----------------[  OK  ]----------------");
            }


        }
    }

    @FXML
    private void doneButton2Action(ActionEvent event) throws IOException, SQLException {
        /**
         *      testar o inserir turmas + sql
         */
        System.out.println("----------------[  ADD Formando  ]----------------");
        String cartaoc = cc1.getText();
        if(!UtilsForm.luhnCheck(cartaoc)){
            cc1.clear();
            invalid_cc.setText("CC inválido");
        }

        if (!nif1.getText().matches("[0-9]{9}")) {
            nif1.clear();
            invalid_cc.setText("NiF inválido");
        }


        if (nif1.getText().matches("[0-9]{9}") && UtilsForm.luhnCheck(cartaoc)){
            int niftest = Integer.parseInt(nif1.getText());

            UtilsForm.isValidNif(niftest);
            invalid_cc.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirma que os dados estão corretos?");
            alert.setContentText(" Criar Novo Registo de Formador");
            String query2 = "INSERT INTO formando (nome,email,fk_cc) VALUES (" + "'" + nome_formando.getText() +
                    "'," + "'" + email_formando.getText() + "'," + "'" + cc1.getText() + "');";
            Optional<ButtonType> result = alert.showAndWait();
            String query1 = "INSERT INTO pessoa (nome,cc,tipo) VALUES ('" + nome_formando.getText()
                    + "','" + cc1.getText() + "' ,'formando');";



            if (result.get() == ButtonType.OK){
                System.out.println("QUERY : " + query1);
                insertStatement(query1);
                insertStatement("unlock tables");

                System.out.println("QUERY : " + query2);
                insertStatement(query2);

                System.out.println("----------------[  OK  ]----------------");
            }


        }
    }


    @FXML
    private void userDoneAction(ActionEvent event) throws IOException, SQLException {
        isRegFormadOk();
        invalid_cc.setText("");
    }



    private boolean isRegFormadOk() throws SQLException {
        System.out.println(pass.getText());
        System.out.println(passcheck.getText());
        if(isUsernameTaken(user.getText()) || !UtilsForm.validateUsername(user.getText()) ){
            user.clear();
            invalid_cc.setText("Escolha outro UserName");
            return false;
        }

        if((pass.getText()).equals(passcheck.getText()) && UtilsForm.validaPassword(pass.getText())){
            System.out.println(passcheck.getText());
            encode = SecurityKey.enCodePass(pass.getText());
            System.out.println("Pass hash: "+encode);
            return true;
        }
        pass.clear();
        passcheck.clear();
        return false;
    }



    protected static boolean isUsernameTaken(String username) throws SQLException {
        MysqlConnect dc = null;
        Connection conn = dc.ConnectDb() ;
        PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM pessoa WHERE username =" +
                " '"+username+"';");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        if(count == 0) {
            return false;
        }
        return true;
    }


    public void updateAccao(){

        btn1.setDisable(false);
        accao.clear();
        accao_desc.clear();
        date_start.setValue(null);
        date_stop.setValue(null);
        combooBox.setValue(null);
        featureCombo.setValue(null);
        btn3.setDisable(true);
        accaoBox.setValue(null);


    }


    protected static void insertStatement(String insert_query){
        MysqlConnect dc = null;

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dc.ConnectDb();

            conn.setAutoCommit(false);
            System.out.println("STATUS ---> Conectado com sucesso!");
            stmt = conn.createStatement();
            System.out.println("QUERY ---> " + insert_query);
            stmt.executeUpdate(insert_query);
            stmt.close();
            conn.commit();
            conn.close();

            //UtilsForm.alertMsg(Alert.AlertType.INFORMATION,(" Dados Validados!"));

        }catch ( Exception e ) {

            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            UtilsForm.alertMsg(Alert.AlertType.WARNING,(" Dados inválidos! "+e.getMessage()));

        }


    }

    public void buildData(){
        ObservableList<String>  cursos = FXCollections.observableArrayList(); //Lista de cursos
        ObservableList<String> ampm = FXCollections.observableArrayList("08h-14h","14h-20h");
        ObservableList<String>  accoesbx = FXCollections.observableArrayList(); //Lista de cursos
        ObservableList<String> tipo = FXCollections.observableArrayList();
        combooBox.setItems(ampm);
        Connection conn = dc.ConnectDb();
        System.out.println("----------------[  OK Cursos ]----------------");
        try{
            String querySQL = "SELECT * FROM accao;";
            String SQL = "SELECT * FROM curso;";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                cursos.add(rs.getString("id_curso")); //add String curso na list
            }
            featureCombo.setItems(cursos); //Carrega a lista de cursos na combo box
            rs = conn.createStatement().executeQuery(querySQL);
            while(rs.next()){
                accoesbx.add(rs.getString("cod_accao")); //add String curso na list
            }
            accaoBox.setItems(accoesbx);
            tipooBox.setItems(cursos);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Erro! Building Data");
        }
    }

    protected static boolean compareTo0(java.util.Date DateIn, java.util.Date DateOut) {
        if (DateIn.before(DateOut) && !(DateIn.equals(DateOut))) {return true;}
        else return false;
    }

    @FXML
    void exit(ActionEvent event){
        Stage AllStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        AllStage.setScene(new Scene(root));
        AllStage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        System.out.println("STATUS ---> EXIT\n\tSee You Later Alligator!");
    }





    @Override
        public void initialize(URL url, ResourceBundle rb) {
        buildData();
        btn3.setDisable(true);

    }


        /**
         * TODO  :) Fix ADD Formador user/password
         */

}
