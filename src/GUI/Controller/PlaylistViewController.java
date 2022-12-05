package GUI.Controller;

import BE.Playlist;
import GUI.Model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistViewController extends BaseController {

    private PlaylistModel playlistModel;
    private Button cxlBtn;
    private TextField playlistName;

    private boolean shouldEditPlaylist;

    @Override
    public void setup() {
        playlistModel = getModel().getPlaylistModel();

        if(playlistModel.getShouldEditPlaylist() == true)
        {
            edit();
        }
        else
        {
            createNew();
        }
        setShouldEditPlaylist();
    }

    /**
     * sets the boolean should edit to match the value of the boolean with the same name in the model
     */
    private void setShouldEditPlaylist()
    {
        shouldEditPlaylist = playlistModel.getShouldEditPlaylist();
    }

    /**
     * sets the text in the textField when you are going to edit a playlist
     */
    private void edit()
    {
        playlistName.setText(playlistModel.getSelectedPlaylist().getTitle());
    }

    /**
     * clears the textFields when before you create a new playlist
     */
    private void createNew()
    {
        playlistName.clear();
    }

    /**
     * closes the window when the button is clicked
     * @param actionEvent
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * handles the save event when the save button is pressed both for new and edited songs
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) throws Exception {

        if (shouldEditPlaylist == false) {
            String plname = playlistName.getText();
            playlistModel.createNewPlaylist(plname);
            Stage stage = (Stage) cxlBtn.getScene().getWindow();
            stage.close();
        } else {
            String plname = playlistName.getText();
            Playlist pl = playlistModel.getSelectedPlaylist();
            playlistModel.editPlaylist(plname, pl);
            playlistModel.setShouldEdit(false);
            Stage stage = (Stage) cxlBtn.getScene().getWindow();
            stage.close();
        }
    }
}
