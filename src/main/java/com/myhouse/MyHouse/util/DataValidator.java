package com.myhouse.MyHouse.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DataValidator {

    private final static String PASSWORDS_FILE="src/main/resources/common_passwords.txt";
    public static boolean isEmailValid(String email) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return patternMatches(email, regexPattern);
    }

    public static boolean isPasswordValid(String password) {
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{12,20}$";
        return patternMatches(password, regexPattern);
    }

    public static boolean patternMatches(String content, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(content)
                .matches();
    }
    public static List<String> loadFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public static boolean isInMostCommonPasswords(String password){
        List<String> mostCommonPasswords= loadFile(PASSWORDS_FILE);
        return mostCommonPasswords.stream().filter(pass -> pass.length() == password.length()).anyMatch(pass -> pass.equals(password));
    }
}