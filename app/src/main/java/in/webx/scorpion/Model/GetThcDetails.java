package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetThcDetails {

    private Integer companyCode;
    private ArrayList<ThcDocketDetail> docketDetailList;
    private Boolean isSuccess;
    private String errorMessage;
    private String thcDate;
    private String thcNo;
    private String toBhCode;
    private Integer totalDockets;
    private Integer totalPackages;
    private Float totalActualWeight;
    private Float totalCftWeight;
    private Integer totalLoadPackages;
    private Float totalLoadActualWeight;
    private Float totalLoadCftWeight;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public ArrayList<ThcDocketDetail> getDocketDetailList() {
        return docketDetailList;
    }

    public void setDocketDetailList(ArrayList<ThcDocketDetail> docketDetailList) {
        this.docketDetailList = docketDetailList;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public Float getTotalActualWeight() {
        return totalActualWeight;
    }

    public void setTotalActualWeight(Float totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    public Float getTotalCftWeight() {
        return totalCftWeight;
    }

    public void setTotalCftWeight(Float totalCftWeight) {
        this.totalCftWeight = totalCftWeight;
    }

    public Integer getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Integer totalLoadPackages) {
        this.totalLoadPackages = totalLoadPackages;
    }

    public Float getTotalLoadActualWeight() {
        return totalLoadActualWeight;
    }

    public void setTotalLoadActualWeight(Float totalLoadActualWeight) {
        this.totalLoadActualWeight = totalLoadActualWeight;
    }

    public Float getTotalLoadCftWeight() {
        return totalLoadCftWeight;
    }

    public void setTotalLoadCftWeight(Float totalLoadCftWeight) {
        this.totalLoadCftWeight = totalLoadCftWeight;
    }
}
