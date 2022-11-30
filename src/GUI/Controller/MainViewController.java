package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;
import GUI.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.TabExpander;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    public TextField filterSearch;
    public Slider timeSlider;
    public TableView songsTable;
    public TableView playlistTable;
    public Slider volumeSlider;
    public TableColumn songTitleColumn;
    public TableColumn songArtistColumn;
    public TableColumn songCategoryColumn;
    public TableColumn songTimeColumn;
    public Button CloseBtn;

    private SongModel musicModel;

    @Override
    public void setup() {
        musicModel = getModel().getSongModel();

        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        songCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        songTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));

        songsTable.getColumns().addAll();
        songsTable.setItems(musicModel.getObservableSongs());


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleNewSong(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }


    public void handleNewPlaylist(ActionEvent event)  throws  IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/PlaylistView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        PlaylistViewController controller = loader.getController();
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New playlist");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }

    public void handleMovePlaylistUp(ActionEvent actionEvent) {
    }

    public void handleMovePlaylistDown(ActionEvent actionEvent) {
    }

    public void handleDeleteSongOnPlaylist(ActionEvent actionEvent) {
    }

    public void handleEditPlaylist(ActionEvent actionEvent) {
    }

    public void handleDeletePlaylist(ActionEvent actionEvent) {
    }

    public void handleAddSongToPlaylist(ActionEvent actionEvent) {
    }

    public void handleEditSong(ActionEvent actionEvent) {
    }

    public void handleDeleteSong(ActionEvent actionEvent) throws Exception {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            musicModel.deleteSong(s);
        }

    }

    public void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) CloseBtn.getScene().getWindow();
        stage.close();
    }
}
