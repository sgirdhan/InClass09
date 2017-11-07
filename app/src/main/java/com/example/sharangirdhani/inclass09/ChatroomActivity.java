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

public class ChatroomActivity extends AppCompatActivity implements getMessagesAsyncTask.IMessage, postMessageAsyncTask.IPostMessage, MessageListAdapter.IMessageAdapter, deleteMessageAsyncTask.IDeleteMessage {
    private String token;
    private int user_id;
    private int thread_id;
    private String thread_name;

    private MessageListAdapter rvAdapter;
    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    TextView threadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        thread_id = -1;
        thread_name = "";


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("token",null);
        user_id = sharedPreferences.getInt("user_id", 0);

        messageArrayList = new ArrayList<Message>();

        threadName = (TextView) findViewById(R.id.tvThreadName);
        ImageButton btnHome = (ImageButton) findViewById(R.id.imgHome);
        ImageButton btnPost = (ImageButton) findViewById(R.id.btnSend);
        final EditText editMessage = (EditText) findViewById(R.id.txtMsg);

        if(getIntent().getExtras() != null) {
            thread_id = getIntent().getExtras().getInt("thread_id");
            thread_name = getIntent().getExtras().getString("thread_name");
            threadName.setText(thread_name);
        }
        if(thread_id != -1) {
            new getMessagesAsyncTask(this, thread_id).execute(token);
        }
        else {
            Toast.makeText(this, "Error getting thread_id", Toast.LENGTH_LONG).show();
        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editMessage.getText().toString();
                new postMessageAsyncTask(ChatroomActivity.this, thread_id).execute(msg, token);
                editMessage.setText("");
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatroomActivity.this, MessageThreadActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        threadName.setText(thread_name);
    }

    public void setAdapterAndNotify(){
        rvAdapter = new MessageListAdapter(this, messageArrayList, R.layout.message_row ,this);
        recyclerView = (RecyclerView) findViewById(R.id.chatRV);
        recyclerView.setAdapter(rvAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void messageUpdate(ArrayList<Message> messages) {
        messageArrayList.clear();
        messageArrayList.addAll(messages);
        setAdapterAndNotify();
    }

    @Override
    public void messagePosted() {
        new getMessagesAsyncTask(this, thread_id).execute(token);
    }

    @Override
    public void deleteMessage(int msg_id) {
        new deleteMessageAsyncTask(this, msg_id).execute(token);
    }

    @Override
    public void isMessageDeleted(boolean resp) {
        if(resp) {
            Toast.makeText(this, "Message successfully deleted", Toast.LENGTH_LONG).show();
            new getMessagesAsyncTask(this, thread_id).execute(token);
        }
        else {
            Toast.makeText(this, "Message not deleted", Toast.LENGTH_LONG).show();
        }

    }
}
