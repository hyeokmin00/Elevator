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
import com.example.elevator.api.model.LiftInfo;

import java.util.List;


public class LiftRecyAdapter extends RecyclerView.Adapter<LiftRecyAdapter.LiftViewHolder> {

    List<LiftInfo> liftInfoInfoList;
    Context context;

    public LiftRecyAdapter(List<LiftInfo> liftInfoInfoList, Context context) {
        this.liftInfoInfoList = liftInfoInfoList;
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
        LiftInfo totalLiftInfoInfo = liftInfoInfoList.get(position);

        //date split
      //  String updatedAtDate = totalLiftInfo.getUpdatedAt().substring(0,10);

        totalLiftViewHolder.tvId.setText(totalLiftInfoInfo.getLiftId());
        totalLiftViewHolder.tvName.setText(totalLiftInfoInfo.getLiftName());
        totalLiftViewHolder.tvStatus.setText(totalLiftInfoInfo.getLiftStatus());
        totalLiftViewHolder.tvAddress.setText(totalLiftInfoInfo.getAddress());
      //  totalLiftViewHolder.tvUpdate.setText(updatedAtDate);
        totalLiftViewHolder.itemView.setOnClickListener((v)->{

            //   Toast.makeText(context,"승강기 번호 : "+mAddrList.get(position).getTotalLiftInfoList().getLiftId(), Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"승강기 id : "+ liftInfoInfoList.get(position).getLiftId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return liftInfoInfoList.size();
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