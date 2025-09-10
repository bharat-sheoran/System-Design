import java.util.ArrayList;

import enums.DeviceType;
import enums.PlayStrategyType;
import managers.PlaylistManager;
import models.Song;

public class MusicPlayerApplication {

    private static MusicPlayerApplication instance;
    private ArrayList<Song> songLibrary;
    private MusicPlayerApplication(){
        songLibrary = new ArrayList<>();
    }

    public static MusicPlayerApplication getInstance(){
        if(instance == null){
            instance = new MusicPlayerApplication();
        }
        return instance;
    }

    public void createSongInLibrary(String title, String artist, String path){
        Song newSong = new Song(title, artist, path);
        songLibrary.add(newSong);
    }

    public Song findSongByTitle(String title){
        for(Song song : songLibrary){
            if(song.getTitle().equals(title)){
                return song;
            }
        }
        return null;
    }

    public void createPlaylist(String playlistName){
        PlaylistManager.getInstance().createPlaylist(playlistName);
    }

    public void addSongToPlaylist(String playlistName, String songTitle){
        Song song = findSongByTitle(songTitle);
        if(song == null){
            throw new Error("Song \"" + songTitle + "\" not found in the library");
        }
        PlaylistManager.getInstance().addSongToPlaylist(playlistName, song);
    }

    public void connectAudioDevice(DeviceType deviceType){
        MusicPlayerFacade.getInstance().connectDevice(deviceType);
    }

    public void selectPlayStrategy(PlayStrategyType strategyType){
        MusicPlayerFacade.getInstance().setPlayStrategy(strategyType);
    }

    public void loadPlaylist(String playlistName){
        MusicPlayerFacade.getInstance().loadPlaylist(playlistName);
    }

    public void playSingleSong(String songTitle){
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new Error("Song not found");
        }
        MusicPlayerFacade.getInstance().playSong(song);
    }

    public void pauseCurrentSong(String songTitle){
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new Error("Song not found");
        }
        MusicPlayerFacade.getInstance().pauseSong(song);
    }

    public void playAllTracksInPlaylist(){
        MusicPlayerFacade.getInstance().playAllTracks();
    }

    public void playPreviousTrackInPlaylist(){
        MusicPlayerFacade.getInstance().playPreviousTrack();
    }

    public void queueSongNext(String songTitle){
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new Error("Song not found");
        }

        MusicPlayerFacade.getInstance().enqueNext(song);
    }
}