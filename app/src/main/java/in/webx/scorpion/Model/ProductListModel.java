package in.webx.scorpion.Model;

public class ProductListModel {
    String ActiveFlag;
    String ProductCode;
    String ProductName;

    public ProductListModel(String activeFlag, String productCode, String productName) {
        ActiveFlag = activeFlag;
        ProductCode = productCode;
        ProductName = productName;
    }

    public String getActiveFlag() {
        return ActiveFlag;
    }

    public void setActiveFlag(String activeFlag) {
        ActiveFlag = activeFlag;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
