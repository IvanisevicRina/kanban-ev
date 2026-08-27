package hr.tvz.kanban.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KanbanApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(KanbanApplication.class.getResource("/hr/tvz/kanban/view/player-setup-view.fxml"));


        Scene scene = new Scene(loader.load(),800,600);
        stage.setTitle("Kanban EV");
        stage.setScene(scene);
        stage.show();

    }

}
