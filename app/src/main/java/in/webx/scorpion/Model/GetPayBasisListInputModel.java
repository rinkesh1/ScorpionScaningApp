package in.webx.scorpion.Model;

public class GetPayBasisListInputModel {
    String Location;
    int TenantId;
    String LastFetchedDateTime;


    public GetPayBasisListInputModel() {
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getTenantId() {
        return TenantId;
    }

    public void setTenantId(int tenantId) {
        TenantId = tenantId;
    }

    public String getLastFetchedDateTime() {
        return LastFetchedDateTime;
    }

    public void setLastFetchedDateTime(String lastFetchedDateTime) {
        LastFetchedDateTime = lastFetchedDateTime;
    }
}
