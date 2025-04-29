package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGetLodingSheetList {
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;

    public RequestGetLodingSheetList() {
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }
}
