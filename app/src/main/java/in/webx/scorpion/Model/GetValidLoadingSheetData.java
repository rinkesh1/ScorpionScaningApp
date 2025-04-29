package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetValidLoadingSheetData {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("docketDetailList")
    @Expose
    private List<DocketDetail> docketDetailList = null;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("lsDate")
    @Expose
    private String lsDate;
    @SerializedName("lsNo")
    @Expose
    private String lsNo;
    @SerializedName("toBhCode")
    @Expose
    private String toBhCode;
    @SerializedName("totalDockets")
    @Expose
    private Integer totalDockets;
    @SerializedName("totalPackages")
    @Expose
    private Integer totalPackages;
    @SerializedName("totalActualWeight")
    @Expose
    private Double totalActualWeight;
    @SerializedName("totalCftWeight")
    @Expose
    private Double totalCftWeight;
    @SerializedName("totalLoadPackages")
    @Expose
    private Integer totalLoadPackages;
    @SerializedName("totalLoadActualWeight")
    @Expose
    private Double totalLoadActualWeight;
    @SerializedName("totalLoadCftWeight")
    @Expose
    private Double totalLoadCftWeight;
    @SerializedName("isBcProcess")
    @Expose
    private String isBcProcess;

    public GetValidLoadingSheetData() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public List<DocketDetail> getDocketDetailList() {
        return docketDetailList;
    }

    public void setDocketDetailList(List<DocketDetail> docketDetailList) {
        this.docketDetailList = docketDetailList;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLsDate() {
        return lsDate;
    }

    public void setLsDate(String lsDate) {
        this.lsDate = lsDate;
    }

    public String getLsNo() {
        return lsNo;
    }

    public void setLsNo(String lsNo) {
        this.lsNo = lsNo;
    }

    public String getToBhCode() {
        return toBhCode;
    }

    public void setToBhCode(String toBhCode) {
        this.toBhCode = toBhCode;
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

    public String getIsBcProcess() {
        return isBcProcess;
    }

    public void setIsBcProcess(String isBcProcess) {
        this.isBcProcess = isBcProcess;
    }
}
