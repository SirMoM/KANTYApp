package de.thm.noah.ruben.kantyapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;
import de.thm.noah.ruben.kantyapp.notifications.NotificationHandler;


/**
 * @author Noah Ruben
 * <p>
 * Das ist der "Loading-Screen"-Controller der App
 */
public class MainActivity extends AppCompatActivity {

    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_loading_view);
        appData = new AppData();

        // lade Daten wenn welche vorhanden sind
        boolean loadDataSuccessfully = loadData(savedInstanceState);

        NotificationHandler.createNotificationChannel(this); // create Notification channel to always  have one ?

        // Get the intent that started this activity
        Intent gotIntent = getIntent();

        // Get the action of the intent
        String action = gotIntent.getAction();

        // Get the type of intent (Text or Image)
        String type = gotIntent.getType();

        // When Intent's action is 'ACTION+SEND' and Type is not null
        if (Intent.ACTION_SEND.equals(action) && type != null && loadDataSuccessfully) {

            // When type is 'text/plain'
            if ("text/plain".equals(type)) {
                String sharedText = gotIntent.getStringExtra(Intent.EXTRA_TEXT);
                Intent intent = new Intent(this, NoteEditActivity.class);
                intent.putExtra(ValueKey.APP_DATA, appData);
                intent.putExtra(ValueKey.NOTE_TEXT, sharedText);
                startActivity(intent);
            }
        } else if(loadDataSuccessfully) {
            Intent intent = new Intent(this, NoteViewActivity.class);
            intent.putExtra(ValueKey.APP_DATA, appData);
            startActivity(intent);
        }
    }

    /**
     * Diese Methode läd alle Daten in den Speicher. Aus aus der Default-Datei.
     *
     * @param savedInstanceState Das Bundle
     * @return Gibt an ob das laden der Daten erfolgreich war.
     */
    private boolean loadData(Bundle savedInstanceState) {
//      Lade die Daten vom Standard-Speicherplatz
        File file = new File(this.getFilesDir(), "notebook.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
//      Token für den JSON-Parser damit er eine Liste parsen kann
        Type listType = new TypeToken<List<Note>>() {}.getType();
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            List<Note> notes = gson.fromJson(str, listType);
            appData.setNotes(notes);
        } catch (FileNotFoundException e) {
            System.out.println("No save file!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
