package GUI;

import GUI.Controller.MainViewController;
import GUI.Model.MyTunesModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View/MainView.fxml"));
        Parent root = loader.load();

        MainViewController controller = loader.getController();
        controller.setModel(new MyTunesModel());
        controller.setup();

        primaryStage.setTitle("MRS2022");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
