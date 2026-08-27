package hr.tvz.kanban.controller;
import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainController {

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

        if (sandraMessage==null || sandraMessage.isBlank()){
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

        if(selectedCar==null){
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
        carModelComboBox.setValue(null);
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
}
