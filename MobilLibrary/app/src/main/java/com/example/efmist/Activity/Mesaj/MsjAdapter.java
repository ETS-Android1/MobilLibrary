package com.example.efmist.Activity.Mesaj;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.Activity.User;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MsjAdapter extends RecyclerView.Adapter<MsjAdapter.MyHolder> {

    Context context;
    List<User> users;
    boolean isFragment;

    FirebaseUser firebaseUser;

    public MsjAdapter(Context context, List<User> users, boolean isFragment) {
        this.context = context;
        this.users = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mesaj_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = users.get(position);

        holder.uname.setText(user.getName());


        try {
            Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_person)
                    .into(holder.image);
        } catch (Exception e) {
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//kişinin üstüne tıkladığında mesaja yönlendirir
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("uid", user.getUid());
                context.startActivity(intent);
                Toast.makeText(context,""+user.getName(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView uname, status;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgMsgProfile);
            uname = itemView.findViewById(R.id.usernameMsg);


        }
    }
}
