package playo.events;

import java.util.ArrayList;

/**
 * @param <T> the event handler
 */
public abstract class Event<T extends EventListener> {
    protected final ArrayList<T> listeners = new ArrayList<>();

    public void addListener(T listener) {
        if (listener == null) throw new NullPointerException("Listener cannot be null!");
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }
}
