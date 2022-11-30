package GUI.Controller;
import BE.Song;
import GUI.Model.SongModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SongViewController extends BaseController {
    @FXML
    private TextField songTitleTxt;
    public TextField artistTxt;
    public ComboBox CategoryCB;
    public TextField timeTxt;
    public TextField fileTxt;

    private SongModel model;
    @Override
    public void setup()
    {

        model = getModel().getSongModel();

        if(model.shouldEditSong() == true)
        {
            songTitleTxt.setText(model.getSelectedSong().getTitle());
            artistTxt.setText(model.getSelectedSong().getArtist());
            timeTxt.setText(model.getSelectedSong().getLength());
            fileTxt.setText(model.getSelectedSong().getFilePath());
        }

    }
    
}
