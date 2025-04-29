package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocketUpdateScan {

    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("LSNo")
    @Expose
    private String lSNo;
    @SerializedName("DOCKNO")
    @Expose
    private String dOCKNO;
    @SerializedName("DOCKSF")
    @Expose
    private String dOCKSF;
    @SerializedName("BCSerialNo")
    @Expose
    private String bCSerialNo;
    @SerializedName("Packages")
    @Expose
    private Integer packages;
    @SerializedName("Weight")
    @Expose
    private Double weight;
    @SerializedName("Volume")
    @Expose
    private Double volume;
    @SerializedName("IsManual")
    @Expose
    private boolean isManual;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getLSNo() {
        return lSNo;
    }

    public void setLSNo(String lSNo) {
        this.lSNo = lSNo;
    }

    public String getDOCKNO() {
        return dOCKNO;
    }

    public void setDOCKNO(String dOCKNO) {
        this.dOCKNO = dOCKNO;
    }

    public String getDOCKSF() {
        return dOCKSF;
    }

    public void setDOCKSF(String dOCKSF) {
        this.dOCKSF = dOCKSF;
    }

    public String getBCSerialNo() {
        return bCSerialNo;
    }

    public void setBCSerialNo(String bCSerialNo) {
        this.bCSerialNo = bCSerialNo;
    }

    public Integer getPackages() {
        return packages;
    }

    public void setPackages(Integer packages) {
        this.packages = packages;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public boolean getIsManual() {
        return isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }
}
