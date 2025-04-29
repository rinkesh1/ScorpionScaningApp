package in.webx.scorpion.RealmDatabase;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class docketBarCodeSeries_Tbl extends RealmObject implements Serializable {

    @PrimaryKey
    private String bcSerialNo;

    @Required
    private Integer companyCode;
    private String dockNo;
    private String dockSf;
    private boolean isBarcodeScanned;
    private String bcScannedDatetime;
    private boolean isContinueScanned;
    private String LsThcNo;
    private String bcsrNumber;
    private String barcodeType;
    private String user;
    private String branchCode;
    private double actualweight;
    private double actualCFweight;
    private boolean isServerPush;

    public docketBarCodeSeries_Tbl() {
    }

    public String getBcSerialNo() {
        return bcSerialNo;
    }

    public void setBcSerialNo(String bcSerialNo) {
        this.bcSerialNo = bcSerialNo;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    public String getDockSf() {
        return dockSf;
    }

    public void setDockSf(String dockSf) {
        this.dockSf = dockSf;
    }

    public boolean getBarcodeScanned() {
        return isBarcodeScanned;
    }

    public void setBarcodeScanned(boolean barcodeScanned) {
        isBarcodeScanned = barcodeScanned;
    }

    public String getBcScannedDatetime() {
        return bcScannedDatetime;
    }

    public void setBcScannedDatetime(String bcScannedDatetime) {
        this.bcScannedDatetime = bcScannedDatetime;
    }

    public boolean getContinueScanned() {
        return isContinueScanned;
    }

    public void setContinueScanned(boolean continueScanned) {
        isContinueScanned = continueScanned;
    }

    public String getLsThcNo() {
        return LsThcNo;
    }

    public void setLsThcNo(String lsThcNo) {
        LsThcNo = lsThcNo;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getBcsrNumber() {
        return bcsrNumber;
    }

    public void setBcsrNumber(String bcsrNumber) {
        this.bcsrNumber = bcsrNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getActualweight() {
        return actualweight;
    }

    public void setActualweight(double actualweight) {
        this.actualweight = actualweight;
    }

    public double getActualCFweight() {
        return actualCFweight;
    }

    public void setActualCFweight(double actualCFweight) {
        this.actualCFweight = actualCFweight;
    }

    public boolean getServerPush() {
        return isServerPush;
    }

    public void setServerPush(boolean serverPush) {
        isServerPush = serverPush;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
