package in.webx.scorpion.Model;

public class TokenGet {

    private String token;
    private Integer expires_In;
    private String userName;
    private String branch;
    private String tenantId;
    private boolean ispartialScan;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getExpires_In() {
        return expires_In;
    }

    public void setExpires_In(Integer expires_In) {
        this.expires_In = expires_In;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isIspartialScan() {
        return ispartialScan;
    }

    public void setIspartialScan(boolean ispartialScan) {
        this.ispartialScan = ispartialScan;
    }
}
