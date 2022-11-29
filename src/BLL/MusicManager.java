package BLL;

import DAL.MusicDAO;

public class MusicManager {

    private MusicDAO musicDAO;

    public MusicManager() {
        musicDAO = new MusicDAO();
    }
}
