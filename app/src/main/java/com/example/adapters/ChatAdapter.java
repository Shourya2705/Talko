package com.example.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Message;
import com.example.talko.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<Message> msgModel;
    Context context;
    String rid;

    public ChatAdapter(ArrayList<Message> msgModel, Context context, String rid) {
        this.msgModel = msgModel;
        this.context = context;
        this.rid = rid;
    }

    int receiver_type=1,sender_type=2;

    public ChatAdapter(ArrayList<Message> msgModel, Context context) {
        this.msgModel = msgModel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==sender_type){
            View view= LayoutInflater.from(context).inflate(R.layout.sam_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.sam_receiver,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
       if(msgModel.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
           return sender_type;
       else
           return receiver_type;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message model= msgModel.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String sender=FirebaseAuth.getInstance().getUid()+rid;
                                database.getReference().child("chats").child(sender)
                                        .child(model.getMsgId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).Smsg.setText(model.getMsg());
            Date date=new Date(model.getTime());
            SimpleDateFormat dateFormat=new SimpleDateFormat("h:mm a");
            String str= dateFormat.format(date);
            ((SenderViewHolder)holder).Stime.setText(str.toString());

        }
        else{
            ((RecieverViewHolder)holder).Rmsg.setText(model.getMsg());
            Date date=new Date(model.getTime());
            SimpleDateFormat dateFormat=new SimpleDateFormat("h:mm a");
            String str= dateFormat.format(date);
            ((RecieverViewHolder)holder).Rtime.setText(str.toString());
        }
    }

    @Override
    public int getItemCount() {
        return msgModel.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView Rmsg,Rtime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            Rmsg=itemView.findViewById(R.id.text);
            Rtime=itemView.findViewById(R.id.time);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView Smsg,Stime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            Smsg=itemView.findViewById(R.id.text1);
            Stime=itemView.findViewById(R.id.time1);
        }
    }
}
