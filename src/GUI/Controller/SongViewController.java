package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;

public class SongViewController extends BaseController {

    @FXML
    private TextField songTitleTxt;
    @FXML
    private TextField artistTxt;
    @FXML
    private TextField fileTxt;

    @Override
    public void setup() {

    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {

        String title = songTitleTxt.getText();
        String artist = artistTxt.getText();
        String pathToFile = fileTxt.getText();

        File file = new File(pathToFile);



    }
}
