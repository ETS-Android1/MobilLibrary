package com.example.efmist.Activity.Mesaj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.efmist.Activity.User;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    CircleImageView userImg;
    TextView userName;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userImg= findViewById(R.id.imgMsgProfile);
        userName = findViewById(R.id.usernameMsg);

        intent = getIntent();
        String userid = intent.getStringExtra("uid");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //  userName.setText(user.getName());
                if (user.getImage().equals("")){
                    //   userImg.setImageResource(R.drawable.ic_person);
                }else {
                    Glide.with(MessageActivity.this).load(user.getImage()).into(userImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }


}