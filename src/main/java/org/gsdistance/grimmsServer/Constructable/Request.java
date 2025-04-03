package org.gsdistance.grimmsServer.Constructable;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

public class Request {
    private UUID uuid;
    private Function<?, ?> onAccept;
    private LocalDateTime timestamp;

    public Request(Function<?, ?> onAccept) {
        uuid = UUID.randomUUID();
        this.onAccept = onAccept;
        this.timestamp = LocalDateTime.now();
    }
}
