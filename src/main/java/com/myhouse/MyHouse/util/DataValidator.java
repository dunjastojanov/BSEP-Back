package com.myhouse.MyHouse.util;

import java.util.regex.Pattern;

public class DataValidator {

    public static boolean isEmailValid(String email) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return patternMatches(email, regexPattern);
    }

    public static boolean patternMatches(String content, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(content)
                .matches();
    }
}
