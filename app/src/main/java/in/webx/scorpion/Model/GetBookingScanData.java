package in.webx.scorpion.Model;

import java.util.ArrayList;

public class GetBookingScanData {
    private Integer companyCode;
    private String entryBy;
    private String currentBranch;
    private Boolean isSuccess;
    private String message;
    private ArrayList<BookingDocketDetail> bookingScan_List;

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<BookingDocketDetail> getBookingScan_List() {
        return bookingScan_List;
    }

    public void setBookingScan_List(ArrayList<BookingDocketDetail> bookingScan_List) {
        this.bookingScan_List = bookingScan_List;
    }
}
