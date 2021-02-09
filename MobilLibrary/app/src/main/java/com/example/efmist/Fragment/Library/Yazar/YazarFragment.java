package com.example.efmist.Fragment.Library.Yazar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class YazarFragment extends Fragment {


    RecyclerView recview;
    YazarAdapter yazarAdapter;

    public YazarFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_yazar, container, false);

       recview=(RecyclerView) v.findViewById(R.id.recview);
       recview.setLayoutManager(new GridLayoutManager(getContext(),3));

        FirebaseRecyclerOptions<Yazar> options =
                new FirebaseRecyclerOptions.Builder<Yazar>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("YazarList"), Yazar.class)
                        .build();

       yazarAdapter= new YazarAdapter(options);
       recview.setAdapter(yazarAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        yazarAdapter.startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
        yazarAdapter.stopListening();
    }


}