package in.webx.scorpion.Model;

public class LocationListModel {
    String ActiveFlag;
    String LocationCode;
    String LocationName;

    public LocationListModel(String activeFlag, String locationCode, String locationName) {
        ActiveFlag = activeFlag;
        LocationCode = locationCode;
        LocationName = locationName;
    }

    public String getActiveFlag() {
        return ActiveFlag;
    }

    public void setActiveFlag(String activeFlag) {
        ActiveFlag = activeFlag;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }
}
