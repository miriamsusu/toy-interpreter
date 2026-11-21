package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IHeap {
    private Map<Integer, IValue> heap = new HashMap<>();
    private int nextFree = 1; // address 0 means null
    @Override
    public int allocate(IValue value) {
        int address = nextFree++;
        heap.put(address, value);
        return address;
    }

    @Override
    public IValue get(int address) throws ADTException {
        return null;
    }

    @Override
    public void update(int address, IValue value) throws ADTException {

    }

    @Override
    public boolean isDefined(int address) {
        return false;
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return Map.of();
    }

    @Override public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }
    // TODO: get and update (a missing address -> ADTException), isDefined,
    // getContent (Map.copyOf(heap)), toString
}