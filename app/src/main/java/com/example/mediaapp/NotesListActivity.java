package com.example.mediaapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        recyclerView = findViewById(R.id.rvNotes);
        dbHelper = new DBHelper(this);

        List<NoteModel> notesList = dbHelper.getAllNotes();
        adapter = new NotesAdapter(this, notesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
