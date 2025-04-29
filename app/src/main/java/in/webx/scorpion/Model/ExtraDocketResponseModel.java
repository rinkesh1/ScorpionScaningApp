package in.webx.scorpion.Model;

import java.util.List;

public class ExtraDocketResponseModel {
    private int companyCode;
    private boolean isSuccess;
    private String errorMessage;
    private List<ExtraDocketListModel> list;

    public void setCompanyCode(int companyCode){
        this.companyCode = companyCode;
    }
    public int getCompanyCode(){
        return this.companyCode;
    }
    public void setIsSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
    public boolean getIsSuccess(){
        return this.isSuccess;
    }
    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
    public void setList(List<ExtraDocketListModel> list){
        this.list = list;
    }
    public List<ExtraDocketListModel> getList(){
        return this.list;
    }
}