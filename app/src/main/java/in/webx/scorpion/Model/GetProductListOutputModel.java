package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetProductListOutputModel {
    String ErrorMessage;
    boolean IsSuccess;
    String LastFetchedDateTime;
    ArrayList<ProductListModel> ProductList=new ArrayList<>();

    public GetProductListOutputModel() {
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

    public ArrayList<ProductListModel> getProductList() {
        return ProductList;
    }

    public void setProductList(ArrayList<ProductListModel> productList) {
        ProductList = productList;
    }
}

