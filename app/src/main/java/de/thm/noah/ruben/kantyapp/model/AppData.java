package de.thm.noah.ruben.kantyapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Noah Ruben
 *
 * Diese Klasse hält alle Daten der App.
 */
public class AppData  implements java.io.Serializable {

    private static final long serialversionUID = 129348938L;


    private List<Note> notes;
    private Set<String> uniqueTags;
    private Set<Integer> uniqueIDs;

    /**
     *
     * @param notes alle notizen der App
     * @param uniqueTags alle Tags der App
     */
    public AppData(List<Note> notes, Set<String> uniqueTags) {
        this.notes = notes;
        this.uniqueTags = uniqueTags;
    }

    public AppData() {
        notes = new ArrayList<Note>();
        uniqueTags = new HashSet<String>();
        uniqueIDs = new HashSet<>();
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

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


    public int generateNewID() {
        int randInt = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        if (uniqueIDs.contains(randInt)){
            return generateNewID();
        } else {
            return randInt;
        }
    }

    /**
     * Diese Methode sucht die Notiz aus der Liste von nNtizen anhand der eindeutigen ID.
     *
     * @param noteID die Notiz-ID zur gesuchten Notiz
     * @return Entweder eine notiz oder "null"
     */
    public Note getNoteByID(Integer noteID) {
        for (Note note : getNotes()) {
            if (note.getID() == noteID){
                return note;
            }
        }
        return null;

    }
}
