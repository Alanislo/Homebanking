package com.mindhub.homebanking.utils;

public final class CardUtils {
    public static String getCardNumber() {
        String random = "";
        int i;
        int min = 1000;
        int max = 8999;
        for (i = 0; i < 3; i++){
            random += (int)(Math.random()*(max - min + 1) + min)+"-";
        }
        random += (int)(Math.random()*(max - min + 1) + min);
        return random;
    }
    public static int getCVV() {
        int cvv = (int)(Math.random()*899+100);
        return cvv;
    }
}
