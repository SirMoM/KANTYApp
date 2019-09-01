package de.thm.noah.ruben.kantyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;
import de.thm.noah.ruben.kantyapp.views.MarkdownWebView;

/**
 * @author Noah Ruben
 * <p>
 * Diese Activity conntrolliert die notelist_view.xmlew.
 * Diese stellt alle Notizen dar.
 */
public class NoteViewActivity extends AppCompatActivity {

    private Intent newNoteIntent;
    private AppData appData;

    /**
     * Which View is shown:
     * <p>
     * true  = MarkdownView
     * <p>
     * false = TextView
     */
    private boolean toggleView = true;


    /**
     * True, wenn {@link NoteViewActivity#deleteNoteOnLongClickHandler} ausgelöst wurde, um zu verhindern, dass {@link NoteViewActivity#openNoteOnTouchHandler} ebenfalls ausgelöst wird.
     */
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
            NoteViewActivity.this.createDeleteAlert(view);
            return true;
        }
    };
    private int idx;
    private boolean notFullList =  false;

    /**
     * Erstellt den Lösch-Alarm
     */
    private void createDeleteAlert(View view) {
        final Integer ID = Integer.valueOf(view.getTransitionName());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoteViewActivity.this);
        alertDialogBuilder.setTitle(R.string.delete_tile);
        alertDialogBuilder.setMessage(R.string.delete_msg);
        alertDialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NoteViewActivity.this.appData.removeNote(ID)) {
                    view.setVisibility(View.GONE); // TODO this is fucking disaster bodge
//                  NoteViewActivity.this.populateNoteView(NoteViewActivity.this.appData.getNotes());
                    longPress = false;
                    appData.saveNotesToFile(NoteViewActivity.this.getFilesDir());
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
        setContentView(R.layout.notelist_view);
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

        populateNoteView(appData.getNotes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main, menu);

//        findViewById(R.id.placeholder)

        idx=0;
        SubMenu subm = menu.getItem(1).getSubMenu(); // get my MenuItem with placeholder submenu
        subm.clear(); // delete place holder
        Set<String> uniqueTags = appData.getUniqueTags();
        for (String tag : uniqueTags) {
            subm.add(0,idx++,0, tag);
        }
        return true;
    }

    /**
     * Diese Methode erzeugt die anklickbaren Views in der Übersicht.
     *
     * @param notes alle notizen der App
     */
    private void populateNoteView(List<Note> notes) {
        LinearLayout view = findViewById(R.id.contendLayout);
//        view.removeAllViewsInLayout();
        view.removeAllViews();

        for (Note note : notes) {
//            AppCompatTextView sep = new AppCompatTextView(NoteViewActivity.this);
//            sep.setText(R.string.separator);
//            view.addView(sep);
            if (toggleView) {
//                System.out.println("MarkdownView");
//                MarkdownView markdownView = new MarkdownView(this);
//                markdownView.loadMarkdown(note.getText() + "\n___\n");
//                markdownView.setOnLongClickListener(this.deleteNoteOnLongClickHandler);
//                markdownView.setLongClickable(true);
//                markdownView.setTransitionName(String.valueOf(note.getID()));
//                markdownView.setOnTouchListener(this.openNoteOnTouchHandler);
//                view.addView(markdownView);
                AppCompatTextView textView = new AppCompatTextView(this);
                textView.setText(note.getText() + "\n\n ==========================================" );
                textView.setClickable(true);
                textView.setLongClickable(true);
                textView.setOnClickListener(this.openNoteOnClickHandler);
                textView.setOnLongClickListener(this.deleteNoteOnLongClickHandler);
                textView.setTransitionName(String.valueOf(note.getID()));
                view.addView(textView);
            } else {
                System.out.println("MarkdownWebView");
                MarkdownWebView markdownView = new MarkdownWebView(this, note.getText().toString());
                markdownView.setOnLongClickListener(this.deleteNoteOnLongClickHandler);
                markdownView.setLongClickable(true);
                markdownView.setTransitionName(String.valueOf(note.getID()));
                markdownView.setOnTouchListener(this.openNoteOnTouchHandler);
                view.addView(markdownView);



            }
        }
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
            default:
                if (0 < item.getItemId() && item.getItemId() < idx){
                    showSelectedTag(item.getTitle().toString());
                }
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSelectedTag(String tag) {
        List<Note> notes = appData.getNotes();
        ArrayList<Note> notesWithTag = new ArrayList<Note>();
        for (Note note: notes) {
            if (note.getTags().contains(tag)){
                System.out.println(note);
                notesWithTag.add(note);
            }
        }
        populateNoteView(notesWithTag);
        notFullList = true;
    }

    @Override
    public void onBackPressed() {
        if(notFullList){
            populateNoteView(appData.getNotes());
        }else {
            appData.saveNotesToFile(this.getFilesDir());
            moveTaskToBack(true);
        }
//        super.onBackPressed();
    }
}
