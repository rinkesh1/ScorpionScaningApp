package in.webx.scorpion.Model;

public class DocketList {
    private String DockNo;

    private String docketSF;

    public void setDocketNo(String docketNo){
        this.DockNo = docketNo;
    }
    public String getDocketNo(){
        return this.DockNo;
    }
    public void setDocketSF(String docketSF){
        this.docketSF = docketSF;
    }
    public String getDocketSF(){
        return this.docketSF;
    }
}
