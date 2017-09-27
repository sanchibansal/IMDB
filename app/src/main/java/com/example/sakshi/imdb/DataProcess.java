package com.example.sakshi.imdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import static com.example.sakshi.imdb.R.id.linearLayout;

/**
 * Created by sakshi on 8/31/2017.
 */

public class DataProcess extends AsyncTask<Void,Void,String> {

    private Context context;
    private String url;
    private ProgressDialog pDialog;
    private DataListener dataListener;
    View view;
    //parameterized constructor
    public DataProcess(Context context, String url, DataListener dataListener,View view) {
        this.context = context;
        this.url = url;
        this.view = view;
        this.dataListener = dataListener;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        //internetavailable = isOnline();
        //TODO: check  connection here also, app crashes
        //creating httpclient
        OkHttpClient okHttpClient = new OkHttpClient();
        //setting timeout time limit
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120,TimeUnit.SECONDS);

        //Requesting url builder
        Request request = new Request.Builder().url(url).build();
        String responsedata = null;
        try {
            //response of the httpclient
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                //setting response data if response is successful
                responsedata = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //returning response data
        return responsedata;
    }
//    private boolean isOnline() {
//        try{
//            // ping to googlevto check internet connectivity
//            Socket socket = new Socket();
//            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 80);
//            socket.connect(socketAddress, 3000);
//            socket.close();
//            return true;
//        } catch (Exception e) {
//            // internet not working
//            return false;
//        }
//    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        if (pDialog.isShowing())
            pDialog.dismiss();
            //calling update list method
            dataListener.updatelist(aVoid);

        /*else{
            Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //isInternetAvailable();
                    isOnline();
                }
            });
            snackbar.show();
        }*/

    }
}
