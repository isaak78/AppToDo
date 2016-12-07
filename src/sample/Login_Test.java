package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Created by pc on 11/10/16.
 */

public class Login_Test extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Valid CC Check: " + UtilsForm.luhnCheck("112652620ZZ9")); // TODO done :)
        System.out.println("REGEX CC Check: " + UtilsForm.isCcValid("112650620Yz3"));
        System.out.println("apache.commons.EmailValidator: isvalid@gmail.com  -> " + UtilsForm.isEmaiValid("isvalid@gmail.com"));
        System.out.println("apache.commons.EmailValidator: is.valid.gmail.com  -> " + UtilsForm.isEmaiValid("is.valid.gmail.com"));
        System.out.println("apache.commons.EmailValidator: i@g.c                -> " + UtilsForm.isEmaiValid("i@g.c"));
        System.out.println(UtilsForm.isValidNif(123456780));
        System.out.println(UtilsForm.isValidNif(1));
        SecurityKey.securiryTest("sherLOCKEDhomes");


        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")); //FXMLDocument pag de login
        Scene scene_login = new Scene(root);
        stage.setScene(scene_login);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
