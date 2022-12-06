package GUI.Controller;

import BE.Song;
import GUI.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SongViewController extends BaseController {
    @FXML
    private Button chooseFileBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField artistTxt;
    @FXML
    private TextField songTitleTxt;
    @FXML
    private TextField fileTxt;

    private SongModel model;

    @Override
    public void setup() {

        model = getModel().getSongModel();
        //TODO Never used the .getSelectedSong()
        if (model.shouldEditSong() == true) {

            if (model.shouldEditSong() == true) {
                edit();
            } else {
                createNew();
            }
        }
    }

        private void edit ()
        {
            songTitleTxt.setText(model.getSelectedSong().getTitle());
            artistTxt.setText(model.getSelectedSong().getArtist());
            fileTxt.setText(model.getSelectedSong().getFilePath());
        }

        private void createNew ()
        {
            songTitleTxt.clear();
            artistTxt.clear();
            fileTxt.clear();
        }


        //Handles the Save button in the new song window.
        @FXML
        public void handleSave (ActionEvent actionEvent) throws Exception {
            if (model.shouldEditSong() == false) {
                String title = songTitleTxt.getText();
                String artist = artistTxt.getText();
                String pathToFile = fileTxt.getText();

                //Takes the duration of the file given, and maps it to an int in seconds,
                //will be converted to the correct visual value later.
                File file = new File(pathToFile);
                AudioFile af = AudioFileIO.getDefaultAudioFileIO().readFile(file);
                int length = af.getAudioHeader().getTrackLength();

                //Sends the info to the model layer.
                model.createSong(title, artist, String.valueOf(length), "TEST", pathToFile);

                //Closes window
                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
            } else {
                String title = songTitleTxt.getText();
                String artist = artistTxt.getText();
                String pathToFile = fileTxt.getText();

                //Updates the selected song
                model.getSelectedSong().setArtist(artist);
                model.getSelectedSong().setTitle(title);
                model.getSelectedSong().setFilePath(pathToFile);
                model.songUpdate(model.getSelectedSong());

                // Closes the window
                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
            }
        }

        @FXML
        private void handleCancel (ActionEvent actionEvent){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }

        //choose a new file, without the user having to copy the filepath.
        public void chooseFile (ActionEvent actionEvent){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select song");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Files", "*"));
            Stage stage = (Stage) chooseFileBtn.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                fileTxt.setText(String.valueOf(selectedFile));
            }
        }
}
