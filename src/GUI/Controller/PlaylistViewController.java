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

    private void setShouldEditPlaylist()
    {
        shouldEditPlaylist = playlistModel.getShouldEditPlaylist();
    }

    private void edit()
    {
        playlistName.setText(playlistModel.getSelectedPlaylist().getTitle());
    }

    private void createNew()
    {
        playlistName.clear();
    }


    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) throws Exception {

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
