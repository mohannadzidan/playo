package playo.events;

public interface EventListener<T> {
    void onEvent(T data);
}
