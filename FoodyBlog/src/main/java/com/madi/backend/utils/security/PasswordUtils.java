package com.madi.backend.utils.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            // Create salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Use PBKDF2 to hash the password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128); // 65536 indicates how many
                                                                                     // iterations
                                                                                     // that this algorithm run for
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Cannot hash the password with PBKDF2WithHmacSHA1");
            return "";
        } catch (InvalidKeySpecException e) {
            System.err.println("Invalid key spec");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
