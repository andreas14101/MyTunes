package GUI.Controller;

import BE.ExceptionHandler;
import BE.Playlist;
import GUI.Model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistViewController extends BaseController {
    private PlaylistModel playlistModel;
    @FXML
    private Button cxlBtn;
    @FXML
    private TextField playlistName;

    private boolean shouldEditPlaylist;
    private ExceptionHandler exceptionHandler;

    @Override
    public void setup() {
        playlistModel = getModel().getPlaylistModel();
        exceptionHandler = new ExceptionHandler();
        createNew();

        if(playlistModel.getShouldEditPlaylist())
        {
            edit();
        }
        setShouldEditPlaylist();
    }

    /**
     * Sets the boolean should edit to match the value of the boolean with the same name in the model
     */
    private void setShouldEditPlaylist()
    {
        shouldEditPlaylist = playlistModel.getShouldEditPlaylist();
    }

    /**
     * Sets the text in the textField when you are going to edit a playlist
     */
    private void edit()
    {
        playlistName.setText(playlistModel.getSelectedPlaylist().getTitle());
    }

    /**
     * Clears the textFields before you create a new playlist
     */
    private void createNew()
    {
        playlistName.clear();
    }

    /**
     * Closes the window when the button is clicked
     * @param actionEvent
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        playlistModel.setShouldEdit(false);
        closeWindow();
    }

    /**
     * Handles the save event when the save button is pressed both for new and edited songs
     * @param actionEvent
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try {
            if (!shouldEditPlaylist) {
                String plname = playlistName.getText();
                playlistModel.createNewPlaylist(plname);
                closeWindow();
            } else {
                String plname = playlistName.getText();
                Playlist pl = playlistModel.getSelectedPlaylist();
                playlistModel.editPlaylist(plname, pl);
                playlistModel.setShouldEdit(false);
                closeWindow();
            }
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }
    public void closeWindow(){
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }
}
