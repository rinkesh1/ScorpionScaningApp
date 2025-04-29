package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetExtraBarcodeDetail {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("list")
    @Expose
    private List<GEBDList> list = null;

    public GetExtraBarcodeDetail() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<GEBDList> getList() {
        return list;
    }

    public void setList(List<GEBDList> list) {
        this.list = list;
    }
}
