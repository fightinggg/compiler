package com.example.utils;

import java.util.*;

public class MergeableCollection<T> implements Collection<T> {
    private final Collection<T>[] c;
    private final Integer size;

    @SafeVarargs
    private MergeableCollection(Collection<T>... c) {
        this.c = c;
        this.size = Arrays.stream(c).mapToInt(Collection::size).sum();
    }

    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T>... c) {
        return new MergeableCollection<T>(c);
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
        throw new RuntimeException("unsupport for contains");
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(c).flatMap(Collection::stream).iterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.stream(c).flatMap(Collection::stream).toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return Arrays.stream(c).flatMap(Collection::stream).toList().toArray(a);
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
        throw new RuntimeException("unsupport for containsAll");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
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
}
