package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by pc on 12/1/16.
 *
 *  Fabrica a tabela de faltas FXMLFormController
 *
 */
public class UserFalta {

    private final StringProperty id;
    private final StringProperty nome;
    private final StringProperty datas;
    private final StringProperty ufcd;
    private final StringProperty falta;
    private final StringProperty justificada;

    public void userFalta(){}



    public UserFalta(String id, String nome, String datas, String ufcd, String falta, String justificada) {
        this.id = new SimpleStringProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.datas = new SimpleStringProperty(datas);
        this.ufcd = new SimpleStringProperty(ufcd);
        this.falta = new SimpleStringProperty(falta);
        this.justificada = new SimpleStringProperty(justificada);
    }




    public String getId() {
        return id.get();
    }

    public String getNome() {
        return nome.get();
    }

    public String getDatas() {
        return datas.get();
    }

    public String getUfcd() {
        return ufcd.get();
    }

    public String getFalta() {
        return falta.get();
    }

    public String getJustificada() {
        return justificada.get();
    }



    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty datasProperty() {
        return datas;
    }

    public StringProperty ufcdProperty() {
        return ufcd;
    }

    public StringProperty faltaProperty() {
        return falta;
    }

    public StringProperty justificadaProperty() {
        return justificada;
    }



    public void setId(String id) {
        this.id.set(id);
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public void setDatas(String datas) {
        this.datas.set(datas);
    }

    public void setUfcd(String ufcd) {
        this.ufcd.set(ufcd);
    }

    public void setFalta(String falta) {
        this.falta.set(falta);
    }

    public void setJustificada(String justificada) {
        this.justificada.set(justificada);
    }
}
