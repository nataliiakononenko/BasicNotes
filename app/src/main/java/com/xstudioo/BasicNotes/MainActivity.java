package com.xstudioo.BasicNotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    TextView noItemText;
    NotesDatabase notesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        noItemText = findViewById(R.id.noItemText);
        notesDatabase = new NotesDatabase(this);
        List<Note> allNotes = notesDatabase.getAllNotes();
        recyclerView = findViewById(R.id.allNotesList);

        FloatingActionButton addFAB = (FloatingActionButton) findViewById(R.id.addNoteButton);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNote.class);
                startActivity(i);
            }
        });

        if(allNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }else {
            noItemText.setVisibility(View.GONE);
            displayList(allNotes);
        }
    }

    private void displayList(List<Note> allNotes) {
        int position = StateHandler.getInstance().getPosition();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.scrollToPosition(position);
        adapter = new Adapter(this, allNotes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settingsButton){
            View menuItemView = findViewById(R.id.toolbar);
            PopupMenu popup = new PopupMenu(this, menuItemView, Gravity.RIGHT);
            popup.inflate(R.menu.popup_menu);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.contact) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"basicorganizer.post@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Basic Notes app");
                        i.putExtra(Intent.EXTRA_TEXT   , "");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "There are no email client available... Please contact basicorganizer.post@gmail.com", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
            });
            popup.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        List<Note> allNotes = notesDatabase.getAllNotes();
        if (!allNotes.isEmpty()) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            StateHandler.getInstance().savePosition(layoutManager.findFirstVisibleItemPosition());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Note> allNotes = notesDatabase.getAllNotes();
        if(allNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }else {
            noItemText.setVisibility(View.GONE);
            displayList(allNotes);
        }
    }
}
