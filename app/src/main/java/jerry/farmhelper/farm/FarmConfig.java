package jerry.farmhelper.farm;


import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;

import jerry.farmhelper.Key;
import jerry.farmhelper.MainActivity;

public class FarmConfig {
    public static String cookies;
    //public static byte[] cookiesChar;
    public static byte[] sKey = new byte[10];
    public static String QQNum;
    public static String id = "1583709838";
    public static final String uinY = "uinY=";
    // public static final String farmTime = "&farmTime=";
    // public static final String farmKey = "&farmKey=";
    // public static final String farmKey2 = "&farmKey2=";
    public static final String ownerId = "&ownerId=";
    public static final String uIdx = "&uIdx=";
    public static final String place = "&place=";
    public static String commonBody;
    public static final String param = "&farmTime=0&farmKey=db03aaa4c6ae8e56943f5dda8791e48b&farmKey2=b8af0db90248dde0ce5dd32128ac78ab";

    public static void setCookiesFormFile(byte[] cookiesByte, int len) {
        int qqLen = len - 21;
        QQNum = new String(cookiesByte, 5, qqLen);
        commonBody = uinY + QQNum + uIdx + id + ownerId + id + param;
        qqLen = len;
        for (int i = 9; i >= 0; ) {
            sKey[i--] = cookiesByte[--qqLen];
        }
        Key.formatToken();
        cookies = new String(cookiesByte, 0, len);
        Log.i("setCookiesFormFile", "cookies=" + cookies);
    }


    public static void setCookies(MainActivity activity, String originalCookies) {
        if (originalCookies == null) return;
        Log.i("setCookies", originalCookies);
        StringBuilder builder = new StringBuilder();
        int len = originalCookies.length();
        char[] chars = new char[len];
        originalCookies.getChars(0, len, chars, 0);
        int o = 70;
        for (; o < len; o++) {
            if (chars[o] == 'o') break;
        }
        int qqLen = 4;
        for (; qqLen < 12; qqLen++) {
            if (chars[o + 1 + qqLen] == ';') {
                break;
            }
        }
        builder.append(chars, o - 4, qqLen + 6);
        QQNum = new String(chars, o + 1, qqLen);
        Log.i("setCookies", "QQ号=" + QQNum);
        int a = o + 3 + qqLen;
        for (; a < len; a++) {
            if (chars[a] == 'k') break;
        }
        builder.append(chars, a - 1, 15);
        a += 4;
        for (len = 0; len < 10; len++) {
            byte b = (byte) chars[a++];
            sKey[len] = b;
        }
        Key.formatToken();
        cookies = builder.toString();

        Log.i("setCookies", "Cookies=" + cookies);
        commonBody = uinY + QQNum + uIdx + id + ownerId + id + param;
        try {
            FileOutputStream output = activity.openFileOutput("sss.txt", Context.MODE_PRIVATE);
            output.write(cookies.getBytes());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static byte[] charTobyte(char[] chars) {
//        byte[] bytes = new byte[chars.length];
//        for (int i = 0; i < chars.length; i++) {
//            bytes[i] = (byte) chars[i];
//        }
//        return bytes;
//    }
//
//    public void clearLog() {
//        optimes = 0;
//    }

//    public static int indexOf0(char[] chars, char[] target) {
//        int t = chars.length - target.length + 1;
//        int a;
//        for (int i = 0; i < t; i += (a + 1)) {
//            for (a = 0; a < target.length; a++) {
//                if (chars[i + a] != target[a]) break;
//                if (a == target.length - 1) return a + 1;
//            }
//        }
//        return -1;
//    }
//
//    public static int indexOf1(char[] chars, char[] target) {
//
//    }
//
//    public static int indexOf2(char[] chars, char[] target) {
//
//    }
}
