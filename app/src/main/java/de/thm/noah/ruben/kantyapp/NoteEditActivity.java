package de.thm.noah.ruben.kantyapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import de.thm.noah.ruben.kantyapp.views.MarkdownWebView;


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

    private boolean showMd = true;
    private MarkdownWebView markdownWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NoteEditActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_view);
        Toolbar editHelperBar = findViewById(R.id.editHelperBar);
        setSupportActionBar(editHelperBar);


        EditText note_edit_view = findViewById(R.id.note);

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
            Serializable extra = getIntent().getSerializableExtra(ValueKey.NOTE_TEXT);
            if (extra != null) {
                note_edit_view.setText((String) extra);
            } else {
                note_edit_view.setText("# Keep a Note to Yourself \n Noah Ruben");
            }
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

        goToNoteViewActivity();
    }

    /**
     * creates and sends the Intend to change views to NoteView TODO GERMAN
     */
    private void goToNoteViewActivity() {
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
        EditText note_text_view = findViewById(R.id.note);
        if (editExistingNote && !deleteNote) {
            preProcessingText(note_text_view.getText().toString(), note);
            note.setText(note_text_view.getText().toString());
        } else if (editExistingNote && deleteNote) {
            if (appData.removeNote(note.getID())) {
                Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        } else if (!editExistingNote && !deleteNote) {
            note = new Note(appData.generateNewID(), new Date(), note_text_view.getText().toString());
            preProcessingText(note_text_view.getText().toString(), note);
            appData.getNotes().add(note);
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
        Pattern tagPattern = Pattern.compile("\\$\\w+\\s?");
        Matcher tagMatcher = tagPattern.matcher(text);
        while (tagMatcher.find()) {
            String tagString = tagMatcher.group().substring(1).trim();
            System.out.println("tagString = " + tagString);
            appData.getUniqueTags().add(tagString);
            if (note.addTagToNote(tagString)) {
                Toast.makeText(this, "Added " + tagString + "-Tag to note!", Toast.LENGTH_LONG).show();
            }
        }

        // Create a Reminder from the note Text if wanted
        Pattern dateTimePattern = Pattern.compile("<[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}(@[0-9]{2}:[0-9]{2})?");
        Matcher dateTimeMatcher = dateTimePattern.matcher(text);
        SimpleDateFormat dTF = new SimpleDateFormat("dd.MM.yyyy@kk:mm");
        SimpleDateFormat dF = new SimpleDateFormat("dd.MM.yyyy");
        while (dateTimeMatcher.find()) {
            Date date = null;
            try {
                date = dTF.parse(dateTimeMatcher.group());
                System.out.println("\t \t \t \t \t \t \t DTF = " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date == null) {
                try {
                    date = dF.parse(dateTimeMatcher.group());
                    System.out.println("\t \t \t \t \t \t \t DF = " + date);
                } catch (ParseException e) {
                e.printStackTrace();
                }
            }
            System.out.println("dateTimeMatcher.group() = " + dateTimeMatcher.group());
            boolean b = note.addReminderDate(dateTimeMatcher.group());
            if (b && date != null) {
                Notification notification = NotificationHandler.getNotification(NoteEditActivity.this, note.getFirstLine());
                NotificationHandler.scheduleNotification(NoteEditActivity.this, notification, date);
                Toast.makeText(this, "Created " + dTF.format(date) + " Notification for this note!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Erstellt die "Benachrichtigung hinzufügen"-Warnung
     */
    private void createAddReminderAlert(Date date) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.add_reminder);
        alertDialogBuilder.setMessage(" " + R.string.add_reminder_msg + (new SimpleDateFormat("dd.MM.yyyy@kk:mm")).format(date));
        alertDialogBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Notification notification = NotificationHandler.getNotification(NoteEditActivity.this, note.getText());
                NotificationHandler.scheduleNotification(NoteEditActivity.this, notification, date);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
//        alertDialogBuilder.create().show();
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
        EditText tv = findViewById(R.id.note);
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_menu_item:
                deleteNote = true;
                onBackPressed();
                return true;
            case R.id.save_menu_item:
                handleAppDataUpdate();
                Toast.makeText(this, "Note(s) saved!", Toast.LENGTH_SHORT).show();
                editExistingNote = true;
                return true;
            case R.id.add_reminder_menu_item:
//                TODO add Reminder
//                      * als Kalender eintrag
                showDatePickerDialog();
                return true;
            case R.id.add_tag_menu_item:
//                TODO add Alert to fill in TAG (Auto complete?)
                return true;
            case R.id.h1:
                addMdBefore(tv, ValueKey.H1);
                return true;
            case R.id.h2:
                addMdBefore(tv, ValueKey.H2);
                return true;
            case R.id.h3:
                addMdBefore(tv, ValueKey.H3);
                return true;
            case R.id.h4:
                addMdBefore(tv, ValueKey.H4);
                return true;
            case R.id.h5:
                addMdBefore(tv, ValueKey.H5);
                return true;
            case R.id.h_rule:
                addMdAfter(tv, ValueKey.H_RULE);
                return true;
            case R.id.date:
                addMdBefore(tv, ValueKey.DATE);
                return true;
            case R.id.tag:
                addMdBefore(tv, ValueKey.TAG);
                return true;
            case R.id.bold:
                addMdBeforeAfter(tv, ValueKey.BOLD);
                return true;
            case R.id.italic:
                addMdBeforeAfter(tv, ValueKey.ITALIC);
            case R.id.strikethrough:
                addMdBeforeAfter(tv, ValueKey.STRIKETHROUGH);
            case R.id.empty_table:
                addMdBefore(tv, ValueKey.EMPTY_TABLE);
                return true;
            case R.id.show_md_menu_item:
                CoordinatorLayout layout = findViewById(R.id.layout);

                EditText view = findViewById(R.id.note);
                if (showMd) {
                    view.setVisibility(View.GONE);
                    markdownWebView = new MarkdownWebView(this, view.getText().toString());
                    layout.addView(markdownWebView);
                    item.setTitle(R.string.show_text);
                    showMd = false;
                } else {
                    view.setVisibility(View.VISIBLE);
                    layout.removeView(markdownWebView);
                    item.setTitle(R.string.show_md);
                    showMd = true;
                }
                return true;
            case R.id.share_menu_item:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Note: " + note.getID());
                share.putExtra(Intent.EXTRA_TEXT, note.getText());

                startActivity(Intent.createChooser(share, "Share note!"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fügt Markdown elemente vor dem markierten Wort / Cursor ein.
     *
     * @param textEditView Die "view" zu der das "Markdown"-Element hinzugefügt werden soll
     * @param textToInsert Das "Markdown"-Element das hinzugefügt werden soll
     */
    private void addMdBefore(EditText textEditView, String textToInsert) {
        int start = Math.max(textEditView.getSelectionStart(), 0);
        int end = Math.max(textEditView.getSelectionEnd(), 0);
        String text = textEditView.getText().toString();
        String selectedText = text.substring(Math.min(start, end), Math.max(start, end));
        String firstTextHalf = text.substring(0, start);
        String lastTextHalf = text.substring(end);
        textToInsert = textToInsert + selectedText;
        textEditView.setText(firstTextHalf + textToInsert + lastTextHalf);
        textEditView.setSelection(end);
    }

    /**
     * Fügt Markdown elemente nach dem markierten Wort / Cursor ein.
     *
     * @param textEditView Die "view" zu der das "Markdown"-Element hinzugefügt werden soll
     * @param textToInsert Das "Markdown"-Element das hinzugefügt werden soll
     */
    private void addMdAfter(EditText textEditView, String textToInsert) {
        int start = Math.max(textEditView.getSelectionStart(), 0);
        int end = Math.max(textEditView.getSelectionEnd(), 0);
        String text = textEditView.getText().toString();
        String selectedText = text.substring(Math.min(start, end), Math.max(start, end));
        String firstTextHalf = text.substring(0, start);
        String lastTextHalf = text.substring(end);
        textToInsert = selectedText + textToInsert;
        textEditView.setText(firstTextHalf + textToInsert + lastTextHalf);
        textEditView.setSelection(end);
    }

    /**
     * Fügt Markdown elemente vor und nach dem markierten Wort / Cursor ein.
     *
     * @param textEditView Die "view" zu der das "Markdown"-Element hinzugefügt werden soll
     * @param textToInsert Das "Markdown"-Element das hinzugefügt werden soll
     */
    private void addMdBeforeAfter(EditText textEditView, String textToInsert) {
        int start = Math.max(textEditView.getSelectionStart(), 0);
        int end = Math.max(textEditView.getSelectionEnd(), 0);
        String text = textEditView.getText().toString();
        String selectedText = text.substring(Math.min(start, end), Math.max(start, end));
        String firstTextHalf = text.substring(0, start);
        String lastTextHalf = text.substring(end);
        textToInsert = textToInsert + selectedText + textToInsert;
        textEditView.setText(firstTextHalf + textToInsert + lastTextHalf);
        textEditView.setSelection(end);
    }

    /**
     * Zeigt den TimePicker an und gibt die Informationen
     * {@link NoteEditActivity#notificationDateTimeCache} und ruft den {@link NotificationHandler}
     * der eine Notification erzeugt und sie entsprechend {@link NoteEditActivity#notificationDateTimeCache} einplant.
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

                Notification notification = NotificationHandler.getNotification(NoteEditActivity.this, note.getFirstLine());
                NotificationHandler.scheduleNotification(NoteEditActivity.this, notification, Date.from(notificationDateTimeCache.atZone(ZoneId.systemDefault()).toInstant()));
            }
        }, now.getHour(), now.getMinute(), true);
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
                notificationDateTimeCache = notificationDateTimeCache.withMonth(i1 + 1);
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

