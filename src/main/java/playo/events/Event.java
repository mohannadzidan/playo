package playo.events;

import java.util.ArrayList;

public class Event<T> {
    private final ArrayList<EventListener<T>> listeners = new ArrayList<>();

    public void populate(T data) {
        for (var l : listeners) l.onEvent(data);
    }

    public void addListener(EventListener<T> listener) {
        if (listener == null) throw new NullPointerException("Listener cannot be null!");
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(EventListener<T> listener) {
        listeners.remove(listener);
    }
}
