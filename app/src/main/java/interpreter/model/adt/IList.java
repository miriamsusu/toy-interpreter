package interpreter.model.adt;

import interpreter.exceptions.ADTException;

public interface IList<T> {
    void add(T elem);
    T get(int idx) throws ADTException;
    int size();
}
