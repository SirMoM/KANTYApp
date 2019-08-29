package de.thm.noah.ruben.kantyapp.model;

import java.io.Serializable;
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
    }

    /**
     * Ruft alle IDs und Tags aus der neuen Liste der Notizen ab und speichert diese.
     */
    private void getAllTagsIDs() {
        HashSet<Integer> newIDs = new HashSet<Integer>();
        HashSet<String> newTags = new HashSet<String>();
        for (Note note: getNotes()) {
            newIDs.add(note.getID());
            newTags.addAll(note.getTags());
        }

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
}
