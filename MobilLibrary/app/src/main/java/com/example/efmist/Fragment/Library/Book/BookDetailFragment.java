package com.example.efmist.Fragment.Library.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.efmist.R;


public class BookDetailFragment extends Fragment {

    String aciklama;
    String imgLink;
    String name;
    String yazar;


    public BookDetailFragment(String aciklama, String imgLink, String name, String yazar) {
        this.aciklama = aciklama;
        this.imgLink = imgLink;
        this.name = name;
        this.yazar = yazar;

    }

    public BookDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);

        ImageView imgholder = v.findViewById(R.id.imgholder);
        TextView nameholder = v.findViewById(R.id.nameholder);
        TextView aciklamaholder = v.findViewById(R.id.aciklamaholder);
        TextView yazarholder = v.findViewById(R.id.yazarholder);


        nameholder.setText(name);
        aciklamaholder.setText(aciklama);
        yazarholder.setText(yazar);
        Glide.with(getContext()).load(imgLink).into(imgholder);


        return v;
    }

    public void onBackPressed() {

        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new BookFragment())
                .addToBackStack(null).commit();

    }
}