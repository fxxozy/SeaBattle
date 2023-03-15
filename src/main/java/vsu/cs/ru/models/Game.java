package vsu.cs.ru.models;

import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

public class Game {
    private Map<Player, Field> playerFields;
    private Map<Player, ArrayList<Ship>> playerShips;
    private Map<Player, ArrayList<Mine>> playerMines;
    private Queue<Player> players;

    public Game(Queue<Player> players) {
        this.players = players;
    }

    public void setPlayerFields(Map<Player, Field> playerFields) {
        this.playerFields = playerFields;
    }

    public void setPlayerShips(Map<Player, ArrayList<Ship>> playerShips) {
        this.playerShips = playerShips;
    }

    public void setPlayerMines(Map<Player, ArrayList<Mine>> playerMines) {
        this.playerMines = playerMines;
    }

    public Queue<Player> getPlayers() {
        return players;
    }

    public Map<Player, Field> getPlayerFields() {
        return playerFields;
    }

    public Map<Player, ArrayList<Ship>> getPlayerShips() {
        return playerShips;
    }

    public Map<Player, ArrayList<Mine>> getPlayerMines() {
        return playerMines;
    }
}

