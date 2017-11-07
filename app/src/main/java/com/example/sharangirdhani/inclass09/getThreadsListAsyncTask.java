package com.example.sharangirdhani.inclass09;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by KashishSyeda on 11/6/2017.
 */

public class getThreadsListAsyncTask extends AsyncTask<String,Void,ArrayList<MessageThreadModel>> {

    private final OkHttpClient client = new OkHttpClient();
    private String token;
    private ArrayList<MessageThreadModel> messageThreadList;
    private IMessageThreadList iMessageThread;

    public getThreadsListAsyncTask(IMessageThreadList iMessageThread) {
        this.iMessageThread = iMessageThread;
    }

    @Override
    protected ArrayList<MessageThreadModel> doInBackground(String... params) {
        messageThreadList = new ArrayList<>();
        token = params[0];

        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread")
                .addHeader("Authorization", "BEARER " + token)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.isSuccessful()) {
            ArrayList<MessageThreadModel> messageList = new ArrayList<>();
            try {
                JSONObject rootJsonObject = new JSONObject(response.body().string());
                JSONArray messagesJsonArray = rootJsonObject.getJSONArray("threads");

                for (int i = 0; i < messagesJsonArray.length(); i++) {
                    JSONObject message = messagesJsonArray.getJSONObject(i);
                    String user_id = message.getString("user_id");
                    String thread_id = message.getString("id");
                    String title = message.getString("title");


                    messageList.add(new MessageThreadModel(title, Integer.parseInt(user_id), Integer.parseInt(thread_id)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageThreadList.clear();
            messageThreadList.addAll(messageList);

        }
        return messageThreadList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<MessageThreadModel> messageThreadList) {
        super.onPostExecute(messageThreadList);
        iMessageThread.messageUpdate(messageThreadList);
    }
    interface IMessageThreadList{
        void messageUpdate(ArrayList<MessageThreadModel> messageThreadList);
    }
}