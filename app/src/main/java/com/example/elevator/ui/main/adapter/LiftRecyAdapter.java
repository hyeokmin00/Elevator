package com.example.elevator.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator.R;
import com.example.elevator.api.model.LiftInfo;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.ui.report.WriteReport;

import java.util.ArrayList;
import java.util.List;


public class LiftRecyAdapter extends RecyclerView.Adapter<LiftRecyAdapter.LiftViewHolder> {

    ArrayList<LiftInfo> liftInfoInfoList;
    Context context;

    public LiftRecyAdapter(ArrayList<LiftInfo> liftInfoInfoList, Context context) {
        this.liftInfoInfoList = liftInfoInfoList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(LiftViewHolder liftViewHolder, int position) {
        final LiftInfo totalLiftInfoInfo = liftInfoInfoList.get(position);

        liftViewHolder.tvId.setText(totalLiftInfoInfo.getLiftId());
        liftViewHolder.tvName.setText(totalLiftInfoInfo.getLiftName());
        liftViewHolder.tvStatus.setText(totalLiftInfoInfo.getLiftStatus());
        liftViewHolder.tvAddress.setText(totalLiftInfoInfo.getAddress());
        //  liftViewHolder.tvUpdate.setText(updatedAtDate);


        liftViewHolder.itemView.setOnClickListener((v) -> {
            Toast.makeText(context,"승강기 id : " + liftInfoInfoList.get(position).getLiftId(), Toast.LENGTH_SHORT).show();
          //  Log.d("Test", "승강기 id : " + liftInfoInfoList.get(position).getLiftId());
            Log.d("Test", "승강기 id : " + liftInfoInfoList.get(position).getLiftId());

        });
    }

    @Override
    public int getItemCount() {
        return liftInfoInfoList.size();
    }

    private OnItemClickEventListener mItemClickListener;

    @Override
    public LiftViewHolder onCreateViewHolder(ViewGroup viewGroup, int a_viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_total_lift, viewGroup, false);
        return new LiftViewHolder(view, mItemClickListener);
    }

    public interface OnItemClickEventListener {
        void onItemClick(View v, int a_position);
    }

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        this.mItemClickListener = listener;
    }


    public class LiftViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvStatus;
        TextView tvAddress;
        TextView tvUpdate;

        public LiftViewHolder(View itemView,
                              final OnItemClickEventListener itemClickListener) {

            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.item_total_id);
            tvName = (TextView) itemView.findViewById(R.id.item_total_name);
            tvStatus = (TextView) itemView.findViewById(R.id.item_total_status);
            tvAddress = (TextView) itemView.findViewById(R.id.item_total_address);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_total_updated_at);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(v, position);
                        Intent intent = new Intent(context, WriteReport.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //    intent.putExtra("lift_id",liftInfoInfoList.get );


                    }
                }
            });
        }
    }


}