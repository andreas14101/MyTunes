package DAL;

import BE.Playlist;

import java.util.List;

public interface ICRUDPlaylist {

    public List<Playlist> getAllPlaylists() throws Exception;

    public Playlist createNewPlaylist(String plname) throws Exception;

    public Playlist editUpdatePlaylist(String plname, Playlist playlist) throws Exception;

    public void deletePlaylist(Playlist playlist) throws Exception;
}
