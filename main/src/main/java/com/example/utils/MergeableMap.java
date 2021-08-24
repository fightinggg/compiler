package com.example.utils;

import java.util.*;
import java.util.stream.Collectors;

public class MergeableMap<K, V> implements Map<K, V> {
    private final Map<K, V>[] maps;
    private final int size;

    @SafeVarargs
    private MergeableMap(Map<K, V>... maps) {
        this.maps = maps;
        this.size = Arrays.stream(maps).mapToInt(Map::size).sum();
        if (size != Arrays.stream(maps).map(Map::keySet).mapToLong(Collection::size).sum()) {
            throw new RuntimeException("error merge map");
        }
    }

    @SafeVarargs
    public static <K, V> Map<K, V> merge(Map<K, V>... maps) {
        return new MergeableMap<>(maps);
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
    public boolean containsKey(Object key) {
        return Arrays.stream(maps).anyMatch(o -> o.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return Arrays.stream(maps).anyMatch(o -> o.containsValue(value));
    }

    @Override
    public V get(Object key) {
        return Arrays.stream(maps)
                .filter(o -> o.containsKey(key))
                .map(o -> o.get(key))
                .findAny()
                .get();
    }

    @Override
    public V put(K key, V value) {
        throw new RuntimeException("unsupport");
    }

    @Override
    public V remove(Object key) {
        throw new RuntimeException("unsupport");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new RuntimeException("unsupport");
    }

    @Override
    public void clear() {
        throw new RuntimeException("unsupport");
    }

    @Override
    public Set<K> keySet() {
        return Arrays.stream(maps).map(Map::keySet).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(maps).map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet());

    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Arrays.stream(maps).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toSet());

    }
}
