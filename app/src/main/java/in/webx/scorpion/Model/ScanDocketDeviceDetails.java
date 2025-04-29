package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanDocketDeviceDetails {
    @SerializedName("EntryBy")
    @Expose
    private Object entryBy;
    @SerializedName("Details")
    @Expose
    private List<DocketUpdateScan> details = null;
    @SerializedName("AppVersion")
    @Expose
    private Object appVersion;
    @SerializedName("OSVersion")
    @Expose
    private Object oSVersion;
    @SerializedName("DeviceDetails")
    @Expose
    private String deviceDetails;

    private String DeviceId;
    private String IMEINo;
    private String ModelName;
    private String DeviceStorage;
    private String ScanLocation;

    public Object getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(Object entryBy) {
        this.entryBy = entryBy;
    }

    public List<DocketUpdateScan> getDetails() {
        return details;
    }

    public void setDetails(List<DocketUpdateScan> details) {
        this.details = details;
    }

    public Object getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Object appVersion) {
        this.appVersion = appVersion;
    }

    public Object getOSVersion() {
        return oSVersion;
    }

    public void setOSVersion(Object oSVersion) {
        this.oSVersion = oSVersion;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public Object getoSVersion() {
        return oSVersion;
    }

    public void setoSVersion(Object oSVersion) {
        this.oSVersion = oSVersion;
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
