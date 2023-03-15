package vsu.cs.ru.models;

import java.util.ArrayList;

public class Mine extends WaterObject {
    public Mine(ArrayList<Cell> cells) {
        super(cells);
    }

    public String getName() {
        return "Мина";
    }
}

