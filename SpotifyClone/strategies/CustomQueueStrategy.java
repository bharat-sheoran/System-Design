package strategies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import models.Playlist;
import models.Song;

public class CustomQueueStrategy extends PlayStrategy {
    private Playlist currPlaylist;
    private int currentIndex;
    private Queue<Song> nextQueue;
    private Stack<Song> prevStack;

    private Song nextSequential() {
        if (currPlaylist.getSize() == 0) {
            throw new Error("Playlist is empty");
        }
        currentIndex = currentIndex + 1;
        return currPlaylist.getSongs().get(currentIndex);
    }

    private Song previousSequential() {
        if (currPlaylist.getSize() == 0) {
            throw new Error("Playlist is empty");
        }
        currentIndex = currentIndex - 1;
        return currPlaylist.getSongs().get(currentIndex);
    }

    public CustomQueueStrategy() {
        currPlaylist = null;
        currentIndex = -1;
        nextQueue = new LinkedList<>();
        prevStack = new Stack<>();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        currPlaylist = playlist;
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            return;
        }

        while (!nextQueue.isEmpty()) {
            nextQueue.remove();
        }

        while (!prevStack.isEmpty()) {
            prevStack.pop();
        }
    }

    @Override
    public Song next() {
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            throw new Error("No Playlist loaded or Playlist is Empty");
        }

        if (!nextQueue.isEmpty()) {
            Song s = nextQueue.peek();
            nextQueue.remove();
            prevStack.push(s);

            ArrayList<Song> list = currPlaylist.getSongs();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == s) {
                    currentIndex = i;
                    break;
                }
            }
            return s;
        }

        return nextSequential();
    }

    @Override
    public boolean hasNext() {
        return ((currentIndex + 1) < currPlaylist.getSize());
    }

    @Override
    public Song prev() {
        if (currPlaylist == null || currPlaylist.getSize() == 0) {
            throw new Error("No Playlist loaded or Playlist is Empty");
        }

        if (!prevStack.isEmpty()) {
            Song s = prevStack.peek();
            prevStack.pop();

            ArrayList<Song> list = currPlaylist.getSongs();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == s) {
                    currentIndex = i;
                    break;
                }
            }
            return s;
        }
        return previousSequential();
    }

    @Override
    public boolean hasPrevious() {
        return (currentIndex - 1 > 0);
    }

    @Override
    public void addToNext(Song song) {
        if (song == null) {
            throw new Error("Song is null");
        }
        nextQueue.add(song);
    }

}
