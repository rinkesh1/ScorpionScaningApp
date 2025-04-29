package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGEBDList {
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("BCSerialNo")
    @Expose
    private String bCSerialNo;

    public RequestGEBDList() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getbCSerialNo() {
        return bCSerialNo;
    }

    public void setbCSerialNo(String bCSerialNo) {
        this.bCSerialNo = bCSerialNo;
    }
}
