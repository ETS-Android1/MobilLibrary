package com.example.efmist.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {


    User user;
    List<User> userList;

    ImageView imgpost, imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtDateName, txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;

    DatabaseReference firebaseDatabase;
    FirebaseDatabase database;

    UserAdapter userAdapter;

    User myUser;
    FirebaseUser firebaseUser;
    String uid,uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId =getIntent().getStringExtra("uid");



        uid = firebaseUser.getUid();


        imgpost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtDateName = findViewById(R.id.post_detail_date_name);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostTitle = findViewById(R.id.post_detail_title);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);

        String postImg = getIntent().getStringExtra("pImg");
        Glide.with(this).load(postImg).into(imgpost);

        String title = getIntent().getExtras().getString("pTitle");
        txtPostTitle.setText(title);

        String description = getIntent().getExtras().getString("pDescription");
        txtPostDesc.setText(description);
//Post sahibinin resmi
//       firebaseDatabase.child("Users").child(uId).child("image").setValue(imgUserPost);
        //Suan giren kullanıcının resmi
//       firebaseDatabase.child("Users").child(uid).child("image").setValue(imgCurrentUser);




        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getImage()).into(imgUserPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}