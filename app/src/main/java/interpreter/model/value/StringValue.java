package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.StringType;

public record StringValue(String val) implements IValue {
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "string";
    }
}
