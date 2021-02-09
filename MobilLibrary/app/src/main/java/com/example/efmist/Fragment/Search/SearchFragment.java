package com.example.efmist.Fragment.Search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.Activity.User;
import com.example.efmist.Activity.UserAdapter;
import com.example.efmist.Fragment.Library.Book.Book;
import com.example.efmist.Fragment.Library.Yazar.Yazar;
import com.example.efmist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    RecyclerView recyclerViewU;
    UserAdapter userAdapter;
    List<User> userList;

    RecyclerView recyclerViewA;
    AdapterA adapterA;
    List<Yazar> yazarList;

    private RecyclerView recyclerViewB;
    private AdapterB adapterB;
    private List<Book> bookList;
    private SocialAutoCompleteTextView searchbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        //init recyclerview
        recyclerViewU = v.findViewById(R.id.recyclerViewUsers);
        recyclerViewA = v.findViewById(R.id.recyclerViewAuthors);
        recyclerViewB = v.findViewById(R.id.recyclerViewBooks);

        searchbar = v.findViewById(R.id.searchbar);
        //set properties
        recyclerViewU.setHasFixedSize(true);
        recyclerViewU.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewA.setHasFixedSize(true);
        recyclerViewA.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewB.setHasFixedSize(true);
        recyclerViewB.setLayoutManager(new LinearLayoutManager(getContext()));

        //init List
        userList = new ArrayList<User>();
        bookList = new ArrayList<Book>();
        yazarList = new ArrayList<Yazar>();


        //get all users
        getAllUsers();
        getAllBooks();
        getAllAuthors();
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUsers(s.toString());
                searchBooks(s.toString());
                searchAuthors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return v;
    }


    private void getAllBooks() {

        FirebaseDatabase.getInstance().getReference().child("BookList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    bookList.add(book);
                    adapterB = new AdapterB(getContext(), bookList);
                    recyclerViewB.setAdapter(adapterB);

                }
                adapterB.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllAuthors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("YazarList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yazarList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Yazar yazar = ds.getValue(Yazar.class);
                    yazarList.add(yazar);
                    adapterA = new AdapterA(getContext(), yazarList);
                    recyclerViewA.setAdapter(adapterA);
                }
                adapterA.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getAllUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                if (TextUtils.isEmpty(searchbar.getText().toString())) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (!user.getUid().equals(firebaseUser.getUid())) {
                            userList.add(user);
                        }
                        userAdapter = new UserAdapter(getContext(), userList, true);
                        recyclerViewU.setAdapter(userAdapter);

                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchBooks(final String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BookList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);

                    if (book.getName().toLowerCase().contains(query.toLowerCase())) {
                        bookList.add(book);
                    }
                }
                adapterB = new AdapterB(getContext(), bookList);
                recyclerViewB.setAdapter(adapterB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchAuthors(final String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("YazarList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yazarList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Yazar yazar = ds.getValue(Yazar.class);

                    if (yazar.getYname().toLowerCase().contains(query.toLowerCase())) {
                        yazarList.add(yazar);
                    }
                }
                adapterA = new AdapterA(getContext(), yazarList);
                recyclerViewA.setAdapter(adapterA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(final String query) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (!user.getUid().equals(firebaseUser.getUid())) {

                        if (user.getName().toLowerCase().contains(query.toLowerCase())
                                || user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                            userList.add(user);
                        }
                        userAdapter = new UserAdapter(getContext(), userList, true);
                        recyclerViewU.setAdapter(userAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
