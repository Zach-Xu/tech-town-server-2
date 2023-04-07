package com.tech.utils;

public class StringUtils {


    public static final String DELIMITER = "\n\n";

    public static String getSubstringAfter(String input) {
        return getSubstringAfter(input, DELIMITER);
    }

    public static String getSubstringAfter(String input, String delimiter) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        if (delimiter == null || delimiter.isEmpty()) {
            return input;
        }

        int index = input.indexOf(delimiter);
        if (index == -1) {
            return "";
        }

        int startIndex = index + delimiter.length();
        if (startIndex >= input.length()) {
            return "";
        }

        return input.substring(startIndex);
    }

    public static boolean isValidGitHubUserLink(String link) {
        // Regular expression to match a valid GitHub user link
        String regex = "^https?:\\/\\/github\\.com\\/[a-zA-Z0-9_-]+(\\/)?$";

        return link.matches(regex);
    }

    public static boolean isValidGitHubUsername(String username){
        // Regular expression to match a valid GitHub username
        String regex = "^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}$";

        return username.matches(regex);
    }

}
