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

import com.example.efmist.Fragment.Library.Book.Book;
import com.example.efmist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterB extends RecyclerView.Adapter<AdapterB.MyHolder>{

    Context context;
    List<Book> bookList;

    public AdapterB(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_book_item, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String bookName = bookList.get(position).getName();
        String yazarN = bookList.get(position).getYazar();
        String bookImg = bookList.get(position).getImgLink();

        holder.bookname.setText(bookName);
        holder.yazarS.setText(yazarN);

        try {
            Picasso.get().load(bookImg).placeholder(R.drawable.ic_person)
                    .into(holder.imgBook);

        } catch (Exception e) {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+bookName,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

ImageView imgBook;
TextView yazarS,bookname;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
        imgBook=itemView.findViewById(R.id.imgBook);
        bookname =itemView.findViewById(R.id.bookname);
        yazarS=itemView.findViewById(R.id.yazarS);
        }
    }

}
