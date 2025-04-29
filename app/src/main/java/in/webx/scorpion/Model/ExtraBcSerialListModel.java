package in.webx.scorpion.Model;

public class ExtraBcSerialListModel {
    private int CompanyCode;

    private String BCSerialNo;
    private String DockNo;
    private String DockSf;
    private int Packages;
    private double Weight;


    public void setCompanyCode(int CompanyCode){
        this.CompanyCode = CompanyCode;
    }
    public int getCompanyCode(){
        return this.CompanyCode;
    }
    public void setBCSerialNo(String BCSerialNo){
        this.BCSerialNo = BCSerialNo;
    }
    public String getBCSerialNo(){
        return this.BCSerialNo;
    }

    public String getDockNo() {
        return DockNo;
    }

    public void setDockNo(String dockNo) {
        DockNo = dockNo;
    }

    public String getDockSf() {
        return DockSf;
    }

    public void setDockSf(String dockSf) {
        DockSf = dockSf;
    }

    public int getPackages() {
        return Packages;
    }

    public void setPackages(int packages) {
        Packages = packages;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }
}
