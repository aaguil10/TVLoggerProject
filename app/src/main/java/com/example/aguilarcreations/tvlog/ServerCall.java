package com.example.aguilarcreations.tvlog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

/**
 * Created by Work on 10/15/17.
 */

public class ServerCall extends AsyncTask {

    public static String GET_MESSAGE = "getMessage";
    Handler.Callback callback = null;
    int TIMEOUT_VALUE = 30000;
    boolean doPost;


    @Override
    protected String doInBackground(Object[] objects) {
        String connection_url = "";
        HashMap<String, String> postDataParams = new HashMap<>();
        if(objects.length > 0){
            connection_url = (String)objects[0];
        }else{
            return "";
        }
        if(objects.length >= 2){
            callback = (Handler.Callback) objects[1];
        }
        if(objects.length >= 3){
            String s = (String) objects[2];
            postDataParams.put("movie", s);
            doPost = true;
        }
        HttpURLConnection urlConnection = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(connection_url);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT_VALUE);
            urlConnection.setReadTimeout(TIMEOUT_VALUE);

            if(doPost) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams)); // encoding and formating the hashmap data for posting other data
                writer.flush();
                writer.close();
                os.close();
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
        return result.toString();
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        String result = (String) o;
        if(callback != null){
            Message m = new Message();
            Bundle d = new Bundle();
            d.putString(GET_MESSAGE,result.toString());
            m.setData(d);
            callback.handleMessage(m);
        }
    }



    // Encoding the Url and formatting the hashmap data
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }


}
