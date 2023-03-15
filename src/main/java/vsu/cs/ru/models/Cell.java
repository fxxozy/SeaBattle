package vsu.cs.ru.models;

import java.util.HashMap;
import java.util.Map;

public class Cell {
    private final int x;
    private final int y;
    private WaterObject waterObject;
    private Map<Direction, Cell> neighbors = new HashMap<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<Direction, Cell> getNeighbors() {
        return neighbors;
    }

    public void setWaterObject(WaterObject waterObject) {
        this.waterObject = waterObject;
    }

    public WaterObject getWaterObject() {
        return waterObject;
    }
}
