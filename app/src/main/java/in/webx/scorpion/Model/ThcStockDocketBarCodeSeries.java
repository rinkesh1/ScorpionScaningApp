package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThcStockDocketBarCodeSeries {
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("DockNo")
    @Expose
    private String dockNo;
    @SerializedName("DockSf")
    @Expose
    private String dockSf;
    @SerializedName("BCSerialNo")
    @Expose
    private String bCSerialNo;
    @SerializedName("IsBarcodeScanned")
    @Expose
    private Boolean isBarcodeScanned;
    @SerializedName("BCScannedDatetime")
    @Expose
    private String bCScannedDatetime;
    @SerializedName("IsContinueScanned")
    @Expose
    private Boolean isContinueScanned;
    /*private Boolean IsDamage;
    private Boolean IsExtraBarcode;
    private Boolean IsManual;
    private Boolean IsPilferage;*/


    public ThcStockDocketBarCodeSeries() {
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

    public String getbCSerialNo() {
        return bCSerialNo;
    }

    public void setbCSerialNo(String bCSerialNo) {
        this.bCSerialNo = bCSerialNo;
    }



    public String getbCScannedDatetime() {
        return bCScannedDatetime;
    }

    public void setbCScannedDatetime(String bCScannedDatetime) {
        this.bCScannedDatetime = bCScannedDatetime;
    }

    public Boolean getBarcodeScanned() {
        return isBarcodeScanned;
    }

    public void setBarcodeScanned(Boolean barcodeScanned) {
        isBarcodeScanned = barcodeScanned;
    }

    public Boolean getContinueScanned() {
        return isContinueScanned;
    }

    public void setContinueScanned(Boolean continueScanned) {
        isContinueScanned = continueScanned;
    }
}
