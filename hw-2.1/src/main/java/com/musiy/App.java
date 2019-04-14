package com.musiy;

import com.musiy.list.CustomArrayList;

import java.util.*;

public class App {

    public static void main(String[] args) {

        // Тестируем, Collections.addAll
        var list = new CustomArrayList<Integer>();
        Collections.addAll(list, 1, 2, 3, null, 3, 2, 1, null, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        Collections.addAll(list, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        var listCopy = new CustomArrayList<Integer>();
        listCopy.addAll(list);
        System.out.println(equals(list, listCopy));
        // увеличим число элементов
        Integer[] array = new Integer[1_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        Collections.addAll(list, array);
        Collections.addAll(listCopy, array);
        System.out.println(equals(list, listCopy));


        // Тестируем  Collections.copy, Collections.sort
        var listSrc = new CustomArrayList<String>();
        var listDest = new CustomArrayList<String>();
        for (int i = 0; i < 1_000_000; i++) {
            int j = i % 26;
            listSrc.add(String.valueOf((char) ('a' + j)));
            listDest.add("");
        }

        Collections.copy(listDest, listSrc);
        System.out.println(equals(listDest, listSrc));
        Collections.sort(listSrc, String::compareTo);
        Collections.sort(listDest, Comparator.reverseOrder());
        ListIterator<String> it1 = listSrc.listIterator();
        ListIterator<String> it2 = listDest.listIterator(listDest.size());
        while (it1.hasNext()) {
            assert it2.hasPrevious();
            String s1 = it1.next();
            String s2 = it2.previous();
            if (!s1.equals(s2)) {
                throw new RuntimeException();
            }
        }
    }

    static <T> boolean equals(Collection<T> c1, Collection<T> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        Iterator<T> it1 = c1.iterator();
        Iterator<T> it2 = c2.iterator();
        while (it1.hasNext()) {
            assert it2.hasNext();
            T t1 = it1.next();
            T t2 = it2.next();
            if (t1 == null) {
                if (t1 != t2) {
                    return false;
                }
            } else if (!t1.equals(t2)) {
                return false;
            }
        }
        return true;
    }
}
