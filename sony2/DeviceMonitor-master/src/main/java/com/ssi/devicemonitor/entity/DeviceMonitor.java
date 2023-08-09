package com.ssi.devicemonitor.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceMonitor {
    private List<Device> devices;
    private Timer statusUpdateTimer;

    public DeviceMonitor() {
        devices = new ArrayList<>();

        // Start the timer to simulate status updates every few seconds
        statusUpdateTimer = new Timer();
        statusUpdateTimer.schedule(new StatusUpdateTask(), 0, 5000); // Update every 5 second
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public void removeDevice(Device device) {
        this.devices.remove(device);
    }


    private class StatusUpdateTask extends TimerTask {
        private Random random = new Random();

        @Override
        public void run() {
            for (Device device : devices) {
                // Simulate random status updates
                boolean isOnline = random.nextBoolean();
                device.setStatus(isOnline ? "Online" : "Offline");
            }
        }
    }
}
