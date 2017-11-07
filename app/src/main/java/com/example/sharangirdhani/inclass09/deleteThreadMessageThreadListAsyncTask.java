package com.example.sharangirdhani.inclass09;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class deleteThreadMessageThreadListAsyncTask extends AsyncTask<String, Void, Boolean> {
    IDeleteThread iDeleteThread;
    String token;
    int user_id;
    private final OkHttpClient client = new OkHttpClient();
    Context context;
    int thread_id;

    public deleteThreadMessageThreadListAsyncTask(IDeleteThread iDeleteThread, Context context, int thread_id) {
        this.iDeleteThread = iDeleteThread;
        this.context = context;
        this.thread_id = thread_id;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        token = params[0];

        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/delete/"+thread_id)
                .addHeader("Authorization", "BEARER " + token)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.isSuccessful();
    }


    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);
        iDeleteThread.sendTheListAfterDeletingTheMessageThread(res);
    }

    interface IDeleteThread{
        void sendTheListAfterDeletingTheMessageThread(Boolean res);
    }
}