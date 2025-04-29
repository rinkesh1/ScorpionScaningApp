package in.webx.scorpion.Adapter;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.VerifyDocketitemModel;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.activity.VerifyTHCSummaryActivity;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;

public class DocketListAdapter extends RecyclerView.Adapter<DocketListAdapter.Holder> {

    ArrayList<VerifyDocketitemModel> mSortList;
    VerifyTHCSummaryActivity context;
    String check = "";

    public DocketListAdapter(VerifyTHCSummaryActivity verifyTHCSummaryActivity, ArrayList<VerifyDocketitemModel> sortList, String check) {
        this.mSortList = sortList;
        this.context = verifyTHCSummaryActivity;
        this.check = check;
    }

    @NonNull
    @Override
    public DocketListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_drag, parent, false);
        return new DocketListAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocketListAdapter.Holder holder, int i) {
        holder.mTvbcSeriseNo.setText(mSortList.get(i).getBcSerialNo());
        holder.mTvDocketNo.setText(mSortList.get(i).getDockNo());
        if(check.equalsIgnoreCase("Scan Package") || check.equalsIgnoreCase("Short Package")){
            holder.mbtClose.setVisibility(View.GONE);
        }

        holder.mbtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Adatper Position: "+i);
                removeAlertDialog(mSortList.get(i).getDockNo(),mSortList.get(i).getBcSerialNo(),i);
            }
        });

        if(check.equalsIgnoreCase("Scan Package")){
            holder.mLinDocket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "Lin Scan PKG 1: "+mSortList.get(i).getDockNo());
                    Log.d("TAG", "Lin Scan PKG 2: "+mSortList.get(i).getBcSerialNo());
                    IsDamageCheck(mSortList.get(i).getDockNo(),mSortList.get(i).getBcSerialNo(),i);
                }
            });
        }

    }


    public void IsDamageCheck(String dockNo, String bcSerialNo, int pos){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_damage_alert);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick Close");
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick Accept :"+dockNo+" / "+bcSerialNo);
                damageBarcodefromList(dockNo,bcSerialNo,pos);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void damageBarcodefromList(String dockNo, String bcSerialNo, int pos) {
        realm.executeTransaction(r -> {
            docketBarCodeSeries_Tbl getBarcode = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("bcSerialNo", bcSerialNo)
                    .equalTo("dockNo", dockNo)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .findFirst();
            if(getBarcode != null){
                docketBarCodeSeries_Tbl bc = new docketBarCodeSeries_Tbl();

                bc.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                bc.setLsThcNo(SharedPreference.getStringValue(ConstantData.SP_THC_NO));
                bc.setDockNo(dockNo);
                bc.setDockSf(getBarcode.getDockSf());
                bc.setBcSerialNo(bcSerialNo);
                bc.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                bc.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                bc.setBarcodeScanned(true);
                bc.setBarcodeType(ConstantData.SP_KEY_DAMAGE);
                realm.insertOrUpdate(bc);

                mSortList.remove(pos);
                notifyItemRemoved(pos);

                context.init();
            }
        });
    }

    public void removeAlertDialog(String dktNo, String bcNo, int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_delete_alert);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick Close");
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick Accept");
                removeBarcodefromList(dktNo,bcNo,pos);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void removeBarcodefromList(String dktNo,String bcNo,int position) {
        Log.d("TAG", "Demage Check: "+check);
        realm.executeTransaction(r -> {
            docketBarCodeSeries_Tbl deletList;
            if(check.equalsIgnoreCase("Damage Package")){
                deletList = r.where(docketBarCodeSeries_Tbl.class)
                                .equalTo("dockNo", dktNo)
                                .equalTo("bcSerialNo", bcNo)
                                .equalTo("isBarcodeScanned", true)
                                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                .findFirst();
            }else {
                Log.d("TAG", "Remove From Extra");

                docketBarCodeSeries_Tbl dltModel = r.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("dockNo", dktNo)
                        .equalTo("bcSerialNo", bcNo)
                        .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .findFirst();
                dltModel.deleteFromRealm();

                if(dltModel != null){
                    mSortList.remove(position);
                    notifyDataSetChanged();
//                    notifyItemRemoved(position);
                    context.init();
                }
                return;
            }

            if(deletList != null){
                docketBarCodeSeries_Tbl bc = new docketBarCodeSeries_Tbl();

                bc.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                bc.setLsThcNo(SharedPreference.getStringValue(ConstantData.SP_THC_NO));
                bc.setDockNo(dktNo);
                bc.setDockSf(deletList.getDockSf());
                bc.setBcSerialNo(bcNo);
                bc.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                bc.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                bc.setBarcodeScanned(true);
                if(check.equalsIgnoreCase("Damage Package")){
                    bc.setBarcodeType(ConstantData.SP_KEY_INWARD);
                }else {
                    bc.setBarcodeType(ConstantData.SP_KEY_DAMAGE);
                }
                r.insertOrUpdate(bc);

                mSortList.remove(position);
                notifyDataSetChanged();
//                notifyItemRemoved(position);

                context.init();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSortList.size();
    }

    public void filterList(ArrayList<VerifyDocketitemModel> filteredList) {
        mSortList = filteredList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView mTvbcSeriseNo,mTvDocketNo;
        ImageButton mbtClose;
        LinearLayout mLinDocket;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mTvbcSeriseNo = itemView.findViewById(R.id.txt_bcSeriseNo);
            mTvDocketNo = itemView.findViewById(R.id.txt_docketNo);
            mbtClose = itemView.findViewById(R.id.bt_close);
            mLinDocket = itemView.findViewById(R.id.lin_docket);
        }
    }
}
