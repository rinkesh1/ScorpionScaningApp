package in.webx.scorpion.Model;

import io.realm.RealmObject;

public class ExtraDocketListModel extends RealmObject {
    private String userId;
    private int companyCode;
    private String dockNo;
    private String dockSf;
    private int totalPackages;
    private String bcSerialNo;
    private String origin;
    private String destination;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyCode(int companyCode){
        this.companyCode = companyCode;
    }
    public int getCompanyCode(){
        return this.companyCode;
    }
    public void setDockNo(String dockNo){
        this.dockNo = dockNo;
    }
    public String getDockNo(){
        return this.dockNo;
    }
    public void setDockSf(String dockSf){
        this.dockSf = dockSf;
    }
    public String getDockSf(){
        return this.dockSf;
    }
    public void setTotalPackages(int totalPackages){
        this.totalPackages = totalPackages;
    }
    public int getTotalPackages(){
        return this.totalPackages;
    }
    public void setBcSerialNo(String bcSerialNo){
        this.bcSerialNo = bcSerialNo;
    }
    public String getBcSerialNo(){
        return this.bcSerialNo;
    }
    public void setOrigin(String origin){
        this.origin = origin;
    }
    public String getOrigin(){
        return this.origin;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        return this.destination;
    }
}
