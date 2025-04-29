package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryType extends RealmObject {

    @PrimaryKey
    private String CodeId;

    private String CodeDesc;
    private String CodeType;

    public DeliveryType() {
    }

    public String getCodeId() {
        return CodeId;
    }

    public void setCodeId(String codeId) {
        CodeId = codeId;
    }

    public String getCodeDesc() {
        return CodeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        CodeDesc = codeDesc;
    }

    public String getCodeType() {
        return CodeType;
    }

    public void setCodeType(String codeType) {
        CodeType = codeType;
    }

}
