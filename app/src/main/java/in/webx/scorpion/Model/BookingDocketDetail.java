package in.webx.scorpion.Model;

import java.util.ArrayList;

public class BookingDocketDetail {

    private String docketNo;
    private String origin;
    private Integer noofPkgs;
    private String vehicleNo;
    private String docketStatus;
    private ArrayList<BookingDocketBarCodeSeries> lst_BCSeries;

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

    public ArrayList<BookingDocketBarCodeSeries> getLst_BCSeries() {
        return lst_BCSeries;
    }

    public void setLst_BCSeries(ArrayList<BookingDocketBarCodeSeries> lst_BCSeries) {
        this.lst_BCSeries = lst_BCSeries;
    }
}
