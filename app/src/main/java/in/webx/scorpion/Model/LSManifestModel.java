package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tushar on 2/9/2017.
 */

public class LSManifestModel {

    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

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
}
