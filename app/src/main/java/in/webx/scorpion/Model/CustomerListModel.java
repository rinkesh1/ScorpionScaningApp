package in.webx.scorpion.Model;

public class CustomerListModel {

    String ActiveFlag;
    String CustomerCode;
    String CustomerName;

    public CustomerListModel(String activeFlag, String customerCode, String customerName) {
        ActiveFlag = activeFlag;
        CustomerCode = customerCode;
        CustomerName = customerName;
    }

    public String getActiveFlag() {
        return ActiveFlag;
    }

    public void setActiveFlag(String activeFlag) {
        ActiveFlag = activeFlag;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
}
