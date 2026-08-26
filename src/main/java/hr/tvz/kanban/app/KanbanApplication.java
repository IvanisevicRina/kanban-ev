package hr.tvz.kanban.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class KanbanApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Label titleLabel = new Label("KanbanEV");
        StackPane root = new StackPane(titleLabel);
        Scene scene = new Scene(root,800,600);
        stage.setTitle("Kanban EV");
        stage.setScene(scene);
        stage.show();

    }

}
