package com.example.sharangirdhani.inclass09;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by sharangirdhani on 11/6/17.
 */

public class postMessageAsyncTask extends AsyncTask<String,Void,Void> {

    private final OkHttpClient client = new OkHttpClient();
    private String token;
    private String message;
    private IPostMessage iPostMessage;
    private int thread_id;

    public postMessageAsyncTask(IPostMessage iPostMessage, int thread_id) {
        this.iPostMessage = iPostMessage;
        this.thread_id = thread_id;
    }

    @Override
    protected Void doInBackground(String... params) {
        message = params[0];
        token = params[1];

        RequestBody requestBody = new FormBody.Builder()
                .add("message", message)
                .add("thread_id", String.valueOf(thread_id))
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/add")
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

    @Override
    protected void onPostExecute(Void aVoid) {
        iPostMessage.messagePosted();
    }

    public interface IPostMessage{
        void messagePosted();
    }
}
