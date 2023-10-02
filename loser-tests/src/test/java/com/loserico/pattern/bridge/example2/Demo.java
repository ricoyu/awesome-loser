package com.loserico.pattern.bridge.example2;

import com.loserico.pattern.bridge.example2.devices.Device;
import com.loserico.pattern.bridge.example2.devices.Radio;
import com.loserico.pattern.bridge.example2.devices.Tv;
import com.loserico.pattern.bridge.example2.remotes.AdvancedRemote;
import com.loserico.pattern.bridge.example2.remotes.BasicRemote;

public class Demo {
    public static void main(String[] args) {
        testDevice(new Tv());
        testDevice(new Radio());
    }

    public static void testDevice(Device device) {
        System.out.println("Tests with basic remote.");
        BasicRemote basicRemote = new BasicRemote(device);
        basicRemote.power();
        device.printStatus();

        System.out.println("Tests with advanced remote.");
        AdvancedRemote advancedRemote = new AdvancedRemote(device);
        advancedRemote.power();
        advancedRemote.mute();
        device.printStatus();
    }
}
