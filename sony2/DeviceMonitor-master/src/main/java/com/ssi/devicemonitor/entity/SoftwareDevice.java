package com.ssi.devicemonitor.entity;

import java.time.LocalDateTime;

public class SoftwareDevice extends Device{
    private LocalDateTime installationData;

    public SoftwareDevice(String name) {
        super(name);
    }
    public SoftwareDevice(String name, String manufacturer,LocalDateTime installationData, int version){
        super(name);
        this.setTypeToSoftware();
        this.setManufacturer(manufacturer);
        this.installationData = installationData;
        this.setVersion(version);
    }
    public void setInstallationData(LocalDateTime date){
        this.installationData = date;
    }

    @Override
    public String toString() {
        String str = "name: "+this.getName()+"\ntype: "+this.getType()+"\nmanufacturer: "+this.getManufacturer()
                +"\nversion: "+this.getVersion()+"\ninstallationData: "+this.installationData;
        return str;
    }
}