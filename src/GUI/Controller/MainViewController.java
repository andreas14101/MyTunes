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
    private TableColumn playlistNameColumn, playlistSongsAmountColumn, playlistTimeColumn, titleColumn;
    @FXML
    private Button CloseBtn, searchBtn, playBtn, editPlaylistBtn, deleteSongFromPlaylistBtn;
    @FXML
    private Button deletePlaylistBtn, deleteSongBtn, EditSongBtn, upArrowBtn, downArrowBtn, leftArrowBtn;
    @FXML
    private Label currentSongPlaying;
    private SongModel musicModel;
    private PlaylistModel playlistModel;
    private File directory;
    boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Media media;
    private Song selectedSong;
    private boolean itemInSongTableChosen, itemInPlaylistTableChosen;
    private int songNumber;
    private boolean isSomethingSelected = false;
    private ExceptionHandler exceptionHandler;
    private boolean playPlaylist;
    private boolean hasChanged = false;


    /**
     * Our setup method which initiates the program
     */
    @Override
    public void setup() {
        hasChanged = false;
        updateSongList();
        placeholders();
        currentSongPlaying.setText("No song currently playing");
        exceptionHandler = new ExceptionHandler();
        try {
            updatePlaylist();
        } catch (Exception ex) {
            exceptionHandler.displayNiceError("Something went wrong starting up the program. Please try again");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons();
        addListenerBtnPlaylists();
        addListenerBtnSongs();
        addListenerBtnSongsInPlaylist();
        checkIfSongSelected();
        checkIfPlSelected();
        clicks();
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
        playlistTable.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Playlist>) (observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            if (newValue != null) {
                deletePlaylistBtn.setDisable(false);
                editPlaylistBtn.setDisable(false);
            } else {
                deletePlaylistBtn.setDisable(true);
                editPlaylistBtn.setDisable(true);
            }
        });
    }

    /**
     * Controls Song table buttons. Enable or disable the edit and the delete option for songs,
     * if a song is selected or not.
     */
    private void addListenerBtnSongs() {
        songsTable.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Song>) (observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            if (newValue != null) {
                EditSongBtn.setDisable(false);
                deleteSongBtn.setDisable(false);
            } else {
                EditSongBtn.setDisable(true);
                deleteSongBtn.setDisable(true);
            }
        });
    }

    /**
     * Controls Songs in playlist table buttons. Enable or disable the move UP/DOWN and the delete option for songs in playlists,
     * if a song is selected or not.
     */
    private void addListenerBtnSongsInPlaylist() {
        songsInsidePlaylist.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Song>) (observable, oldValue, newValue) -> {
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
        });
    }

    /**
     * Controls button for adding songs to playlists. Enable or disable add song to playlist option,
     * if a song is selected and a playlist is selected.
     */
    private void checkIfSongSelected() {
        itemInSongTableChosen = false;
        songsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, boolean goes true, and run check for button, else they will be disabled
                if (newValue != null) {
                    itemInSongTableChosen = true;
                    allowLeftBtn();
                } else {
                    itemInSongTableChosen = false;
                    allowLeftBtn();
                }
            }
        });
    }

    /**
     * Used to check if a playlist is selected. It is called in addListenerBtnAddSongToPl() methode, to check both song
     * and playlist table.
     */
    private void checkIfPlSelected() {
        itemInPlaylistTableChosen = false;
        playlistTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                //If something is selected, boolean goes true, and run check for button, else they will be disabled
                if (newValue != null) {
                    itemInPlaylistTableChosen = true;
                    allowLeftBtn();
                } else {
                    itemInPlaylistTableChosen = false;
                    allowLeftBtn();
                }

            }
        });
    }

    private void allowLeftBtn(){
        if (itemInPlaylistTableChosen && itemInSongTableChosen){
            leftArrowBtn.setDisable(false);
        } else {
            leftArrowBtn.setDisable(true);
        }
    }

    /**
     * Creates the media player which is used to play songs
     */
    private void volumeControl() {
        //controlling volume slider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
            }
        });
    }

    /**
     * Generate a media from selected song. It is used in other methods.
     */
    @FXML
    private void createMedia() {
        try
        {
            if(isPlaying)
            {
                mediaPlayer.stop();
                isPlaying = false;
            }
            if(songsTable.getSelectionModel().getSelectedItem() != null)
            {
                isSomethingSelected = true;
                selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
            }
            if(songsInsidePlaylist.getSelectionModel().getSelectedItem() != null)
            {
                isSomethingSelected = true;
                selectedSong = (Song) songsInsidePlaylist.getSelectionModel().getSelectedItem();
            }
            directory = new File(selectedSong.getFilePath());
            if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
    }
        catch (Exception e)
        {
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Sets the label text when the tableview are empty
     */
    private void placeholders() {
        currentSongPlaying.setText("No song currently playing");
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
    private void updatePlaylist() {
        try {
            playlistModel = getModel().getPlaylistModel();

            playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            playlistSongsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
            playlistTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeLength"));

            playlistTable.getColumns().addAll();
            playlistTable.setItems(playlistModel.getObservablePlaylists());
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
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
     * @param event when btn is clicked
     */
    @FXML
    private void handleNewSong(ActionEvent event) {
        try {
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
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Opens a new window to create a new playList in
     * @param event when btn clicked
     */
    @FXML
    private void handleNewPlaylist(ActionEvent event) {
        try {
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
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Deletes a song from a playlist
     */
    @FXML
    private void handleDeleteSongOnPlaylist(){
        try {
            Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem(); //Get the playlist chosen
            Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem(); //Get the song chosen
            int sId = s.getId(); //Map song id into a variable
            int plId = pl.getId(); //Map playlist id into a variable

            musicModel.removeSongFromPlaylist(sId, plId); //Call the Model to remove the song
            updateSongsInPlaylist(); //Update the table which holds the songs in the playlist
            updatePlaylist(); //Update the playlist table to show actual number of songs and length
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Opens a window where you can edit the name of the playlist
     * @param actionEvent when btn is clicked
     */
    @FXML
    private void handleEditPlaylist(ActionEvent actionEvent) {
        try {
            Playlist selectedPlaylist = (Playlist) playlistTable.getFocusModel().getFocusedItem(); //Get selected Playlist
            if (selectedPlaylist != null) {
                playlistModel.setSelectedPlaylist(selectedPlaylist); //The model saves which playlist you have selected
                playlistModel.setShouldEdit(true); //Places the shouldEdit variable to true
            }
            handleNewPlaylist(actionEvent); //Initiates the same window as we use to create a new playlist to edit the chosen one.
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Deletes the selected playlist
     */
    @FXML
    private void handleDeletePlaylist(){
        try {
            Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + pl.getTitle() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                playlistModel.deletePlaylist(pl);
            }
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Adds the selected song to the selected playlist
     */
    public void handleAddSongToPlaylist(ActionEvent actionEvent) {
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
        updatePlaylist(); //Update the playlist table to show actual number of songs and length
    }

    /**
     * Opens a new window where you can edit the selected song
     * @param actionEvent when btn clicked
     */
    @FXML
    private void handleEditSong(ActionEvent actionEvent) {
        try {
            Song selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                musicModel.setSelectedSong(selectedSong); //The model saves which song you have selected
                musicModel.setShouldEdit(true); //Places the shouldEdit variable to true
            }
            handleNewSong(actionEvent); //Initiates the same window as we use to create a new song to edit the chosen one.
        } catch (Exception e){
            exceptionHandler.displayError(e);
        }
    }

    /**
     * Deletes the selected song
     */
    @FXML
    private void handleDeleteSong() {
        try {
            Song s = (Song) songsTable.getFocusModel().getFocusedItem(); //Get selected song

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "? \nThis will also remove the song in all your playlists.", ButtonType.YES, ButtonType.NO);
            alert.showAndWait(); //Alert which asks whether to delete the song

            if (alert.getResult() == ButtonType.YES) {
                musicModel.deleteSong(s);
            }
        } catch (Exception e){
            exceptionHandler.displayError(e);
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
        if (!isSomethingSelected){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No song selected (ง •_•)ง  ( ͡• ͜ʖ ͡• )  o((⊙﹏⊙))o", ButtonType.CANCEL);
            alert.showAndWait(); //Alert which shows that no song is selected.
        } else if (isPlaying) {
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            playBtn.setText("Pause");
            isPlaying = true;
            if (songsTable.getFocusModel().getFocusedItem() != null) {
                selectedSong = (Song) songsTable.getFocusModel().getFocusedItem();
            } else if (songsInsidePlaylist.getFocusModel().getFocusedItem() != null) {
                selectedSong = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
            }
            currentSongPlaying.setText(selectedSong.getTitle() + " is currently playing");
            volumeControl();
            timeMoveAuto();
            autoPlayNext();
            timeSkip();
            mediaPlayer.play();
        }
    }

    /**
     * plays the next song after the previous song ended
     */
    private void autoPlayNext()
    {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(!timeSlider.isValueChanging())
                {
                    if (Math.round(timeSlider.getValue()) == Math.round(timeSlider.getMax()) && !hasChanged) {
                        nextSong();
                    }
                }
            }
        });
    }

    /**
     * Goes to the next song in either the songs tableview or the next song in the playlist
     */
    public void nextSong() {
        hasChanged = true;
        mediaPlayer.stop();
        if(!playPlaylist)
        {
            if(songsInsidePlaylist.getFocusModel().getFocusedItem() != null)
            {
                songsInsidePlaylist.getSelectionModel().clearSelection();
            }
            switchSongTable();
        }

        if(playPlaylist)
        {
            if(songsTable.getFocusModel().getFocusedItem() != null)
            {
                songsTable.getSelectionModel().clearSelection();
            }
            switchSongInPlaylist();
        }
    }

    /**
     * Switches the song in SongTable to the next song on the user's computer
     */
    private void switchSongTable() {
        int index = songsTable.getSelectionModel().getSelectedIndex() + 1;
        if (index == musicModel.getSongsList().size() || index < 0) {
            index = 0;
        }
        songsTable.getSelectionModel().select(index);
        selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        directory = new File(selectedSong.getFilePath());
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        else if(!directory.exists())
        {
            switchSongTable();
        }
        isPlaying = false;
        playSong();
    }

    /**
     * switches to the next song in the playlist that is on user's computer
     */
    private void switchSongInPlaylist(){
        Playlist play = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getSelectedIndex()+1;
        if(index == play.getNumberOfSongs()  || index < 0)
        {
            index = 0;
        }
        songsInsidePlaylist.getSelectionModel().select(index);
        selectedSong = (Song) songsInsidePlaylist.getSelectionModel().getSelectedItem();
        directory = new File(selectedSong.getFilePath());
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        else if(!directory.exists())
        {
            switchSongInPlaylist();
        }
        isPlaying = false;
        playSong();
    }

    /**
     * Starts the current song over if the duration is >= 7 seconds of playing it
     * Else it goes back to the previous song on the list
     */
    public void previousOrRestartSong(ActionEvent event) {
        mediaPlayer.stop();
        //if more than 7 seconds has passed, the song is restarted, else it is the previous song
       double current = mediaPlayer.getCurrentTime().toSeconds();
        if (current >= 7.0) {
            mediaPlayer.seek(Duration.seconds(0));
            isPlaying = false;
            playBtn.setText("Play");
            playSong();
        }
        else
        {
            hasChanged = true;
            if(!playPlaylist)
            {
                previousSongSongTable();
            }
            if(playPlaylist)
            {
                previousSongInPlaylist();
            }
        }
    }

    /**
     * switches to the previous song in the songTable as long as the index doesn't go below 0 or the file does not exist on the user's computer
     */
    private void previousSongSongTable()
    {
        int index = songsTable.getSelectionModel().getSelectedIndex() - 1;
        if (index == musicModel.getSongsList().size() || index < 0) {
            index = musicModel.getSongsList().size();
        }
        songsTable.getSelectionModel().select(index);
        selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        directory = new File(selectedSong.getFilePath());
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        else if(!directory.exists())
        {
            previousSongSongTable();
        }
        isPlaying = false;
        playSong();
    }

    /**
     * switches to the previous song in the songInPlaylistTable as long as the index doesn't go below 0 or the file does not exist on the user's computer
     */
    private void previousSongInPlaylist()
    {
        Playlist play = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getSelectedIndex()-1;
        if(index == play.getNumberOfSongs()  || index < 0)
        {
            index = play.getNumberOfSongs();
        }
        songsInsidePlaylist.getSelectionModel().select(index);
        selectedSong = (Song) songsInsidePlaylist.getSelectionModel().getSelectedItem();
        directory = new File(selectedSong.getFilePath());
        if (directory.exists()) {
            media = new Media(directory.getAbsoluteFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        else if(!directory.exists())
        {
            previousSongInPlaylist();
        }
        isPlaying = false;
        playSong();
    }

    /**
     * Tracks the time of the song currently playing and displays it as the slider
     */
    public void timeMoveAuto()
    {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!timeSlider.isValueChanging()) {
                double total = mediaPlayer.getTotalDuration().toSeconds();
                double current = mediaPlayer.getCurrentTime().toSeconds();
                timeSlider.setMax(total);
                timeSlider.setValue(current);
                if(current > 1.0)
                {
                    hasChanged = false;
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
     * Nyan cat Easter egg press the button and get taken to the standard browser of your computer on the given url.
     */
    @FXML
    private void handleCat() {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(URI.create("https://www.nyan.cat/index.php?cat=original"));
            } catch (IOException e) {
                exceptionHandler.displayNiceError("You naughty boy, or girl, or horse, or non binary. " +
                        "This button didn't work, please don't try again.");
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

    public void clicks(){

        songsTable.setOnMouseClicked(event -> {
            playPlaylist = false;
            if(mediaPlayer != null)
            {
                mediaPlayer.stop();
            }
            if(songsInsidePlaylist.getFocusModel().getFocusedItem() != null)
            {
                songsInsidePlaylist.getSelectionModel().clearSelection();
            }
            if(event.getClickCount() == 1){
                isPlaying = false;
                playBtn.setText("Play");
                createMedia();
            }
            if (event.getClickCount() == 2) {
                playOnDoubleClick();
            }
        });
        songsInsidePlaylist.setOnMouseClicked(event -> {
            playPlaylist = true;
            if(mediaPlayer != null)
            {
                mediaPlayer.stop();
            }
            if(songsTable.getFocusModel().getFocusedItem() != null)
            {
                songsTable.getSelectionModel().clearSelection();
            }
            if(event.getClickCount() == 1) {

                isPlaying = false;
                playBtn.setText("Play");
                Song title = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
                createMedia();
                currentSongPlaying.setText(title.getTitle() +" is currently playing");
            }
            if (event.getClickCount() == 2) {
                Song title = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
                playOnDoubleClick();
                currentSongPlaying.setText(title.getTitle() +" is currently playing");
            }
        });
    }

    public void handlePlaylistUpdate(MouseEvent mouseEvent) {
        updateSongsInPlaylist();
    }

    /**
     * plays the selected song if it's double clicked
     */
    private void playOnDoubleClick()
    {
        try
        {
        isPlaying = false;
        createMedia();
        playSong();
        }
        catch (Exception e)
        {
            exceptionHandler.displayError(e);
        }
    }
}