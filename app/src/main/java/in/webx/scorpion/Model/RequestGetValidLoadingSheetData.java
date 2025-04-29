package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGetValidLoadingSheetData {
    @SerializedName("LSno")
    @Expose
    private String lSno;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;

    public RequestGetValidLoadingSheetData() {
    }

    public String getlSno() {
        return lSno;
    }

    public void setlSno(String lSno) {
        this.lSno = lSno;
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
