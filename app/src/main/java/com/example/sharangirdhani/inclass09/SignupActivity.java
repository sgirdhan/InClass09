package com.example.sharangirdhani.inclass09;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.OkHttpClient;

public class SignupActivity extends AppCompatActivity implements signupAsyncTask.ISignup{

    private final OkHttpClient client = new OkHttpClient();
    private String token = "test";

    private EditText firstNameText, lastNameText, emailText, passwordText, repeatPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firstNameText = (EditText) findViewById(R.id.editFirstName);
        lastNameText = (EditText) findViewById(R.id.editLastName);
        emailText = (EditText) findViewById(R.id.editEmail);
        passwordText = (EditText) findViewById(R.id.editPassword);
        repeatPasswordText = (EditText) findViewById(R.id.editRepeatPassword);

        Button buttonSignup = (Button) findViewById(R.id.buttonSignup);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String firstName = firstNameText.getText().toString();
                    String lastName = lastNameText.getText().toString();
                    String email = emailText.getText().toString();
                    String password = passwordText.getText().toString();
                    String repeatPassword = repeatPasswordText.getText().toString();

                    if(firstName.equals("") || firstName.isEmpty() || lastName.equals("") || lastName.isEmpty() || email.equals("") || email.isEmpty() || password.equals("") || password.isEmpty()) {
                        Toast.makeText(SignupActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
                    }
                    else if(!password.trim().equals(repeatPassword.trim())) {
                        Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                    else {
                        new signupAsyncTask(SignupActivity.this, SignupActivity.this).execute(email, password, firstName, lastName);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void toastSignUp(String username, String error) {
        if(!username.equals("")) {
            Intent intent = new Intent(this, MessageThreadActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, error , Toast.LENGTH_LONG).show();
        }
    }
}