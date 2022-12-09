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
        //saveBtn.setDisable(true);
        //enableSaveBtn();

        if (model.getShouldEdit() == true) {

            if (model.getShouldEdit() == true) {
                edit();
            } else {
                createNew();
            }
        }
    }

    /*private void enableSaveBtn(){
        categoryCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    listenerFilepathTXT();
                } else {
                    saveBtn.setDisable(true);
                }
            }
        });
    }*/

    /*public boolean listenerFilepathTXT(){
        boolean output = false;
        fileTxt.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    saveBtn.setDisable(false);
                } else {
                    saveBtn.setDisable(true);
                }
            }
        });
        return output;
    }*/

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
    private void handleSave(ActionEvent actionEvent){
        if(saveNotAllowed()){
            String warningMessage = "Please remember to choose a category and fill out the filepath for the song";
            Alert alert = new Alert(Alert.AlertType.WARNING, warningMessage, ButtonType.CANCEL);
            alert.showAndWait();
            return;
        }

        try {
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
                String category = categoryCB.getValue().toString();

                //Updates the selected song
                model.getSelectedSong().setArtist(artist);
                model.getSelectedSong().setTitle(title);
                model.getSelectedSong().setFilePath(pathToFile);
                model.getSelectedSong().setCategory(category);
                model.songUpdate(model.getSelectedSong());

                // Closes the window
                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Check if filepath and category have been chosen. It is used before try in handleSave()
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
     * choose a new file, without the user having to copy the filepath.
     */
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
            exceptionHandler.displayError(e);
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
