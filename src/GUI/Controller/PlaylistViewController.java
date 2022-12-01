package GUI.Controller;

import GUI.Model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistViewController extends BaseController{

    private PlaylistModel playlistModel;
    public Button cxlBtn;
    public TextField playlistName;


    @Override
    public void setup() {
        playlistModel = getModel().getPlaylistModel();
    }

    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) throws Exception {
        String plname = playlistName.getText();
        playlistModel.createNewPlaylist(plname);
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();

    }
}
