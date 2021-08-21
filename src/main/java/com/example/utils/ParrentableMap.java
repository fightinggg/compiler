package com.example.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParrentableMap<K, V> implements Map<K, V> {
    private final Map<K, V> map;
    private final Map<K, V> parrent;

    public ParrentableMap(Map<K, V> map, Map<K, V> parrent) {
        this.map = map;
        this.parrent = parrent;
        if (map.keySet().stream().anyMatch(parrent::containsKey)) {
            throw new RuntimeException("parrentMap and map conflict");
        }
    }

    @Override
    public int size() {
        return map.size() + parrent.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty() && parrent.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key) || parrent.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value) || parrent.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V v = map.get(key);
        return v == null ? parrent.get(key) : v;
    }

    @Override
    public V put(K key, V value) {
        if (parrent.containsKey(key)) {
            throw new RuntimeException("parrentMap Read Only");
        }
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        if (parrent.containsKey(key)) {
            throw new RuntimeException("parrentMap Read Only");
        }
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m.keySet().stream().anyMatch(parrent::containsKey)) {
            throw new RuntimeException("parrentMap Read Only");
        }
        map.putAll(m);
    }

    @Override
    public void clear() {
        if (!parrent.isEmpty()) {
            throw new RuntimeException("parrentMap Read Only");
        }
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return Stream.concat(map.keySet().stream(), parrent.keySet().stream()).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return Stream.concat(map.values().stream(), parrent.values().stream()).collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Stream.concat(map.entrySet().stream(), parrent.entrySet().stream()).collect(Collectors.toSet());
    }
}
