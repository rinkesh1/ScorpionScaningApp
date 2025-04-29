package in.webx.scorpion.Model;

public class CityListModel {
    String ActiveFlag;
    String CityCode;
    String CityName;

    public CityListModel(String activeFlag, String cityCode, String cityName) {
        ActiveFlag = activeFlag;
        CityCode = cityCode;
        CityName = cityName;
    }

    public String getActiveFlag() {
        return ActiveFlag;
    }

    public void setActiveFlag(String activeFlag) {
        ActiveFlag = activeFlag;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
}
