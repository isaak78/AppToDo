package sample;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * Created by pc on 12/6/16.
 */
public class SecurityKey {
    public static void securiryTest(String pass) {
        System.out.println("----- Security Crypt Password Encode -----");
        final String password = pass;
        int i = 0;
        while (i < 10) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(hashedPassword);
            i++;
        }
    }

}
