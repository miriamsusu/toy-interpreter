package interpreter.model.adt;

public interface IDictionary<K,V> {
    boolean isDefined(K id);

    V lookup(K id);
}
