package jerry.farmhelper.net;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RequestQueue {
    public final LinkedList<Request> linkedList = new LinkedList<>();
    public final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    public int state = 0;


    public void add(String url, String body, Request.Listener listener) {
        Request request = new Request(url, body, listener);
        lock.lock();
        linkedList.add(request);
        state++;
        if (linkedList.size() == 1) {
            notEmpty.signal();
        }
        lock.unlock();
    }

    public Request take() throws InterruptedException {
        lock.lock();
        while (linkedList.size() == 0) {
            Log.i("RequestQueue", "队列无数据，开始阻塞");
            notEmpty.await();
        }
        Request request = linkedList.removeFirst();
        lock.unlock();
        return request;
    }
}
