package DAL;

import BE.Playlist;

import java.util.List;

public interface ICRUDPlaylist {

    List<Playlist> getAllPlaylists() throws Exception;

    Playlist createNewPlaylist(String plName) throws Exception;

    Playlist editUpdatePlaylist(String plName, Playlist playlist) throws Exception;

    void deletePlaylist(Playlist playlist) throws Exception;
}
