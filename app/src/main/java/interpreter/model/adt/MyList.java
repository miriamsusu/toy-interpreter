package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayList;

public class MyList<T> implements IList<T> {
    private final ArrayList<T> list= new ArrayList<>();

    @Override
    public void add(T elem) {
        list.add(elem);
    }
    @Override
    public T get(int idx) {
        if(idx<0 || idx>list.size()){
            throw new ADTException("Invalid index, item not in list");
        }
        return list.get(idx);
    }
    @Override
    public int size() {
        return list.size();
    }


    @Override
    public String toString() { return list.toString(); }
}
