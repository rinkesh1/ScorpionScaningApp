package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LsHader_Tbl extends RealmObject {

    @PrimaryKey
    private String lsNo;

    @Required
    private Integer companyCode;
    private String lsDate;
    private String destination;
    private String branchCode;
    private Integer totalDockets;
    private Integer totalPackages;
    private Integer totalLoadPackages;
    private Double totalActualWeight;
    private Double totalCftWeight;
    private Double totalLoadActualWeight;
    private Double totalLoadCftWeight;
    private String isBcProcess;
    private int totalScanBarcode;
    private boolean isLsScane;
    private String user;


    public LsHader_Tbl() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getLsNo() {
        return lsNo;
    }

    public void setLsNo(String lsNo) {
        this.lsNo = lsNo;
    }

    public String getLsDate() {
        return lsDate;
    }

    public void setLsDate(String lsDate) {
        this.lsDate = lsDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public Integer getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Integer totalLoadPackages) {
        this.totalLoadPackages = totalLoadPackages;
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

    public int getTotalScanBarcode() {
        return totalScanBarcode;
    }

    public void setTotalScanBarcode(int totalScanBarcode) {
        this.totalScanBarcode = totalScanBarcode;
    }

    public String getIsBcProcess() {
        return isBcProcess;
    }

    public void setIsBcProcess(String isBcProcess) {
        this.isBcProcess = isBcProcess;
    }

    public boolean getIsLsScane() {
        return isLsScane;
    }

    public void setIsLsScane(boolean lsScane) {
        isLsScane = lsScane;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
