package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PRSDocketDetails extends RealmObject {

    @PrimaryKey
    private String docketID;
    private String UserId;
    @SerializedName("docketNo")
    @Expose
    private String docketNo;
    @SerializedName("customerRefNo")
    @Expose
    private String customerRefNo;
    @SerializedName("docketSufix")
    @Expose
    private String docketSufix;
    @SerializedName("docketDate")
    @Expose
    private String docketDate;
    @SerializedName("paybas")
    @Expose
    private String paybas;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("totalPendingPackages")
    @Expose
    private Double totalPendingPackages;
    @SerializedName("totalArrivedPackages")
    @Expose
    private Double totalArrivedPackages;
    @SerializedName("totalArrivedWeight")
    @Expose
    private String totalArrivedWeight;
    @SerializedName("chargedWeight")
    @Expose
    private Double chargedWeight;
    @SerializedName("packages")
    @Expose
    private Double packages;
    @SerializedName("cewbNo")
    @Expose
    private String cewbNo;
    @SerializedName("contractAmount")
    @Expose
    private Double contractAmount;
    @SerializedName("contractId")
    @Expose
    private String contractId;
    @SerializedName("chargeRate")
    @Expose
    private Double chargeRate;
    @SerializedName("rateType")
    @Expose
    private String rateType;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("minCharge")
    @Expose
    private String minCharge;
    @SerializedName("maxCharge")
    @Expose
    private String maxCharge;
    @SerializedName("scannedPackages")
    @Expose
    private String scannedPackages;
   /* @SerializedName("lst_BCSeries")
    @Expose
    private List<PRSBarcodeDetails> prs_BCSeries;*/
    @SerializedName("vehicleno")
    @Expose
    private String vehicleno;
    private Boolean isDocketScanned;
    private int packageScanCount;
    private String lastScanPackage;

    public String getDocketID() {
        return docketID;
    }

    public void setDocketID(String docketID) {
        this.docketID = docketID;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getCustomerRefNo() {
        return customerRefNo;
    }

    public void setCustomerRefNo(String customerRefNo) {
        this.customerRefNo = customerRefNo;
    }

    public String getDocketSufix() {
        return docketSufix;
    }

    public void setDocketSufix(String docketSufix) {
        this.docketSufix = docketSufix;
    }

    public String getDocketDate() {
        return docketDate;
    }

    public void setDocketDate(String docketDate) {
        this.docketDate = docketDate;
    }

    public String getPaybas() {
        return paybas;
    }

    public void setPaybas(String paybas) {
        this.paybas = paybas;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getTotalPendingPackages() {
        return totalPendingPackages;
    }

    public void setTotalPendingPackages(Double totalPendingPackages) {
        this.totalPendingPackages = totalPendingPackages;
    }

    public Double getTotalArrivedPackages() {
        return totalArrivedPackages;
    }

    public void setTotalArrivedPackages(Double totalArrivedPackages) {
        this.totalArrivedPackages = totalArrivedPackages;
    }

    public String getTotalArrivedWeight() {
        return totalArrivedWeight;
    }

    public void setTotalArrivedWeight(String totalArrivedWeight) {
        this.totalArrivedWeight = totalArrivedWeight;
    }

    public Double getChargedWeight() {
        return chargedWeight;
    }

    public void setChargedWeight(Double chargedWeight) {
        this.chargedWeight = chargedWeight;
    }

    public Double getPackages() {
        return packages;
    }

    public void setPackages(Double packages) {
        this.packages = packages;
    }

    public String getCewbNo() {
        return cewbNo;
    }

    public void setCewbNo(String cewbNo) {
        this.cewbNo = cewbNo;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(Double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMinCharge() {
        return minCharge;
    }

    public void setMinCharge(String minCharge) {
        this.minCharge = minCharge;
    }

    public String getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(String maxCharge) {
        this.maxCharge = maxCharge;
    }

    public String getScannedPackages() {
        return scannedPackages;
    }

    public void setScannedPackages(String scannedPackages) {
        this.scannedPackages = scannedPackages;
    }

   /* public List<PRSBarcodeDetails> getprs_BCSeries() {
        return prs_BCSeries;
    }

    public void setprs_BCSeries(List<PRSBarcodeDetails> prs_BCSeries) {
        this.prs_BCSeries = prs_BCSeries;
    }*/

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }

    public Boolean getDocketScanned() {
        return isDocketScanned;
    }

    public void setDocketScanned(Boolean docketScanned) {
        isDocketScanned = docketScanned;
    }

    public int getPackageScanCount() {
        return packageScanCount;
    }

    public void setPackageScanCount(int packageScanCount) {
        this.packageScanCount = packageScanCount;
    }

    public String getLastScanPackage() {
        return lastScanPackage;
    }

    public void setLastScanPackage(String lastScanPackage) {
        this.lastScanPackage = lastScanPackage;
    }
}
