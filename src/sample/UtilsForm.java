package sample;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;



/**
 * Created by pc on 11/18/16.
 *
 * https://www.cartaodecidadao.pt/images/stories/Algoritmo_Num_Documento_CC.pdf
 *
 * Variante do Check Digit do Cartão do Cidadão baseado no algoritmo de Luhn
 *
 * * --------------------------------------------------------------------------------------------------------------
 * NIF Tem de ter 9 dígitos;
 * O primeiro dígito tem de ser 1, 2, 5, 6, 8 ou 9;
 * O dígito de controlo (último digíto do NIF) é obtido da seguinte forma:
 * 9*d1 + 8*d2 + 7*d3 + 6*d4 + 5*d5 + 4*d6 + 3*d7 + 2*d8 + 1*d9  (em que d1 a d9 são os 9 dígitos do NIF);
 * Esta soma tem de ser múltiplo de 11 (quando divídida por 11 dar 0);
 * Subtraír o resto da divisão da soma por 11 a 11;
 * Se o resultado for 10, é assumído o algarismo 0;
 *
 * *--------------------------------------------------------------------------------------------------------------
 * The password policy is:
 * At least 6 chars
 *
 * Contains at least one digit
 * Contains at least one lower alpha char and one upper alpha char
 * Contains at least one char within a set of special chars (@#%$^ etc.)
 * Does not contain space, tab, etc.
 *
 *
 */


public class UtilsForm {



    public static void alertMsg(Alert.AlertType error, String msg) {

        if (error == Alert.AlertType.ERROR) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRO BOX");
            alert.setHeaderText("Ooops, Problema na ligação com a base de dados");
            alert.setContentText(msg);
            alert.showAndWait();
            System.exit(0);
        } else if (error == Alert.AlertType.INFORMATION) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO BOX");
            alert.setHeaderText("\tINFO!");
            alert.setContentText(msg);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING BOX");
            alert.setHeaderText("\"Ah, não! algo deu errado..");
            alert.setContentText(msg);
            alert.showAndWait();
        }
    }





    public static boolean isEmaiValid(String email){

        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }





    public static boolean isCcValid(String ccNumber){

        return ccNumber.matches("[0-9]{9}[x-zX-Z]{2}[0-9]");
    }




    public static boolean luhnCheck(String ccNumber) {

        if(isCcValid(ccNumber)){
            int sum = 0;
            long array[] = new long [12];
            int c;
            int digit;
            for (int i = 0; i < ccNumber.length(); i++)
            {
                c = ccNumber.charAt(i);
                digit = Character.getNumericValue(c);
                if (Character.isDigit(c)){
                    array[i] = digit;
                }
                if ((ccNumber.toLowerCase()).charAt(i) == 'z') {
                    array[i] = 35;
                }
                else if ((ccNumber.toLowerCase()).charAt(i) == 'y') {
                    array[i] = 34;
                }
            }

            for ( int x = ccNumber.length()-1; x >= 0; --x ) {
                int valor = (int) array[x];
                if(x % 2 == 0){
                    valor *= 2;
                    if (valor > 9)
                        valor -= 9;
                }
                sum += valor;
            }
            return (sum % 10 == 0);
        }else
            return false;
    }

    public static boolean isValidNif(int nif) {

        String regnif = Integer.toString(nif);
        int array[] = new int[9];
        if (regnif.matches("[0-9]{9}")){
            String number = String.valueOf(nif);
            for (int d = 0; d < 9; d++) {
                array[d] = Character.digit(number.charAt(d), 10);
            }
            int checkdigit = array[0];
            if (checkdigit == 1 || checkdigit == 2 || checkdigit == 5 ||
                    checkdigit == 6 || checkdigit == 8 || checkdigit == 9) {
                checkdigit = array[0] * 9;
                for (int i = 2; i <= 8; i++) {
                    checkdigit += (array[i - 1]) * (10 - i);
                }
                checkdigit = 11 - (checkdigit % 11);
                System.out.println(checkdigit);
                //Se der >=10 então o dígito de controlo tem de ser 0
                if (checkdigit >= 10) {
                    checkdigit = 0;
                }
                //Comparamos com o último dígito
                if (checkdigit == array[8]) {
                    System.out.println("NIF " + nif + " VALIDO");
                    return true;
                } else{
                    System.out.println("NIF " + nif + " INVALIDO");
                    return false;

                }
            }

        }
        return false;
    }


    public static boolean validaPassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,}$")){
            alertMsg(Alert.AlertType.INFORMATION,("A password deve conter pelo menos 5 caracteres\n" +
                    "uma letra maiúscula, uma letra minúscula\n" +
                    "um dígito e um caracter especial"));
            return false;

        }
        return true;
    }

    public static boolean validateUsername(String username)  {

        if( !username.matches("[a-zA-Z0-9.\\-_]{3,}")) {
            alertMsg(Alert.AlertType.INFORMATION,("O username deve conter pelo menos 3 caracteres!"));
            return false;
        }

        return true;

    }







}
