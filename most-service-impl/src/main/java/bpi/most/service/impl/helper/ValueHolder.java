package bpi.most.service.impl.helper;

/**
 * A helper class that holds a value of a specific type (per reference).
 *
 * @author Jakob Korherr
 */
public class ValueHolder<T> {

    private T value;

    public ValueHolder() {
    }

    public ValueHolder(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
