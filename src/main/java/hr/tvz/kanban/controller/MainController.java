package hr.tvz.kanban.controller;

import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label weekLabel;

    @FXML Label playersLabel;

    private GameState gameState;

    private KanbanEngine kanbanEngine;

    @FXML
    private void initialize(){
        initializeGame();
        refreshView();
    }

    private void refreshView() {

        weekLabel.setText("Trenutačni tjedan: "+ gameState.getCurrentWeek());
        String playerNames = gameState.getPlayers().stream().map(Player::getName).reduce((firstName, secondName)-> firstName + ", "+ secondName).orElse("Nema igrača");
        playersLabel.setText("Igrači: " + playerNames);


    }

    private void initializeGame() {
        gameState = new GameState();

        Player firstPlayer = new Player("player - 1", "Rina");
        Player secondPlayer = new Player("player-2", "Frane");

        gameState.addPlayer(firstPlayer);
        gameState.addPlayer(secondPlayer);


    }


}
