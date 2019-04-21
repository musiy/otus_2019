package com.musiy.list;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Пример реализации саморасширяющейгося списка на основе массива.
 * @param <T> тип данных массива
 */
public class CustomArrayList<T> implements List<T> {

    /**
     * Ёмкость массива по умолчанию
     */
    private static int DEFAULT_CAPACITY = 16;

    /**
     * Фактор увеличения массива.
     * FACTOR = 2.0 ознчает, что при расширении ёмкость будет увеличена в два раза.
     */
    private static double FACTOR = 2.0;

    /**
     * Внутреннее хранилище
     */
    private Object[] arr;

    /**
     * Текущий размер массива.
     * Изначально = 0. После добавления первого элемента = 1. и т.д.
     * size всегда <= arr.length
     */
    private int size;

    /**
     * Текущее состояние массива.
     * Меняется при изменении массива.
     */
    private int modifications = 0;


    public CustomArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public CustomArrayList(int capacity) {
        initStorage(capacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomListIterator<>(0, modifications);
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(arr, 0, copy, 0, size);
        return copy;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length != size) {
            a = (T1[]) Array.newInstance(a.getClass(), size);
        }
        System.arraycopy(arr, 0, a, 0, size);
        return a;
    }

    @Override
    public boolean add(T t) {
        modifications++;
        if (arr.length == size) {
            extendStorage();
        }
        arr[size++] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        modifications++;
        int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        System.arraycopy(arr, i + 1, arr, i, size - i - 1);
        arr[--size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return false;
        }
        modifications++;
        for (T t : c) {
            add(t);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (c.isEmpty()) {
            return false;
        }
        while (arr.length - size < c.size()) {
            extendStorage();
        }
        modifications++;
        System.arraycopy(arr, index, arr, index + c.size(), size - index);
        for (T t : c) {
            modifications++;
            arr[index++] = t;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        modifications++;
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        modifications++;
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        modifications++;
        initStorage(DEFAULT_CAPACITY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) arr[index];
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        modifications++;
        T prev = (T) arr[index];
        arr[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        addAll(index, Collections.singleton(element));
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        modifications++;
        T o = (T) arr[index];
        System.arraycopy(arr, index + 1, arr, index, size - index);
        arr[size--] = null;
        return o;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (; i < size; i++) {
            if ((o == null && arr[i] == null) ||
                    (o != null && o.equals(arr[i]))) {
                break;
            }
        }
        return i == size ? -1 : i;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            Object prev = arr[i];
            if ((o == null && prev == null)
                    || (o != null && o.equals(prev))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new CustomListIterator<>(0, modifications);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return new CustomListIterator<>(index, modifications);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Метод для расширения внутреннего хранилища
     */
    private void extendStorage() {
        Object[] newArr = new Object[(int) (size * FACTOR)];
        System.arraycopy(arr, 0, newArr, 0, size);
        arr = newArr;
    }

    /**
     * Метод для инициализации внутреннего хранилища
     */
    private void initStorage(int capacity) {
        arr = new Object[capacity];
        this.size = 0;
    }

    /**
     * Итератор по списку
     * @param <E> тип итератора (совпадает с типом массива)
     */
    private class CustomListIterator<E> implements ListIterator<E> {

        /**
         * Индекс элемента который будет возвращен при вызове next()
         */
        private int index;
        /**
         * Индекс элемента который будет возвращен при вызове previous()
         */
        private int lastReturnedIndex = -1;
        /**
         * Состояние изменений основного массива.
         * Состояние не должно изменяться при итерации.
         */
        private int state;

        CustomListIterator(int index, int state) {
            this.index = index;
            this.state = state;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            checkForConcurrentModification();
            if (index >= size) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = index++;
            return (E) arr[lastReturnedIndex];
        }

        @Override
        public boolean hasPrevious() {
            checkForConcurrentModification();
            return index > 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            checkForConcurrentModification();
            if (index == 0) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = --index;
            return (E) arr[lastReturnedIndex];
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            checkForConcurrentModification();
            if (lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }
            System.arraycopy(arr, lastReturnedIndex + 1, arr, lastReturnedIndex,
                    size - lastReturnedIndex - 1);
            size--;
            arr[size] = null;
            if (lastReturnedIndex < index) {
                index--;
            }
            lastReturnedIndex = -1;
        }

        @Override
        public void set(E e) {
            checkForConcurrentModification();
            if (lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }
            arr[lastReturnedIndex] = e;
        }

        @Override
        public void add(E e) {
            checkForConcurrentModification();
            state++;
            modifications++;
            if (size == arr.length) {
                CustomArrayList.this.extendStorage();
            }
            System.arraycopy(arr, index, arr, index + 1, size - index);
            arr[index] = e;
            index++;
            size++;
        }

        /**
         * Изменение списка во время итерации недопустимо.
         */
        private void checkForConcurrentModification() {
            if (state != modifications) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
