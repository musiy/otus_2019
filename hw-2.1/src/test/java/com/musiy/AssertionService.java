package com.musiy;

import java.util.Collection;
import java.util.Iterator;

public class AssertionService {

    <T> boolean equalsCollections(Collection<T> c1, Collection<T> c2) {
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
