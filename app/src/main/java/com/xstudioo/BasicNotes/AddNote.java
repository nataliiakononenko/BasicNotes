package com.xstudioo.BasicNotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar c;
    String timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        noteDetails = findViewById(R.id.noteDetails);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetails.requestFocus();

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // set current date and time
        c = Calendar.getInstance();
        timestamp = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        Log.d("TIME", "Timestamp: "+timestamp);
    }

    private String pad(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save){
            if(noteTitle.getText().length() != 0){
                addNote(noteTitle.getText().toString());
            } else if (noteTitle.getText().length() == 0 & noteDetails.getText().length() != 0) {
                String[] text = noteDetails.getText().toString().split("\\R", 2);
                String title = text[0];
                if (title.length() > 35) {
                    title = text[0].substring(0, 35) + "...";
                }
                addNote(title);
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote(String title) {
        if (!noteDetails.getText().toString().endsWith("\n")) {
            noteDetails.append("\n");
        }
        Note note = new Note(title, noteDetails.getText().toString(), timestamp, timestamp, false, false);
        NotesDatabase sDB = new NotesDatabase(this);
        long id = sDB.addNote(note);
        Note check = sDB.getNote(id);
        Log.d("inserted", "Note: "+ id + " -> Title:" + check.getTitle()+" Date: "+ check.getLastUpdated());
        StateHandler.getInstance().savePosition(0);
        onBackPressed();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
