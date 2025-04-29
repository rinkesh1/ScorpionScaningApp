package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraScannedDkt {
    @SerializedName("CompanyCode")
    @Expose
    private Integer CompanyCode;
    @SerializedName("BCSerialNo")
    @Expose
    private String BCSerialNo;
    private String DockNo;
    private String DockSf;
    private int Packages;
    private double Weight;



    public ExtraScannedDkt() {
    }

    public Integer getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.CompanyCode = companyCode;
    }

    public String getbCSerialNo() {
        return BCSerialNo;
    }

    public void setbCSerialNo(String bCSerialNo) {
        this.BCSerialNo = bCSerialNo;
    }

    public String getBCSerialNo() {
        return BCSerialNo;
    }

    public void setBCSerialNo(String BCSerialNo) {
        this.BCSerialNo = BCSerialNo;
    }

    public String getDockNo() {
        return DockNo;
    }

    public void setDockNo(String dockNo) {
        DockNo = dockNo;
    }

    public String getDockSf() {
        return DockSf;
    }

    public void setDockSf(String dockSf) {
        DockSf = dockSf;
    }

    public int getPackages() {
        return Packages;
    }

    public void setPackages(int packages) {
        Packages = packages;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }
}
