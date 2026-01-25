package org.gsdistance.grimmsServer.Constructable.CInventory;

import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.CustomItem;

import java.util.List;

public record SecondaryInventory(List<Data<CustomItem, Integer>> items, List<Data<Card, Integer>> cards) {
}
