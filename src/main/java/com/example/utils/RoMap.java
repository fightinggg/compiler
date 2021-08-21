package com.example.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RoMap<K, V> implements Map<K, V> {

    private final Map<K, V> innerMap;

    public RoMap(Map<K, V> innerMap) {
        this.innerMap = innerMap;
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return innerMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return innerMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        throw new RuntimeException("ReadOnlyClass");
    }

    @Override
    public V remove(Object key) {
        throw new RuntimeException("ReadOnlyClass");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new RuntimeException("ReadOnlyClass");
    }

    @Override
    public void clear() {
        throw new RuntimeException("ReadOnlyClass");
    }

    @Override
    public Set<K> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return innerMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return innerMap.entrySet();
    }
}
