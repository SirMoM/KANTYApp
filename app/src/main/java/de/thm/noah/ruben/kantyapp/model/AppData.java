package de.thm.noah.ruben.kantyapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Noah Ruben
 * <p>
 * Diese Klasse hält alle Daten der App. <p>
 * Serialisierbar wegen der übergabe in andere Activities.
 */
public class AppData implements java.io.Serializable {

    private static final long serialversionUID = 129348938L;


    /**
     * Liste aller Notizen.
     */
    private List<Note> notes;

    /**
     * Alle "Tags" der gespeicherten Notizen.
     */
    private Set<String> uniqueTags;

    /**
     * Alle "ID's" der gespeicherten Notizen.
     */
    private Set<Integer> uniqueIDs;

    /**
     * @param notes      alle notizen der App
     * @param uniqueTags alle Tags der App
     */
    public AppData(List<Note> notes, Set<String> uniqueTags) {
        this.notes = notes;
        this.uniqueTags = uniqueTags;
    }

    /**
     * Default Konstruktor.
     */
    public AppData() {
        notes = new ArrayList<Note>();
        uniqueTags = new HashSet<String>();
        uniqueIDs = new HashSet<Integer>();
    }

    public Set<String> getUniqueTags() {
        return uniqueTags;
    }

    public void setUniqueTags(Set<String> uniqueTags) {
        this.uniqueTags = uniqueTags;
    }

    public Set<Integer> getUniqueIDs() {
        return uniqueIDs;
    }

    public void setUniqueIDs(Set<Integer> uniqueIDs) {
        this.uniqueIDs = uniqueIDs;
    }

    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Ersetzt die vorhandene Liste mit Notizen und
     *
     * @param notes die Liste von Notizen die gesetzt werden soll
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        setAllTagsAndIDs();
    }

    /**
     * Ruft alle IDs und Tags aus der neuen Liste der Notizen ab und speichert diese.
     */
    private void setAllTagsAndIDs() {
        HashSet<Integer> newIDs = new HashSet<Integer>();
        HashSet<String> newTags = new HashSet<String>();
        for (Note note : getNotes()) {
            newIDs.add(note.getID());
            newTags.addAll(note.getTags());
        }
        setUniqueTags(newTags);
        setUniqueIDs(newIDs);
    }

    /**
     * Generiert einen zufällige eindeutige ID / Integer.
     *
     * @return eine neue eindeutige ID für eine neue Notiz.
     */
    public int generateNewID() {
        int randInt = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        if (uniqueIDs.contains(randInt)) {
            return generateNewID();
        } else {
            uniqueIDs.add(randInt);
            return randInt;
        }
    }

    /**
     * Diese Methode sucht die Notiz aus der Liste von Notizen anhand der eindeutigen ID.
     *
     * @param noteID die Notiz-ID zur gesuchten Notiz
     * @return Entweder eine notiz oder "null"
     */
    public Note getNoteByID(Integer noteID) {
        for (Note note : getNotes()) {
            if (note.getID() == noteID) {
                return note;
            }
        }
        return null;
    }

    /**
     * Löscht die Notiz mir der zugehörigen ID.
     *
     * @param id die  ID der Notiz.
     * @return gibt an ob die Notiz gelöscht werden konnte.
     */
    public boolean removeNote(Integer id) {
        Note noteToDelete = getNoteByID(id);
        try {
            getNotes().remove(noteToDelete);
            uniqueIDs.remove(noteToDelete.getID());
            uniqueTags.removeAll(noteToDelete.getTags());
            // TODO maybe lazy if the performance suffers
            for (Note note : getNotes()) {
                uniqueTags.addAll(note.getTags());
            }
        } catch (Exception exception) {
            System.out.println("Could not remove note");
            return false;
        }
        return true;
    }

    /**
     * Diese Methode speichert alle Notizen in eine Datei mit dem Namen "notebook".
     *
     * @param path der Pfad andem die Datei liegen wird.
     */
    public void saveNotesToFile(File path) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        File file = new File(path, "notebook.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            String notesToJson = gson.toJson(getNotes());
            fileWriter.write(notesToJson);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
