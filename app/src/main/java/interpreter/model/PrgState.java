package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.util.TreeMap;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IHeap heap;
    private final IStmt originalProgram;
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out,
                    IDictionary<StringValue, BufferedReader> fileTable, IHeap heap,
                    IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        this.heap=heap;
        this.fileTable=fileTable;
        exeStack.push(program);
    }
    public static PrgState of(IStmt program){
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(),new MyHeap() ,program);
    }
    public IStack<IStmt> getExeStack() { return exeStack; }
    public IDictionary<String, IValue> getSymTable() { return symTable; }
    public IList<IValue> getOut() { return out; }
    public IStmt getOriginalProgram() { return originalProgram; }
    public IDictionary<StringValue, BufferedReader> getFileTable() { return fileTable;}
    public IHeap getHeap() { return heap; }
    public void setHeap(IHeap newHeap) { heap = newHeap; }
    public void setExeStack(IStack<IStmt> stack){exeStack=stack;}
    public void setSymTable(IDictionary<String, IValue> table){symTable=table;}
    public void setOut(IList<IValue> newOut){out=newOut;}
    public void setFileTable(IDictionary<StringValue,BufferedReader> table){fileTable=table;}

    public String toLogString(){
        var sb=new StringBuilder();
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(s->sb.append(s).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((k,v)->sb.append(k).append(" --> ").append(v).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(v->sb.append(v).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(f -> sb.append(f.val()).append('\n'));
        sb.append("Heap:\n");
        new TreeMap<>(heap.getContent()).forEach((address, value) ->
                sb.append(address).append(" --> ").append(value).append('\n'));
        return sb.toString();

    }
    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n"
                + "SymTable:\n" + symTable
                + "Out: " + out + "\n"
                + "FileTable: " + fileTable.toMap().keySet() + "\n"
                + "Heap: " + new TreeMap<>(heap.getContent()) + "\n";
    }
}
