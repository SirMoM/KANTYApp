package de.thm.noah.ruben.kantyapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;
import de.thm.noah.ruben.kantyapp.notificationes.NotificationHandler;


public class NoteEditActivity extends AppCompatActivity {


    private LocalDateTime notificationDateTimeCache = LocalDateTime.now();

    private AppData appData;

    /**
     * Die Variable gibt an ob eine neue Notiz bearbeitet wird oder eine bestehende.
     */
    private boolean editExistingNote = false;

    /**
     * Die Variable gibt an ob die aktuelle Notiz gelöscht werden soll.
     */
    private boolean deleteNote = false;

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
        try {
            noteID = Integer.valueOf((String) getIntent().getSerializableExtra(ValueKey.NOTE_ID));
        } catch (NumberFormatException numberFormatException) {
            System.err.println(numberFormatException);
        }

        if (noteID == null) {
            note_edit_view.setText("# Keep a Note to Yourself \n Noah Ruben");
            editExistingNote = false;
        } else {
            note = appData.getNoteByID(noteID);
            note_edit_view.setText(note.getText());
            editExistingNote = true;
        }
        note_edit_view.setTextIsSelectable(true);
        note_edit_view.setAutoLinkMask(Linkify.ALL); // List of possible mask value https://developer.android.com/reference/android/text/util/Linkify.html#ALL
    }

    @Override
    public void onBackPressed() {
        TextView note_text_view = findViewById(R.id.note);

        if (editExistingNote && !deleteNote) {
            note.setText(note_text_view.getText().toString());
        } else if (editExistingNote && deleteNote) {
            if (appData.removeNote(note.getID())){
                Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        } else if (!editExistingNote && !deleteNote) {
            appData.getNotes().add(new Note(appData.generateNewID(), new Date(), note_text_view.getText().toString()));
        } else if (!editExistingNote && deleteNote) {
            // nothing happens because than the new note isn't saved
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
            case R.id.delete_menu_item:
                deleteNote = true;
                onBackPressed();
                return true;
            case R.id.save_menu_item:
                appData.saveNotesToFile(this.getFilesDir());
                Toast.makeText(this, "Note(s) saved!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_reminder_menu_item:
//                TODO add Reminder
//                      * as Push-benachichtigung
//                      * als Kalender eintrag
                showDatePickerDialog();

                return true;
            case R.id.add_tag_menu_item:
//                TODO add Alert to fill in TAG (Auto complete?)
                return true;
//            case R.id.add_md_menu_item: TODO real sub points actions
//                return true;
            case R.id.show_md_menu_item:
//                TODO add WebView and Markdown-Parser make this the default thing?
                return true;
            case R.id.share_menu_item:
//                TODO add share dialog
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTimePickerDialog() {
        LocalDateTime now = LocalDateTime.now();
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.timepicker_header, null);


        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        System.out.println(hour + " " + min);
                        notificationDateTimeCache = notificationDateTimeCache.withHour(hour);
                        notificationDateTimeCache = notificationDateTimeCache.withMinute(min);

                        Notification notification = NotificationHandler.getNotification(NoteEditActivity.this, note.getText());
                        NotificationHandler.scheduleNotification(NoteEditActivity.this, notification,notificationDateTimeCache);
                    }

                }, now.getHour(), now.getMinute(), true);
        builder.setOnCancelListener(x->{
            //TODO TOAST????
        });
        builder.setCustomTitle(view);

        builder.show();

    }
    private void showDatePickerDialog() {
        LocalDate today = LocalDate.now();

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                System.out.println(i + " " + i1 + " " + i2);
                notificationDateTimeCache = notificationDateTimeCache.withYear(i);
                notificationDateTimeCache = notificationDateTimeCache.withMonth(i1);
                notificationDateTimeCache = notificationDateTimeCache.withDayOfMonth(i2);
                showTimePickerDialog();
            }
        };

            LayoutInflater inflater = getLayoutInflater();
            View header = inflater.inflate(R.layout.timepicker_header, null);
            DatePickerDialog builder = new DatePickerDialog(this, R.style.DialogTheme, onDateSetListener , today.getYear(), today.getMonthValue(), today.getDayOfMonth());
            builder.setCustomTitle(header);
            builder.show();
        }

}

