package de.thm.noah.ruben.kantyapp.model;

import androidx.appcompat.widget.AppCompatTextView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class AppDataTest {

    AppData classToTest;
    @Before
    public void setUp() throws Exception {
        classToTest = new AppData();
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST1", new ArrayList<String>(Arrays.asList("11", "12", "13", "24")), null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST2", new ArrayList<String>(Arrays.asList("21", "22", "13", "24")), null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST3", new ArrayList<String>(Arrays.asList("21", "32", "3", "4")), null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST4", new ArrayList<String>(Arrays.asList("1", "2", "3", "4")), null, new Date(), new Date()));
    }

    @Test
    public void generateNewIDLoopTest() {
        int newID = 0;
        for (int i = 0; i < 100; i++) {
            newID = classToTest.generateNewID();
        }
        assertFalse(classToTest.getUniqueIDs().contains(newID));
    }

    @Test
    public void generateNewIDTest() {
            int newID = classToTest.generateNewID();
            assertFalse(classToTest.getUniqueIDs().contains(newID));
    }

    @Test
    public void getNoteByIDTest() {
        Integer newID = classToTest.generateNewID();
        Note note = new Note(newID, new Date(), "TEST5");
        classToTest.getNotes().add(note);
        assertEquals(classToTest.getNoteByID(newID), note);

    }

    @Test
    public void removeNoteTest() {
        int size = classToTest.getNotes().size();
        Integer newID = classToTest.generateNewID();
        Note note = new Note(newID, new Date(), "TEST5");
        classToTest.getNotes().add(note);
        classToTest.removeNote(newID);
        assertTrue(classToTest.getNotes().size() == size);
    }
}