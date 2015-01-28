package ru.itmo.antipov.coursework.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.itmo.antipov.coursework.PersistentList;

/**
 * Created by Den on 28.01.15.
 */
public class ListTest {
    @Test
    public void test() {
        PersistentList<Integer> list = new PersistentList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.get(0, 1) == 1);
        assertTrue(list.get(0, 2) == 1);
        assertTrue(list.get(1, 2) == 2);
        assertTrue(list.get(0, 3) == 1);
        assertTrue(list.get(1, 3) == 2);
        assertTrue(list.get(2, 3) == 3);
    }

    @Test//(expected = IndexOutOfBoundsException.class)
    public void test2() {
        PersistentList<Integer> list = new PersistentList<Integer>();
        list.add(1);
        list.add(2);
        list.delete();
        assertTrue(list.get(0, 0) == 1);
        assertTrue(list.get(0, 1) == 1);
        assertTrue(list.get(1, 1) == 2);
        assertTrue(list.get(0, 2) == 1);
        list.get(1, 2);
    }
}
