package com.example.meteoservice;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequest implements Runnable{
    static String CITY = "Moscow";
    static final String KEY = "33047d86981749c792895440231612";
    static final String APIREQUEST = "https://api.weatherapi.com/v1/current.json";

    URL url;
    Handler handler;
    public HttpsRequest(Handler handler) {
        this.handler = handler;
        try {
            url = new URL(APIREQUEST + "?"+"q="+CITY+"&"+"key="+KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            Scanner in = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while(in.hasNext()){
                response.append(in.nextLine());
            }
            in.close();
            connection.disconnect();
            Message msg = Message.obtain();
            msg.obj = response.toString();
            handler.sendMessage(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
