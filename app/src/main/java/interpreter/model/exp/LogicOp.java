package interpreter.model.exp;

public enum LogicOp {
    OR("||"), AND("&&");
    private final String symbol;

    LogicOp(String symbol) {
        this.symbol = symbol;
    }
    @Override public String toString() { return symbol; }
}
