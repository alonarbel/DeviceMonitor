package com.ssi.devicemonitor.entity;

public class HardwareDevice extends Device{

    private String location,macAddress;

    public HardwareDevice(String name) {
        super(name);
        location = "unknown";
        macAddress = "unknown";

    }
    public HardwareDevice(String name, String manufacturer, String location, String macAddress, int version){
        super(name);
        this.setTypeToHardware();
        this.setManufacturer(manufacturer);
        this.location = location;
        this.macAddress = macAddress;
        this.setVersion(version);
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }
    public String toString() {
        String str = "name: "+this.getName()+"\ntype: "+this.getType()+"\nmanufacturer: "+this.getManufacturer()
                +"\nversion: "+this.getVersion()+"\nlocation: "+this.location+"\nmacAddress: "+this.macAddress;
        return str;
    }
}
