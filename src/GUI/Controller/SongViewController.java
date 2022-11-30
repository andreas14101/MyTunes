package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SongViewController extends BaseController {
    @FXML
    private TextField artistTxt;

    @FXML
    private TextField songTitleTxt;
    @FXML
    private TextField fileTxt;


    @Override
    public void setup() {
    }

    @FXML
    private void handleSaveLSH(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException {

        String title = songTitleTxt.getText();
        String artist = artistTxt.getText();
        String pathToFile = fileTxt.getText();

        File file = new File(pathToFile);

        AudioFileFormat baseFileFormat = null;
        baseFileFormat = AudioSystem.getAudioFileFormat(file);
        int length = baseFileFormat.getFrameLength();
        //TODO finish this create song method

    }
    
}
