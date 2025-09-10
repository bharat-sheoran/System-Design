import enums.DeviceType;
import enums.PlayStrategyType;

public class Main {
    
    public static void main(String[] args) {
        MusicPlayerApplication application = MusicPlayerApplication.getInstance();

        // Populate Library
        application.createSongInLibrary("Kesariya", "Arijit singh", "/music/kesariya.mp3");
        application.createSongInLibrary("Chaiyya Chaiyya", "Sukwinder Singh", "/music/chaiyya_chaiyya.mp3");
        application.createSongInLibrary("Tum hi ho", "Arijit singh", "/music/tum_hi_ho.mp3");
        application.createSongInLibrary("Jai ho", "A. R. Rahman", "/music/jai_ho.mp3");
        application.createSongInLibrary("Zinda", "Sidharth Mahadevan", "/music/zinda.mp3");

        // Create Playlist
        application.createPlaylist("Bollywood vibes");
        application.addSongToPlaylist("Bollywood vibes", "Kesariya");
        application.addSongToPlaylist("Bollywood vibes", "Chaiyya Chaiyya");
        application.addSongToPlaylist("Bollywood vibes", "Tum hi ho");
        application.addSongToPlaylist("Bollywood vibes", "Jai ho");

        // Connect Device
        application.connectAudioDevice(DeviceType.BLUETOOTH);

        // Play/Pause Song
        application.playSingleSong("Zinda");
        application.pauseCurrentSong("Zinda");
        application.playSingleSong("Zinda");

        System.out.println("-------------Sequential Playback------------------");
        application.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
        application.loadPlaylist("Bollywood vibes");
        application.playAllTracksInPlaylist();

        System.out.println("-------------Random Playback------------------");
        application.selectPlayStrategy(PlayStrategyType.RANDOM);
        application.loadPlaylist("Bollywood vibes");
        application.playAllTracksInPlaylist();

        System.out.println("-------------Custom Queue Playback------------------");
        application.selectPlayStrategy(PlayStrategyType.CUSTOM_QUEUE);
        application.loadPlaylist("Bollywood vibes");
        application.queueSongNext("Kesariya");
        application.queueSongNext("Tum hi ho");
        application.playAllTracksInPlaylist();

        System.out.println("---------------Play Previous in Sequential-----------------");
        application.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
        application.loadPlaylist("Bollywood vibes");
        application.playAllTracksInPlaylist();

        application.playPreviousTrackInPlaylist();
        application.playPreviousTrackInPlaylist();
    }
}
