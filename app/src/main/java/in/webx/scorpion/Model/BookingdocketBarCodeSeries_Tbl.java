package in.webx.scorpion.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class BookingdocketBarCodeSeries_Tbl extends RealmObject {
    @PrimaryKey
    private String bcSeriesNo;

    @Required
    private String companyCode;
    private String docketNo;
    private Boolean isScanned;
    private String user;
    private String vehicleNo;

    public String getBcSeriesNo() {
        return bcSeriesNo;
    }

    public void setBcSeriesNo(String bcSeriesNo) {
        this.bcSeriesNo = bcSeriesNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public Boolean getScanned() {
        return isScanned;
    }

    public void setScanned(Boolean scanned) {
        isScanned = scanned;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
