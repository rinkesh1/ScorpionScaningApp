package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetBillingPartyListOutputModel {
    String ErrorMessage;
    boolean IsSuccess;
    String LastFetchedDateTime;
    ArrayList<CustomerListModel> CustomerList= new ArrayList<>();

    public GetBillingPartyListOutputModel() {
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

    public ArrayList<CustomerListModel> getCustomerList() {
        return CustomerList;
    }

    public void setCustomerList(ArrayList<CustomerListModel> customerList) {
        CustomerList = customerList;
    }
}

