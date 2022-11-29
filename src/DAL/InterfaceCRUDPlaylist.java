package DAL;

import BE.Playlist;

import java.util.List;

public interface InterfaceCRUDPlaylist {

    public List<Playlist> getAllPlaylists() throws Exception;

    public Playlist createNewPlaylist(Playlist playlist) throws Exception;

    public void editUpdatePlaylist(Playlist playlist) throws Exception;

    public void deletePlaylist(Playlist playlist) throws Exception;
}
