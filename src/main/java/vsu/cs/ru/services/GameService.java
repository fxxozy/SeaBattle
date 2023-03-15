package vsu.cs.ru.services;

import vsu.cs.ru.models.*;

import java.util.*;

public class GameService {
    private final int SIZE_X = 10;
    private final int SIZE_Y = 10;
    private final int MAX_DECKS = 4;
    private final int MAX_MINES = 3;

    private final FieldService fieldService = new FieldService();

    public Game createGame(Queue<Player> players) {
        Game game = new Game(players);
        Map<Player, Field> playerFields = createPlayerFields(players);
        Map<Player, ArrayList<Ship>> playerShips = createPlayerShips(playerFields);
        Map<Player, ArrayList<Mine>> playerMines = createPlayerMines(playerFields, playerShips);
        game.setPlayerFields(playerFields);
        game.setPlayerShips(playerShips);
        game.setPlayerMines(playerMines);
        return game;
    }

    private Map<Player, Field> createPlayerFields(Queue<Player> players) {
        Map<Player, Field> playerFields = new HashMap<>();
        for (Player player: players) {
            playerFields.put(player, fieldService.createField(SIZE_X, SIZE_Y));
        }
        return playerFields;
    }

    private Map<Player, ArrayList<Ship>> createPlayerShips(Map<Player, Field> playerFields) {
        Map<Player, ArrayList<Ship>> playerShips = new HashMap<>();
        for (Player player: playerFields.keySet()) {
            Field currentField = playerFields.get(player);
            ArrayList<Ship> ships = fieldService.placeShips(currentField, MAX_DECKS);
            playerShips.put(player, ships);
        }
        return playerShips;
    }

    private Map<Player, ArrayList<Mine>> createPlayerMines(Map<Player, Field> playerFields,
                                                           Map<Player, ArrayList<Ship>> playerShips) {
        Map<Player, ArrayList<Mine>> playerMines = new HashMap<>();
        for (Player player: playerFields.keySet()) {
            Field currField = playerFields.get(player);
            ArrayList<Ship> currShips = playerShips.get(player);
            ArrayList<Mine> mines = fieldService.placeMines(currField, currShips, MAX_MINES);
            playerMines.put(player, mines);
        }
        return playerMines;
    }

    public void startGame(Game game) {
        Scanner scan = new Scanner(System.in);
        Queue<Player> players = game.getPlayers();
        Map<Player, Field> playerFields = game.getPlayerFields();
        int counter = 1;
        boolean isWin = false;
        boolean isRepeat = true;
        Player currentPlayer = players.poll();
        while (!isWin) {
            if (!isRepeat) {
                currentPlayer = players.poll();
            }
            System.out.printf("Ход номер %d игрока %s\n============================\n", counter, currentPlayer.getName());
            Field currentField = playerFields.get(currentPlayer);
            printPlayerField(currentPlayer, currentField);
            Player nextPlayer = players.peek();

            isRepeat = turn(currentPlayer, nextPlayer, game);

            scan.nextLine();
            if (!isRepeat) {
                players.add(currentPlayer);
            }
            System.out.printf("Конец хода номер %d\n============================\n", counter);
            counter++;
            Player playerWon = checkWin(game, currentPlayer, nextPlayer);
            isWin = playerWon != null;
        }
        players.add(currentPlayer);
        System.out.println("___ИТОГОВЫЕ ПОЛЯ___");
        for (Player player: players) {
            printPlayerField(player, playerFields.get(player));
        }
    }

    private void printPlayerField(Player player, Field field) {
        System.out.printf("Поле игрока %s\n", player.getName());
        fieldService.printField(field, true);
        System.out.println("-----------------------");
    }

    private boolean turn(Player player, Player nextPlayer, Game game) {
        ArrayList<Ship> ownerShips = game.getPlayerShips().get(player);
        Field ownerField = game.getPlayerFields().get(player);

        Field firedField = game.getPlayerFields().get(nextPlayer);
        ArrayList<Ship> enemyShips = game.getPlayerShips().get(nextPlayer);

        printPlayerField(nextPlayer, firedField);

        Cell firedCell = fieldService.getRandomFieldCell(firedField);
        System.out.printf("Бомбим поле игрока %s по координатам %d%c\n", nextPlayer.getName(), firedCell.getY() + 1, firedCell.getX() + 'A');
        firedField.getAvailableCells().remove(firedCell);
        printWaterObjectType(firedCell);
        boolean isRepeat = fire(ownerField, ownerShips, enemyShips, firedField, firedCell);
        return isRepeat;
    }

    private boolean fire(Field ownerField, ArrayList<Ship> ownerShips, ArrayList<Ship> enemyShips, Field firedField, Cell firedCell) {
        WaterObject waterObject = firedCell.getWaterObject();
        if (waterObject == null) {
            return false;
        }
        waterObject.getDamage();
        if (waterObject instanceof Ship) {
            if (!waterObject.isAfloat()) {
                enemyShips.remove(waterObject);
                fieldService.removeAround(firedField, waterObject);
            }
            return true;
        }
        else if (waterObject instanceof Mine) {
            fieldService.removeAround(firedField, waterObject);
            Cell ownerFiredCell = ownerField.getCells().get(firedCell.getY()).get(firedCell.getX());

            if (ownerField.getAvailableCells().contains(ownerFiredCell)) {
                ownerField.getAvailableCells().remove(ownerFiredCell);
            }

            fire(firedField, enemyShips, ownerShips, ownerField, ownerFiredCell);
        }
        return false;
    }

    private Player checkWin(Game game, Player player, Player nextPlayer) {
        ArrayList<Ship> enemyAvailableShips = game.getPlayerShips().get(nextPlayer);
        Player playerWon = null;
        if (enemyAvailableShips.size() == 0) {
            System.out.printf("Поздравляем игрока %s с победой! ٩(̾●̮̮̃̾•̃̾)۶٩(̾●̮̮̃̾•̃̾)۶٩(̾●̮̮̃̾•̃̾)۶\n", player.getName());
            playerWon = player;
        }
        return playerWon;
    }

    private void printWaterObjectType(Cell cell) {
        WaterObject waterObject = cell.getWaterObject();
        String waterObjectName;
        if (waterObject == null) {
            waterObjectName = "воду";
        } else {
            waterObjectName = waterObject.getName();
        }
        System.out.printf("Попадание в %s\n", waterObjectName);
    }
}
