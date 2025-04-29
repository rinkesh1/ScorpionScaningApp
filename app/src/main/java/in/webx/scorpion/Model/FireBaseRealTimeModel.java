package in.webx.scorpion.Model;

public class FireBaseRealTimeModel {
    private String userid;
    private String inDate;
    private String userRequest;

    public FireBaseRealTimeModel(String userid, String inDate, String userRequest) {
        this.userid = userid;
        this.inDate = inDate;
        this.userRequest = userRequest;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }
}
