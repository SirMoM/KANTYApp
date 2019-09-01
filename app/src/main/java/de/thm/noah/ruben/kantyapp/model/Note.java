package de.thm.noah.ruben.kantyapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Noah Ruben
 *
 * Diese Klasse repräsentiert eine Notiz.
 * Der Text kann nur 2,147,483,647 zeichenlang sein.
 */
public class Note implements Serializable {


    /**
     * ID der Notiz
     */
    private final int id;

    /**
     * Text der Notiz
     */
    private String text;

    /**
     * Tags der Notiz
     */
    private List<String> tags;

    /**
     * Alle Daten und Uhrzeiten, wann eine Benachrichtigung für diese Notiz eintrifft
     */
    private Set<String> reminderDates;

    /**
     * Pfad zur notiz (mit ggf. Pseudo-Ordnern usw)
     */
    private String path;

    /**
     *  Datum an dem die Notiz zuletzt bearbeitet wurde.
     */
    private Date modifyDate;

    /**
     *  Datum an dem die Notiz erzeugt wurde.
     */
    private final Date creationDate;

    /**
     * @param id die ID der Notiz
     * @param path Der Pfad (virtuell) der Notiz.
     * @param creationDate der Zeitpunkt der Erstellung der  Notiz.
     */
    public Note(int id, String path, Date creationDate) {
        this.id = id;
        this.path = path;
        this.creationDate = creationDate;
        this.tags = new ArrayList<String>();
        this.reminderDates = new HashSet<String>();
    }

    /**
     * @param id die ID der Notiz
     * @param text Der text der Notiz.
     * @param creationDate der Zeitpunkt der Erstellung der  Notiz.
     */
    public Note(int id, Date creationDate, String text) {
        this.id = id;
        this.text = text;
        this.creationDate = creationDate;
        this.tags = new ArrayList<String>();
        this.reminderDates = new HashSet<String>();
    }

    /**
     * @param id die ID der Notiz
     * @param text der Text der Notiz
     * @param tags die Tags der Notiz
     * @param reminderDates die Notifikation's Zeitpunkte der Notiz
     * @param path der Pfad der Notiz
     * @param creationDate der Zeitpunkt der Erstellung der  Notiz
     * @param modifyDate der Zeitpunkt der letzten Bearbeitung der  Notiz
     */
    public Note(int id, String text, List<String> tags, Set<String> reminderDates, String path, Date creationDate, Date modifyDate) {
        this.id = id;
        this.text = text;
        this.tags = tags;
        this.reminderDates = reminderDates;
        this.path = path;
        this.creationDate = creationDate;
        this.modifyDate = modifyDate;
    }

    /**
     * @return der text der Notiz
     */
    public String getText() {
        return text;
    }

    /**
     * @param text setzt den text der Notiz
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param tags setzt die Tags der Notiz
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return die Tags der Notiz
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @return der Pfad zur Notiz
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path setzt den Pfad zur Notiz
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return den Zeitpunkt an dem die Notiz erstellt wurde
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return den Zeitpunkt an dem die Notiz zuletzt bearbeitet wurde
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * @param modifyDate setzt den Zeitpunkt an dem die Notiz zuletzt bearbeitet wurde
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * @return die ID der Notiz.
     */
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Note: " + id + " {");
        sb.append("text='").append(text).append('\'');
        sb.append(", tags=").append(tags);
        sb.append(", path='").append(path).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append('}');
        return sb.toString();
    }

    /**
     * @return alle Benachichtigungs Zeitpunkte
     */
    public Set<String> getReminderDates() {
        return reminderDates;
    }

    /**
     * @param reminderDates setzt alle Benachrichtigung's Zeitpunkte
     */
    public void setReminderDates(Set<String> reminderDates) {
        this.reminderDates = reminderDates;
    }

    /**
     * @param reminderDate fügt ein Benachrichtigung's Zeitpunkt hinzu.
     * @return true wenn das reminderDate erfolgreich hinzugefügt wurde.
     */
    public boolean addReminderDate(String reminderDate) {
        System.out.println(reminderDates);
        return reminderDates.add(reminderDate);
    }

    /**
     * Ruft die erste Zeile der Notiz ab. <p>
     * Dies kann als "Header" verwendet werden.
     *
     * @Return die Kopfzeile des Hinweises Aka die erste Zeile
     */
    public String getFirstLine(){
        String[] result = getText().split(System.lineSeparator(), 2);
        return result[0];
    }

    /**
     * Fügt Tags einer Notiz hinzu wenn diese den Tag nicht schon besitzt.
     *
     * @param tag Fügt einen Tag zu dem bestehenden tags hinzu.
     * @return ein Bool der angibt ob der Tag hinzugefügt wurde.
     */
    public boolean addTagToNote(String tag) {
        if (!this.getTags().contains(tag)){
            this.getTags().add(tag);
            return true;
        }else {
            return false;
        }
    }
}
