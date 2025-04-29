package in.webx.scorpion.Model;

import io.realm.RealmObject;

public class BookingDocketList_Tbl extends RealmObject {
    private String docketNo;
    private String origin;
    private Integer noofPkgs;
    private String vehicleNo;
    private String docketStatus;
    private String user;
    private Integer scanPackages;
    private Boolean scanDocket;
    private String lastBcSerialNo;

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getNoofPkgs() {
        return noofPkgs;
    }

    public void setNoofPkgs(Integer noofPkgs) {
        this.noofPkgs = noofPkgs;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDocketStatus() {
        return docketStatus;
    }

    public void setDocketStatus(String docketStatus) {
        this.docketStatus = docketStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getLastBcSerialNo() {
        return lastBcSerialNo;
    }

    public void setLastBcSerialNo(String lastBcSerialNo) {
        this.lastBcSerialNo = lastBcSerialNo;
    }
}
