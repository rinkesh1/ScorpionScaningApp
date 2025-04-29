package in.webx.scorpion.Model;

public class BookingDocketStockBarCodeSeries {
    private String docketNo;
    private String bcSeriesNo;
    private Boolean isScanned;

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getBcSeriesNo() {
        return bcSeriesNo;
    }

    public void setBcSeriesNo(String bcSeriesNo) {
        this.bcSeriesNo = bcSeriesNo;
    }

    public Boolean getScanned() {
        return isScanned;
    }

    public void setScanned(Boolean scanned) {
        isScanned = scanned;
    }
}
