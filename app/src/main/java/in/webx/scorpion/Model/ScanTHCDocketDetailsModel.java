package in.webx.scorpion.Model;

public class ScanTHCDocketDetailsModel {
    private String BCSerialNo;
    private int CompanyCode;
    private String DockNo;
    private String DockSf;
    private boolean IsManual;
    private boolean IsBarcodeScanned;
    private String THCno;
    private int Packages;
    private double Volume;
    private double Weight;
    private boolean IsDamage;
    private boolean IsExtraBarcode;
    private boolean IsPilferage;

    public void setBCSerialNo(String BCSerialNo) {
        this.BCSerialNo = BCSerialNo;
    }

    public String getBCSerialNo() {
        return this.BCSerialNo;
    }

    public void setCompanyCode(int CompanyCode) {
        this.CompanyCode = CompanyCode;
    }

    public int getCompanyCode() {
        return this.CompanyCode;
    }

    public void setDockNo(String DockNo) {
        this.DockNo = DockNo;
    }

    public String getDockNo() {
        return this.DockNo;
    }

    public void setDockSf(String DockSf) {
        this.DockSf = DockSf;
    }

    public String getDockSf() {
        return this.DockSf;
    }

    public void setIsManual(boolean IsManual) {
        this.IsManual = IsManual;
    }

    public boolean getIsManual() {
        return this.IsManual;
    }

    public void setIsBarcodeScanned(boolean IsBarcodeScanned) {
        this.IsBarcodeScanned = IsBarcodeScanned;
    }

    public boolean getIsBarcodeScanned() {
        return this.IsBarcodeScanned;
    }

    public void setTHCno(String THCno) {
        this.THCno = THCno;
    }

    public String getTHCno() {
        return this.THCno;
    }

    public void setPackages(int Packages) {
        this.Packages = Packages;
    }

    public int getPackages() {
        return this.Packages;
    }

    public void setVolume(double Volume) {
        this.Volume = Volume;
    }

    public double getVolume() {
        return this.Volume;
    }

    public void setWeight(double Weight) {
        this.Weight = Weight;
    }

    public double getWeight() {
        return this.Weight;
    }

    public boolean isDamage() {
        return IsDamage;
    }

    public void setDamage(boolean damage) {
        IsDamage = damage;
    }

    public boolean isPilferage() {
        return IsPilferage;
    }

    public void setPilferage(boolean pilferage) {
        IsPilferage = pilferage;
    }

    public boolean isExtraBarcode() {
        return IsExtraBarcode;
    }

    public void setExtraBarcode(boolean extraBarcode) {
        IsExtraBarcode = extraBarcode;
    }
}
