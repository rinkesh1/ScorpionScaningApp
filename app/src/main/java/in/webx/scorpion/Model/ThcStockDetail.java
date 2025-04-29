package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ThcStockDetail {
    @SerializedName("CompanyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("TCNO")
    @Expose
    private String tCNO;
    @SerializedName("LSNO")
    @Expose
    private String lSNO;
    @SerializedName("DOCKET")
    @Expose
    private String dOCKET;
    @SerializedName("DOCKETSF")
    @Expose
    private String dOCKETSF;
    @SerializedName("PKGSTOBEARRIVED")
    @Expose
    private Integer pKGSTOBEARRIVED;
    @SerializedName("PKGSARRIVED")
    @Expose
    private Integer pKGSARRIVED;
    @SerializedName("WTTOBEARRIVED")
    @Expose
    private Double wTTOBEARRIVED;
    @SerializedName("WTARRIVED")
    @Expose
    private Double wTARRIVED;
    @SerializedName("ARRVCOND")
    @Expose
    private String aRRVCOND;
    @SerializedName("GODOWN")
    @Expose
    private String gODOWN;
    @SerializedName("DELYDATE")
    @Expose
    private String dELYDATE;
    @SerializedName("DELYSTATUS")
    @Expose
    private String dELYSTATUS;
    @SerializedName("DELYTIME")
    @Expose
    private String dELYTIME;
    @SerializedName("DELY_PROCESS")
    @Expose
    private String dELY_PROCESS;
    @SerializedName("DELYREASON")
    @Expose
    private String dELYREASON;
    @SerializedName("DELYPERSON")
    @Expose
    private String dELYPERSON;
    @SerializedName("CODDODAMOUNT")
    @Expose
    private Double cODDODAMOUNT;
    @SerializedName("CODDODCOLLECTED")
    @Expose
    private Double cODDODCOLLECTED;
    @SerializedName("CODDODNO")
    @Expose
    private String cODDODNO;
    @SerializedName("THCno")
    @Expose
    private String tHCno;
    @SerializedName("IsDeps")
    @Expose
    private String isDeps;
    @SerializedName("IsDamage")
    @Expose
    private String isDamage;
    @SerializedName("DamageQty")
    @Expose
    private Double damageQty;
    @SerializedName("DamageReason")
    @Expose
    private String damageReason;
    @SerializedName("DamageFile")
    @Expose
    private String damageFile;
    @SerializedName("DamageScanImageExtension")
    @Expose
    private String damageScanImageExtension;
    @SerializedName("DamageScanImageName")
    @Expose
    private String damageScanImageName;
    @SerializedName("IsPilfered")
    @Expose
    private String isPilfered;
    @SerializedName("PilferedQty")
    @Expose
    private Double pilferedQty;
    @SerializedName("PilferedReason")
    @Expose
    private String pilferedReason;
    @SerializedName("PilferedFile")
    @Expose
    private String pilferedFile;
    @SerializedName("PilferedScanImageExtension")
    @Expose
    private String pilferedScanImageExtension;
    @SerializedName("PilferedScanImageName")
    @Expose
    private String pilferedScanImageName;
    @SerializedName("IsShort")
    @Expose
    private String isShort;
    @SerializedName("ShortQty")
    @Expose
    private Double shortQty;
    @SerializedName("ShortReason")
    @Expose
    private String shortReason;
    @SerializedName("ShortFile")
    @Expose
    private String shortFile;
    @SerializedName("ShortScanImageExtension")
    @Expose
    private String shortScanImageExtension;
    @SerializedName("ShortScanImageName")
    @Expose
    private String shortScanImageName;
    @SerializedName("IsExtra")
    @Expose
    private String isExtra;
    @SerializedName("ExtraQty")
    @Expose
    private Double extraQty;
    @SerializedName("ExtraReason")
    @Expose
    private String extraReason;
    @SerializedName("ExtraFile")
    @Expose
    private String extraFile;
    @SerializedName("ExtraScanImageExtension")
    @Expose
    private String extraScanImageExtension;
    @SerializedName("ExtraScanImageName")
    @Expose
    private String extraScanImageName;
    @SerializedName("BCSerials")
    @Expose
    private String bCSerials;
    @SerializedName("SUDate")
    @Expose
    private String sUDate;
    @SerializedName("DocketBarCodeSeries")
    @Expose
    private ArrayList<ThcStockDocketBarCodeSeries> docketBarCodeSeries = null;

    public ThcStockDetail() {
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String gettCNO() {
        return tCNO;
    }

    public void settCNO(String tCNO) {
        this.tCNO = tCNO;
    }

    public String getlSNO() {
        return lSNO;
    }

    public void setlSNO(String lSNO) {
        this.lSNO = lSNO;
    }

    public String getdOCKET() {
        return dOCKET;
    }

    public void setdOCKET(String dOCKET) {
        this.dOCKET = dOCKET;
    }

    public String getdOCKETSF() {
        return dOCKETSF;
    }

    public void setdOCKETSF(String dOCKETSF) {
        this.dOCKETSF = dOCKETSF;
    }

    public Integer getpKGSTOBEARRIVED() {
        return pKGSTOBEARRIVED;
    }

    public void setpKGSTOBEARRIVED(Integer pKGSTOBEARRIVED) {
        this.pKGSTOBEARRIVED = pKGSTOBEARRIVED;
    }

    public Integer getpKGSARRIVED() {
        return pKGSARRIVED;
    }

    public void setpKGSARRIVED(Integer pKGSARRIVED) {
        this.pKGSARRIVED = pKGSARRIVED;
    }

    public Double getwTTOBEARRIVED() {
        return wTTOBEARRIVED;
    }

    public void setwTTOBEARRIVED(Double wTTOBEARRIVED) {
        this.wTTOBEARRIVED = wTTOBEARRIVED;
    }

    public Double getwTARRIVED() {
        return wTARRIVED;
    }

    public void setwTARRIVED(Double wTARRIVED) {
        this.wTARRIVED = wTARRIVED;
    }

    public String getaRRVCOND() {
        return aRRVCOND;
    }

    public void setaRRVCOND(String aRRVCOND) {
        this.aRRVCOND = aRRVCOND;
    }

    public String getgODOWN() {
        return gODOWN;
    }

    public void setgODOWN(String gODOWN) {
        this.gODOWN = gODOWN;
    }

    public String getdELYDATE() {
        return dELYDATE;
    }

    public void setdELYDATE(String dELYDATE) {
        this.dELYDATE = dELYDATE;
    }

    public String getdELYSTATUS() {
        return dELYSTATUS;
    }

    public void setdELYSTATUS(String dELYSTATUS) {
        this.dELYSTATUS = dELYSTATUS;
    }

    public String getdELYTIME() {
        return dELYTIME;
    }

    public void setdELYTIME(String dELYTIME) {
        this.dELYTIME = dELYTIME;
    }

    public String getdELY_PROCESS() {
        return dELY_PROCESS;
    }

    public void setdELY_PROCESS(String dELY_PROCESS) {
        this.dELY_PROCESS = dELY_PROCESS;
    }

    public String getdELYREASON() {
        return dELYREASON;
    }

    public void setdELYREASON(String dELYREASON) {
        this.dELYREASON = dELYREASON;
    }

    public String getdELYPERSON() {
        return dELYPERSON;
    }

    public void setdELYPERSON(String dELYPERSON) {
        this.dELYPERSON = dELYPERSON;
    }

    public Double getcODDODAMOUNT() {
        return cODDODAMOUNT;
    }

    public void setcODDODAMOUNT(Double cODDODAMOUNT) {
        this.cODDODAMOUNT = cODDODAMOUNT;
    }

    public Double getcODDODCOLLECTED() {
        return cODDODCOLLECTED;
    }

    public void setcODDODCOLLECTED(Double cODDODCOLLECTED) {
        this.cODDODCOLLECTED = cODDODCOLLECTED;
    }

    public String getcODDODNO() {
        return cODDODNO;
    }

    public void setcODDODNO(String cODDODNO) {
        this.cODDODNO = cODDODNO;
    }

    public String gettHCno() {
        return tHCno;
    }

    public void settHCno(String tHCno) {
        this.tHCno = tHCno;
    }

    public String getIsDeps() {
        return isDeps;
    }

    public void setIsDeps(String isDeps) {
        this.isDeps = isDeps;
    }

    public String getIsDamage() {
        return isDamage;
    }

    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
    }

    public Double getDamageQty() {
        return damageQty;
    }

    public void setDamageQty(Double damageQty) {
        this.damageQty = damageQty;
    }

    public String getDamageReason() {
        return damageReason;
    }

    public void setDamageReason(String damageReason) {
        this.damageReason = damageReason;
    }

    public String getDamageFile() {
        return damageFile;
    }

    public void setDamageFile(String damageFile) {
        this.damageFile = damageFile;
    }

    public String getDamageScanImageExtension() {
        return damageScanImageExtension;
    }

    public void setDamageScanImageExtension(String damageScanImageExtension) {
        this.damageScanImageExtension = damageScanImageExtension;
    }

    public String getDamageScanImageName() {
        return damageScanImageName;
    }

    public void setDamageScanImageName(String damageScanImageName) {
        this.damageScanImageName = damageScanImageName;
    }

    public String getIsPilfered() {
        return isPilfered;
    }

    public void setIsPilfered(String isPilfered) {
        this.isPilfered = isPilfered;
    }

    public Double getPilferedQty() {
        return pilferedQty;
    }

    public void setPilferedQty(Double pilferedQty) {
        this.pilferedQty = pilferedQty;
    }

    public String getPilferedReason() {
        return pilferedReason;
    }

    public void setPilferedReason(String pilferedReason) {
        this.pilferedReason = pilferedReason;
    }

    public String getPilferedFile() {
        return pilferedFile;
    }

    public void setPilferedFile(String pilferedFile) {
        this.pilferedFile = pilferedFile;
    }

    public String getPilferedScanImageExtension() {
        return pilferedScanImageExtension;
    }

    public void setPilferedScanImageExtension(String pilferedScanImageExtension) {
        this.pilferedScanImageExtension = pilferedScanImageExtension;
    }

    public String getPilferedScanImageName() {
        return pilferedScanImageName;
    }

    public void setPilferedScanImageName(String pilferedScanImageName) {
        this.pilferedScanImageName = pilferedScanImageName;
    }

    public String getIsShort() {
        return isShort;
    }

    public void setIsShort(String isShort) {
        this.isShort = isShort;
    }

    public Double getShortQty() {
        return shortQty;
    }

    public void setShortQty(Double shortQty) {
        this.shortQty = shortQty;
    }

    public String getShortReason() {
        return shortReason;
    }

    public void setShortReason(String shortReason) {
        this.shortReason = shortReason;
    }

    public String getShortFile() {
        return shortFile;
    }

    public void setShortFile(String shortFile) {
        this.shortFile = shortFile;
    }

    public String getShortScanImageExtension() {
        return shortScanImageExtension;
    }

    public void setShortScanImageExtension(String shortScanImageExtension) {
        this.shortScanImageExtension = shortScanImageExtension;
    }

    public String getShortScanImageName() {
        return shortScanImageName;
    }

    public void setShortScanImageName(String shortScanImageName) {
        this.shortScanImageName = shortScanImageName;
    }

    public String getIsExtra() {
        return isExtra;
    }

    public void setIsExtra(String isExtra) {
        this.isExtra = isExtra;
    }

    public Double getExtraQty() {
        return extraQty;
    }

    public void setExtraQty(Double extraQty) {
        this.extraQty = extraQty;
    }

    public String getExtraReason() {
        return extraReason;
    }

    public void setExtraReason(String extraReason) {
        this.extraReason = extraReason;
    }

    public String getExtraFile() {
        return extraFile;
    }

    public void setExtraFile(String extraFile) {
        this.extraFile = extraFile;
    }

    public String getExtraScanImageExtension() {
        return extraScanImageExtension;
    }

    public void setExtraScanImageExtension(String extraScanImageExtension) {
        this.extraScanImageExtension = extraScanImageExtension;
    }

    public String getExtraScanImageName() {
        return extraScanImageName;
    }

    public void setExtraScanImageName(String extraScanImageName) {
        this.extraScanImageName = extraScanImageName;
    }

    public String getbCSerials() {
        return bCSerials;
    }

    public void setbCSerials(String bCSerials) {
        this.bCSerials = bCSerials;
    }

    public String getsUDate() {
        return sUDate;
    }

    public void setsUDate(String sUDate) {
        this.sUDate = sUDate;
    }

    public ArrayList<ThcStockDocketBarCodeSeries> getDocketBarCodeSeries() {
        return docketBarCodeSeries;
    }

    public void setDocketBarCodeSeries(ArrayList<ThcStockDocketBarCodeSeries> docketBarCodeSeries) {
        this.docketBarCodeSeries = docketBarCodeSeries;
    }
}
