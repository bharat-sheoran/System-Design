package core;

import device.IAudioOutputDevice;
import models.Song;

public class AudioEngine {
    private Song currSong;
    private boolean isPaused;

    public AudioEngine(){
        this.currSong = null;
        this.isPaused = false;
    }

    public String getCurrentSongTitle(){
        if (currSong != null) {
            currSong.getTitle();
        }
        return "";
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void play(IAudioOutputDevice audio, Song song){
        if (song == null) {
            throw new Error("Cannot play a null song");
        }

        if (isPaused && song == currSong) {
            isPaused = false;
            System.out.println("Resuming Song: " + song.getTitle() + "\n");
            audio.playAudio(song);
            return;
        }

        currSong = song;
        isPaused = false;
        System.out.println("Playing Song: " + song.getTitle() + "\n");
        audio.playAudio(song);
    }

    public void pause(){
        if (currSong == null) {
            throw new Error("No Song is Currently playing");
        }
        if(isPaused){
            throw new Error("Song is Already Paused");
        }
        System.out.println("Pausing Song" + currSong.getTitle() + "\n");
    }
}
