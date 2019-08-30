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
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;
import de.thm.noah.ruben.kantyapp.notifications.NotificationHandler;


/**
 * @author Noah Ruben
 * <p>
 * Diese Activity conntrolliert die note_edit_view.xml View.
 * Diese stellt alle Notizen dar.
 */
public class NoteEditActivity extends AppCompatActivity {

//  Dies stellt die DateTime dar, die der Benutzer eingibt.
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


    /**
     * Die Notiz, die gerade bearbeitet wird.
     */
    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NoteEditActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_view);
        Toolbar editHelperBar = findViewById(R.id.editHelperBar);
        setSupportActionBar(editHelperBar);


        TextView note_edit_view = findViewById(R.id.note);

//      AppData aus Intend laden
        appData = (AppData) getIntent().getSerializableExtra(ValueKey.APP_DATA);

//      Wenn das Intend eine Notiz, als Extra enthält, wird die Notiz geladen
        Integer noteID = null;
        try {
            noteID = Integer.valueOf((String) getIntent().getSerializableExtra(ValueKey.NOTE_ID));
        } catch (NumberFormatException numberFormatException) {
            System.out.println(numberFormatException);
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
        handleAppDataUpdate();

        Intent newNoteIntent = new Intent(this, NoteViewActivity.class);
        newNoteIntent.putExtra(ValueKey.APP_DATA, appData);
        startActivity(newNoteIntent);
        finish();
    }

    /**
     * Behandelt die verschiedenen Möglichkeiten, die auftreten können, und aktualisiert die appData entsprechend.
     * <p>
     * Speichern der Notiz(en)
     * Löschen der Notiz und speichern der Notizen.
     */
    private void handleAppDataUpdate() {
        TextView note_text_view = findViewById(R.id.note);
        if (editExistingNote && !deleteNote) {
            preProcessingText(note_text_view.getText().toString(), note);
            note.setText(note_text_view.getText().toString());
        } else if (editExistingNote && deleteNote) {
            if (appData.removeNote(note.getID())) {
                Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        } else if (!editExistingNote && !deleteNote) {
            preProcessingText(note_text_view.getText().toString(), note);
            appData.getNotes().add(new Note(appData.generateNewID(), new Date(), note_text_view.getText().toString()));
        } else if (!editExistingNote && deleteNote) {
            // nothing happens because than the new note isn't saved
        }
        appData.saveNotesToFile(getFilesDir());
    }


    /**
     * Diese Methode fügt Tags und Daten aus dem Text zur Notiz hinzu.
     *
     * @param text Der zu verarbeitende Text
     */
    private void preProcessingText(String text, Note note) {
//      TODO get tags to note
        Pattern tagPattern = Pattern.compile("@\\w+\\s?");
        Matcher tagMatcher = tagPattern.matcher(text);
        if (tagMatcher.find()){
            for (int i = 0; i < tagMatcher.groupCount(); i++) {
                System.out.println(tagMatcher.group(i));
            }
        }
        Pattern datePattern = Pattern.compile("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");
        Matcher dateMatcher = datePattern.matcher(text);
        if (dateMatcher.find()){
            for (int i = 0; i < dateMatcher.groupCount(); i++) {
                System.out.println(dateMatcher.group(i));
            }
        }
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
                handleAppDataUpdate();
                Toast.makeText(this, "Note(s) saved!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_reminder_menu_item:
//                TODO add Reminder
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

    /**
     * Shows the
     */
    private void showTimePickerDialog() {
        LocalDateTime now = LocalDateTime.now();
        LayoutInflater inflater = getLayoutInflater();

        // no real parent
        View header = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                System.out.println(hour + " " + min);
                notificationDateTimeCache = notificationDateTimeCache.withHour(hour);
                notificationDateTimeCache = notificationDateTimeCache.withMinute(min);
                notificationDateTimeCache = notificationDateTimeCache.withSecond(0);
                notificationDateTimeCache = notificationDateTimeCache.withNano(0);

                Notification notification = NotificationHandler.getNotification(NoteEditActivity.this, notificationDateTimeCache.toString());
                NotificationHandler.scheduleNotification(NoteEditActivity.this, notification, Date.from( notificationDateTimeCache.atZone( ZoneId.systemDefault()).toInstant()));
            }

        }, now.getHour(), now.getMinute(), true);
//        builder.setOnCancelListener(x -> {
//            //TODO TOAST????
//        });
        builder.setCustomTitle(header);
        builder.show();
    }

    /**
     * Zeigt den DatePicker an und gibt die Informationen
     * {@link NoteEditActivity#notificationDateTimeCache} und ruft
     * {@link NoteEditActivity#showTimePickerDialog()} auf.
     */
    private void showDatePickerDialog() {
        LocalDate today = LocalDate.now();

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                System.out.println(i + " " + i1 + " " + i2);
                notificationDateTimeCache = notificationDateTimeCache.withYear(i);
                // monate von 0-11 ihr ficker
                notificationDateTimeCache = notificationDateTimeCache.withMonth(i1+1);
                notificationDateTimeCache = notificationDateTimeCache.withDayOfMonth(i2);
                showTimePickerDialog();
            }
        };
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.timepicker_header, null);
        // TODO DOKU monate fangen hier mit 0 an i guess
        DatePickerDialog builder = new DatePickerDialog(this, R.style.DialogTheme, onDateSetListener, today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        builder.setCustomTitle(header);
        builder.show();
    }

}

