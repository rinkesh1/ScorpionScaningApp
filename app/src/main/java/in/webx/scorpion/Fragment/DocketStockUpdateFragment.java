package in.webx.scorpion.Fragment;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.WareHouse;
import in.webx.scorpion.activity.DocketThcSummaryActivity;
import in.webx.scorpion.util.CommonMethod;
import io.realm.RealmResults;

public class DocketStockUpdateFragment extends Fragment {

    View view;
    String dockNo, dockSf, ArrivalCondition, Godown, DelivaryType;
    ImageView Sm_back;
    int ArrivalCode = 0;

    public DocketStockUpdateFragment(String dockNo, String dockSf) {
        this.dockNo = dockNo;
        this.dockSf = dockSf;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_docket_stock_update, container, false);

        view.setOnTouchListener((v, event) -> true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        init();
    }


    @SuppressLint("SetTextI18n")
    private void init() {

        Sm_back = view.findViewById(R.id.Sm_back);
        Sm_back.setOnClickListener(this::goToActivity);

        TextView stockUpdate_DocketNo = view.findViewById(R.id.stockUpdate_DocketNo);
        TextView stockUpdate_ThcNo = view.findViewById(R.id.stockUpdate_ThcNo);
        TextView stockUpdate_Package = view.findViewById(R.id.stockUpdate_Package);
        TextView stockUpdate_DateOfBooking = view.findViewById(R.id.stockUpdate_DateOfBooking);
        TextView stockUpdate_Update = view.findViewById(R.id.stockUpdate_Update);

        ThcDocketList_Tbl thcDocketListTbl = realm.where(ThcDocketList_Tbl.class)
                .equalTo("dockNo", dockNo)
                .equalTo("dockSf", dockSf).findFirst();

        assert thcDocketListTbl != null;
        stockUpdate_DocketNo.setText(thcDocketListTbl.getDockNo());
        stockUpdate_ThcNo.setText(thcDocketListTbl.getThcNo());
        stockUpdate_Package.setText(thcDocketListTbl.getScanPackages() + "/" + thcDocketListTbl.getTotalLoadPackages());
        stockUpdate_DateOfBooking.setText(CommonMethod.getCurrentDateTimeNew());

        stockUpdate_Update.setOnClickListener(v -> save());

        //Spinner AlertArrivalCondition
        final AutoCompleteTextView stockUpdateArrivalCondition = view.findViewById(R.id.stockUpdate_ArrivalCondition);

        RealmResults<ArrivalCondition> arrivalConditionList = realm.where(ArrivalCondition.class).findAll();

        ArrayList<String> ArrivalConditionList = new ArrayList<>();

        for (ArrivalCondition arrivalCondition : arrivalConditionList) {
            ArrivalConditionList.add(arrivalCondition.getCodeDesc());
        }

        ArrayAdapter<String> arrivalConditionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ArrivalConditionList);
        arrivalConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stockUpdateArrivalCondition.setAdapter(arrivalConditionAdapter);

        stockUpdateArrivalCondition.setText(thcDocketListTbl.getArrivalCondition(), false);

        //Spinner Godown
        final AutoCompleteTextView stockUpdateGodown = view.findViewById(R.id.stockUpdate_Godown);

        RealmResults<WareHouse> wareHousesList = realm.where(WareHouse.class).findAll();

        ArrayList<String> GoDownList = new ArrayList<>();

        for (WareHouse wareHouses : wareHousesList) {
            GoDownList.add(wareHouses.getCodeDesc());
        }

        ArrayAdapter<String> goDownAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, GoDownList);
        goDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stockUpdateGodown.setAdapter(goDownAdapter);

        stockUpdateGodown.setText(thcDocketListTbl.getGodown(), false);

        //Spinner DelivaryType
        final AutoCompleteTextView stockUpdateDelivaryType = view.findViewById(R.id.stockUpdate_DelivaryType);

        RealmResults<DeliveryType> DeliveryTypeList = realm.where(DeliveryType.class).findAll();

        ArrayList<String> DeliveryTypeArrayList = new ArrayList<>();

        for (DeliveryType DeliveryType : DeliveryTypeList) {
            DeliveryTypeArrayList.add(DeliveryType.getCodeDesc());
        }

        ArrayAdapter<String> DeliveryTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, DeliveryTypeArrayList);
        DeliveryTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stockUpdateDelivaryType.setAdapter(DeliveryTypeAdapter);

        stockUpdateDelivaryType.setText(thcDocketListTbl.getDeliveryType(), false);

        stockUpdateArrivalCondition.setOnItemClickListener((parent, view, position, id) -> {
            ArrivalCondition = ArrivalConditionList.get(position);

            for (ArrivalCondition arrivalCondition : arrivalConditionList) {
                if (arrivalCondition.getCodeDesc().equals(ArrivalCondition)) {
                    ArrivalCode = arrivalCondition.getCodeId();
                }
            }
        });

        stockUpdateGodown.setOnItemClickListener((parent, view, position, id) ->
                Godown = GoDownList.get(position));

        stockUpdateDelivaryType.setOnItemClickListener((parent, view, position, id) ->
                DelivaryType = DeliveryTypeArrayList.get(position));

    }

    //onCLick goToActivity backPress
    private void goToActivity(View view) {
        Intent intent = new Intent(requireActivity().getApplication(), DocketThcSummaryActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void save() {
        realm.executeTransaction(realm -> {
            ThcDocketList_Tbl thcDocketList_tbl = realm.where(ThcDocketList_Tbl.class)
                    .equalTo("dockNo", dockNo)
                    .equalTo("dockSf", dockSf).findFirst();

            if (ArrivalCondition != null) {
                assert thcDocketList_tbl != null;
                thcDocketList_tbl.setArrivalCondition(ArrivalCondition);
                thcDocketList_tbl.setArrivalCode(ArrivalCode);
            }

            if (Godown != null) {
                assert thcDocketList_tbl != null;
                thcDocketList_tbl.setGodown(Godown);
            }

            if (DelivaryType != null) {
                assert thcDocketList_tbl != null;
                thcDocketList_tbl.setDeliveryType(DelivaryType);
            }

            assert thcDocketList_tbl != null;
            realm.insertOrUpdate(thcDocketList_tbl);

            Intent intent = new Intent(requireActivity().getApplication(), DocketThcSummaryActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}