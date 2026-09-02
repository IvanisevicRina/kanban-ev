package hr.tvz.kanban.controller;

import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSetupController {

    @FXML
    private TextField firstPlayerField, secondPlayerField, thirdPlayerField, fourthPlayerField;

    @FXML
    private Button startGameButton;

    @FXML
    private Label validationLabel;

    @FXML
    private void initialize() {
        validationLabel.setText("");
    }

    @FXML void startGame(){
        List<String> playerNames = collectPlayerNames();
        if (!arePlayerNamesValid(playerNames)){
            return;
        }
        GameState gameState = createGamestate(playerNames);
        openMainView(gameState);
    }

    private void openMainView(GameState gameState) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/kanban/view/main-view.fxml"));
            Scene mainScene = new Scene(loader.load(), 800, 600);
            MainController mainController = loader.getController();
            mainController.setGameState(gameState);

            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(mainScene);
            stage.setTitle("Kanban EV");
            stage.show();
        }catch (IOException exception) {
            validationLabel.setText(
                    "Glavni ekran igre nije moguće otvoriti." + exception.getMessage()
            );
        }
    }

    private GameState createGamestate(List<String> playerNames) {
        GameState gameState = new GameState();
        for(int index =0; index < playerNames.size();index++){
            Player player = new Player("player-"+(index +1), playerNames.get(index));
            gameState.addPlayer(player);
        }
        return gameState;
    }

    private boolean arePlayerNamesValid(List<String> playerNames) {
        if (playerNames.size()<2) {
            validationLabel.setText("Potrebno je unjeti najmanje dva igrača!");
            return false;
        }

        long differentNames = playerNames.stream().map(String::toLowerCase).distinct().count();

        if(differentNames != playerNames.size()){
            validationLabel.setText("Imena igrača moraju bit različita");
            return false;
        }
        validationLabel.setText("");
        return true;

    }

    private List<String> collectPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        addNameIfEntered(playerNames,firstPlayerField.getText());
        addNameIfEntered(playerNames,secondPlayerField.getText());
        addNameIfEntered(playerNames,thirdPlayerField.getText());
        addNameIfEntered(playerNames,fourthPlayerField.getText());
        return playerNames;
    }

    private void addNameIfEntered(List<String> playerNames, String enteredName) {
        String name = enteredName.trim();
        if (!name.isEmpty()){
            playerNames.add(name);
        }

    }

}
