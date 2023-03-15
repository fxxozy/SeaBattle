package vsu.cs.ru;

import vsu.cs.ru.models.Game;
import vsu.cs.ru.models.Player;
import vsu.cs.ru.services.GameService;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Queue<Player> players = new LinkedList<>();
        players.add(new Player("Vasya"));
        players.add(new Player("Petya"));
        GameService gameService = new GameService();
        Game game = gameService.createGame(players);
        gameService.startGame(game);
    }
}
