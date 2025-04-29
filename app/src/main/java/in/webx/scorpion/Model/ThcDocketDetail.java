package in.webx.scorpion.Model;

import java.util.ArrayList;

public class ThcDocketDetail {

    private String DockNo;
    private String ThcNo;
    private String TcNo;
    private String DockSf;
    private String Origin;
    private String Destination;
    private Float TotalActualWeight;
    private Float TotalCftWeight;
    private Integer TotalPackages;
    private Integer TotalLoadPackages;
    private Float TotalLoadActualWeight;
    private Float TotalLoadCftWeight;
    private ArrayList<ThcDocketBarCodeSeries> docketBarCodeSeries;

    public String getDockNo() {
        return DockNo;
    }

    public void setDockNo(String dockNo) {
        DockNo = dockNo;
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

    public String getDockSf() {
        return DockSf;
    }

    public void setDockSf(String dockSf) {
        DockSf = dockSf;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public Float getTotalActualWeight() {
        return TotalActualWeight;
    }

    public void setTotalActualWeight(Float totalActualWeight) {
        TotalActualWeight = totalActualWeight;
    }

    public Float getTotalCftWeight() {
        return TotalCftWeight;
    }

    public void setTotalCftWeight(Float totalCftWeight) {
        TotalCftWeight = totalCftWeight;
    }

    public Integer getTotalPackages() {
        return TotalPackages;
    }

    public void setTotalPackages(Integer totalPackages) {
        TotalPackages = totalPackages;
    }

    public Integer getTotalLoadPackages() {
        return TotalLoadPackages;
    }

    public void setTotalLoadPackages(Integer totalLoadPackages) {
        TotalLoadPackages = totalLoadPackages;
    }

    public Float getTotalLoadActualWeight() {
        return TotalLoadActualWeight;
    }

    public void setTotalLoadActualWeight(Float totalLoadActualWeight) {
        TotalLoadActualWeight = totalLoadActualWeight;
    }

    public Float getTotalLoadCftWeight() {
        return TotalLoadCftWeight;
    }

    public void setTotalLoadCftWeight(Float totalLoadCftWeight) {
        TotalLoadCftWeight = totalLoadCftWeight;
    }

    public ArrayList<ThcDocketBarCodeSeries> getDocketBarCodeSeries() {
        return docketBarCodeSeries;
    }

    public void setDocketBarCodeSeries(ArrayList<ThcDocketBarCodeSeries> docketBarCodeSeries) {
        this.docketBarCodeSeries = docketBarCodeSeries;
    }
}
