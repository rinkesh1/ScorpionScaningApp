package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PRSBarcodeDetails extends RealmObject {
    @PrimaryKey
    private String barcodeId;
    private String UserId;

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

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
