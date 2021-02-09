package com.example.efmist.Not;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efmist.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NoteActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NoteBook");
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
         //action bar yazısı
        setTitle("Not Defteri");

        FloatingActionButton btnAddNote = findViewById(R.id.addNotBtn);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, NewNoteActivity.class));
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("ntitle",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
        .setQuery(query,Note.class).build();

        adapter = new NoteAdapter(options);

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewNot);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
          //kaydırarak silme
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(NoteActivity.this,
                        "" + id + ".not açılıyor. ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(recyclerView.getContext(),EditNoteActivity.class);


                intent.putExtra("ntitle",note.getNTitle());
                intent.putExtra("description",note.getDescription());
                intent.putExtra("title",note.getTitle());
                intent.putExtra("author",note.getAuthor());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}