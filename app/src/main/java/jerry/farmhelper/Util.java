package jerry.farmhelper;

public class Util {

    private static StringBuilder builder = new StringBuilder(7);

    public static String calculateTime(int time) {
        int h = time / 3600;
        time = time - h * 3600;
        int m = time / 60;
        time = time - m * 60;
        builder.setLength(0);
        builder.append((char) (h + '0')).append(':');
        formatTime(m);
        builder.append(':');
        formatTime(time);
        return builder.toString();
    }

    private static void formatTime(int time) {
        if (time < 10) {
            builder.append('0');
            builder.append((char) (time + '0'));
        } else {
            int s = (time / 10);
            builder.append((char) (s + '0'));
            builder.append((char) (time - s * 10 + '0'));
        }
    }
}
