package com.example.talko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.Models.Message;
import com.example.adapters.ChatAdapter;
import com.example.talko.databinding.ActivityChatBinding;
import com.example.talko.databinding.ActivityGroupchatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupchatActivity extends AppCompatActivity {
    ActivityGroupchatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupchatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final ArrayList<Message> msgModel=new ArrayList<>();

        final String sender=FirebaseAuth.getInstance().getUid();
        binding.textView.setText("Group Chat");

        final ChatAdapter adapter=new ChatAdapter(msgModel,this);
        binding.chatRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecycler.setLayoutManager(layoutManager);

        database.getReference().child("Group chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        msgModel.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Message models=snapshot1.getValue(Message.class);
                            msgModel.add(models);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


       binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg=binding.enter.getText().toString();
                final Message model=new Message(sender,msg);
                model.setTime(new Date().getTime());
                binding.enter.setText("");
                database.getReference().child("Group chats")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GroupchatActivity.this,"Message send",Toast.LENGTH_SHORT).show();
                        }
                });
            }
        });

    }
}