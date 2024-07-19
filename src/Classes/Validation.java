package Classes;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean emptyInput(ArrayList<String> textInput) {
        for (String input : textInput) {
            if (input.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public static boolean validUsername(String username) {
        final String userRegex = "^[А-Я][а-я]+ [А-Я][а-я]+$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(userRegex);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean validCourseNum(String courseNum) {
        final String courseNumRegex = "^[0-9]{5}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(courseNumRegex);
        matcher = pattern.matcher(courseNum);

        return matcher.matches();
    }

    public static boolean validEGN(String egn) {
        final String egnRegex = "^[0-9]{10}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(egnRegex);
        matcher = pattern.matcher(egn);

        return matcher.matches();
    }

    public static boolean validPhoneNum(String phoneNum) {
        final String phoneNumRegex = "^[0-9]{10}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(phoneNumRegex);
        matcher = pattern.matcher(phoneNum);

        return matcher.matches();
    }

    public static boolean onlyNumberInput(String onlyNumbers) {
        final String onlyNumbersRegex = "^[0-9]+$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(onlyNumbersRegex);
        matcher = pattern.matcher(onlyNumbers);

        return matcher.matches();
    }
}