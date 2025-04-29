package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ThcDocketList_Tbl extends RealmObject {

    @Required
    private String dockNo;

    @Required
    private String dockSf;
    private String dockDate;
    private String origin;
    private String destination;
    private String branchCode;
    private Double totalActualWeight;
    private Double totalCftWeight;
    private Integer totalPackages;
    private Integer totalLoadPackages;
    private Double totalLoadActualWeight;
    private Double totalLoadCftWeight;
    private Integer scanPackages;
    private Boolean scanDocket;
    private String ThcNo;
    private String TcNo;
    private String lastBcSerialNo;
    private String user;
    private Integer ArrivalCode;
    private String ArrivalCondition;
    private String Godown;
    private String DeliveryType;

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    public String getDockSf() {
        return dockSf;
    }

    public void setDockSf(String dockSf) {
        this.dockSf = dockSf;
    }

    public String getDockDate() {
        return dockDate;
    }

    public void setDockDate(String dockDate) {
        this.dockDate = dockDate;
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

    public Integer getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(Integer totalPackages) {
        this.totalPackages = totalPackages;
    }

    public Integer getTotalLoadPackages() {
        return totalLoadPackages;
    }

    public void setTotalLoadPackages(Integer totalLoadPackages) {
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

    public Integer getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(Integer scanPackages) {
        this.scanPackages = scanPackages;
    }

    public Boolean getScanDocket() {
        return scanDocket;
    }

    public void setScanDocket(Boolean scanDocket) {
        this.scanDocket = scanDocket;
    }

    public String getThcNo() {
        return ThcNo;
    }

    public void setThcNo(String thcNo) {
        ThcNo = thcNo;
    }

    public String getTcNo() {
        return TcNo;
    }

    public void setTcNo(String tcNo) {
        TcNo = tcNo;
    }

    public String getLastBcSerialNo() {
        return lastBcSerialNo;
    }

    public void setLastBcSerialNo(String lastBcSerialNo) {
        this.lastBcSerialNo = lastBcSerialNo;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
