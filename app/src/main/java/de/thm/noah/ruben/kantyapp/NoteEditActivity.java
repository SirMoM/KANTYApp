package de.thm.noah.ruben.kantyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

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
        Toolbar editHelperBar = findViewById(R.id.editHelperBar);
        setSupportActionBar(editHelperBar);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toggleView:
                System.out.println("piazsgdf");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

