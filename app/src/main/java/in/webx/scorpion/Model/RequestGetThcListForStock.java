package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGetThcListForStock {

    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("THCNo")
    @Expose
    private String tHCNo;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;

    public RequestGetThcListForStock() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String gettHCNo() {
        return tHCNo;
    }

    public void settHCNo(String tHCNo) {
        this.tHCNo = tHCNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
