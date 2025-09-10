package strategies;

import models.Playlist;
import models.Song;

public class SequentialPlayStrategy extends PlayStrategy{
    private Playlist currPlaylist;
    private int currentIndex;

    public SequentialPlayStrategy(){
        currPlaylist = null;
        currentIndex = -1;
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        currPlaylist = playlist;
        currentIndex = -1;
    }

    @Override
    public Song next() {
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            throw new Error("No Playlist loaded or Playlist is Empty");
        }
        currentIndex = currentIndex + 1;
        return currPlaylist.getSongs().get(currentIndex);
    }

    @Override
    public boolean hasNext() {
        return ((currentIndex + 1) < currPlaylist.getSize());
    }

    @Override
    public Song prev() {
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            throw new Error("No Playlist Loaded or Playlist is empty");
        }

        currentIndex = currentIndex - 1;
        return currPlaylist.getSongs().get(currentIndex);
    }

    @Override
    public boolean hasPrevious() {
        return (currentIndex - 1 > 0);
    }
    
}
