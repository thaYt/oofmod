package me.thayt.oofmod.managers;

import me.thayt.oofmod.utils.Event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final List<Event> events = new ArrayList<>();

    public EventManager() {
        reset();
    }

    public void register(Event event) {
        event.register();
        events.add(event);
    }

    public void unregister(Event event) {
        event.unregister();
        events.remove(event);
    }

    public void reset() {
        events.forEach(this::unregister);
        events.clear();
    }

}
