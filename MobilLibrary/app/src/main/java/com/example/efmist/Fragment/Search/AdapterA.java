package com.example.efmist.Fragment.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.Fragment.Library.Yazar.Yazar;
import com.example.efmist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterA extends RecyclerView.Adapter<AdapterA.MyHolder> {

    Context context;
    List<Yazar> yazars;

    public AdapterA(Context context, List<Yazar> yazars) {
        this.context = context;
        this.yazars = yazars;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.author_item, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String yazarImage = yazars.get(position).getYimgLink();
        final String yazarName = yazars.get(position).getYname();

        holder.yname.setText(yazarName);

        try {
            Picasso.get().load(yazarImage).placeholder(R.drawable.ic_person)
                    .into(holder.yimgLink);

        } catch (Exception e) {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+yazarName,Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return yazars.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {


        ImageView yimgLink;
        TextView yname;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            yimgLink = itemView.findViewById(R.id.imgProfile);
            yname = itemView.findViewById(R.id.username);

        }
    }
}
