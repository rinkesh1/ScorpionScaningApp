package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class THCStockUpdate {
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("FinYear")
    @Expose
    private String finYear;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("isDEPSFromAndroid")
    @Expose
    private String isDEPSFromAndroid;

    private String DeviceId;
    private String IMEINo;
    private String ModelName;
    private String AppVersion;
    private String DeviceStorage;
    private String ScanLocation;

    @SerializedName("ThcStockDetail")
    @Expose
    private ArrayList<ThcStockDetail> thcStockDetail = null;
    @SerializedName("ExtraScannedDkts")
    @Expose
    private ArrayList<ExtraScannedDkt> ExtraScannedDkts = null;

    public THCStockUpdate() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getIsDEPSFromAndroid() {
        return isDEPSFromAndroid;
    }

    public void setIsDEPSFromAndroid(String isDEPSFromAndroid) {
        this.isDEPSFromAndroid = isDEPSFromAndroid;
    }

    public ArrayList<ThcStockDetail> getThcStockDetail() {
        return thcStockDetail;
    }

    public void setThcStockDetail(ArrayList<ThcStockDetail> thcStockDetail) {
        this.thcStockDetail = thcStockDetail;
    }

    public ArrayList<ExtraScannedDkt> getExtraScannedDkts() {
        return ExtraScannedDkts;
    }

    public void setExtraScannedDkts(ArrayList<ExtraScannedDkt> extraScannedDkts) {
        this.ExtraScannedDkts = extraScannedDkts;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
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
