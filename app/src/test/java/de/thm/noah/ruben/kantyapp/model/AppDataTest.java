package de.thm.noah.ruben.kantyapp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class AppDataTest {

    private AppData classToTest;

    @Before
    public void setUp(){
        classToTest = new AppData();
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST1", new ArrayList<String>(Arrays.asList("11", "12", "13", "24")), reminderDates, null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST2", new ArrayList<String>(Arrays.asList("21", "22", "13", "24")), reminderDates, null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST3", new ArrayList<String>(Arrays.asList("21", "32", "3", "4")), reminderDates, null, new Date(), new Date()));
        classToTest.getNotes().add(new Note(classToTest.generateNewID(), "TEST4", new ArrayList<String>(Arrays.asList("1", "2", "3", "4")), reminderDates, null, new Date(), new Date()));
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
//

//        assertFalse(classToTest.getUniqueIDs().contains(newID));
    }

    @Test
    public void generateNewIDTest() {
            int newID = classToTest.generateNewID();
            assertFalse(classToTest.getUniqueIDs().contains(newID));
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