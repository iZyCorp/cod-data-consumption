package fr.izy.moon;


import io.github.izycorp.codapi.events.EventHandler;
import io.github.izycorp.codapi.events.Listener;
import io.github.izycorp.codapi.events.ListenerPriority;
import io.github.izycorp.codapi.events.components.ErrorInRequestEvent;
import io.github.izycorp.codapi.events.components.PreRequestEvent;

public class MyListener extends Listener {

    @EventHandler(priority = ListenerPriority.NORMAL)
    public void onPreRequestLowPriority(PreRequestEvent event) {
        System.out.println(event.getGeneratedUrl());
    }

    @EventHandler(priority = ListenerPriority.HIGH)
    public void onPreRequestHighPriority(ErrorInRequestEvent event) {
        event.getCatchedException().printStackTrace();
    }
}