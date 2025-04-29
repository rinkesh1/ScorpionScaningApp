package in.webx.scorpion.Model;

public class AuthenticationInputModel {
    private String UserName;
    private String Password;
    private String CompanyCode;
    private String FCMToken = "fgb6fwT07PLZaCg8aFRKXN:APA91bFXjSnsN26zEY1VBu0QGygf1bIW_IaNw0O0yCmPASua5aeFeo-8xfsKB7GFmOqr1L6ASuvcSYtCety6GUHyo9X3b0M42inANQBh66fxjr2WGVYtNdOmHS04PutqcjBc4IS3XCwO";
    private String AuthType;
    private String CompanyAlias;
    private String AppVersion;

    public AuthenticationInputModel() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getAuthType() {
        return AuthType;
    }

    public void setAuthType(String authType) {
        AuthType = authType;
    }

    public String getCompanyAlias() {
        return CompanyAlias;
    }

    public void setCompanyAlias(String companyAlias) {
        CompanyAlias = companyAlias;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }
}
