package com.example.sharangirdhani.inclass09;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by sharangirdhani on 11/6/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListRecyclerViewHolder> {
    private Context mContext;
    private List<Message> messageList;
    private int rowResId;
    private SharedPreferences sharedPreferences;
    private String token;
    private OkHttpClient client;
    private int loggedInUserId;
    IMessageAdapter iMessageAdapter;

    public MessageListAdapter(Context mContext, List<Message> messageList, int rowResId, IMessageAdapter iMessageAdapter) {
        this.mContext = mContext;
        this.messageList = messageList;
        this.rowResId = rowResId;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.token = sharedPreferences.getString("token","");
        this.loggedInUserId = sharedPreferences.getInt("user_id", 0);
        client = new OkHttpClient();
        this.iMessageAdapter = iMessageAdapter;
    }

    @Override
    public MessageListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.message_row,parent,false);
        MessageListRecyclerViewHolder messageListRecyclerViewHolder = new MessageListRecyclerViewHolder(view);
        return messageListRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final MessageListRecyclerViewHolder holder, final int position) {
        final Message currentMessage = messageList.get(position);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMessageAdapter.deleteMessage(currentMessage.getId());
            }
        });
        if (currentMessage.getUser_id() == this.loggedInUserId) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.textMessage.setText(currentMessage.getMessage());
        holder.textWriter.setText(currentMessage.getFullName());
        holder.textTime.setText(currentMessage.getPrettyTime());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageListRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textMessage;
        TextView textWriter;
        TextView textTime;
        public MessageListRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.btnDelete);
            textMessage = (TextView) itemView.findViewById(R.id.tvTextMsg);
            textWriter = (TextView) itemView.findViewById(R.id.tvName);
            textTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }

    interface IMessageAdapter
    {
        void deleteMessage(int msg_id);
    }



}