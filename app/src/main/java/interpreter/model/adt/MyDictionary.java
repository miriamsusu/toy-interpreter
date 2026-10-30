package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> map;

    public MyDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) {
        if (!map.containsKey(key)) {
            throw new ADTException("Key " + key + " is not defined");
        }
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            throw new ADTException("Key " + key + " is already defined");
        }
        map.put(key, value);
    }

    @Override
    public void remove(K key) throws ADTException {
        if(!map.containsKey(key)){
            throw new ADTException("Invalid key!");
        }
        map.remove(key);
    }

    @Override
    public Map<K, V> toMap() {
        return Map.copyOf(map);
    }

    @Override
    public void update(K key, V value) {
        if (!map.containsKey(key)) {
            throw new ADTException("Key " + key + " is not defined");
        }
        map.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}