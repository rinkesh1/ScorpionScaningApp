package in.webx.scorpion.Model;

public class RequestPRSUpdateData {
    private String VehicleNumber="";
    private String BookedBy="";
    private String DocketNumber="";

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getBookedBy() {
        return BookedBy;
    }

    public void setBookedBy(String bookedBy) {
        BookedBy = bookedBy;
    }

    public String getDocketNumber() {
        return DocketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        DocketNumber = docketNumber;
    }
}
