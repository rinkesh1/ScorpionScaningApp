package in.webx.scorpion.Model;

public class GetBookingStockUpdate {
    private Boolean isSuccess;
    private String message;

    public GetBookingStockUpdate() {
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
