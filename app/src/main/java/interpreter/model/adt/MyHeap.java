package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        if(!heap.containsKey(address)){
            throw new ADTException("Key does not exist");

        }
        return heap.get(address);
    }

    @Override
    public void update(int address, IValue value) throws ADTException {
        if(!heap.containsKey(address)){
            throw new ADTException("Key does not exist");
        }
        heap.put(address,value);
    }

    @Override
    public boolean isDefined(int address) {
        if(heap.containsKey(address))
            return true;
        else
            return false;
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return Map.copyOf(heap);
    }

    @Override public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var entry : heap.entrySet())
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        return sb.toString();
    }
}