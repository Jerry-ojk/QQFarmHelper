package jerry.farmhelper.net;

import android.os.Process;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import jerry.farmhelper.Key;
import jerry.farmhelper.farm.FarmConfig;

public class NetWork extends Thread {
    private static int count = 0;
    public static final RequestQueue queue = new RequestQueue();
    public static MyHandler handler = new MyHandler();
    //public static OkHttpClient client = new OkHttpClient();
    public volatile boolean isRunning = true;
    public static SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();


    public NetWork() {
        super(null, null, "NetWork", 0);
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    }

    public void stopSelf() {
        isRunning = false;
        interrupt();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                final Request request = queue.take();
                //Log.i("NetWork", "开始第" + (++count) + "个请求:" + request.url);
                String response = postBySocket(request.url, request.body);
                if (response != null) {
                    //Log.i("NetWork", request.url + "\n" + request.body + "\n" + response);
                    handler.post(request, JSON.parse(response));
                } else {
                    Log.e("NetWork", "第" + count + "个请求失败\n" + request.url + "\n" + request.body);
                }
            }
        } catch (InterruptedException e) {
            Log.i("NetWork", "InterruptedException");
        } finally {
            if (queue.lock.isHeldByCurrentThread()) queue.lock.unlock();
            Log.i("NetWork", "退出线程");
        }
    }

//    private String postByOk(String url, String body) {
//        long a = System.currentTimeMillis();
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url + "?g_tk=" + Key.getToken())
//                .addHeader("Cookie", FarmConfig.cookies)
//                .post(FormBody.create(null, body.getBytes(Charset.defaultCharset())))
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            String data = response.body().string();
//            Log.i("postByOk", response.headers().toString());
//            Log.i("postByOk", data);
//            Log.i("postByOk", "时间=" + (System.currentTimeMillis() - a));
//            return data;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private String postBySocket(String url, String body) {
        long a = System.currentTimeMillis();
        int t = 0;
        while (t < 3) {
            t++;
            try {
                SSLSocket socket = (SSLSocket) socketFactory.createSocket("101.226.90.180", 443);
                StringBuilder builder = new StringBuilder(400);
                OutputStream outStream = socket.getOutputStream();
                builder.append("POST ").append(url).append(Key.g_tk).append(" HTTP/1.1\r\n")
                        .append("Host: nc.qzone.qq.com\r\n")
                        .append("Content-Length: ").append(body.length()).append("\r\n")
                        .append("Connection: close\r\n")
                        .append("Cookie: ").append(FarmConfig.cookies).append("\r\n")
                        .append("\r\n").append(body);
                outStream.write(builder.toString().getBytes());
                InputStream inputStream = socket.getInputStream();
                byte[] buff = new byte[12];
                if (inputStream.read(buff, 0, 12) != 12) {
                    Log.i("postBySocket", "读取头部出错");
                    socket.close();
                    continue;
                }
                int code = (buff[9] - '0') * 100 + (buff[10] - '0') * 10 + buff[11] - '0';
                Log.i("postBySocket", "状态码=" + code);
                if (code != 200 || inputStream.skip(98) != 98) {
                    Log.i("postBySocket", "skip出错");
                    socket.close();
                    continue;
                }
                int l = 0;
                int tLen = 0;
                for (; l < 6; l++) {
                    int n = inputStream.read();
                    if (n == '\r') {
                        break;
                    } else {
                        tLen = (tLen * 10 + n - '0');
                    }
                }
                System.out.println("tLen=" + tLen);

                if (inputStream.skip(45) != 45) {
                    socket.close();
                    continue;
                }
                if (tLen <= 0) {
                    socket.close();
                    return "";
                }
                byte[] data = new byte[tLen];
                int rLen = 0;
                while (rLen != tLen) {
                    int len = inputStream.read(data, rLen, tLen - rLen);
                    if (len == -1) break;
                    rLen += len;
                    //System.out.println("rLen=" + rLen + ",tLen" + tLen);
                }
                if (rLen != tLen) {
                    System.out.println("长度不对");
                    socket.close();
                    return "";
                }
                socket.close();
                return new String(data, 0, tLen);
            } catch (Exception e) {
                e.printStackTrace();
            }
            t++;
        }
        Log.i("postBySocket", "次数=" + t + ",时间=" + (System.currentTimeMillis() - a));
        return null;
    }

//    private String post(String url, String body) {
//        long a = System.currentTimeMillis();
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(null, url).openConnection();
//            connection.setRequestMethod(post);
//            connection.setUseCaches(false);
//            connection.setRequestProperty("Connection", "close");
//            connection.setRequestProperty("Cookie", FarmConfig.cookies);
//            connection.setDoOutput(true);
//            connection.getOutputStream().write(body.getBytes());
//            int code = connection.getResponseCode();
//            if (code == HttpURLConnection.HTTP_OK) {
//                Set<String> keys = connection.getHeaderFields().keySet();
//                Log.i("post", keys.size() + "");
//                for (String key : keys) {
//                    String val = connection.getHeaderField(key);
//                    Log.i("post", key + "=" + val);
//                }
//                BufferedReader reader = new BufferedReader(new InputStreamReader(
//                        connection.getInputStream()));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    Log.i("post", line);
//                }
//            } else {
//                Log.e("post", "响应码：" + code);
//            }
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.i("post", "时间" + (System.currentTimeMillis() - a));
//        return null;
//    }
}
