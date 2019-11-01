package com.bawei.danli;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {
    private NetUtil(){

    }
    public static NetUtil getInstance(){
        return NetUrl.netUtil;
    }
    private static class NetUrl{
        private static NetUtil netUtil=new NetUtil();
    }



    public String io2String(InputStream inputStream) throws IOException {
        byte[] bytes=new byte[1024];
        int len=-1;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        String json=null;
        try {
            while((len=inputStream.read(bytes))!=-1){
                byteArrayOutputStream.write(bytes,0,len);
            }
            byte[] bytes1 = byteArrayOutputStream.toByteArray();
            json = new String(bytes1);
        }catch (IOException i){
            i.printStackTrace();
        }finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
        return json;
    }

    public Bitmap io2Bitmap(InputStream inputStream){
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
    @SuppressLint("StaticFieldLeak")
    public void doget(final String httpUrl, final MyBackCall myBackCall){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                HttpURLConnection httpURLConnection=null;
                InputStream inputStream=null;
                String json="";
                try {
                    URL url = new URL(httpUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode()==200){
                        inputStream = httpURLConnection.getInputStream();
                        json = io2String(inputStream);
                    }else{
                        Log.e("tag","请求失败");
                    }

                }catch (IOException i){
                    i.printStackTrace();
                }finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }catch (IOException i){
                            i.printStackTrace();
                        }
                    }
                }

                return json;
            }

            @Override
            protected void onPostExecute(String s) {
                myBackCall.doGet(s);
            }
        }.execute();

    }


    @SuppressLint("StaticFieldLeak")
    public void dogetphone(final String httpUrl, final MyBackCall myBackCall){
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                HttpURLConnection httpURLConnection=null;
                InputStream inputStream=null;
                Bitmap bitmap=null;
                try {
                    URL url = new URL(httpUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode()==200){
                        inputStream = httpURLConnection.getInputStream();
                        bitmap = io2Bitmap(inputStream);
                    }else{
                        Log.e("tag","请求失败");
                    }

                }catch (IOException i){
                    i.printStackTrace();
                }finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }catch (IOException i){
                            i.printStackTrace();
                        }
                    }
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                myBackCall.doGetPhono(bitmap);
            }
        }.execute();

    }

    public boolean hasNet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null&&activeNetworkInfo.isAvailable()){
            return true;
        }else{
            return false;
        }
    }

    public boolean WIFI(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null&&activeNetworkInfo.isAvailable()&&activeNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI){
            return true;
        }else{
            return false;
        }
    }

    public boolean phono(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null&&activeNetworkInfo.isAvailable()&&activeNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
            return true;
        }else{
            return false;
        }
    }



    public interface MyBackCall{
        void doGet(String json);
        void doGetPhono(Bitmap bitmap);
    }

}
