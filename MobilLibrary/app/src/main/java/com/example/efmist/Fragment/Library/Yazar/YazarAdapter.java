package com.example.efmist.Fragment.Library.Yazar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.efmist.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class YazarAdapter extends FirebaseRecyclerAdapter<Yazar,YazarAdapter.myviewHolder> {


    public YazarAdapter(@NonNull FirebaseRecyclerOptions<Yazar> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull final Yazar model) {

        holder.nameText.setText(model.getYname());
        holder.hayatiText.setText(model.getHayati());
        holder.tarihText.setText(model.getTarih());
        Glide.with(holder.imgView.getContext()).load(model.getYimgLink()).into(holder.imgView);

        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity =(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new YazarDetailFragment(model.getHayati(),model.getYimgLink(),model.getYname(),model.getTarih()))
                        .addToBackStack(null).commit();


            }
        });

    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yazar_item,parent,false);
        return new myviewHolder(view);
    }

    public class myviewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;
        TextView nameText, hayatiText, tarihText;

        public myviewHolder(View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.imgView);
            nameText = itemView.findViewById(R.id.nameText);
            tarihText=itemView.findViewById(R.id.tarihText);
            hayatiText = itemView.findViewById(R.id.hayatiText);
        }

    }

}
