package device;

import external.WiredSpeakerApi;
import models.Song;

public class WiredSpeakerAdapter implements IAudioOutputDevice {
    private WiredSpeakerApi wiredSpeakerApi;

    public WiredSpeakerAdapter(WiredSpeakerApi wiredSpeakerApi){
        this.wiredSpeakerApi = wiredSpeakerApi;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        this.wiredSpeakerApi.playSoundViaWire(payload);
    }
}
