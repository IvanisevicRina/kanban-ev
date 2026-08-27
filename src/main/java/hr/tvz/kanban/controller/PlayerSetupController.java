package hr.tvz.kanban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayerSetupController {

    @FXML
    private TextField firstPlayerField;

    @FXML
    private TextField secondPlayerField;

    @FXML
    private TextField thirdPlayerField;

    @FXML
    private TextField fourthPlayerField;

    @FXML
    private Button startGameButton;

    @FXML
    private Label validationLabel;

    @FXML
    private void initialize() {
        validationLabel.setText("");
    }

}
