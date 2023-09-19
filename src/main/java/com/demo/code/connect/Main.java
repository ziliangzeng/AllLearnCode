package com.demo.code.connect;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) {


        String url0 = "http://10.19.199.1:30100/tianyingaivehiclecapture/tianyingai/1002/20230404/712c6a7244564d8191a2575fdf57d690.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=5024U4780810919%2F20230915%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230915T012122Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=08f124e9e43bec1aa117a7f73d28518768a26e36b75100566669f119b5eecd63";
        String url1 = "http://10.19.65.135:50005/tianyingaibehaviordetectalarmcapture/tianyingai/5005/20230915/437664b90d4f410995314d064b6ecd6c.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=5024U4780810919%2F20230915%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230915T063427Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=00c7c12e9ae006545907f8ce9a7b1bce67ef3732c6b415a38ce00125298198ca";


        try {
            Thread thread = new Thread(() -> {
                URL url = null;
                try {
                    url = new URL(url0);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    System.out.println("1:::" + responseCode);
//                    InputStream inputStream = urlConnection.getInputStream();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {

                    throw new RuntimeException(e);
                }

            });
            thread.start();

            URL url2 = new URL(url1);
            HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
//            urlConnection2.connect();
            int responseCode2 = urlConnection2.getResponseCode();
            System.out.println("2:::" + responseCode2);
            InputStream inputStream1 = urlConnection2.getInputStream();


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
