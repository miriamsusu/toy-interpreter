package interpreter.model.adt;

public interface IDictionary<K,V> {
    boolean isDefined(K id);

    V lookup(K id);
    void update(K key, V value);
    void put(K key, V value);
}
