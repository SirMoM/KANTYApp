package de.thm.noah.ruben.kantyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;


public class NoteEditActivity extends AppCompatActivity {


    private AppData appData;
    private boolean edit = false;
    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NoteEditActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_view);
        TextView note_edit_view = (TextView) findViewById(R.id.note);
        appData = (AppData) getIntent().getSerializableExtra(ValueKey.APP_DATA);
        Integer noteID = null;
        try{
            noteID = Integer.valueOf((String) getIntent().getSerializableExtra(ValueKey.NOTE_ID));
        }catch (NumberFormatException numberFormatException){
            System.err.println(numberFormatException);
        }

        if (noteID == null){
            note_edit_view.setText("# Keep a Note to Yourself \n Noah Ruben");
        } else {
            note = appData.getNoteByID(noteID);
            note_edit_view.setText(note.getText());
            edit = true;
        }
        note_edit_view.setTextIsSelectable(true);
        note_edit_view.setAutoLinkMask(Linkify.ALL); // List of possible mask value https://developer.android.com/reference/android/text/util/Linkify.html#ALL
// TODO MARKDOWN ? maby swich views?
    }

    @Override
    public void onBackPressed() {
        TextView note_text_view = (TextView) findViewById(R.id.note);
        if (edit) {
            note.setText(note_text_view.getText().toString());
        }else {
            appData.getNotes().add(new Note(appData.generateNewID(), new Date(), note_text_view.getText().toString()));
        }

        Intent newNoteIntent = new Intent(this, NoteViewActivity.class);
        newNoteIntent.putExtra(ValueKey.APP_DATA, appData);
        startActivity(newNoteIntent);
        finish();
    }
}

