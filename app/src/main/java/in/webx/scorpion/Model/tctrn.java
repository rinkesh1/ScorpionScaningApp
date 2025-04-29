package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class tctrn {


    @SerializedName("TCNO")
    @Expose
    private String tCNO;
    @SerializedName("TCSF")
    @Expose
    private String tCSF;
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
    @SerializedName("DocketBarCodeSeries")
    @Expose
    private ArrayList<DocketBarCodeSeries> docketBarCodeSeries = null;

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

    public ArrayList<DocketBarCodeSeries> getDocketBarCodeSeries() {
        return docketBarCodeSeries;
    }

    public void setDocketBarCodeSeries(ArrayList<DocketBarCodeSeries> docketBarCodeSeries) {
        this.docketBarCodeSeries = docketBarCodeSeries;
    }

}
