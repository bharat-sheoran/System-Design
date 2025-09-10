package strategies;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import models.Playlist;
import models.Song;

public class RandomPlayStrategy extends PlayStrategy {
    private Playlist currPlaylist;
    private ArrayList<Song> remainingSongs;
    private Stack<Song> history;

    public RandomPlayStrategy() {
        currPlaylist = null;
        remainingSongs = new ArrayList<>();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        currPlaylist = playlist;
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            return;
        }

        remainingSongs = currPlaylist.getSongs();
        history = new Stack<>();
    }

    @Override
    public Song next() {
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            throw new Error("No Playlist Loaded or playlist is empty.");
        }
        if (remainingSongs.isEmpty()) {
            throw new Error("No more songs to play in the playlist.");
        }

        Random random = new java.util.Random(System.currentTimeMillis());
        int idx = random.nextInt(remainingSongs.size());
        Song selectedSong = remainingSongs.get(idx);

        Song temp = remainingSongs.get(idx);
        remainingSongs.set(idx, remainingSongs.get(remainingSongs.size() - 1));
        remainingSongs.set(remainingSongs.size() - 1, temp);
        remainingSongs.remove(remainingSongs.size() - 1);    
        history.push(selectedSong);
        return selectedSong;
    }


    @Override
    public boolean hasNext() {
        return currPlaylist != null && !remainingSongs.isEmpty();
    }

    @Override
    public Song prev() {
        if (history.isEmpty()) {
            throw new Error("No Previous Song Availaible");
        }

        Song song = history.peek();
        history.pop();
        return song;
    }

    @Override
    public boolean hasPrevious() {
        return history.size() > 0;
    }

}
