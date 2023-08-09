package com.ssi.devicemonitor.controller;

import com.ssi.devicemonitor.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.Proxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceMonitorController {
    @FXML
    private ListView<Device> deviceListView;
    @FXML
    private TextField deviceNameTextField;
    private DeviceMonitor deviceMonitor;
    //task4
    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton hardwareButton,computer1Button;
    @FXML
    private ChoiceBox manufacturer,macAddress;
    @FXML
    private Spinner<Integer> versionSpinner,timeSpinner;
    @FXML
    private Label commentsLabel;

    String[] manufacturers ={"SONY","APPLE","NVIDIA"};

    String[] macAddresses={
            "00-1B-63-84-45-E6",
            "00-B0-D0-63-C2-26"
    };
    String currManufacturer, currMacAddress;
    int currVersion, currHour;
    //task5
    @FXML
    private TextArea dataField;

    //task7
    private LocalTime lastUpdate,nextUpdate;


    public void initialize() {
        deviceMonitor = new DeviceMonitor();
        deviceListView.setCellFactory(deviceListView -> new DeviceListCell());

        //task4
        manufacturer.getItems().addAll(manufacturers);
        manufacturer.setValue("SONY");
        macAddress.getItems().addAll(macAddresses);
        macAddress.setValue("00-1B-63-84-45-E6");
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
        valueFactory1.setValue(1);
        versionSpinner.setValueFactory(valueFactory1);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,23);
        valueFactory2.setValue(0);
        timeSpinner.setValueFactory(valueFactory2);



        // Add context menu to ListView
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove");//task2
        MenuItem dataItem = new MenuItem("Data");//task5
        MenuItem editItem = new MenuItem("Edit");//task6

        removeItem.setOnAction(event -> {
            Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
            if (selectedDevice != null) {
                deviceMonitor.removeDevice(selectedDevice);
                refreshListView();
                commentsLabel.setText("Device Removed successfully!");
                resetAllFields();
            }
        });
        //data about device task 5
        dataItem.setOnAction(event -> {
            Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
            if (selectedDevice != null) {
                dataField.setText(selectedDevice.toString());
                commentsLabel.setText(selectedDevice.getName()+" data is presented successfully!");
            }
        });

        //edit device task 6
        editItem.setOnAction(event -> {
            Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
            String location;
            LocalDate newDate;
            if(selectedDevice != null){
                int index = deviceMonitor.getDevices().indexOf(selectedDevice);
                if(deviceNameTextField.getText().length()==0){
                    commentsLabel.setText("please enter a name!");
                    return;
                }
                if(computer1Button.isSelected()){
                    location = "computer1";
                }
                else {
                    location = "computer2";
                }
                if(datePicker.getValue()==null){
                    commentsLabel.setText("ERROR: Please enter a date.");
                    return;
                }
                newDate = datePicker.getValue();
                currHour = timeSpinner.getValue();
                LocalTime time = LocalTime.of(currHour,0);
                if(hardwareButton.isSelected()){//the new edited item will be hardware
                    if(selectedDevice.getType().equals("HARDWARE")){//the device is now hardware
                        HardwareDevice hardwareDevice = (HardwareDevice) selectedDevice;
                        hardwareDevice.setLocation(location);
                        hardwareDevice.setMacAddress(macAddress.getValue().toString());
                        selectedDevice = hardwareDevice;
                    }
                    else{//the device is now software
                        deviceMonitor.getDevices().remove(index);
                        HardwareDevice hardwareDevice = new HardwareDevice(deviceNameTextField.getText(),manufacturer.getValue().toString(),location,
                                macAddress.getValue().toString(),versionSpinner.getValue());
                        deviceMonitor.getDevices().add(index, hardwareDevice);
                        selectedDevice = hardwareDevice;
                    }
                }else {//the new edited device will be software
                    if(selectedDevice.getType().equals("SOFTWARE")){//the device is now software
                        SoftwareDevice softwareDevice = (SoftwareDevice) selectedDevice;
                        softwareDevice.setInstallationData(LocalDateTime.of(newDate,time));
                        selectedDevice = softwareDevice;
                    }
                    else {//the device is now hardware
                        deviceMonitor.getDevices().remove(index);
                        SoftwareDevice softwareDevice = new SoftwareDevice(deviceNameTextField.getText(),manufacturer.getValue().toString(),
                                LocalDateTime.of(newDate,time),versionSpinner.getValue());
                        deviceMonitor.getDevices().add(index, softwareDevice);
                        selectedDevice = softwareDevice;
                    }
                }
                selectedDevice.setName(deviceNameTextField.getText());
                selectedDevice.setManufacturer(manufacturer.getValue().toString());
                selectedDevice.setVersion(versionSpinner.getValue());
                refreshListView();
                resetAllFields();
                dataField.setText(selectedDevice.toString());
                commentsLabel.setText("Device edited successfully!");
            }
        });
        contextMenu.getItems().addAll(removeItem,dataItem,editItem);
        deviceListView.setContextMenu(contextMenu);
    }
    private void resetAllFields(){//reset all fields to default
        deviceNameTextField.clear();
        dataField.setText("");
        versionSpinner.getValueFactory().setValue(1);
        timeSpinner.getValueFactory().setValue(0);
        manufacturer.setValue("SONY");
        macAddress.setValue("00-1B-63-84-45-E6");
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void clearData(){//clear the data field
        dataField.setText("");
        commentsLabel.setText("Data cleared successfully");
    }

    @FXML
    private void addDevice() {
        if(deviceNameTextField.getText().length()==0){
            commentsLabel.setText("please enter a name!");
            return;
        }
        String deviceName = deviceNameTextField.getText();
        Device newDevice;
        currManufacturer = manufacturer.getValue().toString();
        currVersion = versionSpinner.getValue();
        if(hardwareButton.isSelected()){
            currMacAddress = macAddress.getValue().toString();
            String location;
            if(computer1Button.isSelected()){
                location = "computer1";
            }
            else {
                location = "computer2";
            }
            newDevice = new HardwareDevice(deviceName,currManufacturer,location,currMacAddress,currVersion);
        }else {
            if(datePicker.getValue()==null){
                commentsLabel.setText("ERROR: Please enter a date.");
                return;
            }
            LocalDate date = datePicker.getValue();
            currHour = timeSpinner.getValue();
            LocalTime time = LocalTime.of(currHour,0);
            newDevice = new SoftwareDevice(deviceName,currManufacturer, LocalDateTime.of(date,time),currVersion);
        }
        commentsLabel.setText(newDevice.getName()+" added successfully");
        deviceMonitor.addDevice(newDevice);
        deviceNameTextField.clear();
        refreshListView();
        resetAllFields();
    }


    public void refreshListView() {//refresh the list view to support all the devices
        deviceListView.setItems(FXCollections.observableList(deviceMonitor.getDevices()));
    }

    @FXML
    private void showUpdateTimeDialog(){//for task 7
        if(lastUpdate==null){
            commentsLabel.setText("ERROR: didn't updated yet.");
            return;
        }
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Times");
        dialog.setHeaderText("Device List View Update Times");
        Label lastUpdateTimeLabel = new Label("Last Update Time: " + getLastUpdateTime());
        Label nextUpdateTimeLabel = new Label("Next Update Time: " + getNextUpdateTime());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(lastUpdateTimeLabel, nextUpdateTimeLabel);
        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
    private String getLastUpdateTime() {
        return lastUpdate.toString().substring(0,8);
    }

    private String getNextUpdateTime() {
        return nextUpdate.toString().substring(0,8);
    }


    private class DeviceListCell extends ListCell<Device> {
        @Override
        protected void updateItem(Device device, boolean empty) {
            super.updateItem(device, empty);
            lastUpdate = LocalTime.now();
            nextUpdate = lastUpdate.plusSeconds(5);
            if (device == null || empty) {
                setText(null);
                setGraphic(null);
                setStyle(""); // Reset the cell style
            } else {
                setText(device.getName() + " - " + device.getStatus());

                // Set the cell style based on the device status
                if (device.getStatus().equals("Online")) {
                    setStyle("-fx-text-fill: green;");
                } else if (device.getStatus().equals("Offline")) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle(""); // Reset the cell style
                }
            }

        }
    }
}
