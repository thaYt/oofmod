package me.thayt.oofmod.utils;

import net.weavemc.loader.api.event.EventBus;

public class Event {
    public void register() {
        EventBus.subscribe(this);
    }

    public void unregister() {
        EventBus.unsubscribe(this);
    }
}
