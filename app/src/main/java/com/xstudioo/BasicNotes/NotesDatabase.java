package com.xstudioo.BasicNotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "SimpleDB";
    private static final String TABLE_NAME = "NotesTable";

    public NotesDatabase(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_UPDATED = "updated";
    private static final String KEY_CREATED = "created";
    private static final String KEY_FLAG = "flag";
    private static final String KEY_STATUS = "status";


    @Override
    public void onCreate(SQLiteDatabase db) {
         String createDb = "CREATE TABLE "+TABLE_NAME+" ("+
                 KEY_ID+" INTEGER PRIMARY KEY,"+
                 KEY_TITLE+" TEXT,"+
                 KEY_CONTENT+" TEXT,"+
                 KEY_UPDATED+" TEXT,"+
                 KEY_CREATED+" TEXT,"+
                 KEY_FLAG+" TEXT,"+
                 KEY_STATUS+" TEXT"
                 +" )";
         db.execSQL(createDb);
    }

    // upgrade db if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE,note.getTitle());
        v.put(KEY_CONTENT,note.getContent());
        v.put(KEY_UPDATED,note.getLastUpdated());
        v.put(KEY_CREATED,note.getCreated());
        v.put(KEY_FLAG,note.getFlag());
        v.put(KEY_STATUS,note.getStatus());

        // inserting data into db
        long ID = db.insert(TABLE_NAME,null,v);
        return  ID;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_UPDATED,KEY_CREATED,KEY_FLAG,KEY_STATUS};
       Cursor cursor=  db.query(TABLE_NAME,query,KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        return new Note(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Boolean.parseBoolean(cursor.getString(5)),
                Boolean.parseBoolean(cursor.getString(6)));
    }

    public List<Note> getAllNotes(){
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME+" ORDER BY "+KEY_ID+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setLastUpdated(cursor.getString(3));
                note.setCreated(cursor.getString(4));
                note.setFlag(Boolean.parseBoolean(cursor.getString(5)));
                note.setStatus(Boolean.parseBoolean(cursor.getString(6)));
                allNotes.add(note);
            }while (cursor.moveToNext());
        }

        return allNotes;

    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getTitle() + "\n ID -> "+note.getId());
        c.put(KEY_TITLE,note.getTitle());
        c.put(KEY_CONTENT,note.getContent());
        c.put(KEY_UPDATED,note.getLastUpdated());
        c.put(KEY_CREATED,note.getCreated());
        c.put(KEY_FLAG,note.getFlag());
        c.put(KEY_STATUS,note.getStatus());
        return db.update(TABLE_NAME,c,KEY_ID+"=?",new String[]{String.valueOf(note.getId())});
    }

    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
}
