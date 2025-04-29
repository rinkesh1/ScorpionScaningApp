package in.webx.scorpion.Model;

import java.util.ArrayList;

public class BookingStockUpdate {
    private Integer companyCode;
    private String entryBy;
    private String currentBranch;
    private ArrayList<BookingDocketStockDetail> bookingScan_List;

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

    public ArrayList<BookingDocketStockDetail> getBookingScan_List() {
        return bookingScan_List;
    }

    public void setBookingScan_List(ArrayList<BookingDocketStockDetail> bookingScan_List) {
        this.bookingScan_List = bookingScan_List;
    }
}
