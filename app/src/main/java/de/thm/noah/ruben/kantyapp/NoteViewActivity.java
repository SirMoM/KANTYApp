package de.thm.noah.ruben.kantyapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;
import us.feras.mdv.MarkdownView;

public class NoteViewActivity extends AppCompatActivity {

    Intent newNoteIntent;
    AppData appData;

    /**
     * Which type is shown
     */
    private boolean toggleView = true;

    private boolean longPress = false;
    private View.OnClickListener openNoteOnClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // populate newest intend  with newest "AppData" and "Note" to edit
            newNoteIntent = new Intent(NoteViewActivity.this, NoteEditActivity.class);
            newNoteIntent.putExtra(ValueKey.APP_DATA, appData);
            newNoteIntent.putExtra(ValueKey.NOTE_ID, view.getTransitionName());
            startActivity(newNoteIntent);
        }
    };
    private View.OnLongClickListener deleteNoteOnLongClickHandler = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            longPress = true;
            NoteViewActivity.this.createAlert(view);
            return true;
        }
    };

    private void createAlert(View view) {
        final Integer ID = Integer.valueOf(view.getTransitionName());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoteViewActivity.this);
        alertDialogBuilder.setTitle(R.string.delete_tile);
        alertDialogBuilder.setMessage(R.string.delete_msg);
        alertDialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NoteViewActivity.this.appData.removeNote(ID)) {
                    NoteViewActivity.this.populateNoteView(NoteViewActivity.this.appData.getNotes());
                    longPress = false;
                }
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                longPress = false;
            }
        });
        alertDialogBuilder.create().show();
    }

    private final View.OnTouchListener openNoteOnTouchHandler = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                return false;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                return false;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && !longPress) {
                // populate newest intend  with newest "AppData" and "Note" to edit
                newNoteIntent = new Intent(NoteViewActivity.this, NoteEditActivity.class);
                newNoteIntent.putExtra(ValueKey.APP_DATA, appData);
                newNoteIntent.putExtra(ValueKey.NOTE_ID, view.getTransitionName());
                startActivity(newNoteIntent);
            }
            return false;
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
        addNoteButton.setOnClickListener(this.openNoteOnClickHandler);


//                new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(newNoteIntent);
//            }
//        });

        populateNoteView(appData.getNotes());
    }

    /**
     * Diese Methode erzeugr die anklickbaren widgets in der übersicht.
     *
     * @param notes alle notizen der App
     */
    @SuppressLint("ClickableViewAccessibility")
    private void populateNoteView(List<Note> notes) {
        LinearLayout view = findViewById(R.id.contendLayout);
//        view.removeAllViewsInLayout();
        view.removeAllViews();

        for (Note note : notes) {
            AppCompatTextView sep = new AppCompatTextView(NoteViewActivity.this);
            sep.setText("_______________________________");
            view.addView(sep);
            if (toggleView) {
                MarkdownView markdownView = new MarkdownView(this);
                markdownView.loadMarkdown(note.getText());
                markdownView.setOnLongClickListener(this.deleteNoteOnLongClickHandler);
                markdownView.setLongClickable(true);
                markdownView.setTransitionName(String.valueOf(note.getID()));
                markdownView.setOnTouchListener(this.openNoteOnTouchHandler);
                view.addView(markdownView);
            } else {
                AppCompatTextView textView = new AppCompatTextView(this);
                textView.setText(note.getText());
                textView.setClickable(true);
                textView.setLongClickable(true);
                textView.setOnClickListener(this.openNoteOnClickHandler);
                textView.setOnLongClickListener(this.deleteNoteOnLongClickHandler);
                textView.setTransitionName(String.valueOf(note.getID()));
                view.addView(textView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_note:
                startActivity(newNoteIntent);
                return true;
            case R.id.toggleView:
                toggleView = !toggleView;
                populateNoteView(appData.getNotes());
                return true;
            case R.id.action_delete_note:
                System.out.println("TODO !!!!!!!!!!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        appData.saveNotesToFile(this.getFilesDir());
        moveTaskToBack(true);
    }

//    private void saveNotesToFile(AppData appData) {
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//        File file = new File(this.getFilesDir(), "notebook.json");
//        try {
//            FileWriter fileWriter = new FileWriter(file);
//            String notesToJson = gson.toJson(appData.getNotes());
//            fileWriter.write(notesToJson);
//            fileWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
