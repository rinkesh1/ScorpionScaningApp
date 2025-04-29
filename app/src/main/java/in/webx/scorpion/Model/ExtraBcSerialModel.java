package in.webx.scorpion.Model;
import java.util.ArrayList;

public class ExtraBcSerialModel
{
    private int CompanyCode;
    private String BranchCode;

    private ArrayList<ExtraBcSerialListModel> List;

    public void setCompanyCode(int CompanyCode){
        this.CompanyCode = CompanyCode;
    }
    public int getCompanyCode(){
        return this.CompanyCode;
    }
    public void setBranchCode(String BranchCode){
        this.BranchCode = BranchCode;
    }
    public String getBranchCode(){
        return this.BranchCode;
    }
    public void setList(ArrayList<ExtraBcSerialListModel> List){
        this.List = List;
    }
    public ArrayList<ExtraBcSerialListModel> getList(){
        return this.List;
    }
}
