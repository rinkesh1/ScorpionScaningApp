package in.webx.scorpion.Model;

public class RequestBookingScanData {

    private String CompanyCode;
    private String CurrentLocation;
    private String VehicleNo;
    private String DocketNo;
    private String UserId;

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
