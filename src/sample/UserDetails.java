package sample;

/**
 *  Esta class actua como model class, getters,setters e property values para a tabela formadores
 *
 *  A API de properties comtem diversas classes que adicionam "super-poderes" aos tipos mais comuns do
 *  java e que podem ser extensíveis para serem usarmos com objetos da aplicação.
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by pc on 11/20/16.
 */

public class UserDetails {

    private final StringProperty id;
    private final StringProperty nome;
    private final StringProperty apelido;
    private final StringProperty cc;




    //Default constructor
    public UserDetails(String id, String nome, String apelido, String cc) {
        this.nome = new SimpleStringProperty(nome);
        this.apelido = new SimpleStringProperty(apelido);
        this.cc = new SimpleStringProperty(cc);
        this.id = new SimpleStringProperty(id);
    }



    //Getters
    public String getNome() {
        return nome.get();
    }

    public String getApelido() {
        return apelido.get();
    }

    public String getCc() {
        return cc.get();
    }

    public String getId() {
        return id.get();
    }


    //Setters
    public void setNome(String value) {
        nome.set(value);
    }

    public void setApelido(String value) {
        apelido.set(value);
    }

    public void setCc(String value) {
        cc.set(value);
    }

    public void setId(String id) {
        this.id.set(id);
    }


    //Property values
    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty apelidoProperty() {
        return apelido;
    }

    public StringProperty ccProperty() {
        return cc;
    }

    public StringProperty idProperty() {
        return id;
    }






}
