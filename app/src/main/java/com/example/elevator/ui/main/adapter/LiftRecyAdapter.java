package com.example.elevator.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator.R;
import com.example.elevator.api.model.Lift;

import java.util.List;


public class LiftRecyAdapter extends RecyclerView.Adapter<LiftRecyAdapter.LiftViewHolder> {

    List<Lift> liftInfoList;
    Context context;

    public LiftRecyAdapter(List<Lift> liftInfoList, Context context) {
        this.liftInfoList = liftInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public LiftViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_total_lift, viewGroup, false);
        LiftViewHolder totalLiftViewHolder = new LiftViewHolder(rootView);
        return totalLiftViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull LiftViewHolder totalLiftViewHolder, int position) {
        Lift totalLiftInfo = liftInfoList.get(position);

        //date split
      //  String updatedAtDate = totalLiftInfo.getUpdatedAt().substring(0,10);


        totalLiftViewHolder.tvId.setText(totalLiftInfo.getLift_id());
        totalLiftViewHolder.tvName.setText(totalLiftInfo.getLift_name());
        totalLiftViewHolder.tvStatus.setText(totalLiftInfo.getLift_status());
        totalLiftViewHolder.tvAddress.setText(totalLiftInfo.getLift_address());
      //  totalLiftViewHolder.tvUpdate.setText(updatedAtDate);
        totalLiftViewHolder.itemView.setOnClickListener((v)->{

            //   Toast.makeText(context,"승강기 번호 : "+mAddrList.get(position).getTotalLiftInfoList().getLiftId(), Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"승강기 id : "+liftInfoList.get(position).getLift_id(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return liftInfoList.size();
    }

    public class LiftViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvStatus;
        TextView tvAddress;
        TextView tvUpdate;

        public LiftViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.item_total_id);
            tvName = (TextView) itemView.findViewById(R.id.item_total_name);
            tvStatus = (TextView) itemView.findViewById(R.id.item_total_status);
            tvAddress = (TextView) itemView.findViewById(R.id.item_total_address);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_total_updated_at);
        }
    }
}