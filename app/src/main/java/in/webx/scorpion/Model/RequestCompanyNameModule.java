package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCompanyNameModule {

    @SerializedName("CompanyAlias")
    @Expose
    private String CompanyAlias;

    public RequestCompanyNameModule() {
    }

    public String getCompanyAlias() {
        return CompanyAlias;
    }

    public void setCompanyAlias(String companyAlias) {
        CompanyAlias = companyAlias;
    }
}
