package com.example.sharangirdhani.inclass09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessageThreadActivity extends AppCompatActivity implements getThreadsListAsyncTask.IMessageThreadList,
        MessageThreadAdapter.IMessageThreadAdapter, deleteThreadMessageThreadListAsyncTask.IDeleteThread,
        addToMessageThreadAsyncTask.IAddtoMessageThread{

    TextView txtViewLoggedUser;
    ImageButton btnAddMessageThread;
    EditText edtTitleofMessageThread;
    List<MessageThreadModel> messageThreadList;
    MessageThreadAdapter messageThreadAdapter;
    LinearLayoutManager layoutManager;
    String token;
    String titleOfThread;
    String loggedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);
        messageThreadList = new ArrayList<>();
        titleOfThread = "";

        edtTitleofMessageThread = (EditText) findViewById(R.id.editTextAddThread);
        txtViewLoggedUser = (TextView) findViewById(R.id.textViewName);
        btnAddMessageThread = (ImageButton) findViewById(R.id.imageButtonAddThread);

        if (getIntent().getExtras() != null) {
            loggedUserName = getIntent().getExtras().getString("username");
            txtViewLoggedUser.setText(loggedUserName);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("token",null);
        new getThreadsListAsyncTask(this).execute(token);

        ((ImageButton) findViewById(R.id.imageButtonLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageThreadActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token");
                editor.remove("user_id");
                editor.remove("username");
                editor.commit();
                Intent intent = new Intent(MessageThreadActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnAddMessageThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleOfThread = edtTitleofMessageThread.getText().toString();
                if (!(titleOfThread.trim().equals("") || titleOfThread.isEmpty())) {
                    new addToMessageThreadAsyncTask(titleOfThread, MessageThreadActivity.this, MessageThreadActivity.this).execute(token);
                    edtTitleofMessageThread.setText("");
                    titleOfThread = "";
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loggedUserName = sharedPreferences.getString("username",null);
        txtViewLoggedUser.setText(loggedUserName);
    }


    public void setAdapterAndNotify(){
        messageThreadAdapter = new MessageThreadAdapter(messageThreadList, MessageThreadActivity.this,this);
        RecyclerView recyclerViewMessageThreads = ((RecyclerView)findViewById(R.id.recyclerViewThreads));
        recyclerViewMessageThreads.setAdapter(messageThreadAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewMessageThreads.setLayoutManager(layoutManager);
        messageThreadAdapter.notifyDataSetChanged();
    }

    @Override
    public void messageUpdate(ArrayList<MessageThreadModel> messageList) {
        messageThreadList = messageList;
        int n = messageThreadList.size();
        setAdapterAndNotify();
    }

    @Override
    public void deleteThread(int thread_id) {
        new deleteThreadMessageThreadListAsyncTask(this, this, thread_id).execute(token);
    }

    @Override
    public void goToChatRoom(int thread_id, String thread_name) {
        Intent intent = new Intent(this, ChatroomActivity.class);
        intent.putExtra("thread_id", thread_id);
        intent.putExtra("thread_name", thread_name);
        startActivity(intent);
    }

    @Override
    public void sendTheListAfterDeletingTheMessageThread(Boolean res) {
        new getThreadsListAsyncTask(this).execute(token);
        if (res) {
            Toast.makeText(this, "Removed from Message Thread", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error : removing message thread", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void inputAddedToMessageThreadList() {
        new getThreadsListAsyncTask(this).execute(token);
    }
}