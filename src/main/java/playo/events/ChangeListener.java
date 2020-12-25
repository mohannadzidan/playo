package playo.events;

public interface ChangeListener<T> extends EventListener {
    void onChange(T data);
}
