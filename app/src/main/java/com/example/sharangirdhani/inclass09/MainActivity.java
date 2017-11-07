package com.example.sharangirdhani.inclass09;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharangirdhani.inclass09.databinding.ActivityMainBinding;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements loginAsyncTask.ILogin {
    private ActivityMainBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private String token = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.editName)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.editPassword)).getText().toString().trim();

                try {
                    if(username.equals("") || username.isEmpty() || password.isEmpty() || password.equals("")) {
                        Toast.makeText(MainActivity.this, "Both email and passwords must be filled", Toast.LENGTH_LONG).show();
                    }
                    else {
                        new loginAsyncTask(MainActivity.this, MainActivity.this).execute(username, password);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void toastLogin(String username) {
        if(!username.equals("")) {
            Intent intent = new Intent(this, MessageThreadActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_LONG).show();
        }
    }
}
