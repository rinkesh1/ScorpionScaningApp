package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenratePRSModel {
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("prsNo")
    @Expose
    private String prsNo;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getPrsNo() {
        return prsNo;
    }

    public void setPrsNo(String prsNo) {
        this.prsNo = prsNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
