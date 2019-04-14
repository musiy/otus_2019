package com.musiy.list;

import java.util.*;

public class CustomArrayList<T> implements List<T> {

    private static int DEFAULT_SIZE = 1;

    private static double FACTOR = 2.0;

    private Object[] arr;

    private int size;

    private int modifications = 0;

    public CustomArrayList() {
        this(DEFAULT_SIZE);
    }

    @SuppressWarnings("unchecked")
    public CustomArrayList(int size) {
        arr = new Object[size];
        this.size = 0;
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
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (arr[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(arr[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomListIterator<>(0);
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(arr, 0, copy, 0, size);
        return copy;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        modifications++;
        if (arr.length == size) {
            Object[] newArr = new Object[(int) (size * FACTOR)];
            System.arraycopy(arr, 0, newArr, 0, size);
            arr = newArr;
        }
        arr[size++] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        modifications++;
        int i = 0;
        found:
        {
            if (o == null) {
                for (; i < size; i++) {
                    if (arr[i] == null) {
                        break found;
                    }
                }
            } else {
                for (; i < size; i++) {
                    if (o.equals(arr[i])) {
                        break found;
                    }
                }
            }
            return false;
        }
        System.arraycopy(arr, i + 1, arr, i, size - i - 1);
        arr[--size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) arr[index];
    }

    @Override
    public T set(int index, T element) {
        modifications++;
        T prev = (T) arr[index];
        arr[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new CustomListIterator<>(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (!(index > 0 && index <= size)) {
            throw new IndexOutOfBoundsException();
        }
        return new CustomListIterator<>(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class CustomListIterator<E> implements ListIterator<E> {

        private int index;
        private int lastReturnedIndex = -1;

        CustomListIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (index == size) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = index++;
            return (E) arr[lastReturnedIndex];
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
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
            if (lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }
            arr[lastReturnedIndex] = e;
        }

        @Override
        public void add(E e) {
            modifications++;
            if (size == arr.length) {
                Object[] newArr = new Object[(int) (size * FACTOR)];
                System.arraycopy(arr, 0, newArr, 0, size);
                arr = newArr;

            }
            System.arraycopy(arr, index, arr, index + 1, size - index);
            arr[index] = e;
            index++;
            size++;
        }
    }
}
