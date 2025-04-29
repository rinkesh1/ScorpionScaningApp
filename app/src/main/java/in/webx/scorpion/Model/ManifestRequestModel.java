package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ManifestRequestModel {


    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("finyear")
    @Expose
    private String finyear;
    @SerializedName("tchdr")
    @Expose
    //private tchdr tchdr;
    private List<tchdr> tchdr = new ArrayList<>();
    @SerializedName("tctrn")
    @Expose
    private List<tctrn> tctrn = new ArrayList<>();
    @SerializedName("wtds")
    @Expose
    private List<wtd> wtds = new ArrayList<>();
    @SerializedName("NonSelDKT")
    @Expose
    private List<Object> nonSelDKT = new ArrayList<>();
    @SerializedName("ExtraScannedDkts")
    @Expose
    private List<Object> extraScannedDkts = new ArrayList<>();


    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFinyear() {
        return finyear;
    }

    public void setFinyear(String finyear) {
        this.finyear = finyear;
    }

    public List<tchdr> getTchdr() {
        return tchdr;
    }

    public void setTchdr(List<tchdr> tchdr) {
        this.tchdr = tchdr;
    }

    public List<tctrn> getTctrn() {
        return tctrn;
    }

    public void setTctrn(List<tctrn> tctrn) {
        this.tctrn = tctrn;
    }

    public List<wtd> getWtds() {
        return wtds;
    }

    public void setWtds(List<wtd> wtds) {
        this.wtds = wtds;
    }

    public List<Object> getNonSelDKT() {
        return nonSelDKT;
    }

    public void setNonSelDKT(List<Object> nonSelDKT) {
        this.nonSelDKT = nonSelDKT;
    }

    public List<Object> getExtraScannedDkts() {
        return extraScannedDkts;
    }

    public void setExtraScannedDkts(List<Object> extraScannedDkts) {
        this.extraScannedDkts = extraScannedDkts;
    }


}
