package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestGetExtraBarcodeDetail {
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("List")
    @Expose
    private List<RequestGEBDList> list = null;

    public RequestGetExtraBarcodeDetail() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public List<RequestGEBDList> getList() {
        return list;
    }

    public void setList(List<RequestGEBDList> list) {
        this.list = list;
    }
}
