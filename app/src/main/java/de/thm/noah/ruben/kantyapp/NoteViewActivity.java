package de.thm.noah.ruben.kantyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;

public class NoteViewActivity extends AppCompatActivity {

    Intent newNoteIntent;
    AppData appData;
    private View.OnClickListener openNoteHandler = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // populate newest intend  with newest "AppData" and "Note" to edit
            newNoteIntent = new Intent(NoteViewActivity.this, NoteEditActivity.class);
            newNoteIntent.putExtra(ValueKey.APP_DATA, appData);
            newNoteIntent.putExtra(ValueKey.NOTE_ID, view.getTransitionName());
            startActivity(newNoteIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NoteViewActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

//    @Override
//    protected void onStart() {
//        System.out.println("NoteViewActivity.onStart");
//        super.onStart();
//        appData = (AppData) getIntent().getSerializableExtra(ValueKey.APP_DATA);
//        populateNoteView(appData.getNotes());
//
//    }

    @Override
    protected void onResume() {
        super.onResume();

        // get the newest AppData Object
        appData = (AppData) getIntent().getSerializableExtra(ValueKey.APP_DATA);

        // populate newest intend  with newest AppData
        newNoteIntent = new Intent(this, NoteEditActivity.class);
        newNoteIntent.putExtra(ValueKey.APP_DATA, appData);

        // Update onClickListener TODO dose this replace tho old ?
        FloatingActionButton addNoteButton = findViewById(R.id.addNote);
        addNoteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(newNoteIntent);
            }
        });

        populateNoteView(appData.getNotes());


    }

    /**
     * Diese Methode erzeugr die anklickbaren widgets in der übersicht.
     *
     * @param notes alle notizen der App
     */
    private void populateNoteView(List<Note> notes) {
        TableLayout view = findViewById(R.id.contendLayout);

        System.out.println("Size: " + notes.size());

        for (Note note : notes) {
            System.out.println(notes.indexOf(note));
            AppCompatTextView textView = new AppCompatTextView(this);
            textView.setText(note.getText());
            textView.setClickable(true);
            textView.setOnClickListener(this.openNoteHandler);
//            textView.addExtraDataToAccessibilityNodeInfo(new AccessibilityNodeInfo(), "test", new Bundle().);
            textView.setTransitionName(String.valueOf(note.getID()));
            view.addView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_note:
                System.out.println("NoteViewActivity.onOptionsItemSelected");
                startActivity(newNoteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveNotesToFile(appData);
        moveTaskToBack(true);
    }

    private void saveNotesToFile(AppData appData) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
//            FileOutputStream fOut = openFileOutput("notebook", Context.MODE_APPEND);
        File file = new File(this.getFilesDir(), "notebook.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            String notesToJson = gson.toJson(appData.getNotes());
            fileWriter.write(notesToJson);
            fileWriter.flush();
//            for (Note note : appData.getNotes()) {
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
