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

    @Test(expected = IndexOutOfBoundsException.class)
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

    @Test
    public void test3() {
        PersistentList<Integer> list = new PersistentList<Integer>();
        for (int i = 0; i < 100; i++) {
            list.add(i + 1);
        }
        for (int i = 0; i < 50; i++) {
            list.delete();
        }
        for (int vers = 0; vers < 100; vers++)
            for (int i = 0; i < vers; i++) {
                assertTrue(list.get(i, vers) == i + 1);
            }
        for (int vers = 100; vers < 150; vers++) {
            for (int i = 0; i < 199 - vers; i++) {
                assertTrue(list.get(i, vers) == i + 1);
            }
        }
        boolean flag = false;
        for (int vers = 0; vers < 150; vers++) {
            try {
//                System.out.println(vers + " " + (101 - Math.abs(vers - 100)));
                list.get(101 - Math.abs(vers - 100), vers);
            } catch (IndexOutOfBoundsException e) {
                flag = true;
            }
            assertTrue(flag);
            flag = false;
        }
    }

    @Test
    public void test4() {
        PersistentList<Integer> list = new PersistentList<Integer>();
        for (int i = 0; i < 100; i++) {
            list.add(i + 1);
        }
        for (int i = 9; i >= 0; i--) {
            list.add(100 + i, 50);
        }

        for (int i = 0; i < 50; i++) {
            assertTrue(list.get(i, 109) == i + 1);
        }
        for (int i = 50; i < 60; i++) {
            assertTrue(list.get(i, 109) == 50 + i);
        }
        for (int i = 60; i < 110; i++) {
            assertTrue(list.get(i, 109) == i - 9);
        }
    }
}
