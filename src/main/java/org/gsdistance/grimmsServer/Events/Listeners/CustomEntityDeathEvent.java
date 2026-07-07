package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDeathRegister;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

public class CustomEntityDeathEvent {
    public CustomEntityDeathEvent() {
    }

    public static void Event(CustomEntityDeathRegister event) {
        CustomEntityManager.currentRegistry.remove(event.getEntity().getUniqueId());
    }
}
