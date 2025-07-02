package org.gsdistance.grimmsServer.Constructable.CInventory;

import java.util.UUID;

public class Card {
    public final String name;
    public final String description;
    public final String id;
    public final UUID uuid = UUID.randomUUID();
    public final String type;
    public final String rarity;
    public final byte grade;

    public Card(String name, String description, String id, String type, String rarity, byte grade) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.grade = grade;
    }
}
