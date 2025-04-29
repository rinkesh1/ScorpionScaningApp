package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetDestinationListOutputModel {
    String ErrorMessage;
    boolean IsSuccess;
    String LastFetchedDateTime;
    ArrayList<LocationListModel> LocationList=new ArrayList<>();

    public GetDestinationListOutputModel() {
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

    public ArrayList<LocationListModel> getLocationList() {
        return LocationList;
    }

    public void setLocationList(ArrayList<LocationListModel> locationList) {
        LocationList = locationList;
    }
}

