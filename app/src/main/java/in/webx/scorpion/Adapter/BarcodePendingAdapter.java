package in.webx.scorpion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.PRSBarcodeDetails;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.PRSBarcodePickupActivity;

public class BarcodePendingAdapter extends RecyclerView.Adapter<BarcodePendingAdapter.ViewHolder> {

    PRSBarcodePickupActivity context;
    ArrayList<PRSBarcodeDetails> barcodeList;

    public BarcodePendingAdapter(ArrayList<PRSBarcodeDetails> barcodeList, PRSBarcodePickupActivity prsBarcodePickupActivity) {
        this.context = prsBarcodePickupActivity;
        this.barcodeList = barcodeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thc_pending_docket_barcode_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_barcodeNo.setText(barcodeList.get(position).getBcSeriesNo());
        holder.tv_dktNo.setText(barcodeList.get(position).getDocketNo());
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_dktNo, tv_barcodeNo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_dktNo = itemView.findViewById(R.id.tv_dktNo);
            tv_barcodeNo = itemView.findViewById(R.id.tv_barcodeNo);
        }
    }
}
