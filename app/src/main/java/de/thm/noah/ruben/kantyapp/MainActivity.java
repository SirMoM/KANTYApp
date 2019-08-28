package de.thm.noah.ruben.kantyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import de.thm.noah.ruben.kantyapp.model.AppData;
import de.thm.noah.ruben.kantyapp.model.ValueKey;

/**
 * @author Noah Ruben
 *
 * Das ist der Main Controller der App
 */
public class MainActivity extends AppCompatActivity{
    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = new AppData();

        if (loadData(savedInstanceState)){
            Intent intent = new Intent(this, NoteViewActivity.class);
            intent.putExtra(ValueKey.APP_DATA, appData);
            startActivity(intent);
        }
    }

    /**
     * Diese Methode läd alle Daten in den Speicher
     *
     * @param savedInstanceState
     */
    private boolean loadData(Bundle savedInstanceState) {
        return true;
    }
}
