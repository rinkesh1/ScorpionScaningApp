package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GLSLList {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("lsNo")
    @Expose
    private String lsNo;
    @SerializedName("lsDate")
    @Expose
    private String lsDate;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("totalDockets")
    @Expose
    private Integer totalDockets;
    @SerializedName("totalPackages")
    @Expose
    private Integer totalPackages;
    @SerializedName("totalLoadPackages")
    @Expose
    private Integer totalLoadPackages;
    @SerializedName("totalActualWeight")
    @Expose
    private Double totalActualWeight;
    @SerializedName("totalCftWeight")
    @Expose
    private Double totalCftWeight;
    @SerializedName("totalLoadActualWeight")
    @Expose
    private Double totalLoadActualWeight;
    @SerializedName("totalLoadCftWeight")
    @Expose
    private Double totalLoadCftWeight;
    @SerializedName("isBcProcess")
    @Expose
    private String isBcProcess;

    public GLSLList() {
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

    public String getIsBcProcess() {
        return isBcProcess;
    }

    public void setIsBcProcess(String isBcProcess) {
        this.isBcProcess = isBcProcess;
    }
}
