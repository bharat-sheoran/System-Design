package factories;

import device.BluetoothSpeakerAdapter;
import device.IAudioOutputDevice;
import device.WiredSpeakerAdapter;
import enums.DeviceType;
import external.BluetoothSpeakerApi;
import external.WiredSpeakerApi;

public class DeviceFactory {
    public static IAudioOutputDevice createDevice(DeviceType deviceType){
        if (deviceType == DeviceType.BLUETOOTH) {
            return new BluetoothSpeakerAdapter(new BluetoothSpeakerApi());
        } else if(deviceType == DeviceType.WIRED){
            return new WiredSpeakerAdapter(new WiredSpeakerApi());
        } else {
            return new WiredSpeakerAdapter(new WiredSpeakerApi());
        }
    }
}
