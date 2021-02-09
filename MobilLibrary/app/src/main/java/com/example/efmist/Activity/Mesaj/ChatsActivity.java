package com.example.efmist.Activity.Mesaj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.Activity.User;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private String id;
    String title;
    private List<String> idList;

    private RecyclerView recyclerView;
    // private UserAdapter userAdapter;
    private List<User> users;
    MsjAdapter msjAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent= getIntent();
        // id = intent.getStringExtra("id");
        title=intent.getStringExtra("title");

        setTitle("Mesajlar");

        recyclerView=findViewById(R.id.recyFollower);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users= new ArrayList<User>();


        idList = new ArrayList<String>();

        getFollowing();

    }

    private void  getFollowing(){
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add((snapshot.getKey()));
                }
                msjAdapter= new MsjAdapter(ChatsActivity.this,users,false);

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void showUsers() {
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id : idList) {
                        if (user.getUid().equals(id)) {
                            users.add(user);
                        }
                    }
                }
                Log.d("list f", users.toString());
                msjAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(msjAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

