package hr.tvz.kanban.ui;

import hr.tvz.kanban.controller.ReplayController;
import hr.tvz.kanban.model.WeekAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ReplayWindow {

    public void show(List<WeekAction> actions){
        Objects.requireNonNull(actions,"Lista poteza ne smije biti prazna");
        try{
            FXMLLoader loader = new FXMLLoader(ReplayWindow.class.getResource("/hr/tvz/kanban/view/replay.fxml"));
            Parent root = loader.load();
            ReplayController controller = loader.getController();
            controller.setActions(actions);
            Scene scene = new Scene(root, 600, 350);
            Stage replayStage = new Stage();
            replayStage.setTitle("Kanban EV - XML replay");
            replayStage.setScene(scene);
            replayStage.setOnHidden(event -> controller.stopReplay());
            replayStage.show();
        } catch (IOException e){
            throw new IllegalStateException("Nije moguće otvoriti replay prozor", e);
        }



    }


}
