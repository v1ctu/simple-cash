package com.github.v1ctu.cache;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class Cache<K, V> {

    private final Map<K, V> elements = new WeakHashMap<>();

    public void put(K key, V value) {
        elements.put(key, value);
    }

    public void remove(K key) {
        elements.remove(key);
    }

    public V get(K key) {
        return elements.get(key);
    }

    public Collection<V> values() {
        return elements.values();
    }

    public abstract void save();


}
