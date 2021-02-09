package com.example.efmist.Fragment.Library.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class BookFragment extends Fragment {


    RecyclerView recyclerView;
    BookAdapter libraryAdapter;


    public BookFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book, container, false);


        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("BookList"), Book.class)
                        .build();

        libraryAdapter= new BookAdapter(options);
        recyclerView.setAdapter(libraryAdapter);


        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        libraryAdapter.startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
        libraryAdapter.stopListening();
    }

}
