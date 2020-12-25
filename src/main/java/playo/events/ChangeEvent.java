package playo.events;

public class ChangeEvent<T> extends Event<ChangeListener<T>> {
    public void invoke(T data) {
        for (var l : listeners) l.onChange(data);
    }
}
