package vsu.cs.ru.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Field {
    private ArrayList<Cell> availableCells;
    private Map<Integer, Map<Integer, Cell>> cells;
    private int sizeX;
    private int sizeY;

    public Field(int sizeX, int sizeY) {
        this.availableCells = new ArrayList<>();
        this.cells = new HashMap<>();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Map<Integer, Map<Integer, Cell>> getCells() {
        return cells;
    }

    public ArrayList<Cell> getAvailableCells() {
        return availableCells;
    }
}
