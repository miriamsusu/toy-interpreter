package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.Map;

public interface IDictionary<K,V> {
    boolean isDefined(K id);

    V lookup(K id);
    void update(K key, V value);
    void put(K key, V value);
    void remove(K key) throws ADTException; // new: missing key -> ADTException
    Map<K, V> toMap(); // new: a read-only copy (Map.copyOf)

}
