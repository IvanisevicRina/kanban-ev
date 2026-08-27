package hr.tvz.kanban.controller;

import hr.tvz.kanban.engine.Department;
import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.model.ActionResult;
import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label weekLabel;

    @FXML Label playersLabel;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Label playerStatsLabel;

    @FXML
    private Label actionResultLabel;

    @FXML
    private Label sandraMessageLabel;

    @FXML
    private Button designButton;

    @FXML
    private Button logisticsButton;

    @FXML
    private Button assemblyButton;

    @FXML
    private Button testingButton;

    private GameState gameState;

    private KanbanEngine kanbanEngine;

    private int currentPlayerIndex;



    private void refreshView() {

        weekLabel.setText("Trenutačni tjedan: "+ gameState.getCurrentWeek());
        String playerNames = gameState.getPlayers().stream().map(Player::getName).reduce((firstName, secondName)-> firstName + ", "+ secondName).orElse("Nema igrača");
        playersLabel.setText("Igrači: " + playerNames);

        if(gameState.getPlayers().isEmpty()){
            currentPlayerLabel.setText("Nema igrača");
            playerStatsLabel.setText("");
            return;
        }
        Player currentPlayer = getCurrentPlayer();

        currentPlayerLabel.setText(currentPlayer.getName());

        playerStatsLabel.setText("Bodovi: " + currentPlayer.getScore() + " Dizajn: " + currentPlayer.getDesignPoints() + " Komponente: "+ currentPlayer.getComponents()+ " Automobili: " + currentPlayer.getCars().size());

        String sandraMessage = gameState.getLastSandraMessage();

        if (sandraMessage==null || sandraMessage.isBlank()){
            sandraMessageLabel.setText("Sandra još nije izvršila tjednu evaluaciju");
        } else {
            sandraMessageLabel.setText("Sandra: "+sandraMessage);
        }


    }

    @FXML
    private void selectDesign() {
        performDepartmentAction(DepartmentType.DESIGN);
    }

    @FXML
    private void selectLogistics() {
        performDepartmentAction(DepartmentType.LOGISTICS);
    }

    @FXML
    private void selectAssembly() {
        performDepartmentAction(DepartmentType.ASSEMBLY);
    }

    @FXML
    private void selectTesting() {
        performDepartmentAction(DepartmentType.TESTING);
    }


    private void performDepartmentAction(DepartmentType departmentType){

        Player currentPlayer = getCurrentPlayer();
        ActionResult result = kanbanEngine.performAction(currentPlayer.getId(),departmentType);
        actionResultLabel.setText(result.message());
        if(result.successful()){
            moveToNextPlayer();
        }
        refreshView();

    }

    private void moveToNextPlayer() {
        currentPlayerIndex++;
        if(currentPlayerIndex >=gameState.getPlayers().size()){
            currentPlayerIndex=0;
        }
    }

    private Player getCurrentPlayer() {
        return gameState.getPlayers().get(currentPlayerIndex);
    }


    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.kanbanEngine = new KanbanEngine(gameState);
        this.currentPlayerIndex=0;

        actionResultLabel.setText("");
        refreshView();

    }




}
