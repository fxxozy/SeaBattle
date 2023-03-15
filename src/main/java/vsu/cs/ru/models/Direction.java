package vsu.cs.ru.models;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public Direction next() {
        switch (this) {
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            default:
                return UP;
        }
    }
}
