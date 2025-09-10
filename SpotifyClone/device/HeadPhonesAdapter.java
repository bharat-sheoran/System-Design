package device;

import external.HeadPhoneSpeakerApi;
import models.Song;

public class HeadPhonesAdapter implements IAudioOutputDevice {
    private HeadPhoneSpeakerApi headPhoneSpeakerApi;

    public HeadPhonesAdapter(HeadPhoneSpeakerApi headPhoneSpeakerApi){
        this.headPhoneSpeakerApi = headPhoneSpeakerApi;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        this.headPhoneSpeakerApi.playSoundViaHeadphone(payload);
    }
}
