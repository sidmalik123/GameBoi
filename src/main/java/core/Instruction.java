package core;

public abstract class Instruction<T> {

    private T value;

    public Instruction(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
