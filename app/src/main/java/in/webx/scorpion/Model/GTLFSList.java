package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GTLFSList {

    @SerializedName("thcDate")
    @Expose
    private String thcDate;
    @SerializedName("thcNo")
    @Expose
    private String thcNo;
    @SerializedName("toBhCode")
    @Expose
    private String toBhCode;
    @SerializedName("totalDockets")
    @Expose
    private Double totalDockets;
    @SerializedName("totalPackages")
    @Expose
    private Double totalPackages;
    @SerializedName("totalActualWeight")
    @Expose
    private Double totalActualWeight;
    @SerializedName("totalCftWeight")
    @Expose
    private Double totalCftWeight;
    @SerializedName("totalLoadPackages")
    @Expose
    private Double totalLoadPackages;
    @SerializedName("totalLoadActualWeight")
    @Expose
    private Double totalLoadActualWeight;
    @SerializedName("totalLoadCftWeight")
    @Expose
    private Double totalLoadCftWeight;

    public GTLFSList() {
    }

    public String getThcDate() {
        return thcDate;
    }

    public void setThcDate(String thcDate) {
        this.thcDate = thcDate;
    }

    public String getThcNo() {
        return thcNo;
    }

    public void setThcNo(String thcNo) {
        this.thcNo = thcNo;
    }

    public String getToBhCode() {
        return toBhCode;
    }

    public void setToBhCode(String toBhCode) {
        this.toBhCode = toBhCode;
    }

    public Double getTotalDockets() {
        return totalDockets;
    }

    public void setTotalDockets(Double totalDockets) {
        this.totalDockets = totalDockets;
    }

    public Double getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(Double totalPackages) {
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

    public Double getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Double totalLoadPackages) {
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
}
