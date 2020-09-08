package com.example.eli_tshirts.POJO;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation helper class for email and password validation
 */
public class Validation {

    /**
     * Static method to validate if parameter is email or not
     * @param email - email input
     * @return - true if email or false if not
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Static method to validate if parameter is password or not
     * @param password - password input
     * @return - true if password or false if not
     */
    public static boolean isValidPassword(final String password) {
        final String PASSWORD_PATTERN = "^(?=.[0-9])(?=.[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}