package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class wtd {


    @SerializedName("DOCKNO")
    @Expose
    private String dOCKNO;
    @SerializedName("DOCKSF")
    @Expose
    private String dOCKSF;
    @SerializedName("LOADPKGSNO")
    @Expose
    private String lOADPKGSNO;
    @SerializedName("LOADACTUWT")
    @Expose
    private String lOADACTUWT;
    @SerializedName("LOADCFTWT")
    @Expose
    private String lOADCFTWT;
    @SerializedName("TOBH_CODE")
    @Expose
    private String tOBH_CODE;
    @SerializedName("TCDT")
    @Expose
    private String tCDT;

    public String getDOCKNO() {
        return dOCKNO;
    }

    public void setDOCKNO(String dOCKNO) {
        this.dOCKNO = dOCKNO;
    }

    public String getDOCKSF() {
        return dOCKSF;
    }

    public void setDOCKSF(String dOCKSF) {
        this.dOCKSF = dOCKSF;
    }

    public String getLOADPKGSNO() {
        return lOADPKGSNO;
    }

    public void setLOADPKGSNO(String lOADPKGSNO) {
        this.lOADPKGSNO = lOADPKGSNO;
    }

    public String getLOADACTUWT() {
        return lOADACTUWT;
    }

    public void setLOADACTUWT(String lOADACTUWT) {
        this.lOADACTUWT = lOADACTUWT;
    }

    public String getLOADCFTWT() {
        return lOADCFTWT;
    }

    public void setLOADCFTWT(String lOADCFTWT) {
        this.lOADCFTWT = lOADCFTWT;
    }

    public String getTOBH_CODE() {
        return tOBH_CODE;
    }

    public void setTOBH_CODE(String tOBH_CODE) {
        this.tOBH_CODE = tOBH_CODE;
    }

    public String getTCDT() {
        return tCDT;
    }

    public void setTCDT(String tCDT) {
        this.tCDT = tCDT;
    }


}
