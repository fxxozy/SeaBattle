package vsu.cs.ru.models;

import java.util.ArrayList;

public class Ship extends WaterObject {

    public Ship(ArrayList<Cell> cells) {
        super(cells);
    }

    public String getName() {
        return "Корабль";
    }
}
