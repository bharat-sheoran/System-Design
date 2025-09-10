package models;

import java.util.ArrayList;

public class Playlist {
    private String playlistName;
    private ArrayList<Song> songList;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        songList = new ArrayList<>();
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public ArrayList<Song> getSongs() {
        return songList;
    }

    public int getSize(){
        return songList.size();
    }

    public void addSongToPlaylist(Song song){
        if (song == null) {
            throw new Error("Can not add Null song to Playlist");
        }
        songList.add(song);
    }
}
