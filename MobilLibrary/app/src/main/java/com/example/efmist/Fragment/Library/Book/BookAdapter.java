package com.example.efmist.Fragment.Library.Book;

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

public class BookAdapter extends FirebaseRecyclerAdapter<Book, BookAdapter.myViewHolder> {


    public BookAdapter(@NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull final Book model) {
        //text
        holder.name.setText(model.getName());
        holder.yazar.setText(model.getYazar());
        holder.aciklama.setText(model.getAciklama());
        //image glide
        Glide.with(holder.imgLink.getContext()).load(model.getImgLink()).into(holder.imgLink);

        holder.imgLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new BookDetailFragment(model.getAciklama(), model.getImgLink(),
                                model.getName(), model.getYazar()))
                        .addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        //book item id
        ImageView imgLink;
        TextView name, yazar, aciklama;


        public myViewHolder(View itemView) {
            super(itemView);

            imgLink = itemView.findViewById(R.id.imgLink);
            name = itemView.findViewById(R.id.name);
            yazar = itemView.findViewById(R.id.yazar);
            aciklama = itemView.findViewById(R.id.aciklama);


        }
    }

}
