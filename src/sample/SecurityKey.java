package sample;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by pc on 12/6/16.
 *
 * EX: $2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa
 *
 * 2a identifies the bcrypt algorithm version that was used.
 * 10 is the cost factor; 210 iterations of the key derivation function are used
 * vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa
 * is the salt and the cipher text, concatenated and encoded in a modified Base-64.
 * The first 22 characters decode to a 16-byte value for the salt.
 * The remaining characters are cipher text to be compared for authentication.
 *
 * password #=> "secret1"
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
