package GUI.Controller;

import BE.Category;
import GUI.Model.SongModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;
import java.io.IOException;

public class SongViewController extends BaseController {
    @FXML
    private Button chooseFileBtn;
    private ComboBox categoryCB;
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

    private boolean shouldEdit;

    @Override
    public void setup() {

        model = getModel().getSongModel();

        if (model.getShouldEdit() == true) {

            if (model.getShouldEdit() == true) {
                edit();
            } else {
                createNew();
            }
        }
    }

    /**
     * sets the shouldEdit boolean to match the value of the shouldEdit boolean from the model
     */
    private void setShouldEdit()
    {
        shouldEdit = model.getShouldEdit();
    }

    /**
     * sets the textfields text to match the selected songs information when editing a song
     */
    private void edit()
    {
            songTitleTxt.setText(model.getSelectedSong().getTitle());
            artistTxt.setText(model.getSelectedSong().getArtist());
            fileTxt.setText(model.getSelectedSong().getFilePath());
        }
    /**
     * clears the textfields when creating a new song
     */
    private void createNew()
    {
        songTitleTxt.clear();
        artistTxt.clear();
        fileTxt.clear();
    }

    /**
     * handles save button in new/edit song window.
     * @param actionEvent - Button pressed
     * @throws Exception
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) throws Exception {
        if (model.getShouldEdit() == false) {
                String title = songTitleTxt.getText();
                String artist = artistTxt.getText();
                String category = categoryCB.getValue().toString();
                String pathToFile = fileTxt.getText();

                //Takes the duration of the file given, and maps it to an int in seconds,
                //will be converted to the correct visual value later.
                File file = new File(pathToFile);
                AudioFile af = AudioFileIO.getDefaultAudioFileIO().readFile(file);
                int length = af.getAudioHeader().getTrackLength();
                //Sends the info to the model layer.
                model.createSong(title, artist, String.valueOf(length), category, pathToFile);

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

    /**
     * closes the window when the button is clicked
     * @param actionEvent
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        model.setShouldEdit(false);
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets categories in the dropdown menu for categories
     */
    private void setCategoryCB() {
        try {
            //Create list and add categories.
            ObservableList<Category> list = FXCollections.observableArrayList();
            list = model.getObservableCategories();

            //Load categories to combobox
            categoryCB.setItems(list);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * opens a new window so you can add more categories or delete existing ones
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleMoreCategoriesBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/NewCategoryView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        CategoryViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New Category");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();

        setCategoryCB();
    }
}
