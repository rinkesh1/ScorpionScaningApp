package in.webx.scorpion.Model;

import io.realm.RealmObject;

public class RMInvoiceDataModel extends RealmObject {
    private int invoiceNo;
    private int amount;

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
