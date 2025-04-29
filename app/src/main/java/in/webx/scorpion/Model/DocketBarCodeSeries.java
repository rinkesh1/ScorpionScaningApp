package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DocketBarCodeSeries implements Serializable {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("dockNo")
    @Expose
    private String dockNo;
    @SerializedName("dockSf")
    @Expose
    private String dockSf;
    @SerializedName("bcSerialNo")
    @Expose
    private String bcSerialNo;
    @SerializedName("isBarcodeScanned")
    @Expose
    private boolean isBarcodeScanned;
    @SerializedName("bcScannedDatetime")
    @Expose
    private Object bcScannedDatetime;
    @SerializedName("isContinueScanned")
    @Expose
    private boolean isContinueScanned;

    private String bcsrNo;

    public DocketBarCodeSeries() {
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

    public String getBcSerialNo() {
        return bcSerialNo;
    }

    public void setBcSerialNo(String bcSerialNo) {
        this.bcSerialNo = bcSerialNo;
    }

    public boolean getIsBarcodeScanned() {
        return isBarcodeScanned;
    }

    public void setIsBarcodeScanned(boolean isBarcodeScanned) {
        this.isBarcodeScanned = isBarcodeScanned;
    }

    public Object getBcScannedDatetime() {
        return bcScannedDatetime;
    }

    public void setBcScannedDatetime(Object bcScannedDatetime) {
        this.bcScannedDatetime = bcScannedDatetime;
    }

    public boolean getIsContinueScanned() {
        return isContinueScanned;
    }

    public void setIsContinueScanned(boolean isContinueScanned) {
        this.isContinueScanned = isContinueScanned;
    }

    public String getBcsrNo() {
        return bcsrNo;
    }

    public void setBcsrNo(String bcsrNo) {
        this.bcsrNo = bcsrNo;
    }
}
