package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;
import GUI.Model.SongModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.TabExpander;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainViewController extends BaseController implements Initializable {

    //all of the instance variables. Available everywhere in the class
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
    public Button searchBtn;
    public Button playBtn;

    private SongModel musicModel;

    private File directory;
    private File[] files;

    boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Media media;
    private ArrayList<File> songs;

    private int songNumber;
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
        boolean isPlaying = false;
        songs = new ArrayList<File>();
        directory = new File("C:\\Users\\aneho\\OneDrive - Erhvervsakademi Sydvest\\Github\\MyTunes\\DataSongs");
        files = directory.listFiles();  //stores files in directory

        if (files != null) {
            for (File file : files) {
                songs.add(file);
                System.out.println(file);
            }
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);


        //controlling volumenslider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
    }

    public void handleNewSong(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
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
        controller.setModel(super.getModel());
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

    public void handleEditSong(ActionEvent actionEvent) throws Exception {
        musicModel.shouldEditSong();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
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

    public void handleSearch(ActionEvent actionEvent) {
        if(searchBtn.getText().equals("Search")){
            if(filterSearch.getText() != null){
                String search = filterSearch.getText().toLowerCase();
                songsTable.setItems(musicModel.filteredSongs(search));
            }
            searchBtn.setText("Clear");
        } else if (searchBtn.getText().equals("Clear")) {
            filterSearch.setText("");
            songsTable.setItems(musicModel.getObservableSongs());
            searchBtn.setText("Search");
        }
    }

    public void playSong(ActionEvent actionEvent) {
        //play and pause the song
        //if song is playing, then set button to pause
        if (isPlaying){
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        }
        else{
            playBtn.setText("Pause");
            isPlaying = true;
            mediaPlayer.play();
        }



    }
}
