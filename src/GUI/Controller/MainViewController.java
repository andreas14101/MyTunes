package GUI.Controller;

import BE.ExceptionHandler;
import BE.Playlist;
import BE.Song;
import GUI.Model.PlaylistModel;
import GUI.Model.SongModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    //all the instance variables. Available everywhere in the class
    @FXML
    private TextField filterSearch;
    @FXML
    private Slider timeSlider, volumeSlider;
    @FXML
    private TableView songsTable, playlistTable;
    @FXML
    private TableColumn songTitleColumn, songArtistColumn, songCategoryColumn, songTimeColumn;
    @FXML
    private Button CloseBtn, searchBtn, playBtn;
    @FXML
    private TableColumn playlistNameColumn, playlistSongsAmountColumn, playlistTimeColumn;
    @FXML
    private Label currentSongPlaying;
    @FXML
    private TableView songsInsidePlaylist;
    @FXML
    private TableColumn titleColumn;
    private SongModel musicModel;
    private PlaylistModel playlistModel;
    private File directory;
    boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Media media;
    private List<Song> allSongsFromDb;
    private int songNumber;
    private boolean isSomethingChosen;
    private ExceptionHandler exceptionHandler;

    @Override
    public void setup() {
        updateSongList();
        placeholders();
        mediaPlayerMethod();
        currentSongPlaying.setText("(none) is currently playing");
        exceptionHandler = new ExceptionHandler();
        try {
            updatePlaylist();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mediaPlayerMethod() {
        isSomethingChosen = false;

        createMedia();

        isSomethingChosen = true;
        //controlling volumeSlider
        volumeSlider.valueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
    }

    private void createMedia()
    {
        allSongsFromDb = musicModel.getSongsList();
        List<String> filePaths = new ArrayList<>();
        if(allSongsFromDb != null)
        {
            for (Song s: allSongsFromDb)
            {
                filePaths.add(s.getFilePath());
            }
        }
        directory = new File(filePaths.get(songNumber));
        if(directory.exists())
        {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
    }

    /**
     * sets the label text when the tableview are empty
     */
    private void placeholders() {
        songsInsidePlaylist.setPlaceholder(new Label("No songs on playlist"));
        songsTable.setPlaceholder(new Label("No songs added yet"));
        playlistTable.setPlaceholder(new Label("No playlist created yet"));
    }

    /**
     * updates the songs in playlist tableview
     */
    private void updateSongsInPlaylist() {
        try {
            Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
            playlistModel = getModel().getPlaylistModel();

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));

            songsInsidePlaylist.getColumns().addAll();
            songsInsidePlaylist.setItems(playlistModel.getSongsOnPL(pl.getId()));


        } catch (Exception ex) {
            exceptionHandler.displayError(ex);
        }
    }

    /**
     * Updates the playlist tableview
     */
    private void updatePlaylist() throws Exception {
        playlistModel = getModel().getPlaylistModel();

        playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        playlistSongsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
        playlistTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeLength"));


        playlistTable.getColumns().addAll();
        playlistTable.setItems(playlistModel.getObservablePlaylists());
    }

    /**
     * updates the songs tableview
     */
    private void updateSongList() {
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
        //normalSelect();
        Clicks();
    }

    /**
     * opens a new window to show a detailed view when adding a new song
     *
     * @param event when btn is clicked
     * @throws IOException throws Exception
     */
    @FXML
    private void handleNewSong(ActionEvent event) throws IOException {
        AnchorPane pane = openWindow();
        // Create the dialog stage
        Stage dialogWindow = new Stage();

        dialogWindow.setTitle("New song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }

    /**
     * opens a new window to create a new playList in
     *
     * @param event when btn clicked
     * @throws IOException Exception handle
     */
    @FXML
    private void handleNewPlaylist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/PlaylistView.fxml"));
        AnchorPane pane = loader.load();
        PlaylistViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();
        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New playlist");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }

    /**
     * deletes a song from a playlist
     */
    @FXML
    private void handleDeleteSongOnPlaylist() throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int sId = s.getId();
        int plId = pl.getId();

        musicModel.removeSongFromPlaylist(sId, plId);
        updateSongsInPlaylist();
        updatePlaylist();
    }

    /**
     * opens a window where you can edit the name of the playlist
     *
     * @param actionEvent when btn is clicked
     * @throws IOException handle Exception
     */
    @FXML
    private void handleEditPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        if (selectedPlaylist != null) {
            playlistModel.setSelectedPlaylist(selectedPlaylist);
            playlistModel.setShouldEdit(true);
        }
        handleNewPlaylist(actionEvent);

        //return selectedPlaylist;
    }

    /**
     * deletes the selected playlist
     * @throws Exception exception handle
     */
    @FXML
    private void handleDeletePlaylist() throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + pl.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            playlistModel.deletePlaylist(pl);
        }
    }

    /**
     * adds the selected song to the selected playlist
     */

    public void handleAddSongToPlaylist() throws Exception {
        //Get chosen playlist & song
        Playlist pl = (Playlist) playlistTable.getSelectionModel().getSelectedItem();
        Song s = (Song) songsTable.getSelectionModel().getSelectedItem();

        //Save the id's of the two into two variables
        int sId = s.getId();
        int plId = pl.getId();

        //Boolean which indicates whether the song is present on the playlist or not
        boolean songPresent = false;

        //For loop which goes through all songs inside the playlist
        for (int i = 0; i < songsInsidePlaylist.getItems().size(); i++) {

            //Get the songs in the playlist and their id's
            Song SiP = (Song) songsInsidePlaylist.getItems().get(i);
            int SiPID = SiP.getId();

            //if the song id and one of the id's from the songs in the playlist match, display a warning.
            if (SiPID == sId) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Song already in Playlist", ButtonType.CANCEL);
                alert.showAndWait();
                //Change boolean which indicates whether the song is present or not to true.
                songPresent = true;
            }

        }
        //If song is not present add it to the playlist.
        if (!songPresent) {
            musicModel.addSongToPlaylist(sId, plId);
            updateSongsInPlaylist();

        }
        updatePlaylist();
    }

    /**
     * opens a new window where you can edit the selected song
     *
     * @param actionEvent when btn clicked
     * @throws IOException exception handle
     */
    @FXML
    private void handleEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        musicModel.setSelectedSong(selectedSong);
        musicModel.setShouldEdit(true);
        AnchorPane pane = openWindow();
        // Create the dialog stage
        Stage dialogWindow = new Stage();

        dialogWindow.setTitle("Edit song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }
    public AnchorPane openWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();
        return pane;
    }

    /**
     * deletes the selected song
     * @throws Exception exception handle
     */
    @FXML
    private void handleDeleteSong() throws Exception {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            musicModel.deleteSong(s);
        }
    }


    /**
     * closes the window when the button is pressed
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) CloseBtn.getScene().getWindow();
        stage.close();
    }


    /**
     * on the first click of the button it searches through the song table to fit the query on the second click of the button it clears the search query
     */
    @FXML
    private void handleSearch() {
        if (searchBtn.getText().equals("Search")) {
            if (filterSearch.getText() != null) {
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


    /**
     * on the first click of the button plays selected song on the second click pauses the song
     */
    public void playSong() {

        //play and pause the song
        //if song is playing, then set button to pause
        if (isPlaying && isSomethingChosen) {
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else if (!isPlaying && isSomethingChosen) {
            playBtn.setText("Pause");
            isPlaying = true;
            timeMoveAuto();
            timeSkip();
            mediaPlayer.play();
        } else {
            System.out.println("No song selected (ง •_•)ง  ( ͡• ͜ʖ ͡• )  o((⊙﹏⊙))o");
        }
    }


    /**
     * goes to the next song in either the songs tableview or the next song in the playlist
     */
    public void nextSong() {
        allSongsFromDb = musicModel.getSongsList();
        if (songNumber < allSongsFromDb.size()) {
            songNumber++;
            shiftSong();
        } else {
            songNumber = 0;
            shiftSong();
        }
    }

    /**
     * switches the song
     */
    public void shiftSong() {
        allSongsFromDb = musicModel.getSongsList();
        mediaPlayer.stop();
        List<String> filePaths = new ArrayList<>();
        if(allSongsFromDb != null)
        {
            for (Song s: allSongsFromDb)
            {
                filePaths.add(s.getFilePath());
            }
        }
        directory = new File(filePaths.get(songNumber));
        if(directory.exists()){
        media = new Media(directory.getAbsoluteFile().toURI().toString()); //makes a command, possible for mediaPlayer to read
        mediaPlayer = new MediaPlayer(media);   //sets the song
        currentSongPlaying.setText(allSongsFromDb.get(songNumber).getTitle() + " is currently playing");
        isPlaying = false;
        playBtn.setText("play");
        playSong();}
    }

    /**
     * starts the current song over if the duration is >= 7 seconds of playing it else it goes back to the previous song on the list
     */
    public void previousOrRestartSong() {
        allSongsFromDb = musicModel.getSongsList();
        //if more than 7 seconds has passed, the song is restarted, else it is the previous song
        double current = mediaPlayer.getCurrentTime().toSeconds();
        if (current >= 7.0) {
            mediaPlayer.seek(Duration.seconds(0));
            isPlaying = false;
            playBtn.setText("play");
            playSong();
        } else if (songNumber > 0) {
            songNumber--;
            playBtn.setText("play");
            shiftSong();
        } else {
            songNumber = allSongsFromDb.size();
            playBtn.setText("play");
            shiftSong();
        }
    }

    /**
     * tracks the time of the song that is currently playing
     */
    public void timeMoveAuto()
    {
      mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
          if (!timeSlider.isValueChanging()) {
              double total = mediaPlayer.getTotalDuration().toSeconds();
              double current = mediaPlayer.getCurrentTime().toSeconds();
              timeSlider.setMax(total);
              timeSlider.setValue(current);
          }
      });
    }

    /**
     * skips time in the song to match the slider if it gets moved by the user.
     */
    public void timeSkip() {
        timeSlider.setOnMouseClicked(event -> mediaPlayer.seek(Duration.seconds(timeSlider.getValue())));
    }

    /**
     * Nyan cat Easter egg press the button and get taken to the standard browser of you computer on the given url.
     */
    @FXML
    private void handleCat() {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(URI.create("https://www.nyan.cat/index.php?cat=original"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * shows the songs in a playlist in the songs in playlist tableview
     */
    @FXML
    private void handlePlaylistUpdate() {
        updateSongsInPlaylist();
    }

    /**
     * Moves the current highlighted song UP in the playlist,
     * so the user is able to sort their playlist according to their wishes.
     */
    @FXML
    private void handleMoveSongUp() {
        //Get focused song
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getFocusedIndex();

        //Move focused song up if it is not at the top
        if (index > 0) {
            songsInsidePlaylist.getItems().set(index, songsInsidePlaylist.getItems().get(index - 1));
            songsInsidePlaylist.getItems().set(index - 1, s);

            //Keep the item you moved selected
            songsInsidePlaylist.getSelectionModel().select(index - 1);
        } else {
            //Show error if at top and the user tries to move it up
            Alert alert = new Alert(Alert.AlertType.ERROR, "Song already at top of playlist", ButtonType.CANCEL);
            alert.showAndWait();
        }

    }

    /**
     * Moves the current highlighted song DOWN in the playlist,
     * so the user is able to sort their playlist according to their wishes.
     */
    @FXML
    private void handleMoveSongDown() {
        //Get focused song
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getFocusedIndex();

        //Move focused song down if it is not at the bottom
        if (index < songsInsidePlaylist.getItems().size() - 1) {
            songsInsidePlaylist.getItems().set(index, songsInsidePlaylist.getItems().get(index + 1));
            songsInsidePlaylist.getItems().set(index + 1, s);

            //Keep the item you moved selected
            songsInsidePlaylist.getSelectionModel().select(index + 1);
        } else {
            //Show error if at bottom and the user tries to move it down
            Alert alert = new Alert(Alert.AlertType.ERROR, "Song already at bottom of playlist", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }


    public void Clicks(){
        songsTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1){
                selectedSong();
            }
            if (event.getClickCount() == 2) {
                currentSongPlaying.setText(selectedSong() + " is currently playing");
                playSong();
            }
        });
        songsInsidePlaylist.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1) {
                selectedSongFromPlaylist();
            }
            if (event.getClickCount() == 2) {
                currentSongPlaying.setText(selectedSongFromPlaylist() + " is currently playing");
                playSong();
            }
        });
    }

    public String selectedSong() {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();
        songSelection(s);
        return s.getTitle();

    }
    public String selectedSongFromPlaylist() {
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        songSelection(s);
        return s.getTitle();
    }
    private void songSelection(Song s) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        isSomethingChosen = true;
        isPlaying = false;
        System.out.println("Chosen song from Table: " + s.getTitle());
        directory = new File(s.getFilePath());
        media = new Media(directory.getAbsoluteFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);

    }
}

