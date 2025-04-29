package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetPayBasisListOutputModel {
    String ErrorMessage;
    boolean IsSuccess;
    String LastFetchedDateTime;
    ArrayList<PayBasisListModel> PayBasisList=new ArrayList<>();

    public GetPayBasisListOutputModel() {
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

    public ArrayList<PayBasisListModel> getPayBasisList() {
        return PayBasisList;
    }

    public void setPayBasisList(ArrayList<PayBasisListModel> payBasisList) {
        PayBasisList = payBasisList;
    }
}


