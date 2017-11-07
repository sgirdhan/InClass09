package com.example.sharangirdhani.inclass09;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sharangirdhani on 11/6/17.
 */

public class loginAsyncTask extends AsyncTask<String, Void, Void> {
    private final OkHttpClient client = new OkHttpClient();
    private String email;
    private String password;
    private ILogin iLogin;
    private Context context;
    private String username;
    private String token;

    public loginAsyncTask(ILogin iLogin, Context context) {
        this.iLogin = iLogin;
        this.context = context;
        this.username = "";
        this.token = "";
    }

    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);
        iLogin.toastLogin(username);
    }

    @Override
    protected Void doInBackground(String... params) {
        email = params[0];
        password = params[1];

        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/login")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {

            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response.body().string());
                    if (jsonResponse.getString("status").equals("ok")) {
                        token = jsonResponse.getString("token");
                        username = jsonResponse.getString("user_fname") + " " + jsonResponse.getString("user_lname");

                        int user_id = Integer.parseInt(jsonResponse.getString("user_id"));
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putString("username", username);
                        editor.putInt("user_id", user_id);
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    interface ILogin {
        void toastLogin(String username);
    }
}
