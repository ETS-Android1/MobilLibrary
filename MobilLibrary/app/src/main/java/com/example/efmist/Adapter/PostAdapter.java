package com.example.efmist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.efmist.Activity.PostDetailActivity;
import com.example.efmist.Activity.User;
import com.example.efmist.Fragment.Post;
import com.example.efmist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Myholder> {

    Context context;
    List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder holder, int position) {



        final Post post = postList.get(position);
        String title = postList.get(position).getpTitle();
        String desc = postList.get(position).getpDescription();
        String postImg = postList.get(position).getpImg();

        holder.postTitle.setText(title);
        holder.postDesc.setText(desc);

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);


                        if (user.getImage().equals("")) {
                            holder.postProfile.setImageResource(R.mipmap.ic_launcher);
                        } else {

                            Picasso.get().load(user.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.postProfile);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Glide.with(context).load(postImg).into(holder.postImg);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        ImageView postImg;
        TextView postTitle;
        TextView postDesc;
        ImageView postProfile;
        ImageView popPostProfile;
        ImageView currentUserimg;
        TextView postDateTime;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            postImg = itemView.findViewById(R.id.row_post_img);
            postTitle = itemView.findViewById(R.id.row_post_title);
            postProfile = itemView.findViewById(R.id.row_post_profile_img);
            popPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            postDesc = itemView.findViewById(R.id.row_post_desc);
            currentUserimg = itemView.findViewById(R.id.post_detail_currentuser_img);
            postDateTime = itemView.findViewById(R.id.post_detail_date_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(context, PostDetailActivity.class);

                    int position = getAbsoluteAdapterPosition();
                    final Post post = postList.get(position);
                    postDetailActivity.putExtra("pTitle", postList.get(position).getpTitle());
                    postDetailActivity.putExtra("pDescription", postList.get(position).getpDescription());
                    postDetailActivity.putExtra("pImg", postList.get(position).getpImg());
                    postDetailActivity.putExtra("pId", postList.get(position).getpId());
                    postDetailActivity.putExtra("uid", postList.get(position).getUid());
                    postDetailActivity.putExtra("pId", postList.get(position).getpId());
                    postDetailActivity.putExtra("pTime",postList.get(position).getpTime());


                    // postDetailActivity.putExtra("pUsername",postList.get(position).getUsername());
                    context.startActivity(postDetailActivity);


                }
            });

        }
    }

}
