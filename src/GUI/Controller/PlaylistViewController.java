package GUI.Controller;

import BE.Playlist;
import GUI.Model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistViewController extends BaseController {

    private PlaylistModel playlistModel;
    public Button cxlBtn;
    public TextField playlistName;


    @Override
    public void setup() {
        playlistModel = getModel().getPlaylistModel();
        playlistName.setText(playlistModel.getSelectedPlaylist().getTitle());
    }

    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) throws Exception {

        //TODO Find out what this does
        if (playlistModel.shouldEditPlaylist() == false) {
            String plname = playlistName.getText();
            playlistModel.createNewPlaylist(plname);
            Stage stage = (Stage) cxlBtn.getScene().getWindow();
            stage.close();
        } else {
            String plname = playlistName.getText();
            Playlist pl = playlistModel.getSelectedPlaylist();
            playlistModel.editPlaylist(plname, pl);
            Stage stage = (Stage) cxlBtn.getScene().getWindow();
            stage.close();
        }
    }
}
