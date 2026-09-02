package hr.tvz.kanban.controller;

import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.WeekAction;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class ReplayController {

    @FXML
    private Label actionLabel;

    @FXML
    private Label progressLabel;

    private List<WeekAction> replayActions = List.of();

    private Timeline timeline = new Timeline();

    private int currentActionIndex;

    public void setActions(List<WeekAction> actions){
        replayActions = List.copyOf(Objects.requireNonNull(actions));
        createTimeLine();
        resetReplay();
    }

    private void resetReplay() {
        stopReplay();
        currentActionIndex=0;
        actionLabel.setText("Klikni Pokreni za početak replaya.");
        progressLabel.setText("0/" + replayActions.size());

    }

    private void createTimeLine() {

        timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> showNextAction()));
        timeline.setCycleCount(Animation.INDEFINITE);

    }


    @FXML
    private void handleStart(){
        timeline.play();

    }

    @FXML
    private void handlePause(){
        timeline.pause();
    }

    @FXML
    private void handleReset(){
        resetReplay();
    }

    public void stopReplay(){
        timeline.stop();
    }

    private void showNextAction() {

        if(currentActionIndex >= replayActions.size()){
            finishReplay();
            return;
        }
        WeekAction action = replayActions.get(currentActionIndex);
        actionLabel.setText(formatAction(action));
        progressLabel.setText((currentActionIndex+1) +" / " + replayActions.size());
        currentActionIndex=currentActionIndex+1;

    }

    private String formatAction(WeekAction action) {
        return "Radni tjedan: " + action.weekNumber() + "\nIgrač: "+action.playerId() + "\nOdjel: " + formatDepartment(action.departmentType()) + "\n\n" + action.description();


    }

    private String formatDepartment(DepartmentType departmentType) {
        return switch (departmentType){
            case DESIGN -> "Dizajn";
            case LOGISTICS -> "Logistika";
            case ASSEMBLY -> "Montaža";
            case TESTING -> "Testiranje";
        };
    }

    private void finishReplay() {

        stopReplay();
        actionLabel.setText("Replay je završen");
        progressLabel.setText(replayActions.size() + " / " + replayActions.size());

    }


}
