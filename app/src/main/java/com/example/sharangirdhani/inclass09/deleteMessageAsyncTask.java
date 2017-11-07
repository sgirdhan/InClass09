package com.example.sharangirdhani.inclass09;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sharangirdhani on 11/6/17.
 */

public class deleteMessageAsyncTask extends AsyncTask<String, Void, Boolean> {
    private final OkHttpClient client = new OkHttpClient();
    private String token;
    private int message_id;

    private IDeleteMessage iDM;

    public deleteMessageAsyncTask(IDeleteMessage iDM, int message_id) {
        this.iDM = iDM;
        this.message_id = message_id;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        token = params[0];

        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/delete/"+this.message_id)
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
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        iDM.isMessageDeleted(aBoolean);
    }

    public interface IDeleteMessage{
        void isMessageDeleted(boolean resp);
    }
}
