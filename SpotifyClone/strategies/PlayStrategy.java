package strategies;

import models.Playlist;
import models.Song;

public abstract class PlayStrategy {
    public abstract void setPlaylist(Playlist playlist);
    public abstract Song next();
    public abstract boolean hasNext();
    public abstract Song prev();
    public abstract boolean hasPrevious();
    public void addToNext(Song song) {};
}
