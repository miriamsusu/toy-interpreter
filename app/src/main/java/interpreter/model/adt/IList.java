package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.List;

public interface IList<T> {
    void add(T elem);
    T get(int idx) throws ADTException;
    int size();
    List<T> toList();
}
