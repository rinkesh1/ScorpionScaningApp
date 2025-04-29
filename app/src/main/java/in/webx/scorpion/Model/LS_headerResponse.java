package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LS_headerResponse {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("mfCode")
    @Expose
    private String mfCode;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMfCode() {
        return mfCode;
    }

    public void setMfCode(String mfCode) {
        this.mfCode = mfCode;
    }

}
