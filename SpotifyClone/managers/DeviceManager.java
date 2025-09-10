package managers;

import device.IAudioOutputDevice;
import enums.DeviceType;
import factories.DeviceFactory;

public class DeviceManager {
    private static DeviceManager instance;
    private IAudioOutputDevice currAudioOutputDevice;

    private DeviceManager(){
        currAudioOutputDevice = null;
    }

    public static DeviceManager getInstance(){
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public void connect(DeviceType deviceType){
        currAudioOutputDevice = DeviceFactory.createDevice(deviceType);

        switch (deviceType) {
            case DeviceType.BLUETOOTH:
                System.out.println("Bluetooth Device Connected");
                break;
            case DeviceType.WIRED:
                System.out.println("Wired Device Connected");
                break;
            case DeviceType.HEADPHONES:
                System.out.println("Headphones Device Connected");
                break;
            default:
                break;
        }
    }

    public IAudioOutputDevice getOutputDevice(){
        if (currAudioOutputDevice == null) {
            throw new Error("No Output device is connected");
        }
        return currAudioOutputDevice;
    }

    public boolean hasOutputDevice(){
        return currAudioOutputDevice != null;
    }
}
