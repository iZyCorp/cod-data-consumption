package fr.izy.moon;

import io.github.izycorp.moonapi.events.EventHandler;
import io.github.izycorp.moonapi.events.Listener;
import io.github.izycorp.moonapi.events.ListenerPriority;
import io.github.izycorp.moonapi.events.components.PreRequestEvent;

public class MyListener extends Listener {

    @EventHandler(priority = ListenerPriority.NORMAL)
    public void onPreRequestLowPriority(PreRequestEvent event) {
        System.out.println(event.getGeneratedUrl());
    }
}