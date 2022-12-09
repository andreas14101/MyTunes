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
import javafx.scene.input.MouseEvent;
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
    private TableView songsTable, playlistTable, songsInsidePlaylist;
    @FXML
    private TableColumn songTitleColumn, songArtistColumn, songCategoryColumn, songTimeColumn;
    @FXML
    private TableColumn playlistNameColumn, playlistSongsAmountColumn, playlistTimeColumn;
    @FXML
    private Button CloseBtn, searchBtn, playBtn, editPlaylistBtn, deleteSongFromPlaylistBtn;
    @FXML
    private Button deletePlaylistBtn, deleteSongBtn, EditSongBtn, upArrowBtn, downArrowBtn, leftArrowBtn;
    @FXML
    private Label currentSongPlaying;
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
    private boolean isSomethingChosen = false;
    private ExceptionHandler exceptionHandler;

    /**
     * Our setup method which initiates the program
     */
    @Override
    public void setup() {
        updateSongList();
        placeholders();
        mediaPlayerMethod();
        currentSongPlaying.setText("No song currently playing");
        exceptionHandler = new ExceptionHandler();
        try {
            updatePlaylist();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons();
        addListenerBtnPlaylists();
        addListenerBtnSongs();
        addListenerBtnSongsInPlaylist();
        addListenerBtnAddSongsToPl();
        Clicks();
    }

    /**
     * Disable buttons that shouldn't be usable before a selection in a table has been made
     */
    private void disableButtons() {
        deletePlaylistBtn.setDisable(true);
        deleteSongBtn.setDisable(true);
        EditSongBtn.setDisable(true);
        editPlaylistBtn.setDisable(true);
        deleteSongFromPlaylistBtn.setDisable(true);
        upArrowBtn.setDisable(true);
        downArrowBtn.setDisable(true);
        leftArrowBtn.setDisable(true);
    }

    /**
     * Controls playlist table buttons. Enable or disable the edit and the delete option for playlists,
     * if a playlist is selected or not.
     */
    private void addListenerBtnPlaylists() {
        playlistTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    deletePlaylistBtn.setDisable(false);
                    editPlaylistBtn.setDisable(false);
                } else {
                    deletePlaylistBtn.setDisable(true);
                    editPlaylistBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls Song table buttons. Enable or disable the edit and the delete option for songs,
     if a song is selected or not.
     */
    private void addListenerBtnSongs() {
        songsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    EditSongBtn.setDisable(false);
                    deleteSongBtn.setDisable(false);
                } else {
                    EditSongBtn.setDisable(true);
                    deleteSongBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls Songs in playlist table buttons. Enable or disable the move UP/DOWN and the delete option for songs in playlists,
     * if a song is selected or not.
     */
    private void addListenerBtnSongsInPlaylist() {
        songsInsidePlaylist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    deleteSongFromPlaylistBtn.setDisable(false);
                    upArrowBtn.setDisable(false);
                    downArrowBtn.setDisable(false);
                } else {
                    deleteSongFromPlaylistBtn.setDisable(true);
                    upArrowBtn.setDisable(true);
                    downArrowBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls button for adding songs to playlists. Enable or disable add song to playlist option,
     * if a song is selected and a playlist is selected.
     */
    private void addListenerBtnAddSongsToPl() {
        songsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    checkIfPlSelected();
                } else {
                    leftArrowBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Used to check if a playlist is selected. It is called in addListenerBtnAddSongToPl() methode, to check both song
     * and playlist table.
     */
    private void checkIfPlSelected() {
        playlistTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    leftArrowBtn.setDisable(false);
                } else {
                    leftArrowBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Creates the media player which is used to play songs
     */
    private void mediaPlayerMethod() {
        isSomethingChosen = false;
        createMedia();
        //controlling volume slider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
    }


    /**
     * Store the filepaths from the songs in the DB into a directory.
     * This directory can then be played by the media-player.
     */
    private void createMedia() {
        allSongsFromDb = musicModel.getSongsList(); //Gets all songs from the DB
        List<String> filePaths = new ArrayList<>(); //Creates a List of Strings
        if (allSongsFromDb != null) {
            for (Song s : allSongsFromDb) {
                filePaths.add(s.getFilePath()); //Add all filepaths from the songs into the list
            }
        }
        directory = new File(filePaths.get(songNumber)); //Create a directory which the media player can use to play songs from
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
    }

    /**
     * Sets the label text when the tableview are empty
     */
    private void placeholders() {
        songsInsidePlaylist.setPlaceholder(new Label("No songs on playlist"));
        songsTable.setPlaceholder(new Label("No songs added yet"));
        playlistTable.setPlaceholder(new Label("No playlist created yet"));
    }

    /**
     * Updates the songs in playlist tableview
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
     * Updates the songs tableview
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

    /**
     * Opens a new window to show a detailed view when adding a new song
     *
     * @param event when btn is clicked
     * @throws IOException throws Exception
     */
    @FXML
    private void handleNewSong(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();
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
     * Opens a new window to create a new playList in
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
     * Deletes a song from a playlist
     */
    @FXML
    private void handleDeleteSongOnPlaylist() throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem(); //Get the playlist chosen
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem(); //Get the song chosen
        int sId = s.getId(); //Map song id into a variable
        int plId = pl.getId(); //Map playlist id into a variable

        musicModel.removeSongFromPlaylist(sId, plId); //Call the Model to remove the song
        updateSongsInPlaylist(); //Update the table which holds the songs in the playlist
        updatePlaylist(); //Update the playlist table to show actual number of songs and length
    }

    /**
     * Opens a window where you can edit the name of the playlist
     *
     * @param actionEvent when btn is clicked
     * @throws IOException handle Exception
     */
    @FXML
    private void handleEditPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = (Playlist) playlistTable.getFocusModel().getFocusedItem(); //Get selected Playlist
        if (selectedPlaylist != null) {
            playlistModel.setSelectedPlaylist(selectedPlaylist); //The model saves which playlist you have selected
            playlistModel.setShouldEdit(true); //Places the shouldEdit variable to true
        }
        handleNewPlaylist(actionEvent); //Initiates the same window as we use to create a new playlist to edit the chosen one.
    }

    /**
     * Deletes the selected playlist
     * @throws Exception exception handle
     */
    @FXML
    private void handleDeletePlaylist() throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + pl.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait(); //Shows an alert asking whether you should delete the selected playlist.
        if (alert.getResult() == ButtonType.YES) {
            playlistModel.deletePlaylist(pl); //Call the model to delete the playlist
        }
    }

    /**
     * Adds the selected song to the selected playlist
     */
    public void handleAddSongToPlaylist(ActionEvent actionEvent) throws Exception {
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

            //If the song id and one of the id's from the songs in the playlist match, display a warning.
            if (SiPID == sId) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Song already in Playlist", ButtonType.CANCEL);
                alert.showAndWait();
                //Change boolean which indicates whether the song is present or not to true.
                songPresent = true;
            }

        }
        //If song is not present add it to the playlist.
        if (!songPresent) {
            musicModel.addSongToPlaylist(sId, plId); //Call the model to add the song to the playlist.
            updateSongsInPlaylist(); //Update the songs inside the tableview
        }
        updatePlaylist(); //Update the playlists table to show actual number of songs and length
    }

    /**
     * Opens a new window where you can edit the selected song
     *
     * @param actionEvent when btn clicked
     * @throws IOException exception handle
     */
    @FXML
    private void handleEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            musicModel.setSelectedSong(selectedSong); //The model saves which song you have selected
            musicModel.setShouldEdit(true); //Places the shouldEdit variable to true
        }
        handleNewSong(actionEvent); //Initiates the same window as we use to create a new song to edit the chosen one.
    }

    /**
     * Deletes the selected song
     * @throws Exception exception handle
     */
    @FXML
    private void handleDeleteSong() throws Exception {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem(); //Get selected song

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "? \nThis will also remove the song in all your playlists.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait(); //Alert which asks whether to delete the song

        if (alert.getResult() == ButtonType.YES) {
            musicModel.deleteSong(s);
        }
    }

    /**
     * Closes the window when the button is pressed
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) CloseBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * On the first click of the button it searches through the song table to fit the query
     * On the second click of the button it clears the search query
     */
    @FXML
    private void handleSearch() {
        if (searchBtn.getText().equals("Search")) { //First check the button to see if it set to Search.
            if (filterSearch.getText() != null) {
                String search = filterSearch.getText().toLowerCase(); //Get the String written into search field.
                songsTable.setItems(musicModel.filteredSongs(search)); //Send the String down to model and set the new observable list
            }
            searchBtn.setText("Clear"); //Change the button to be called Clear
        } else if (searchBtn.getText().equals("Clear")) {
            filterSearch.clear(); //Changes the text back to being empty
            songsTable.setItems(musicModel.getObservableSongs()); //Change the list back to the original observable list
            searchBtn.setText("Search"); //Set the button text back to Search
        }
    }

    /**
     * On the first click of the button plays selected song
     * On the second click pauses the song
     */
    public void playSong() {
        if (!isSomethingChosen){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No song selected (ง •_•)ง  ( ͡• ͜ʖ ͡• )  o((⊙﹏⊙))o", ButtonType.CANCEL);
            alert.showAndWait(); //Alert which shows that no song is selected.
        }
        else if (isPlaying) {
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            playBtn.setText("Pause");
            isPlaying = true;
            timeMoveAuto();
            timeSkip();
            mediaPlayer.play();
        }
    }

    /**
     * Goes to the next song in either the songs tableview or the next song in the playlist
     */
    public void nextSong(ActionEvent event) {
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
     * Switches the song
     */
    private void shiftSong() {
        allSongsFromDb = musicModel.getSongsList();
        mediaPlayer.stop();
        List<String> filePaths = new ArrayList<>();
        if (allSongsFromDb != null) {
            for (Song s : allSongsFromDb) {
                filePaths.add(s.getFilePath());
            }
        }
        directory = new File(filePaths.get(songNumber));
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString()); //makes a command, possible for mediaPlayer to read
            mediaPlayer = new MediaPlayer(media);   //sets the song
            currentSongPlaying.setText(allSongsFromDb.get(songNumber).getTitle() + " is currently playing");
            isPlaying = false;
            playBtn.setText("Play");
            playSong();
        }
    }

    /**
     * Starts the current song over if the duration is >= 7 seconds of playing it
     * Else it goes back to the previous song on the list
     */
    public void previousOrRestartSong(ActionEvent event) {
        allSongsFromDb = musicModel.getSongsList();
        double current = mediaPlayer.getCurrentTime().toSeconds();
        if (current >= 7.0) {
            mediaPlayer.seek(Duration.seconds(0));
            isPlaying = false;
            playBtn.setText("Play");
            playSong();
        } else if (songNumber > 0) {
            songNumber--;
            playBtn.setText("Play");
            shiftSong();
        } else {
            songNumber = allSongsFromDb.size();
            playBtn.setText("Play");
            shiftSong();
        }
    }

    /**
     * Tracks the time of the song currently playing and displays it as the slider
     */
    public void timeMoveAuto() {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if (!timeSlider.isValueChanging()) {
                    double total = mediaPlayer.getTotalDuration().toSeconds();
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    timeSlider.setMax(total);
                    timeSlider.setValue(current);
                }
            }
        });
    }

    /**
     * Skips time in the song to match the slider if it gets moved by the user.
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
     * Moves the current highlighted song UP in the playlist,
     * So the user is able to sort their playlist according to their wishes.
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
            if (event.getClickCount() == 1) {
                currentSongPlaying.setText(selectedSong() + " is currently playing");
                selectedSong();
            }
            if (event.getClickCount() == 2) {
                currentSongPlaying.setText(selectedSong() + " is currently playing");
                playSong();
            }
        });
        songsInsidePlaylist.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongPlaying.setText(selectedSongFromPlaylist() + " is currently playing");
                selectedSongFromPlaylist();
            }
            if (event.getClickCount() == 2) {
                currentSongPlaying.setText(selectedSongFromPlaylist() + " is currently playing");
                playSong();
            }
        });
    }

    /**
     * Set the current song chosen to selected song
     * @return String with the title of the song you have selected
     */
    public String selectedSong() {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();
        songSelection(s);
        return s.getTitle();
    }

    /**
     * Set the current song chosen to selected song in a playlist
     * @return String with the title of the song you have selected in the playlist
     */
    public String selectedSongFromPlaylist() {
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        songSelection(s);
        return s.getTitle();
    }

    /**
     * If the song is doubleclicked on it stops what it is playing now, and starts playing the chosen song.
     * @param s
     */
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

    public void handlePlaylistUpdate(MouseEvent mouseEvent) {
        updateSongsInPlaylist();
    }
}