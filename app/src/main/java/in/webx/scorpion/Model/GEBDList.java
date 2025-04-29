package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GEBDList {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("dockNo")
    @Expose
    private String dockNo;
    @SerializedName("dockSf")
    @Expose
    private String dockSf;
    @SerializedName("totalPackages")
    @Expose
    private Integer totalPackages;
    @SerializedName("bcSerialNo")
    @Expose
    private String bcSerialNo;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;

    public GEBDList() {
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

    public Integer getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(Integer totalPackages) {
        this.totalPackages = totalPackages;
    }

    public String getBcSerialNo() {
        return bcSerialNo;
    }

    public void setBcSerialNo(String bcSerialNo) {
        this.bcSerialNo = bcSerialNo;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
