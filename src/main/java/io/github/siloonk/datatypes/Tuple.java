package io.github.siloonk.datatypes;

public class Tuple<T, R> {

    public final T key;
    public final R value;

    public Tuple(final T key, final R value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public R getValue() {
        return value;
    }
}
