package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Fragment.DocketStockUpdateFragment;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.activity.DocketThcSummaryActivity;

public class SummaryDocketAdapter extends RecyclerView.Adapter<SummaryDocketAdapter.SummaryDocketViewHolder> {

    DocketThcSummaryActivity docketThcSummaryActivity;
    ArrayList<ThcDocketList_Tbl> thcDocketListTbls;

    public SummaryDocketAdapter(ArrayList<ThcDocketList_Tbl> thcDocketListTbls, DocketThcSummaryActivity docketThcSummaryActivity) {

        this.thcDocketListTbls = thcDocketListTbls;
        this.docketThcSummaryActivity = docketThcSummaryActivity;
    }

    @NonNull
    @Override
    public SummaryDocketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.summary_docket_list, parent, false);
        return new SummaryDocketViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull SummaryDocketViewHolder holder, int position) {

        holder.smDocket.setText(thcDocketListTbls.get(position).getDockNo()+thcDocketListTbls.get(position).getDockSf());
        holder.smMFNumber.setText(thcDocketListTbls.get(position).getTcNo());
        holder.smScanPkg.setText(""+thcDocketListTbls.get(position).getScanPackages());
        holder.smTotalPkg.setText(""+thcDocketListTbls.get(position).getTotalLoadPackages());
        holder.smDestination.setText(""+thcDocketListTbls.get(position).getDestination());

        if(thcDocketListTbls.get(position).getScanPackages() == 0)
        {
            holder.smDelivary.setText("PENDING");
            holder.smDelivary.setTextColor(Color.parseColor("#c82506"));
        }else if (thcDocketListTbls.get(position).getScanPackages().equals(thcDocketListTbls.get(position).getTotalLoadPackages()))
        {
            holder.smDelivary.setText("COMPLETED");
            holder.smDelivary.setTextColor(Color.parseColor("#32CD32"));
        }else
        {
            holder.smDelivary.setText("CONTINUE");
            holder.smDelivary.setTextColor(Color.parseColor("#FF5F1F"));
        }

       // LinearLayout SmLayout = ((DocketThcSummaryActivity) docketThcSummaryActivity).SmLayout;

        holder.itemView.setOnClickListener(v -> {
            Fragment fragment = new DocketStockUpdateFragment(thcDocketListTbls.get(position).getDockNo(),thcDocketListTbls.get(position).getDockSf());
            FragmentTransaction fragmentTransaction = docketThcSummaryActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Container,fragment).addToBackStack(null).commit();
        });

    }

    @Override
    public int getItemCount() {
        return thcDocketListTbls.size();
    }

    public static class SummaryDocketViewHolder extends RecyclerView.ViewHolder {

        TextView smDocket,smMFNumber,smScanPkg,smTotalPkg,smDestination,smDelivary;

        public SummaryDocketViewHolder(@NonNull View itemView) {
            super(itemView);

            smDocket = itemView.findViewById(R.id.sm_docket);
            smMFNumber = itemView.findViewById(R.id.sm_mf_number);
            smScanPkg = itemView.findViewById(R.id.sm_scanPkg);
            smTotalPkg = itemView.findViewById(R.id.sm_totalPkg);
            smDestination = itemView.findViewById(R.id.sm_destination);
            smDelivary = itemView.findViewById(R.id.sm_delivary);

        }
    }
}
