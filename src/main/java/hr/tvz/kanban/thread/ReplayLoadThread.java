package hr.tvz.kanban.thread;

import hr.tvz.kanban.model.WeekAction;
import hr.tvz.kanban.replay.ReplaySummary;
import hr.tvz.kanban.service.LocalGameFileService;
import hr.tvz.kanban.ui.ReplayWindow;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.List;

public class ReplayLoadThread implements Runnable{

    private final LocalGameFileService fileService;
    private final Label actionResultLabel;


    public ReplayLoadThread(LocalGameFileService fileService, Label actionResultLabel) {
        this.fileService = fileService;
        this.actionResultLabel = actionResultLabel;
    }

    @Override
    public void run() {
        try{
            List<WeekAction> actions = fileService.loadReplay();
            ReplaySummary summary = fileService.loadReplaySummary();
            Platform.runLater(()-> showReplay(actions,summary));
        } catch (IllegalStateException e) {
            Platform.runLater(()->showError(e));
        }

    }

    private void showError(IllegalStateException e) {
        actionResultLabel.setText("Pokretanje replaya nije uspjelo: " + e.getMessage());
    }

    private void showReplay(List<WeekAction> actions, ReplaySummary summary) {

        try{
            new ReplayWindow().show(actions);
            actionResultLabel.setText("Replay je otvoren. Poteza: "+ summary.actionCount() + ", igrača: " + summary.playerCount()+ ", zadnji tjedan: "+ summary.lastWeekNumber());
        } catch (IllegalStateException e) {
            showError(e);
        }


    }
}
