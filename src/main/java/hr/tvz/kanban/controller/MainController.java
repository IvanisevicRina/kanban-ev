package hr.tvz.kanban.controller;
import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.model.*;
import hr.tvz.kanban.replay.ReplaySummary;
import hr.tvz.kanban.service.LocalGameFileService;
import hr.tvz.kanban.thread.ReplayLoadThread;
import hr.tvz.kanban.ui.ReplayWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.nio.file.Path;
import java.util.List;

public class MainController {

    private final LocalGameFileService fileService = new LocalGameFileService();

    @FXML
    private Label weekLabel, playersLabel, currentPlayerLabel, playerStatsLabel,actionResultLabel, sandraMessageLabel, gameResultLabel;

    @FXML
    private Button testingButton,logisticsButton,assemblyButton, designButton;

    @FXML
    private ComboBox<CarModel> carModelComboBox;

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

        if (sandraMessage.isBlank()){
            sandraMessageLabel.setText("Sandra još nije izvršila tjednu evaluaciju");
        } else {
            sandraMessageLabel.setText("Sandra: "+sandraMessage);
        }

        if (gameState.isGameFinished()){
            showGameResult();
            setGameButtonsDisabled(true);
        } else{
            gameResultLabel.setText("");
            setGameButtonsDisabled(false);
        }
    }


    private void setGameButtonsDisabled(boolean disabled) {

        designButton.setDisable(disabled);
        logisticsButton.setDisable(disabled);
        assemblyButton.setDisable(disabled);
        testingButton.setDisable(disabled);
        carModelComboBox.setDisable(disabled);
    }

    private void showGameResult() {
        int highestScore = gameState.getPlayers().stream().mapToInt(Player::getScore).max().orElse(0);
        String winnerNames = gameState.getPlayers()
                .stream()
                .filter(player -> player.getScore() == highestScore)
                .map(Player::getName)
                .reduce((firstame, secondName)->firstame + ", "+ secondName)
                .orElse("Nema pobjednika");

        long numberOfWinners=gameState.getPlayers()
                .stream()
                .filter(player -> player.getScore()==highestScore)
                .count();

        if (numberOfWinners==1){
            gameResultLabel.setText("Pobjednik je " + winnerNames + " sa " + highestScore+ " bodova");
        }else {
            gameResultLabel.setText("Igra je završila izjednačeno. Pobjedničko mjesto dijele : " + winnerNames +" sa osvojenih "+highestScore);
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
        CarModel selectedCar = carModelComboBox.getValue();

        if(carModelComboBox.getSelectionModel().isEmpty()){
            actionResultLabel.setText("Prije montaže odaberi model auta");
            return;
        }
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.setSelectedCarModel(selectedCar);

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
        carModelComboBox.getSelectionModel().clearSelection();
    }
    private Player getCurrentPlayer() {
        return gameState.getPlayers().get(currentPlayerIndex);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.kanbanEngine = new KanbanEngine(gameState);
        this.currentPlayerIndex=0;
        carModelComboBox.getItems().setAll(CarModel.values());
        carModelComboBox.setValue(CarModel.CITY);
        actionResultLabel.setText("");
        refreshView();
    }
    @FXML
    private void handleSaveGame(){
        try {
            Path saveFile = fileService.saveGame((kanbanEngine));
            actionResultLabel.setText("Igra je spremljena u datoteku: " +saveFile);

        } catch (IllegalStateException exception){
            actionResultLabel.setText("Spremanje nije uspilo: "+ exception.getMessage()  );
        }
    }

    @FXML
    private void handleLoadGame(){
        actionResultLabel.setText("Učitavanje replaya...");
        Runnable replayTask = new ReplayLoadThread(fileService,actionResultLabel);
        Thread replayThread = new Thread(replayTask, "replay-loader");
        replayThread.setDaemon(true);
        replayThread.start();
    }

    @FXML
    private void handleExportReplay(){
        try{
            Path replayFile = fileService.exportReplay(kanbanEngine);
            actionResultLabel.setText("Replay je spremljen u xml: " + replayFile);

        } catch (IllegalStateException e){
            actionResultLabel.setText("Izvoz replaya neuspješan: " + e.getMessage());
        }
    }
    @FXML
    private void handleLoadReplay(){
        try{
            List<WeekAction> actions = fileService.loadReplay();
            ReplaySummary summary = fileService.loadReplaySummary();

            new ReplayWindow().show(actions);

            actionResultLabel.setText("Replay je otvoren. Poteza: " + summary.actionCount()+ ", igrača: " + summary.playerCount() + ", zadnji tjedan: " + summary.lastWeekNumber());

        } catch (IllegalStateException e){
            actionResultLabel.setText("Pokretanje replay nije uspio:" + e.getMessage());
        }
    }
}