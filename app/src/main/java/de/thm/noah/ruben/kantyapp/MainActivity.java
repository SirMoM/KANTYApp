package de.thm.noah.ruben.kantyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.Note;
import de.thm.noah.ruben.kantyapp.model.ValueKey;

/**
 * @author Noah Ruben
 * <p>
 * Das ist der Main Controller der App
 */
public class MainActivity extends AppCompatActivity {
    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = new AppData();

        // lade Daten wenn welche vorhanden sind
        if (loadData(savedInstanceState)) {
            Intent intent = new Intent(this, NoteViewActivity.class);
            intent.putExtra(ValueKey.APP_DATA, appData);
            startActivity(intent);
        }
    }

    /**
     * Diese Methode läd alle Daten in den Speicher. Aus aus der Default-Datei.
     *
     * @param savedInstanceState das Bundle
     *
     * @return der boolean gibt an ob das laden Erfolgreich war.
     */
    private boolean loadData(Bundle savedInstanceState) {
        File file = new File(this.getFilesDir(), "notebook.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
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
            e.printStackTrace();
            return false;
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
