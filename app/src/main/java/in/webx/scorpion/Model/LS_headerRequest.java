package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LS_headerRequest {

    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("LSNo")
    @Expose
    private String lSNo;
    @SerializedName("LSSF")
    @Expose
    private String lSSF;
    @SerializedName("LSDate")
    @Expose
    private String lSDate;
    @SerializedName("Dockets")
    @Expose
    private Integer dockets;
    @SerializedName("Packages")
    @Expose
    private Integer packages;
    @SerializedName("Weight")
    @Expose
    private Double weight;
    @SerializedName("Volume")
    @Expose
    private Double volume;
    @SerializedName("UpdateBy")
    @Expose
    private String updateBy;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;
    @SerializedName("OSVersion")
    @Expose
    private String oSVersion;
    @SerializedName("DeviceDetails")
    @Expose
    private String deviceDetails;

    private String DeviceId;
    private String IMEINo;
    private String ModelName;
    private String DeviceStorage;
    private String ScanLocation;


    private List<DocketList> DocketList;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getLSNo() {
        return lSNo;
    }

    public void setLSNo(String lSNo) {
        this.lSNo = lSNo;
    }

    public String getLSSF() {
        return lSSF;
    }

    public void setLSSF(String lSSF) {
        this.lSSF = lSSF;
    }

    public String getLSDate() {
        return lSDate;
    }

    public void setLSDate(String lSDate) {
        this.lSDate = lSDate;
    }

    public Integer getDockets() {
        return dockets;
    }

    public void setDockets(Integer dockets) {
        this.dockets = dockets;
    }

    public Integer getPackages() {
        return packages;
    }

    public void setPackages(Integer packages) {
        this.packages = packages;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOSVersion() {
        return oSVersion;
    }

    public void setOSVersion(String oSVersion) {
        this.oSVersion = oSVersion;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public List<DocketList> getDocketList() {
        return DocketList;
    }

    public void setDocketList(List<DocketList> docketList) {
        DocketList = docketList;
    }

    public String getoSVersion() {
        return oSVersion;
    }

    public void setoSVersion(String oSVersion) {
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
