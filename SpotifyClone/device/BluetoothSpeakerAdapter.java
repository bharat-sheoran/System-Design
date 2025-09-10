package device;

import external.BluetoothSpeakerApi;
import models.Song;

public class BluetoothSpeakerAdapter implements IAudioOutputDevice {
    private BluetoothSpeakerApi bluetoothSpeakerApi;

    public BluetoothSpeakerAdapter(BluetoothSpeakerApi bluetoothSpeakerApi){
        this.bluetoothSpeakerApi = bluetoothSpeakerApi;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        this.bluetoothSpeakerApi.playSoundViaBluetooth(payload);
    }
    
}
