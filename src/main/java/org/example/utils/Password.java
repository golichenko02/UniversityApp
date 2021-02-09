package org.example.utils;

import java.util.Random;

public class Password {
    private static char[] symbols = {'!', '@', '#', '$', '%', '_'};
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lower = upper.toLowerCase();


    private static int randomSymbolIndx(char[] symbols) {
        Random r1 = new Random();
        int symbolIndx = r1.nextInt(symbols.length);
        return symbolIndx;
    }

    private static String randomNumeral(int count) {
        Random r1 = new Random();
        int numeral = r1.nextInt(10);
        count--;
        if (count == 0) {
            return String.valueOf(numeral);
        }
        return numeral + randomNumeral(count);
    }

    private static String randomUpperIndx(String s, int count) {
        Random r1 = new Random();
        int upperIndx = r1.nextInt(s.length());
        count--;
        if (count == 0) {
            return String.valueOf(s.charAt(upperIndx));
        }
        return s.charAt(upperIndx) + randomUpperIndx(s, count);
    }

    private static String randomLowerIndx(String s, int count) {
        Random r1 = new Random();
        int lowerIndx = r1.nextInt(s.length());
        count--;
        if (count == 0) {
            return String.valueOf(s.charAt(lowerIndx));
        }
        return s.charAt(lowerIndx) + randomLowerIndx(s, count);
    }

    private static String shufflePassword(String s) {
        Random r1 = new Random();
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int j = r1.nextInt(s.length());
            char temp = arr[j];
            arr[j] = arr[i];
            arr[i] = temp;
        }
        System.out.print("Password: ");
        return String.valueOf(arr);
    }

    public static String showPassword(){
        String password = randomNumeral(3);
        password += symbols[randomSymbolIndx(symbols)];
        password += randomUpperIndx(upper, 2);
        password += randomLowerIndx(lower, 2);
        return shufflePassword(password);
    }

}
