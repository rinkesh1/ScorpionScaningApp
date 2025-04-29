package in.webx.scorpion.Model;

public class InvoiceDataModel {
    private String invoiceNo;
    private String product;
    private String packagetype;
    private String noofpkg;
    private String decvalue;
    private String actweight;
    private String chardegweight;
    private String invoicevalue;

    public InvoiceDataModel(String invoiceNo, String product, String packagetype, String noofpkg, String decvalue, String actweight, String chardegweight, String invoicevalue) {
        this.invoiceNo = invoiceNo;
        this.product = product;
        this.packagetype = packagetype;
        this.noofpkg = noofpkg;
        this.decvalue = decvalue;
        this.actweight = actweight;
        this.chardegweight = chardegweight;
        this.invoicevalue = invoicevalue;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPackagetype() {
        return packagetype;
    }

    public void setPackagetype(String packagetype) {
        this.packagetype = packagetype;
    }

    public String getNoofpkg() {
        return noofpkg;
    }

    public void setNoofpkg(String noofpkg) {
        this.noofpkg = noofpkg;
    }

    public String getDecvalue() {
        return decvalue;
    }

    public void setDecvalue(String decvalue) {
        this.decvalue = decvalue;
    }

    public String getActweight() {
        return actweight;
    }

    public void setActweight(String actweight) {
        this.actweight = actweight;
    }

    public String getChardegweight() {
        return chardegweight;
    }

    public void setChardegweight(String chardegweight) {
        this.chardegweight = chardegweight;
    }

    public String getInvoicevalue() {
        return invoicevalue;
    }

    public void setInvoicevalue(String invoicevalue) {
        this.invoicevalue = invoicevalue;
    }
}
