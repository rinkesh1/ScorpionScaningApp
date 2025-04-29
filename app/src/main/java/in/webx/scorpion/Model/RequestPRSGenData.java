package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPRSGenData {

    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("VehicleNo")
    @Expose
    private String vehicleNo;
    @SerializedName("DocketNo")
    @Expose
    private String docketNo;
    @SerializedName("UserId")
    @Expose
    private String UserId;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
