package in.webx.scorpion.Model;

import java.util.ArrayList;

import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryProcess;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.WareHouse;

public class GetStockUpdateSelectionData
{

    ArrayList<ArrivalCondition> ArrivalCondition;
    ArrayList<WareHouse> WareHouse;
    ArrayList<DeliveryProcess> DeliveryProcess;
    ArrayList<DeliveryType> DeliveryType;

    public ArrayList<ArrivalCondition> getArrivalCondition() {
        return ArrivalCondition;
    }

    public void setArrivalCondition(ArrayList<ArrivalCondition> arrivalCondition) {
        ArrivalCondition = arrivalCondition;
    }

    public ArrayList<WareHouse> getWareHouse() {
        return WareHouse;
    }

    public void setWareHouse(ArrayList<WareHouse> wareHouse) {
        WareHouse = wareHouse;
    }

    public ArrayList<DeliveryProcess> getDeliveryProcess() {
        return DeliveryProcess;
    }

    public void setDeliveryProcess(ArrayList<DeliveryProcess> deliveryProcess) {
        DeliveryProcess = deliveryProcess;
    }

    public ArrayList<DeliveryType> getDeliveryType() {
        return DeliveryType;
    }

    public void setDeliveryType(ArrayList<DeliveryType> deliveryType) {
        DeliveryType = deliveryType;
    }
}
