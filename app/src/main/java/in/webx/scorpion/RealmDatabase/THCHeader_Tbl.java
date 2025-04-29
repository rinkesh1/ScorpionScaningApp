package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class THCHeader_Tbl extends RealmObject {

    @PrimaryKey
    private String thcNo;

    private String thcDate;
    private String toBhCode;
    private String branchCode;
    private Double totalDockets;
    private Double totalPackages;
    private Double totalActualWeight;
    private Double totalCftWeight;
    private Double totalLoadPackages;
    private Double totalLoadActualWeight;
    private Double totalLoadCftWeight;
    private int totalScanBarcode;
    private boolean isTHCScane;
    private String user;
    private Integer ArrivalCode;
    private String ArrivalCondition;
    private String Godown;
    private String DeliveryType;
    private String LastBarcodePkg;

    public THCHeader_Tbl() {
    }

    public String getThcDate() {
        return thcDate;
    }

    public void setThcDate(String thcDate) {
        this.thcDate = thcDate;
    }

    public String getThcNo() {
        return thcNo;
    }

    public void setThcNo(String thcNo) {
        this.thcNo = thcNo;
    }

    public String getToBhCode() {
        return toBhCode;
    }

    public void setToBhCode(String toBhCode) {
        this.toBhCode = toBhCode;
    }

    public Double getTotalDockets() {
        return totalDockets;
    }

    public void setTotalDockets(Double totalDockets) {
        this.totalDockets = totalDockets;
    }

    public Double getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(Double totalPackages) {
        this.totalPackages = totalPackages;
    }

    public Double getTotalActualWeight() {
        return totalActualWeight;
    }

    public void setTotalActualWeight(Double totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    public Double getTotalCftWeight() {
        return totalCftWeight;
    }

    public void setTotalCftWeight(Double totalCftWeight) {
        this.totalCftWeight = totalCftWeight;
    }

    public Double getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Double totalLoadPackages) {
        this.totalLoadPackages = totalLoadPackages;
    }

    public Double getTotalLoadActualWeight() {
        return totalLoadActualWeight;
    }

    public void setTotalLoadActualWeight(Double totalLoadActualWeight) {
        this.totalLoadActualWeight = totalLoadActualWeight;
    }

    public Double getTotalLoadCftWeight() {
        return totalLoadCftWeight;
    }

    public void setTotalLoadCftWeight(Double totalLoadCftWeight) {
        this.totalLoadCftWeight = totalLoadCftWeight;
    }

    public int getTotalScanBarcode() {
        return totalScanBarcode;
    }

    public void setTotalScanBarcode(int totalScanBarcode) {
        this.totalScanBarcode = totalScanBarcode;
    }

    public boolean isTHCScane() {
        return isTHCScane;
    }

    public void setTHCScane(boolean THCScane) {
        isTHCScane = THCScane;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getArrivalCode() {
        return ArrivalCode;
    }

    public void setArrivalCode(Integer arrivalCode) {
        ArrivalCode = arrivalCode;
    }

    public String getArrivalCondition() {
        return ArrivalCondition;
    }

    public void setArrivalCondition(String arrivalCondition) {
        ArrivalCondition = arrivalCondition;
    }

    public String getGodown() {
        return Godown;
    }

    public void setGodown(String godown) {
        Godown = godown;
    }

    public String getDeliveryType() {
        return DeliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        DeliveryType = deliveryType;
    }

    public String getLastBarcodePkg() {
        return LastBarcodePkg;
    }

    public void setLastBarcodePkg(String lastBarcodePkg) {
        LastBarcodePkg = lastBarcodePkg;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
