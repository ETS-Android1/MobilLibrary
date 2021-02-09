package com.example.efmist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.efmist.Activity.Mesaj.ChatsActivity;
import com.example.efmist.Activity.Mesaj.MsjAdapter;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowingActivity extends AppCompatActivity {
    private String id;
    String title;
    private List<String> idList;

    private RecyclerView recyclerView;
    // private UserAdapter userAdapter;
    private List<User> users;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);


        setTitle("Takip ettiklerim");


        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent= getIntent();
        title=intent.getStringExtra("title");


        recyclerView=findViewById(R.id.rvFollowing);
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
                userAdapter= new UserAdapter(FollowingActivity.this,users,false);
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
                userAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}