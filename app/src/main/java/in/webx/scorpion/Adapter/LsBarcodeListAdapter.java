package in.webx.scorpion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;

public class LsBarcodeListAdapter extends RecyclerView.Adapter<LsBarcodeListAdapter.LsBarcodeListViewHolder> {

    ArrayList<docketBarCodeSeries_Tbl> docketList;

    public LsBarcodeListAdapter(ArrayList<docketBarCodeSeries_Tbl> docketList) {
        this.docketList = docketList;
    }

    @NonNull
    @Override
    public LsBarcodeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_complete_docket_barcode_list, parent, false);
        return new LsBarcodeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LsBarcodeListViewHolder holder, int position) {
        holder.tvDktNo.setText(docketList.get(position).getDockNo());
        holder.tvBarcodeNo.setText(docketList.get(position).getBcSerialNo());
    }


    @Override
    public int getItemCount() {
        return docketList.size();
    }

    public static class LsBarcodeListViewHolder extends RecyclerView.ViewHolder {
        TextView tvDktNo, tvBarcodeNo;
        public LsBarcodeListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDktNo = itemView.findViewById(R.id.tv_dktNo);
            tvBarcodeNo = itemView.findViewById(R.id.tv_barcodeNo);
        }
    }
}
