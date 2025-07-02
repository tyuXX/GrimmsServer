package org.gsdistance.grimmsServer.Constructable.CInventory;

import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.CustomItem;

import java.util.List;

public class SecondaryInventory {
    public final List<Data<CustomItem, Integer>> items;
    public final List<Data<Card, Integer>> cards;

    public SecondaryInventory(List<Data<CustomItem, Integer>> items, List<Data<Card, Integer>> cards) {
        this.items = items;
        this.cards = cards;
    }
}
