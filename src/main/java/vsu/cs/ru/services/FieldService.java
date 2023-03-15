package vsu.cs.ru.services;

import vsu.cs.ru.models.*;

import java.util.*;

public class FieldService {
    private final char AFLOAT_SHIP_CHAR = '█';
    private final char SEA_CHAR = '-';
    private final char MINE_CHAR = '*';
    private final char HIDE_CHAR = '@';
    private final int MAX_TRIES = 100;


    public Field createField(int sizeX, int sizeY) {
        Field field = new Field(sizeX, sizeY);
        Map<Integer, Map<Integer, Cell>> cells = field.getCells();
        ArrayList<Cell> availableCells = field.getAvailableCells();
        for (int y = 0; y < sizeY; y++) {
            cells.put(y, new HashMap<>());
            for (int x = 0; x < sizeX; x++) {
                Cell newCell = new Cell(x, y);
                availableCells.add(newCell);
                cells.get(y).put(x, newCell);
            }
        }

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Cell currCell = cells.get(y).get(x);
                Map<Direction, Cell> currNeighbors = currCell.getNeighbors();
                if (y == 0) {
                    currNeighbors.put(Direction.UP, null);
                } else {
                    currNeighbors.put(Direction.UP, cells.get(y - 1).get(x));
                }
                if (y == sizeY - 1) {
                    currNeighbors.put(Direction.DOWN, null);
                } else {
                    currNeighbors.put(Direction.DOWN, cells.get(y + 1).get(x));
                }
                currNeighbors.put(Direction.LEFT, cells.get(y).get(x - 1));
                currNeighbors.put(Direction.RIGHT, cells.get(y).get(x + 1));

            }
        }
        return field;
    }

    public Cell getRandomFieldCell(Field field) {
        Random rnd = new Random();
        ArrayList<Cell> availableCells = field.getAvailableCells();
        return availableCells.get(rnd.nextInt(availableCells.size()));
    }

    public ArrayList<Ship> placeShips(Field field, int maxDeck) {
        ArrayList<Ship> ships = new ArrayList<>();
        int maxShipNumber = 1;
        for (int decks = maxDeck; decks >= 1; decks--) {
            for (int currShipNumber = 0; currShipNumber < maxShipNumber; currShipNumber++) {
                Ship newShip = createNewShip(field, ships, decks);
                if (newShip != null) {
                    ships.add(newShip);
                } else {
                    return null;
                }
            }
            maxShipNumber++;
        }
        for (Ship ship : ships) {
            ArrayList<Cell> shipPos = ship.getLocation();
            for (Cell cell : shipPos) {
                field.getCells().get(cell.getY()).get(cell.getX()).setWaterObject(ship);
            }
        }
        return ships;
    }

    private Ship createNewShip(Field field, ArrayList<Ship> ships, int deckNb) {
        Random rnd = new Random();
        boolean isVertical = rnd.nextBoolean();
        ArrayList<Cell> occupiedCells = getLocations(ships);

        for (int counter = 0; counter < MAX_TRIES; counter++) {
            ArrayList<Cell> shipPosition = getShipPos(field, isVertical, deckNb, occupiedCells);
            if (shipPosition != null) {
                return new Ship(shipPosition);
            }
        }
        return null;
    }

    private ArrayList<Cell> getShipPos(Field field, boolean isVertical, int deckNumber, ArrayList<Cell> occupiedPos) {
        ArrayList<Cell> shipPos = new ArrayList<>();
        Cell startPos = getRandomFieldCell(field);

        if (occupiedPos.contains(startPos) || !isFreeAround(startPos, occupiedPos)) {
            return null;
        } else {
            shipPos.add(startPos);
            for (int currDeck = 0; currDeck < deckNumber - 1; currDeck++) {
                Cell newPos;
                if (isVertical) {
                    newPos = startPos.getNeighbors().get(Direction.DOWN);
                } else {
                    newPos = startPos.getNeighbors().get(Direction.RIGHT);
                }
                if (newPos != null && !occupiedPos.contains(newPos) && isFreeAround(newPos, occupiedPos)) {
                    shipPos.add(newPos);
                    startPos = newPos;
                } else {
                    return null;
                }
            }
        }
        return shipPos;
    }

    private ArrayList<Cell> getLocations(ArrayList<Ship> ships) {
        ArrayList<Cell> locations = new ArrayList<>();
        for (Ship ship : ships) {
            locations.addAll(ship.getLocation());
        }
        return locations;
    }

    private boolean isFreeAround(Cell currPos, ArrayList<Cell> occupiedPos) {
        Map<Direction, Cell> neighbors = currPos.getNeighbors();
        for (Direction direction : neighbors.keySet()) {
            Cell firstPartDir = neighbors.get(direction);
            if (firstPartDir != null) {
                if (occupiedPos.contains(firstPartDir)) {
                    return false;
                }
                if (occupiedPos.contains(firstPartDir.getNeighbors().get(direction.next()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Mine> placeMines(Field field, ArrayList<Ship> ships, int minesNb) {
        ArrayList<Mine> mines = new ArrayList<>();
        ArrayList<Cell> occupiedCells = getLocations(ships);
        for (int i = 0; i < minesNb; i++) {
            for (int counter = 0; counter < 100; counter++) {
                Cell pos = getRandomFieldCell(field);
                if (pos.getWaterObject() == null && isFreeAround(pos, occupiedCells)) {
                    ArrayList<Cell> minePos = new ArrayList<>(Collections.singletonList(pos));
                    occupiedCells.add(pos);
                    mines.add(new Mine(minePos));
                    break;
                }
            }
        }
        for (Mine mine : mines) {
            ArrayList<Cell> minePos = mine.getLocation();
            for (Cell cell : minePos) {
                field.getCells().get(cell.getY()).get(cell.getX()).setWaterObject(mine);
            }
        }
        return mines;
    }

    public void removeAround(Field field, WaterObject waterObject) {
        ArrayList<Cell> availableCells = field.getAvailableCells();
        for (Cell cell : waterObject.getLocation()) {
            Map<Direction, Cell> neighbors = cell.getNeighbors();
            for (Direction direction : neighbors.keySet()) {
                Cell firstPartDir = neighbors.get(direction);
                if (firstPartDir != null) {
                    availableCells.remove(firstPartDir);
                    Cell nextCell = firstPartDir.getNeighbors().get(direction.next());
                    if (nextCell != null) {
                        availableCells.remove(nextCell);
                    }
                }
            }
        }
    }

    public void printField(Field field, boolean hide) {
        Map<Integer, Map<Integer, Cell>> cells = field.getCells();
        System.out.print("   ");
        for (char ch = 'A'; ch < 'A' + field.getSizeX(); ch++) {
            System.out.printf("%c ", ch);
        }
        System.out.println();
        for (int y = 0; y < field.getSizeY(); y++) {
            System.out.printf("%2d ", y + 1);
            for (int x = 0; x < field.getSizeX(); x++) {
                Cell currCell = cells.get(y).get(x);
                if (field.getAvailableCells().contains(currCell) && hide) {
                    System.out.printf("%c ", HIDE_CHAR);
                    continue;
                }
                WaterObject currWaterObj = currCell.getWaterObject();

                if (currWaterObj instanceof Ship) {
                    System.out.printf("%c ", AFLOAT_SHIP_CHAR);
                } else if (currWaterObj instanceof Mine) {
                    System.out.printf("%c ", MINE_CHAR);
                } else {
                    System.out.printf("%c ", SEA_CHAR);
                }
            }
            System.out.println();
        }
    }
}
