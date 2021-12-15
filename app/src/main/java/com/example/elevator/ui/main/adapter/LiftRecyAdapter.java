package com.example.elevator.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator.R;
import com.example.elevator.api.model.LiftInfo;
import com.example.elevator.api.roomdb.Lift;
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.ui.report.WriteReportActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//context or List가 아닌 DB 객체를 전달받아 사용함
// 해당 RecyclerView api에서 바로 받아오는 것이 아니라 DB에 저장된 데이터를 가져와 출력하기 때문임

public class LiftRecyAdapter extends RecyclerView.Adapter<com.example.elevator.ui.main.adapter.LiftRecyAdapter.LiftViewHolder> {

    private List<Lift> items = new ArrayList<>();
    Context context;

    public LiftRecyAdapter(Context context, List<Lift> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public void onBindViewHolder(LiftViewHolder liftViewHolder, int position) {
        final Lift totalLiftInfoInfo = items.get(position);


        liftViewHolder.tvId.setText(totalLiftInfoInfo.getLiftId());
        liftViewHolder.tvName.setText(totalLiftInfoInfo.getName());
        liftViewHolder.tvStatus.setText(totalLiftInfoInfo.getStatus());
        liftViewHolder.tvAddress.setText(totalLiftInfoInfo.getAddr());
        liftViewHolder.tvUpdate.setText(totalLiftInfoInfo.getCreateAt().substring(0,10));


        liftViewHolder.itemView.setOnClickListener((v) -> {
            Log.d("Test", "LiftRecyAdapter - 승강기 id : " + items.get(position).getLiftId());

            Intent intent = new Intent(context, WriteReportActivity.class);
            intent.putExtra("lift_id", items.get(position).getLiftId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return items.size();
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
                        Intent intent = new Intent(context, WriteReportActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //    intent.putExtra("lift_id",liftInfoInfoList.get );

                    }
                }
            });
        }
    }

    //todo pullrefresh
    public void setItem(List<Lift> data) {
        items = data;
        notifyDataSetChanged();
    }


}