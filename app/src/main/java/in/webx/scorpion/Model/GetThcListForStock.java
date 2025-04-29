package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetThcListForStock {

    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("list")
    @Expose
    private List<GTLFSList> gtlfsList = null;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    public GetThcListForStock() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public List<GTLFSList> getGtlfsList() {
        return gtlfsList;
    }

    public void setGtlfsList(List<GTLFSList> gtlfsList) {
        this.gtlfsList = gtlfsList;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }
}
