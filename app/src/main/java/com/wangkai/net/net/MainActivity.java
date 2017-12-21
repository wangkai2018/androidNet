package com.wangkai.net.net;

import android.icu.util.Output;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity {

    EditText et1;
    Button btn1;
    String result="";
    String input="";

    public String send(final String a) {

       new Thread(){

            public void run()
            {
                //建立链接
                HttpURLConnection conn;
                String uri = "http://192.168.1.112:8080/MyServer/FromAndroid";
                /*建立HTTP Post连线*/
                try {
                    //建立地址
                    URL url= new URL(uri);
                    //打开链接
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(500);
                    //输出流，流向服务端
                    OutputStream os=conn.getOutputStream();
                    //写入服务端
                    os.write(URLEncoder.encode(input,"utf-8").getBytes());
                    int code = conn.getResponseCode();
                    Log.i("abc",code+"");
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String state = getStreamFromInputstream(is);
                        Log.i("bcd",state);
                        result =state;
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
        return result;
    }
    //根据输入流返回字符串
    private static String getStreamFromInputstream(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String html = baos.toString();
        baos.close();
        return html;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1= (EditText) findViewById(R.id.et1);
        btn1= (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                input=et1.getText().toString();
                String res= send(input);
                Log.i("test:",res);
            }
        });
    }
}
