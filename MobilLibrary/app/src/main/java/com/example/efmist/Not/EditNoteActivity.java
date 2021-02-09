package com.example.efmist.Not;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efmist.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTextNotTitle;
    private EditText editTextDescription;
    private EditText editTextTitle;
    private EditText editTextAuthor;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NoteBook");
    private NoteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //action bar yazısı
        setTitle("Not Defteri");

        editTextNotTitle = findViewById(R.id.EditTextNoteTitle);
        editTextDescription = findViewById(R.id.EditTextDescription);
        editTextTitle = findViewById(R.id.EditTextBookTitle);
        editTextAuthor = findViewById(R.id.EditTextAuthor);


        editTextNotTitle.setText(getIntent().getStringExtra("ntitle")+"");
        editTextDescription.setText(getIntent().getStringExtra("description")+"");
        editTextTitle.setText(getIntent().getStringExtra("title")+"");
        editTextAuthor.setText(getIntent().getStringExtra("author")+"");



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {

        String ntitle = editTextNotTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();

        if (ntitle.trim().isEmpty()|| description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("NoteBook");


      //  notebookRef.add(new Note(ntitle,description,title,author,page,priority));
        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}