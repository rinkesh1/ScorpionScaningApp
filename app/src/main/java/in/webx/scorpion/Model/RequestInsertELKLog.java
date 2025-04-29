package in.webx.scorpion.Model;

public class RequestInsertELKLog {

    private String UserId;
    private String Application;
    private String TenantId;
    private String ModuleName;
    private String ModuleType;
    private String EventDateTime;
    private String DocumentNo;
    private String EventType;
    private String AppType;
    private String en;
    private String EventRandomNo;
    private String LoginRandomNo;

    public RequestInsertELKLog() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getApplication() {
        return Application;
    }

    public void setApplication(String application) {
        Application = application;
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getModuleType() {
        return ModuleType;
    }

    public void setModuleType(String moduleType) {
        ModuleType = moduleType;
    }

    public String getEventDateTime() {
        return EventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        EventDateTime = eventDateTime;
    }

    public String getDocumentNo() {
        return DocumentNo;
    }

    public void setDocumentNo(String documentNo) {
        DocumentNo = documentNo;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getAppType() {
        return AppType;
    }

    public void setAppType(String appType) {
        AppType = appType;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEventRandomNo() {
        return EventRandomNo;
    }

    public void setEventRandomNo(String eventRandomNo) {
        EventRandomNo = eventRandomNo;
    }

    public String getLoginRandomNo() {
        return LoginRandomNo;
    }

    public void setLoginRandomNo(String loginRandomNo) {
        LoginRandomNo = loginRandomNo;
    }
}
