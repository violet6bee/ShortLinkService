package org.example;

public class RandomShortLink {
    public static String generateRandomString(){
        String allSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newSymbols = new StringBuilder();
        for(int i=0; i < 7; i++){
            int randomIndex = (int)(Math.random() * allSymbols.length());
            newSymbols.append(allSymbols.charAt(randomIndex));
        }
        return newSymbols.toString();
    }
}
