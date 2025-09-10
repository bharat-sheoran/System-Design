import core.AudioEngine;
import device.IAudioOutputDevice;
import enums.DeviceType;
import enums.PlayStrategyType;
import managers.DeviceManager;
import managers.PlaylistManager;
import managers.StrategyManager;
import models.Playlist;
import models.Song;
import strategies.PlayStrategy;

public class MusicPlayerFacade {
    private static MusicPlayerFacade instance;
    private AudioEngine audioEngine;
    private Playlist loadedPlaylist;
    private PlayStrategy playStrategy;

    private MusicPlayerFacade() {
        loadedPlaylist = null;
        playStrategy = null;
        audioEngine = new AudioEngine();
    }

    public static MusicPlayerFacade getInstance() {
        if (instance == null) {
            instance = new MusicPlayerFacade();
        }
        return instance;
    }

    public void connectDevice(DeviceType deviceType){
        DeviceManager.getInstance().connect(deviceType);
    }

    public void setPlayStrategy(PlayStrategyType strategyType){
        this.playStrategy = StrategyManager.getInstance().getStrategy(strategyType);
    }

    public void loadPlaylist(String name){
        this.loadedPlaylist = PlaylistManager.getInstance().getPlaylist(name);
        if (playStrategy == null) {
            throw new Error("Play Strategy not set before loading");
        }
        playStrategy.setPlaylist(loadedPlaylist);
    }

    public void playSong(Song song){
        if (!DeviceManager.getInstance().hasOutputDevice()) {
            throw new Error("No audio Device Connected");
        }
        IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
        audioEngine.play(device, song);
    }

    public void pauseSong(Song song){
        if (audioEngine.getCurrentSongTitle() == song.getTitle()) {
            throw new Error("Cannot pause \n" + song.getTitle() + "\"; not currently playing");
        }
        audioEngine.pause();
    }

    public void playAllTracks(){
        if (loadedPlaylist == null) {
            throw new Error("No playlist Loaded");
        }
        while (playStrategy.hasNext()) {
            Song nextSong = playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, nextSong);
        }
        System.out.println("Completed playlist: " + loadedPlaylist.getPlaylistName());
    }

    public void playNextTrack(){
        if (loadedPlaylist == null) {
            throw new Error("No Playlist loaded");
        }

        if (playStrategy.hasNext()) {
            Song nextSong = playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, nextSong);
        }else{
            System.out.println("Completed Playlist" + loadedPlaylist.getPlaylistName());
        }
    }

    public void playPreviousTrack(){
        if (loadedPlaylist == null) {
            throw new Error("No Playlist Loaded");
        }
        if (playStrategy.hasPrevious()) {
            Song prevSong = playStrategy.prev();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, prevSong);
        }else{
            System.out.println("No previous track");
        }
    }

    public void enqueNext(Song song){
        playStrategy.addToNext(song);
    }
}