package sample;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by pc on 12/6/16.
 */


class SecurityKey {

    static boolean autox(String pass1, String pass2){
        System.out.println("Pass TXT: "+pass1);
        System.out.println("Crypt Pass: "+pass2);
        if(BCrypt.checkpw(pass1,pass2)){
            System.out.println("pois eh igual");
            return true;
        }
        return false;
    }


    static String enCodePass(String passtxt){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(passtxt);

    }

}
