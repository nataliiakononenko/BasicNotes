package com.xstudioo.BasicNotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Note> notes;
    private ArrayList<Long> selectedNotesIDs = new ArrayList<Long>();
    private boolean multiSelect = false;
    private NotesDatabase sDB;

    Adapter(Context context, List<Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.sDB = new NotesDatabase(context);
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (Long noteID : selectedNotesIDs) {
                sDB.deleteNote(noteID);
                notes.remove(noteID);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedNotesIDs.clear();
            notes = sDB.getAllNotes();
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String  title    = notes.get(i).getTitle();
        String  lastUpdated     = notes.get(i).getLastUpdated();

        viewHolder.nTitle.setText(title);
        viewHolder.nFrame.setBackgroundColor(Color.parseColor("#F7FCFF"));
        String date = lastUpdated.split("_")[0];
        viewHolder.nDate.setText(date);
        viewHolder.nID.setText(String.valueOf(notes.get(i).getId()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle, nDate, nTime, nID;
        ConstraintLayout nFrame;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nTitle  = itemView.findViewById(R.id.nTitle);
            nDate   = itemView.findViewById(R.id.nDate);
            nID     = itemView.findViewById(R.id.listId);
            nFrame  = itemView.findViewById(R.id.nFrame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(v);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!multiSelect) {
                        ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
                    }
                    selectItem(v);
                    return true;
                }
            });
        }

        void selectItem(View v) {
            long id = notes.get(getAdapterPosition()).getId();
            if (multiSelect) {
                if (selectedNotesIDs.contains(id)) {
                    selectedNotesIDs.remove(id);
                    nFrame.setBackgroundColor(Color.parseColor("#F7FCFF"));
                } else {
                    selectedNotesIDs.add(id);
                    nFrame.setBackgroundColor(Color.parseColor("#b3e3ff"));
                }
            }
            else {
                Intent i = new Intent(v.getContext(), EditNote.class);
                i.putExtra("ID", id);
                v.getContext().startActivity(i);
            }
        }
    }
}
