package jerry.farmhelper.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MyHandler extends Handler {
    public MyHandler() {
        super(Looper.myLooper(), null);
    }

    public void post(final Request request, final Object response) {
        post(new Runnable() {
            @Override
            public void run() {
                request.listener.onResponse(response);
            }
        });
    }


    @Override
    public void dispatchMessage(Message msg) {
        NetWork.queue.state--;
        msg.getCallback().run();
    }
}
