package com.example.sharangirdhani.inclass09;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MessageThreadAdapter extends RecyclerView.Adapter<MessageThreadAdapter.MessageThreadRecylerViewHolder>{


    private List<MessageThreadModel> messageThreadList;
    private Context context;
    private IMessageThreadAdapter iMessageThreadAdapter;
    int user_id;

    public MessageThreadAdapter(List<MessageThreadModel> messageThreadList, Context context, IMessageThreadAdapter messageThreadAdapter) {
        this.messageThreadList = messageThreadList;
        this.context = context;
        this.iMessageThreadAdapter = messageThreadAdapter;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        user_id = prefs.getInt("user_id", 0);
    }

    @Override
    public MessageThreadRecylerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.thread_row,parent,false);
        MessageThreadRecylerViewHolder messageThreadRecylerViewHolder = new MessageThreadRecylerViewHolder(view);
        return messageThreadRecylerViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageThreadRecylerViewHolder holder, int position) {
        final MessageThreadModel messageThreadModel = messageThreadList.get(position);

        holder.textThreadTitle.setText(messageThreadModel.getTitle());
        if(user_id==messageThreadModel.getUserID()){
            holder.btnDeleteThread.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btnDeleteThread.setVisibility(View.INVISIBLE);
        }

        if(holder.btnDeleteThread.getVisibility()==View.VISIBLE){
            holder.btnDeleteThread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int thread_id = messageThreadModel.getThreadID();
                    iMessageThreadAdapter.deleteThread(thread_id);
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int thread_id = messageThreadModel.getThreadID();
                String thread_name = messageThreadModel.getTitle();
                iMessageThreadAdapter.goToChatRoom(thread_id, thread_name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageThreadList.size();
    }

    public class MessageThreadRecylerViewHolder extends RecyclerView.ViewHolder {
        private TextView textThreadTitle;
        private ImageButton btnDeleteThread;
        public MessageThreadRecylerViewHolder(View itemView) {
            super(itemView);
            btnDeleteThread = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteThread);
            textThreadTitle = (TextView) itemView.findViewById(R.id.textViewThreadName);
        }
    }

    interface IMessageThreadAdapter
    {
        void deleteThread(int thread_id);
        void goToChatRoom(int thread_id, String thread_name);
//        void sendInstructorInformation(int position);
//        void removeDataFromFirstList(Music music);
//        void refreshUpperList();

    }
}