package de.thm.noah.ruben.kantyapp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AppDataTest {

    private AppData classToTest;

    @Before
    public void setUp(){
        classToTest = new AppData();
    }

    @Test
    public void generateNewIDLoopTest() {

        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            int newID = classToTest.generateNewID();
            if (ints.contains(newID)) {
                System.out.println(classToTest);
                fail(i + ". entry was not unique: " + newID );
            }
            ints.add(newID);
        }
    }

    @Test
    public void generateNewIDTest() {
            int newID = classToTest.generateNewID();
            assertTrue(classToTest.getUniqueIDs().contains(newID));
    }

    @Test
    public void getNoteByIDTest() {
        int newID = classToTest.generateNewID();
        Note note = new Note(newID, new Date(), "TEST5");
        classToTest.getNotes().add(note);
        assertEquals(classToTest.getNoteByID(newID), note);

    }

    @Test
    public void removeNoteTest() {
        int size = classToTest.getNotes().size();
        int newID = classToTest.generateNewID();
        Note note = new Note(newID, new Date(), "TEST5");
        classToTest.getNotes().add(note);
        classToTest.removeNote(newID);
        assertEquals(classToTest.getNotes().size(), size);
    }
}