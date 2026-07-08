package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDamageByEntityRegister;

public class CustomEntityDamageByEntityEvent {
    public CustomEntityDamageByEntityEvent() {
    }

    public static void Event(CustomEntityDamageByEntityRegister event) {
        // This event is only called for non-player entities
    }
}
