package com.musiy;

import com.musiy.list.CustomArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

    @Test
    void testContains1() {
        var list = new CustomArrayList<Integer>();
        list.add(1);
        list.add(null);
        assertTrue(list.contains(null));
        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
    }

    @Test
    void testContains2() {
        var list = new CustomArrayList<Integer>();
        list.add(null);
        list.add(1);
        assertTrue(list.contains(1));
    }

    @Test
    void testRemove() {
        var list = new CustomArrayList<String>();
        list.add(null);
        assertFalse(list.remove("no"));
        list = new CustomArrayList<>();
        list.add("mon");
        list.add("tue");
        list.add("wed");
        list.add("thu");
        list.add("fri");
        list.add("sat");
        list.add("sun");
        list.add(null);
        assertEquals(list.size(), 8);
        list.remove(null);
        list.remove("sun");
        list.remove("sat");
        list.remove("thu");
        list.remove("mon");
        assertEquals(list.get(0), "tue");
        assertEquals(list.get(1), "wed");
        assertEquals(list.get(2), "fri");
        assertEquals(list.size(), 3);
    }
}
