package managers;

import java.util.HashMap;
import java.util.Map;

import models.Playlist;
import models.Song;

public class PlaylistManager {
    private static PlaylistManager instance;
    Map<String, Playlist> playlists;

    private PlaylistManager(){
        playlists = new HashMap<>();
    }

    public static PlaylistManager getInstance(){
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    public void createPlaylist(String name){
        if (playlists.containsKey(name)) {
            throw new Error("Playlist name already exists");
        }
        playlists.put(name, new Playlist(name));
    }

    public void addSongToPlaylist(String playlistName, Song song){
        if (!playlists.containsKey(playlistName)) {
            throw new Error("Playlist does not exist");
        }
        playlists.get(playlistName).addSongToPlaylist(song);
    }

    public Playlist getPlaylist(String name){
        if (!playlists.containsKey(name)) {
            throw new Error("Playlist not found");
        }
        return playlists.get(name);
    }
}
