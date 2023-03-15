package com.example.talko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Models.Message;
import com.example.adapters.ChatAdapter;
import com.example.talko.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        getSupportActionBar().hide();

       final String senderId= mAuth.getUid();
        String recieveId= getIntent().getStringExtra("Id");
        String userName= getIntent().getStringExtra("name");
        String profile= getIntent().getStringExtra("pic");
        binding.textView.setText(userName);
        Picasso.get().load(profile).placeholder(R.drawable.avatar).into(binding.profile);

        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

       final ArrayList<Message> msgModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(msgModels,this,recieveId);

        binding.chatRecycler.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecycler.setLayoutManager(layoutManager);

        final String sender=senderId+recieveId;
        final String receiver=recieveId+senderId;

        database.getReference().child("chats")
                .child(sender)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        msgModels.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Message models=snapshot1.getValue(Message.class);
                            models.setMsgId(snapshot1.getKey());
                            msgModels.add(models);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=binding.enter.getText().toString();
                final Message model=new Message(senderId,msg);
                model.setTime(new Date().getTime());
                binding.enter.setText("");

                database.getReference().child("chats")
                        .child(sender)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiver)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });

    }
}