package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetCityListOutputModel {
    String ErrorMessage;
    boolean IsSuccess;
    String LastFetchedDateTime;
    ArrayList<CityListModel> CityList=new ArrayList<>();

    public GetCityListOutputModel() {
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public String getLastFetchedDateTime() {
        return LastFetchedDateTime;
    }

    public void setLastFetchedDateTime(String lastFetchedDateTime) {
        LastFetchedDateTime = lastFetchedDateTime;
    }

    public ArrayList<CityListModel> getCityList() {
        return CityList;
    }

    public void setCityList(ArrayList<CityListModel> cityList) {
        CityList = cityList;
    }
}

