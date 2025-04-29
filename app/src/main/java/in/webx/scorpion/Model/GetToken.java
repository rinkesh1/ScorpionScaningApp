package in.webx.scorpion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetToken {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("employeeType")
    @Expose
    private String employeeType;
    @SerializedName("customerId")
    @Expose
    private Object customerId;
    @SerializedName("expires_In")
    @Expose
    private Integer expires_In;
    @SerializedName("drsDriverWise")
    @Expose
    private Object drsDriverWise;
    @SerializedName("drsPushEnable")
    @Expose
    private Object drsPushEnable;
    @SerializedName("loc_Level")
    @Expose
    private String loc_Level;
    @SerializedName("MasterUserData")
    @Expose
    private MasterUserData masterUserData;

    public GetToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public Integer getExpires_In() {
        return expires_In;
    }

    public void setExpires_In(Integer expires_In) {
        this.expires_In = expires_In;
    }

    public Object getDrsDriverWise() {
        return drsDriverWise;
    }

    public void setDrsDriverWise(Object drsDriverWise) {
        this.drsDriverWise = drsDriverWise;
    }

    public Object getDrsPushEnable() {
        return drsPushEnable;
    }

    public void setDrsPushEnable(Object drsPushEnable) {
        this.drsPushEnable = drsPushEnable;
    }

    public String getLoc_Level() {
        return loc_Level;
    }

    public void setLoc_Level(String loc_Level) {
        this.loc_Level = loc_Level;
    }

    public MasterUserData getMasterUserData() {
        return masterUserData;
    }

    public void setMasterUserData(MasterUserData masterUserData) {
        this.masterUserData = masterUserData;
    }
}
