package GUI.Controller;

import BE.Category;
import GUI.Model.SongModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

public class SongViewController extends BaseController {
    @FXML
    private ComboBox categoryCB;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField artistTxt;
    private ObservableList<Category> test;
    @FXML
    private TextField songTitleTxt;
    @FXML
    private TextField fileTxt;

    private SongModel model;

    @Override
    public void setup() {

        model = getModel().getSongModel();
        setCategories();

        if (model.shouldEditSong() == true) {
            songTitleTxt.setText(model.getSelectedSong().getTitle());
            artistTxt.setText(model.getSelectedSong().getArtist());
            //fileTxt.setText(model.getSelectedSong().getFilePath());
        }
    }

    private void setCategories(){
        try{
            //System.out.println("Categories in view: " + model.getObservableCategories());
            //categoryCB.setItems(model.getObservableCategories());
            test = model.getObservableCategories();
            System.out.println(test);
            categoryCB.setItems(test);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Handles the Save button in the new song window.
    @FXML
    public void handleSave(ActionEvent actionEvent) throws Exception {
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
    }

    //Closes the window if Cancel is pressed.
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
