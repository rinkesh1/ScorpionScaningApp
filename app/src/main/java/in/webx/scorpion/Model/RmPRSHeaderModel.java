package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RmPRSHeaderModel implements Serializable {
    @SerializedName("companyCode")
    @Expose
    private Integer companyCode;
    @SerializedName("entryBy")
    @Expose
    private String entryBy;
    @SerializedName("engineNo")
    @Expose
    private String engineNo;
    @SerializedName("currentBranch")
    @Expose
    private String currentBranch;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vehicleno")
    @Expose
    private String vehicleno;
    @SerializedName("vehicletype")
    @Expose
    private String vehicletype;
    @SerializedName("vehicleTons")
    @Expose
    private String vehicleTons;
    @SerializedName("ftlype")
    @Expose
    private String ftlype;
    @SerializedName("weightLoaded")
    @Expose
    private String weightLoaded;
    @SerializedName("ehengineno")
    @Expose
    private String ehengineno;
    @SerializedName("chasisno")
    @Expose
    private String chasisno;
    @SerializedName("rcbkno")
    @Expose
    private String rcbkno;
    @SerializedName("vehicleregdt")
    @Expose
    private String vehicleregdt;
    @SerializedName("vehiclepermitdt")
    @Expose
    private String vehiclepermitdt;
    @SerializedName("insurancevaliditydt")
    @Expose
    private String insurancevaliditydt;
    @SerializedName("fitnessvaliditydt")
    @Expose
    private String fitnessvaliditydt;
    @SerializedName("vendorcode")
    @Expose
    private String vendorcode;
    @SerializedName("vendortype")
    @Expose
    private String vendortype;
    @SerializedName("vendorname")
    @Expose
    private String vendorname;
    @SerializedName("drivername")
    @Expose
    private String drivername;
    @SerializedName("driver1Licence")
    @Expose
    private String driver1Licence;
    @SerializedName("drivermobileno")
    @Expose
    private String drivermobileno;
    @SerializedName("licenseno")
    @Expose
    private String licenseno;
    @SerializedName("issuebyrto")
    @Expose
    private String issuebyrto;
    @SerializedName("licenseveldt")
    @Expose
    private String licenseveldt;
    @SerializedName("prstype")
    @Expose
    private String prstype;
    @SerializedName("totalWeight")
    @Expose
    private String totalWeight;
    @SerializedName("capacityutilization")
    @Expose
    private String capacityutilization;
    @SerializedName("startKM")
    @Expose
    private String startKM;
    @SerializedName("endKM")
    @Expose
    private String endKM;
    @SerializedName("financialDetail")
    @Expose
    private String financialDetail;
    @SerializedName("financialYear")
    @Expose
    private String financialYear;
    @SerializedName("branchCode")
    @Expose
    private String branchCode;
    @SerializedName("manualPrsNo")
    @Expose
    private String manualPrsNo;
    @SerializedName("marketVehicle")
    @Expose
    private String marketVehicle;
    @SerializedName("prsDate")
    @Expose
    private String prsDate;
    @SerializedName("prsTime")
    @Expose
    private String prsTime;
    @SerializedName("rcBookNo")
    @Expose
    private String rcBookNo;
    @SerializedName("isOverLoad")
    @Expose
    private String isOverLoad;
    @SerializedName("docketDetails")
    @Expose
    private List<RmPRSDocketModel> docketDetails = null;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getVehicleTons() {
        return vehicleTons;
    }

    public void setVehicleTons(String vehicleTons) {
        this.vehicleTons = vehicleTons;
    }

    public String getFtlype() {
        return ftlype;
    }

    public void setFtlype(String ftlype) {
        this.ftlype = ftlype;
    }

    public String getWeightLoaded() {
        return weightLoaded;
    }

    public void setWeightLoaded(String weightLoaded) {
        this.weightLoaded = weightLoaded;
    }

    public String getEhengineno() {
        return ehengineno;
    }

    public void setEhengineno(String ehengineno) {
        this.ehengineno = ehengineno;
    }

    public String getChasisno() {
        return chasisno;
    }

    public void setChasisno(String chasisno) {
        this.chasisno = chasisno;
    }

    public String getRcbkno() {
        return rcbkno;
    }

    public void setRcbkno(String rcbkno) {
        this.rcbkno = rcbkno;
    }

    public String getVehicleregdt() {
        return vehicleregdt;
    }

    public void setVehicleregdt(String vehicleregdt) {
        this.vehicleregdt = vehicleregdt;
    }

    public String getVehiclepermitdt() {
        return vehiclepermitdt;
    }

    public void setVehiclepermitdt(String vehiclepermitdt) {
        this.vehiclepermitdt = vehiclepermitdt;
    }

    public String getInsurancevaliditydt() {
        return insurancevaliditydt;
    }

    public void setInsurancevaliditydt(String insurancevaliditydt) {
        this.insurancevaliditydt = insurancevaliditydt;
    }

    public String getFitnessvaliditydt() {
        return fitnessvaliditydt;
    }

    public void setFitnessvaliditydt(String fitnessvaliditydt) {
        this.fitnessvaliditydt = fitnessvaliditydt;
    }

    public String getVendorcode() {
        return vendorcode;
    }

    public void setVendorcode(String vendorcode) {
        this.vendorcode = vendorcode;
    }

    public String getVendortype() {
        return vendortype;
    }

    public void setVendortype(String vendortype) {
        this.vendortype = vendortype;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriver1Licence() {
        return driver1Licence;
    }

    public void setDriver1Licence(String driver1Licence) {
        this.driver1Licence = driver1Licence;
    }

    public String getDrivermobileno() {
        return drivermobileno;
    }

    public void setDrivermobileno(String drivermobileno) {
        this.drivermobileno = drivermobileno;
    }

    public String getLicenseno() {
        return licenseno;
    }

    public void setLicenseno(String licenseno) {
        this.licenseno = licenseno;
    }

    public String getIssuebyrto() {
        return issuebyrto;
    }

    public void setIssuebyrto(String issuebyrto) {
        this.issuebyrto = issuebyrto;
    }

    public String getLicenseveldt() {
        return licenseveldt;
    }

    public void setLicenseveldt(String licenseveldt) {
        this.licenseveldt = licenseveldt;
    }

    public String getPrstype() {
        return prstype;
    }

    public void setPrstype(String prstype) {
        this.prstype = prstype;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getCapacityutilization() {
        return capacityutilization;
    }

    public void setCapacityutilization(String capacityutilization) {
        this.capacityutilization = capacityutilization;
    }

    public String getStartKM() {
        return startKM;
    }

    public void setStartKM(String startKM) {
        this.startKM = startKM;
    }

    public String getEndKM() {
        return endKM;
    }

    public void setEndKM(String endKM) {
        this.endKM = endKM;
    }

    public String getFinancialDetail() {
        return financialDetail;
    }

    public void setFinancialDetail(String financialDetail) {
        this.financialDetail = financialDetail;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getManualPrsNo() {
        return manualPrsNo;
    }

    public void setManualPrsNo(String manualPrsNo) {
        this.manualPrsNo = manualPrsNo;
    }

    public String getMarketVehicle() {
        return marketVehicle;
    }

    public void setMarketVehicle(String marketVehicle) {
        this.marketVehicle = marketVehicle;
    }

    public String getPrsDate() {
        return prsDate;
    }

    public void setPrsDate(String prsDate) {
        this.prsDate = prsDate;
    }

    public String getPrsTime() {
        return prsTime;
    }

    public void setPrsTime(String prsTime) {
        this.prsTime = prsTime;
    }

    public String getRcBookNo() {
        return rcBookNo;
    }

    public void setRcBookNo(String rcBookNo) {
        this.rcBookNo = rcBookNo;
    }

    public String getIsOverLoad() {
        return isOverLoad;
    }

    public void setIsOverLoad(String isOverLoad) {
        this.isOverLoad = isOverLoad;
    }

    public List<RmPRSDocketModel> getDocketDetails() {
        return docketDetails;
    }

    public void setDocketDetails(List<RmPRSDocketModel> docketDetails) {
        this.docketDetails = docketDetails;
    }
}
