package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class docketDetailList {

    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("dockNo")
    @Expose
    private String dockNo;
    @SerializedName("dockSf")
    @Expose
    private String dockSf;
    @SerializedName("dockDate")
    @Expose
    private String dockDate;
    @SerializedName("origin")
    @Expose
    private Object origin;
    @SerializedName("destination")
    @Expose
    private Object destination;
    @SerializedName("totalActualWeight")
    @Expose
    private Double totalActualWeight;
    @SerializedName("totalCftWeight")
    @Expose
    private Double totalCftWeight;
    @SerializedName("totalPackages")
    @Expose
    private Integer totalPackages;
    @SerializedName("totalLoadPackages")
    @Expose
    private Integer totalLoadPackages;
    @SerializedName("totalLoadActualWeight")
    @Expose
    private Double totalLoadActualWeight;
    @SerializedName("totalLoadCftWeight")
    @Expose
    private Double totalLoadCftWeight;
    @SerializedName("docketBarCodeSeries")
    @Expose
    private List<DocketBarCodeSeries> docketBarCodeSeries = null;

    public docketDetailList() {
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

    public String getDockDate() {
        return dockDate;
    }

    public void setDockDate(String dockDate) {
        this.dockDate = dockDate;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public Object getDestination() {
        return destination;
    }

    public void setDestination(Object destination) {
        this.destination = destination;
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

    public List<DocketBarCodeSeries> getDocketBarCodeSeries() {
        return docketBarCodeSeries;
    }

    public void setDocketBarCodeSeries(List<DocketBarCodeSeries> docketBarCodeSeries) {
        this.docketBarCodeSeries = docketBarCodeSeries;
    }


}
