package device;

import models.Song;

public interface IAudioOutputDevice {
    public void playAudio(Song song);
}
