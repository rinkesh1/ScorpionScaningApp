package in.webx.scorpion.Model;

public class ThcDocketBarCodeSeries {

    private String companyCode;
    private String dockNo;
    private String DockSf;
    private String bcSerialNo;
    private String isBarcodeScanned;
    private String bcScannedDatetime;
    private String isContinueScanned;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    public String getDockSf() {
        return DockSf;
    }

    public void setDockSf(String dockSf) {
        DockSf = dockSf;
    }

    public String getBcSerialNo() {
        return bcSerialNo;
    }

    public void setBcSerialNo(String bcSerialNo) {
        this.bcSerialNo = bcSerialNo;
    }

    public String getIsBarcodeScanned() {
        return isBarcodeScanned;
    }

    public void setIsBarcodeScanned(String isBarcodeScanned) {
        this.isBarcodeScanned = isBarcodeScanned;
    }

    public String getBcScannedDatetime() {
        return bcScannedDatetime;
    }

    public void setBcScannedDatetime(String bcScannedDatetime) {
        this.bcScannedDatetime = bcScannedDatetime;
    }

    public String getIsContinueScanned() {
        return isContinueScanned;
    }

    public void setIsContinueScanned(String isContinueScanned) {
        this.isContinueScanned = isContinueScanned;
    }
}
