package GUI.Controller;

import BE.Playlist;
import BE.Song;
import GUI.Model.MyTunesModel;
import GUI.Model.PlaylistModel;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.TabExpander;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
    public TableColumn playlistNameColumn;
    public TableColumn playlistSongsAmountColumn;
    public TableColumn playlistTimeColumn;

    public Button playBtn;
    public Button forwardBtn;
    public Label currentSongPlaying;
    public Button backBtn;

    public TableView songsInsidePlaylist;
    public TableColumn titleColumn;

    private SongModel musicModel;
    private PlaylistModel playlistModel;


    private File directory;
    private File[] files;

    boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Media media;
    private ArrayList<File> songs;

    private int songNumber;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void setup() {
        updateSongList();
        updatePlaylist();

    }

    private void updateSongsInPlaylist() {
        try {
            Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
            playlistModel = getModel().getPlaylistModel();


            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));



            songsInsidePlaylist.getColumns().addAll();
            songsInsidePlaylist.setItems(playlistModel.getSongsOnPL(pl.getId()));





        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updatePlaylist() {
        playlistModel = getModel().getPlaylistModel();

        playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        playlistSongsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
        playlistTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeLength"));


        playlistTable.getColumns().addAll();
        playlistTable.setItems(playlistModel.getObservablePlaylists());
    }

    private void updateSongList() {
        musicModel = getModel().getSongModel();

        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        songCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        songTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));

        songsTable.getColumns().addAll();
        songsTable.setItems(musicModel.getObservableSongs());
    }

    public void updatePLList() {
        playlistTable.getColumns().removeAll();

        playlistTable.getColumns().addAll();
        playlistTable.setItems(playlistModel.getObservablePlaylists());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     /* boolean isPlaying = false;
        songs = new ArrayList<File>();
        directory = new File("DataSongs");
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

        //Controlling timeslider
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            }
        });

        //Display the song on the label
        currentSongPlaying.setText(songs.get(songNumber).getName());
    */
    }

    public void handleNewSong(ActionEvent event) throws IOException {
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
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }


    public void handleNewPlaylist(ActionEvent event) throws IOException {
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
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();


    }

    public void handleMovePlaylistUp(ActionEvent actionEvent) {
    }

    public void handleMovePlaylistDown(ActionEvent actionEvent) {
    }

    public void handleDeleteSongOnPlaylist(ActionEvent actionEvent) {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        int sId = s.getId();
        int plId = pl.getId();

        musicModel.removeSongFromPlaylist(sId, plId);
        updateSongsInPlaylist();
    }

    public void handleEditPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        if (selectedPlaylist != null) {
            playlistModel.setSelectedPlaylist(selectedPlaylist);
        }

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
        dialogWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();

        //return selectedPlaylist;
    }

    public void handleDeletePlaylist(ActionEvent actionEvent) throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + pl.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            playlistModel.deletePlaylist(pl);
        }
    }

    public void handleAddSongToPlaylist(ActionEvent actionEvent) {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        int sId = s.getId();
        int plId = pl.getId();

        musicModel.addSongToPlaylist(sId, plId);
        updateSongsInPlaylist();
    }

    public void handleEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        musicModel.setSelectedSong(selectedSong);
        musicModel.shouldEditSong();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("Edit song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
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

    public void playSong() {
        //begin to track the progress
        beginTimer();
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

    public void nextSong() {
        if (songNumber < songs.size()-1){
            songNumber++;
            shiftSong();
        }
        else {
            songNumber = 0;
            shiftSong();
        }
    }
    public void shiftSong(){
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString()); //makes a command, possible for mediaPlayer to read
        mediaPlayer = new MediaPlayer(media);   //sets the song

        currentSongPlaying.setText(songs.get(songNumber).getName());
        isPlaying = false;
        playSong();
    }

    public void previosOrRestartSong() {
        //if more than 7 seconds has passed, the song is restartet, else it is the previos song
        double current = mediaPlayer.getCurrentTime().toSeconds();
        if (current >= 7.0){
            mediaPlayer.seek(Duration.seconds(0));
            isPlaying = false;
            playSong();
        }else{
            //look for witch song the previos is
            if (songNumber > 0){
                songNumber--;
                shiftSong();
            }
            else {
                songNumber = songs.size()-1;
                shiftSong();
            }

        }
    }
    public void beginTimer(){
        timer = new Timer();
        timerTask = new TimerTask(){
            //Timertask is the task to be executed.
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                int endTot = (int) Math.round(end);
                double howFar = current/end;

                System.out.println(Math.round(howFar * 100*100)/100+ " % \tgennem\t" + currentSongPlaying.getText() + "\t Current time: "+ Math.round(current)+ "\t Total Duration: " + endTot);
                timeSlider.setValue(Math.round(howFar * 100 * 100)/100);
                if (howFar == 1){
                    nextSong();
                    cancelTimer();
                }
            }
        };
        //executes the timertask after 1000 milliSeconds = 1 second
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

    }
    public void cancelTimer(){
        timer.cancel();

    }

    public void timeChanged(MouseDragEvent mouseDragEvent) {
        double howfarNew = Math.round(timeSlider.getValue())/100;
        /**
         (current/end)*100=howfar%
         (100 second far/ 200 second end)=0.5 howFarTo1
         end*(howfar%/100)) = current
         current/howfar% = end
         (howfarnew/100) * end = current
         */

        double end = media.getDuration().toSeconds();
        double newTimeOnSong = end * howfarNew;
        System.out.println(newTimeOnSong);
        mediaPlayer.seek(Duration.seconds(newTimeOnSong));
    }

    public void handleCat(ActionEvent event) throws InterruptedException {
        Desktop desktop = Desktop.getDesktop();
        if(desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(URI.create("https://www.nyan.cat/index.php?cat=original"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handlePlaylistUpdate(MouseEvent mouseEvent)
    {
        updateSongsInPlaylist();
    }
}
