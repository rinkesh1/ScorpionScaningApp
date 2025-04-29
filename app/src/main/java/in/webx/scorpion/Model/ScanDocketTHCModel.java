package in.webx.scorpion.Model;

import java.util.List;

public class ScanDocketTHCModel {
    private String AppVersion;
    private List<ScanTHCDocketDetailsModel> Details;
    private String DeviceDetails;
    private String EntryBy;
    private String OSVersion;

    private String DeviceId;
    private String IMEINo;
    private String ModelName;
    private String DeviceStorage;
    private String ScanLocation;


    public void setAppVersion(String AppVersion) {
        this.AppVersion = AppVersion;
    }

    public String getAppVersion() {
        return this.AppVersion;
    }

    public void setDetails(List<ScanTHCDocketDetailsModel> Details) {
        this.Details = Details;
    }

    public List<ScanTHCDocketDetailsModel> getDetails() {
        return this.Details;
    }

    public void setDeviceDetails(String DeviceDetails) {
        this.DeviceDetails = DeviceDetails;
    }

    public String getDeviceDetails() {
        return this.DeviceDetails;
    }

    public void setEntryBy(String EntryBy) {
        this.EntryBy = EntryBy;
    }

    public String getEntryBy() {
        return this.EntryBy;
    }

    public void setOSVersion(String OSVersion) {
        this.OSVersion = OSVersion;
    }

    public String getOSVersion() {
        return this.OSVersion;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getIMEINo() {
        return IMEINo;
    }

    public void setIMEINo(String IMEINo) {
        this.IMEINo = IMEINo;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getDeviceStorage() {
        return DeviceStorage;
    }

    public void setDeviceStorage(String deviceStorage) {
        DeviceStorage = deviceStorage;
    }

    public String getScanLocation() {
        return ScanLocation;
    }

    public void setScanLocation(String scanLocation) {
        ScanLocation = scanLocation;
    }
}

