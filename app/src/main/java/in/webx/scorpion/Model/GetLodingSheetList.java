package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLodingSheetList {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("list")
    @Expose
    private List<GLSLList> list = null;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    public GetLodingSheetList() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public List<GLSLList> getList() {
        return list;
    }

    public void setList(List<GLSLList> list) {
        this.list = list;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }
}
