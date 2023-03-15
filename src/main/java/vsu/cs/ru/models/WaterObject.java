package vsu.cs.ru.models;

import java.util.ArrayList;

public abstract class WaterObject {
    protected ArrayList<Cell> location;
     int health;

    public WaterObject(ArrayList<Cell> cells) {
        this.location = cells;
        this.health = cells.size();
    }

    public ArrayList<Cell> getLocation() {
        return location;
    }

    public void getDamage() {
        health--;
    }

    public boolean isAfloat() {
        return health > 0;
    }

    public String getName() {
        return "Водный объект";
    }
}
