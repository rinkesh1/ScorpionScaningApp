package in.webx.scorpion.RealmDatabase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ArrivalCondition extends RealmObject {

    @PrimaryKey
    private Integer CodeId;

    private String CodeDesc;
    private String CodeType;

    public ArrivalCondition() {
    }

    public Integer getCodeId() {
        return CodeId;
    }

    public void setCodeId(Integer codeId) {
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
