package jerry.farmhelper.net;


public class Request {
    public int times = 3;
    public final String url;
    public final String body;
    public final Listener listener;

    public interface Listener {
        void onResponse(Object object);
    }

    public Request(String url, String body, Listener listener) {
        this.url = url;
        this.body = body;
        this.listener = listener;
    }
}
