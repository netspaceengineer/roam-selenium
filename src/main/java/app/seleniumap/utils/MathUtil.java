package app.seleniumap.utils;


import java.util.Random;
@SuppressWarnings("unused")
public class MathUtil {

    private static String sLpTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz";
    private static String sLpTableWS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static String nLpTable = "1234567890";
    public static int generateRandomNumberByLength(int numLength) {
        return Integer.parseInt(randomizeCharacters(nLpTable, numLength));
    }

    public static int generateRandomNumber(int min,int max) {
        int number = (int)(Math.random() * (max - min + 1) + min);
        return number;
    }

    public static int generateRandomNumber() {
        return Integer.parseInt(randomizeCharacters(nLpTable, 5));
    }

    private static String randomizeCharacters(String lookUpTable, int len) {
        StringBuilder s = new StringBuilder();
        Random rnd = new Random();
        while (s.length() < len) {
            int index = (int) (rnd.nextFloat() * lookUpTable.length());
            s.append(lookUpTable.charAt(index));
        }
        String sResult = s.toString();
        return sResult;
    }

    public static String toHex(long d) {
        double rem;

        // For storing result
        String res="";

        // Digits in hexadecimal number system
        char hex[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        while(d>0)
        {
            rem=d%16;
            res=hex[(int) rem]+res;
            d=d/16;
        }
        return res;

    }
}
