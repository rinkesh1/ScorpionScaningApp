package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class tchdr {


    @SerializedName("LSNO")
    @Expose
    private String lSNO;
    @SerializedName("TCNO")
    @Expose
    private String tCNO;
    @SerializedName("TCSF")
    @Expose
    private String tCSF;
    @SerializedName("TCDT")
    @Expose
    private String tCDT;
    @SerializedName("TCBR")
    @Expose
    private String tCBR;
    @SerializedName("TOBH_CODE")
    @Expose
    private String tOBH_CODE;
    @SerializedName("TOT_DKT")
    @Expose
    private double tOT_DKT;
    @SerializedName("TOT_PKGS")
    @Expose
    private double tOT_PKGS;
    @SerializedName("TOT_ACTUWT")
    @Expose
    private double tOT_ACTUWT;
    @SerializedName("TOT_CFTWT")
    @Expose
    private double tOT_CFTWT;
    @SerializedName("TOT_LOAD_PKGS")
    @Expose
    private double tOT_LOAD_PKGS;
    @SerializedName("TOT_LOAD_ACTWT")
    @Expose
    private double tOT_LOAD_ACTWT;
    @SerializedName("TOT_LOAD_CFTWT")
    @Expose
    private double tOT_LOAD_CFTWT;
    @SerializedName("TCHDRFLAG")
    @Expose
    private String tCHDRFLAG;
    @SerializedName("TFLAG_YN")
    @Expose
    private String tFLAG_YN;
    @SerializedName("ENTRYBY")
    @Expose
    private String eNTRYBY;
    @SerializedName("ROUTE_CODE")
    @Expose
    private String rOUTE_CODE;
    @SerializedName("IsBCProcess")
    @Expose
    private String isBCProcess;

    public String getLSNO() {
        return lSNO;
    }

    public void setLSNO(String lSNO) {
        this.lSNO = lSNO;
    }

    public String getTCNO() {
        return tCNO;
    }

    public void setTCNO(String tCNO) {
        this.tCNO = tCNO;
    }

    public String getTCSF() {
        return tCSF;
    }

    public void setTCSF(String tCSF) {
        this.tCSF = tCSF;
    }

    public String getTCDT() {
        return tCDT;
    }

    public void setTCDT(String tCDT) {
        this.tCDT = tCDT;
    }

    public String getTCBR() {
        return tCBR;
    }

    public void setTCBR(String tCBR) {
        this.tCBR = tCBR;
    }

    public String getTOBH_CODE() {
        return tOBH_CODE;
    }

    public void setTOBH_CODE(String tOBH_CODE) {
        this.tOBH_CODE = tOBH_CODE;
    }

    public double getTOT_DKT() {
        return tOT_DKT;
    }

    public void setTOT_DKT(double tOT_DKT) {
        this.tOT_DKT = tOT_DKT;
    }

    public double getTOT_PKGS() {
        return tOT_PKGS;
    }

    public void setTOT_PKGS(double tOT_PKGS) {
        this.tOT_PKGS = tOT_PKGS;
    }

    public double getTOT_ACTUWT() {
        return tOT_ACTUWT;
    }

    public void setTOT_ACTUWT(double tOT_ACTUWT) {
        this.tOT_ACTUWT = tOT_ACTUWT;
    }

    public double getTOT_CFTWT() {
        return tOT_CFTWT;
    }

    public void setTOT_CFTWT(double tOT_CFTWT) {
        this.tOT_CFTWT = tOT_CFTWT;
    }

    public double getTOT_LOAD_PKGS() {
        return tOT_LOAD_PKGS;
    }

    public void setTOT_LOAD_PKGS(double tOT_LOAD_PKGS) {
        this.tOT_LOAD_PKGS = tOT_LOAD_PKGS;
    }

    public double getTOT_LOAD_ACTWT() {
        return tOT_LOAD_ACTWT;
    }

    public void setTOT_LOAD_ACTWT(double tOT_LOAD_ACTWT) {
        this.tOT_LOAD_ACTWT = tOT_LOAD_ACTWT;
    }

    public double getTOT_LOAD_CFTWT() {
        return tOT_LOAD_CFTWT;
    }

    public void setTOT_LOAD_CFTWT(double tOT_LOAD_CFTWT) {
        this.tOT_LOAD_CFTWT = tOT_LOAD_CFTWT;
    }

    public String getTCHDRFLAG() {
        return tCHDRFLAG;
    }

    public void setTCHDRFLAG(String tCHDRFLAG) {
        this.tCHDRFLAG = tCHDRFLAG;
    }

    public String getTFLAG_YN() {
        return tFLAG_YN;
    }

    public void setTFLAG_YN(String tFLAG_YN) {
        this.tFLAG_YN = tFLAG_YN;
    }

    public String getENTRYBY() {
        return eNTRYBY;
    }

    public void setENTRYBY(String eNTRYBY) {
        this.eNTRYBY = eNTRYBY;
    }

    public String getROUTE_CODE() {
        return rOUTE_CODE;
    }

    public void setROUTE_CODE(String rOUTE_CODE) {
        this.rOUTE_CODE = rOUTE_CODE;
    }

    public String getIsBCProcess() {
        return isBCProcess;
    }

    public void setIsBCProcess(String isBCProcess) {
        this.isBCProcess = isBCProcess;
    }


}
