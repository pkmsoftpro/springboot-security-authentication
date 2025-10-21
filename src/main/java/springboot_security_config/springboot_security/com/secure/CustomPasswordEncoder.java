package springboot_security_config.springboot_security.com.secure;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by prashant.mod on 23-07-2024 Tuesday 12:36:12 pm
 *
 */
@Service
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        int length = rawPassword.length();
        StringBuilder encryptedPassword = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char encryptedChar = (char) (rawPassword.charAt(i) + 1);
            encryptedPassword.append(encryptedChar);
        }

        return encryptedPassword.toString();
    }


    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String decryptedPassword = decryptPassword(encodedPassword);
        return decryptedPassword.equals(rawPassword.toString());
    }

    public String decryptPassword(String userPassword) {
        int length = userPassword.length();
        StringBuilder decryptedPassword = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char decryptedChar = (char) (userPassword.charAt(i) - 1);
            decryptedPassword.append(decryptedChar);
        }

        return decryptedPassword.toString().trim();
    }
}
