package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class GetValidLoadingSheetData_Tbl extends RealmObject {

    @PrimaryKey
    private String lsNo;

    @Required
    private Integer companyCode;
    private Boolean isSuccess;
    private String errorMessage;
    private String lsDate;
    private String toBhCode;
    private Integer totalDockets;
    private Integer totalPackages;
    private Double totalActualWeight;
    private Double totalCftWeight;
    private Integer totalLoadPackages;
    private Double totalLoadActualWeight;
    private Double totalLoadCftWeight;
    private Integer totalScanBarcode;
    private String isBcProcess;
    private String user;

    public GetValidLoadingSheetData_Tbl() {
    }

    public String getLsNo() {
        return lsNo;
    }

    public void setLsNo(String lsNo) {
        this.lsNo = lsNo;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLsDate() {
        return lsDate;
    }

    public void setLsDate(String lsDate) {
        this.lsDate = lsDate;
    }

    public String getToBhCode() {
        return toBhCode;
    }

    public void setToBhCode(String toBhCode) {
        this.toBhCode = toBhCode;
    }

    public Integer getTotalDockets() {
        return totalDockets;
    }

    public void setTotalDockets(Integer totalDockets) {
        this.totalDockets = totalDockets;
    }

    public Integer getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(Integer totalPackages) {
        this.totalPackages = totalPackages;
    }

    public Double getTotalActualWeight() {
        return totalActualWeight;
    }

    public void setTotalActualWeight(Double totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    public Double getTotalCftWeight() {
        return totalCftWeight;
    }

    public void setTotalCftWeight(Double totalCftWeight) {
        this.totalCftWeight = totalCftWeight;
    }

    public Integer getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Integer totalLoadPackages) {
        this.totalLoadPackages = totalLoadPackages;
    }

    public Double getTotalLoadActualWeight() {
        return totalLoadActualWeight;
    }

    public void setTotalLoadActualWeight(Double totalLoadActualWeight) {
        this.totalLoadActualWeight = totalLoadActualWeight;
    }

    public Double getTotalLoadCftWeight() {
        return totalLoadCftWeight;
    }

    public void setTotalLoadCftWeight(Double totalLoadCftWeight) {
        this.totalLoadCftWeight = totalLoadCftWeight;
    }

    public Integer getTotalScanBarcode() {
        return totalScanBarcode;
    }

    public void setTotalScanBarcode(Integer totalScanBarcode) {
        this.totalScanBarcode = totalScanBarcode;
    }

    public String getIsBcProcess() {
        return isBcProcess;
    }

    public void setIsBcProcess(String isBcProcess) {
        this.isBcProcess = isBcProcess;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}