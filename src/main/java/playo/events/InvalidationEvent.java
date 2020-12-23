package playo.events;

public class InvalidationEvent extends Event<InvalidationListener> {
    public void invoke(){
        for(var l : listeners)l.onInvalidate();
    }
}
