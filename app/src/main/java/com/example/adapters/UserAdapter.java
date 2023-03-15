package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Models.Users;
import com.example.talko.ChatActivity;
import com.example.talko.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;

    public UserAdapter(ArrayList<Users> list,Context context){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
          Users users= list.get(position);
          Picasso.get().load(users.getProfile()).placeholder(R.drawable.avatar3).into(holder.image);
          holder.user.setText(users.getName());

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                .orderByChild("time")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                holder.lastmsg.setText(dataSnapshot.child("msg").getValue().toString());                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(context, ChatActivity.class);
                  intent.putExtra("Id",users.getUserId());
                  intent.putExtra("pic",users.getProfile());
                  intent.putExtra("name",users.getName());
                  context.startActivity(intent);
              }
          });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView user,lastmsg;

            public ViewHolder(@NonNull View item){
                super(item);
                image=item.findViewById(R.id.image);
                user=item.findViewById(R.id.tV);
                lastmsg=item.findViewById(R.id.tV2);
            }
    }
}
