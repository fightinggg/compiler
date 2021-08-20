package com.example.utils;

import java.util.*;

public class ReadableList<T> implements List<T> {
    private final List<T> target;

    public ReadableList(List<T> c) {
        this.target = c;
    }

    @Override
    public int size() {
        return target.size();
    }

    @Override
    public boolean isEmpty() {
        return target.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return target.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return target.iterator();
    }

    @Override
    public Object[] toArray() {
        return target.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return target.toArray(a);
    }

    @Override
    public boolean add(T t) {
        throw new RuntimeException("unsupport for add");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("unsupport for remove");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return target.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new RuntimeException("unsupport for addAll");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException("unsupport for addAll");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("unsupport for removeAll");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("unsupport for retainAll");
    }

    @Override
    public void clear() {
        throw new RuntimeException("unsupport for clear");
    }

    @Override
    public T get(int index) {
        return target.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new RuntimeException("unsupport for set");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("unsupport for add");
    }

    @Override
    public T remove(int index) {
        throw new RuntimeException("unsupport for remove");
    }

    @Override
    public int indexOf(Object o) {
        return target.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return target.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return target.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return target.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return target.subList(fromIndex, toIndex);
    }
}
