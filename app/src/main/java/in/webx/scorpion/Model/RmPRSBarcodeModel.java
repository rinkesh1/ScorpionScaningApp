package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RmPRSBarcodeModel {
    @SerializedName("docketNo")
    @Expose
    private String docketNo;
    @SerializedName("bcSeriesNo")
    @Expose
    private String bcSeriesNo;
    @SerializedName("isScanned")
    @Expose
    private Boolean isScanned;
    @SerializedName("vehicleno")
    @Expose
    private String vehicleno;

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getBcSeriesNo() {
        return bcSeriesNo;
    }

    public void setBcSeriesNo(String bcSeriesNo) {
        this.bcSeriesNo = bcSeriesNo;
    }

    public Boolean getIsScanned() {
        return isScanned;
    }

    public void setIsScanned(Boolean isScanned) {
        this.isScanned = isScanned;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }
}
