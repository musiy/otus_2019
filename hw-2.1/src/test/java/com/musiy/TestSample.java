package com.musiy;

import com.musiy.list.CustomArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSample {

    private AssertionService assertionService;

    @BeforeEach
    public void testSetUp() {
        assertionService = new AssertionService();
    }

    @Test
    public void testAddAll() {
        // Тестируем, Collections.addAll
        var list = new CustomArrayList<Integer>();
        Collections.addAll(list, 1, 2, 3, null, 3, 2, 1, null, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        Collections.addAll(list, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        var listCopy = new CustomArrayList<Integer>();
        listCopy.addAll(list);
        assertTrue(assertionService.equalsCollections(list, listCopy));
        // увеличим число элементов
        Integer[] array = new Integer[1_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        Collections.addAll(list, array);
        Collections.addAll(listCopy, array);
        assertTrue(assertionService.equalsCollections(list, listCopy));
    }

    @Test
    public void testCopy() {
        var list = new CustomArrayList<String>();
        var listCopy = new CustomArrayList<String>();
        for (int i = 0; i < 1_000_000; i++) {
            int j = i % 26;
            list.add(String.valueOf((char) ('a' + j)));
            listCopy.add("");
        }
        Collections.copy(listCopy, list);
        assertTrue(assertionService.equalsCollections(list, listCopy));
    }

    @Test
    public void testSort() {
        var list = new CustomArrayList<String>();
        var listCopy = new CustomArrayList<String>();
        for (int i = 0; i < 1_000_000; i++) {
            int j = i % 26;
            list.add(String.valueOf((char) ('a' + j)));
            listCopy.add(String.valueOf((char) ('a' + j)));
        }
        Collections.sort(list, String::compareTo);
        Collections.sort(listCopy, Comparator.reverseOrder());
        ListIterator<String> it1 = list.listIterator();
        ListIterator<String> it2 = listCopy.listIterator(listCopy.size());
        while (it1.hasNext()) {
            assert it2.hasPrevious();
            String s1 = it1.next();
            String s2 = it2.previous();
            if (!s1.equals(s2)) {
                throw new RuntimeException();
            }
        }
    }

}
