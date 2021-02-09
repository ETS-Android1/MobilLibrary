package com.example.efmist.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {

    Context context;
    List<User> users;
    boolean isFragment;

    FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> users, boolean isFragment) {
        this.context = context;
        this.users = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = users.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.uname.setText(user.getName());
        holder.email.setText(user.getEmail());

        try {
            Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_person)
                    .into(holder.image);
        } catch (Exception e) {
        }

        isFollowed(user.getUid(), holder.btnFollow);

        if (user.getUid().equals(firebaseUser.getUid())) {
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnFollow.getText().toString().equals(("follow"))) {

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child((firebaseUser.getUid())).child("following")
                            .child(user.getUid()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getUid()).child("followers").child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child((firebaseUser.getUid())).child("following")
                            .child(user.getUid()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getUid()).child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//kişinin üstüne tıkladığında mesaja yönlendirir

            }
        });

    }

    private void isFollowed(final String id, final Button btnFollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists())
                    btnFollow.setText("following");
                else
                    btnFollow.setText("follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView uname, email;
        Button btnFollow;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgProfile);
            uname = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            btnFollow = itemView.findViewById(R.id.btnfollow);
        }
    }

}
