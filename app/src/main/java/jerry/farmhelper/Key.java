package jerry.farmhelper;

import jerry.farmhelper.farm.FarmConfig;

public class Key {
    //   public static MessageDigest messageDigest;
    public static String g_tk;


//    static {
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }

    public static void formatToken() {
        int hashCode = 5381;
        for (int i = 0; i < 10; i++)
            hashCode += (hashCode << 5) + FarmConfig.sKey[i];
        g_tk = Integer.toString(hashCode & 0x7fffffff);
    }

//    public static String farmTime;
//    public static final String key1 = "OPdfqwn^&*w(281flebnd1##roplaq";
//    public static final String key2 = "(UuqQ=Ze93spP*:s1E/kkGt-:s^|Su";
//    public static String farmKey;
//    public static String farmKey2;

//    public void updateKey() {
//        long time = System.currentTimeMillis();
//        farmTime = Integer.toString((int) (time / 1000), 10);
//
//        int index = farmTime.charAt(9) - '0';
//
//        messageDigest.reset();
//
//        String key = key1.substring(index);
//        messageDigest.update(key.getBytes());
//        farmKey = toHex(messageDigest.digest());
//
//        messageDigest.reset();
//
//        key = key2.substring(index);
//        messageDigest.update(key.getBytes());
//        farmKey2 = toHex(messageDigest.digest());
//    }

//    public static String getTime() {
//        long time = System.currentTimeMillis();
//        farmTime = Integer.toString((int) (time / 1000), 10);
//        return farmTime;
//    }

//    public static String getKey() {
//        messageDigest.reset();
//        String key = key1.substring(farmTime.charAt(9) - '0');
//        messageDigest.update(key.getBytes());
//        return toHex(messageDigest.digest());
//    }
//
//    public static String getKey2() {
//        messageDigest.reset();
//        String key = key2.substring(farmTime.charAt(9) - '0');
//        messageDigest.update(key.getBytes());
//        return toHex(messageDigest.digest());
//    }

//    public static String toHex(byte[] bytes) {
//        StringBuilder builder = new StringBuilder(bytes.length * 2);
//        for (byte item : bytes) {
//            int bt = item & 0xFF;
//            if (bt < 0x10) builder.append("0");
//            builder.append(Integer.toHexString(bt));
//        }
//        String data = builder.toString();
//        Log.i("toHex", data + builder.capacity());
//        return data;
//    }
}
