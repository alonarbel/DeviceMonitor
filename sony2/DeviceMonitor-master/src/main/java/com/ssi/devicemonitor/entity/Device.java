package com.ssi.devicemonitor.entity;

public abstract class Device {
    private String name;
    private String status;

    private int version;

    private String manufacturer;

    private Enum type;

    enum Type {
        SOFTWARE,
        HARDWARE
    }

    public Device(String name) {
        this.name = name;
        this.status = "Offline"; // Set initial status to Offline
        this.version = 0;
        this.manufacturer="unknown";
        this.type = Type.SOFTWARE;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getManufacturer(){
        return manufacturer;
    }
    public int getVersion(){
        return version;
    }
    public String getType(){
        return type.toString();
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTypeToSoftware(){
        this.type = Type.SOFTWARE;
    }
    public void setTypeToHardware(){
        this.type = Type.HARDWARE;
    }
    public void setManufacturer(String man){
        this.manufacturer = man;
    }
    public void setVersion(int newVersion){
        this.version = newVersion;
    }
}
