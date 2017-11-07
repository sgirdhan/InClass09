package com.example.sharangirdhani.inclass09;

/**
 * Created by sharangirdhani on 11/6/17.
 */

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class getMessagesAsyncTask extends AsyncTask<String,Void,ArrayList<Message>> {
    private final OkHttpClient client = new OkHttpClient();
    private String token;
    private int thread_id;
    private ArrayList<Message> messageArrayList;
    private IMessage iMessage;

    public getMessagesAsyncTask(IMessage iMessage, int thread_id) {
        this.iMessage = iMessage;
        this.thread_id = thread_id;
    }

    @Override
    protected ArrayList<Message> doInBackground(String... params) {
        messageArrayList = new ArrayList<>();
        token = params[0];

        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/messages/"+this.thread_id)
                .addHeader("Authorization", "BEARER " + token)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.isSuccessful()) {
            ArrayList<Message> messageList = new ArrayList<>();
            try {
                JSONObject rootJsonObject = new JSONObject(response.body().string());
                JSONArray messagesJsonArray = rootJsonObject.getJSONArray("messages");

                for (int i = 0; i < messagesJsonArray.length(); i++) {
                    JSONObject message = messagesJsonArray.getJSONObject(i);
                    String firstName = message.getString("user_fname");
                    String lastName = message.getString("user_lname");
                    int id = message.getInt("id");
                    int user_id = message.getInt("user_id");
                    String msg = message.getString("message");
                    String created = message.getString("created_at");
                    messageList.add(new Message(firstName, lastName, id, user_id, msg, created));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageArrayList.clear();
            messageArrayList.addAll(messageList);

        }
        return messageArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Message> messages) {
        iMessage.messageUpdate(messages);
    }

    public interface IMessage{
        void messageUpdate(ArrayList<Message> messages);
    }
}
