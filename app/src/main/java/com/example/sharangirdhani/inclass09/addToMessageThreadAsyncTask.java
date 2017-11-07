package com.example.sharangirdhani.inclass09;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by KashishSyeda on 11/6/2017.
 */

public class addToMessageThreadAsyncTask extends AsyncTask<String,Void,Void> {
    private final OkHttpClient client = new OkHttpClient();
    private String token;
    private String title;
    private IAddtoMessageThread iAddtoMessageThread;
    private Context context;

    public addToMessageThreadAsyncTask(String title, IAddtoMessageThread iAddtoMessageThread, Context context) {
        this.title = title;
        this.iAddtoMessageThread = iAddtoMessageThread;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        iAddtoMessageThread.inputAddedToMessageThreadList();
    }

    @Override
    protected Void doInBackground(String... params) {
        token = params[0];

        RequestBody requestBody = new FormBody.Builder()
                .add("title", title)
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/add")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Authorization", "BEARER " + token)
                .post(requestBody)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    interface IAddtoMessageThread{
        void inputAddedToMessageThreadList();
    }
}