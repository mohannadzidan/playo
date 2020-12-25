package playo.events;

public class KeyedChange<T> extends Change<T> {
    private final String key;
    public KeyedChange(String key, T oldValue, T newValue) {
        super(oldValue, newValue);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
