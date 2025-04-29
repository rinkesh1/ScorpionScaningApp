package in.webx.scorpion.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RMdocketAddRemoveModel extends RealmObject {
    @PrimaryKey
    private String docketNo;
    private String docketSF;

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public String getDocketSF() {
        return docketSF;
    }

    public void setDocketSF(String docketSF) {
        this.docketSF = docketSF;
    }
}
