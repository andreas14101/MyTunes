package GUI.Controller;

import BE.Category;
import BE.ExceptionHandler;
import BE.Playlist;
import GUI.Model.SongModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
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
    private ComboBox categoryCB;
    @FXML
    private Button cancelBtn, saveBtn, chooseFileBtn;
    @FXML
    private TextField artistTxt, songTitleTxt, fileTxt;

    private SongModel model;
    private boolean shouldEdit;
    private ExceptionHandler exceptionHandler;

    @Override
    public void setup() {
        model = getModel().getSongModel();
        setCategoryCB();
        exceptionHandler = new ExceptionHandler();
        if (model.getShouldEdit())
        {edit();}
        else
        {createNew();}
    }

    /**
     * When editing a song, place the text of the chosen song into the text fields of the song view
     */
    private void edit()
    {
            songTitleTxt.setText(model.getSelectedSong().getTitle());
            artistTxt.setText(model.getSelectedSong().getArtist());
            fileTxt.setText(model.getSelectedSong().getFilePath());
    }
    /**
     * Clears the text fields when creating a new song
     */
    private void createNew()
    {
        songTitleTxt.clear();
        artistTxt.clear();
        fileTxt.clear();
    }

    /**
     * Handles save button in new/edit song window.
     * @param actionEvent - Button pressed
     */
    @FXML
    private void handleSave(ActionEvent actionEvent){
        if(saveNotAllowed()){
            String warningMessage = "Please remember to choose a category and fill out the filepath for the song";
            Alert alert = new Alert(Alert.AlertType.WARNING, warningMessage, ButtonType.CANCEL);
            alert.showAndWait();
            return;
        }
        try {
            String title = songTitleTxt.getText();
            String artist = artistTxt.getText();
            String category = categoryCB.getValue().toString();
            String pathToFile = fileTxt.getText();

            if (!model.getShouldEdit()) {

                File file = new File(pathToFile);
                AudioFile af = AudioFileIO.getDefaultAudioFileIO().readFile(file);
                int length = af.getAudioHeader().getTrackLength();

                //Sends the info to the model layer.
                model.createSong(title, artist, String.valueOf(length), category, pathToFile);

                closeWindow();
            } else {
                //Updates the selected song
                model.getSelectedSong().setArtist(artist);
                model.getSelectedSong().setTitle(title);
                model.getSelectedSong().setFilePath(pathToFile);
                model.getSelectedSong().setCategory(category);
                model.songUpdate(model.getSelectedSong());

                closeWindow();
            }
        } catch (Exception e){
            exceptionHandler.displayNiceError("Something went wrong. Make sure you have written the correct filepath " +
                    "to the song and category is correct as well.");
        }
    }

     /** Check if filepath and category have been chosen. It is used before try in handleSave()
     * @return - true means something is missing, false means save is allowed
     */
    private boolean saveNotAllowed(){
        //Checks the two fields.
        boolean categoryCheck = categoryCB.getSelectionModel().isEmpty();
        boolean filepathCheck = fileTxt.getText().isEmpty();

        //if either of them are empty, it will be true if on condition is empty
        if(categoryCheck || filepathCheck){
            return true;
        }
        else return false;
    }

    /**
    * A button which opens a file chooser, so the user can find the file of the song he wants to add
    * @param actionEvent
    */
    public void chooseFile (ActionEvent actionEvent){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select song");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.mp3","*.wav"));
        Stage stage = (Stage) chooseFileBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            fileTxt.setText(String.valueOf(selectedFile));
        }
    }

    /**
     * Closes the window when the button is clicked
     * @param actionEvent
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        model.setShouldEdit(false); //Revert the shouldEdit variable to false again
        closeWindow();
    }

    /**
     * Sets categories in the dropdown menu for categories
     */
    private void setCategoryCB() {
        try {
            //Create list and add categories.
            ObservableList<Category> list;
            list = model.getObservableCategories();

            //Load categories to combobox
            categoryCB.setItems(list);
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Opens a new window, so you can add more categories or delete existing ones
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

    /**
     * Closes the window
     */
    public void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

}
