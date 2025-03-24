package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.viewHolders.ParkingSpaceViewHolder;
import com.root.sms.viewHolders.RoomViewHolder;
import com.root.sms.vo.ParkingSpaceVO;
import com.root.sms.vo.RoomVO;

import java.util.List;

public class ParkingSpaceAdapter extends RecyclerView.Adapter<ParkingSpaceViewHolder> {

    private final List<ParkingSpaceVO> parkingSpaceVOList;

    public ParkingSpaceAdapter(List<ParkingSpaceVO> parkingSpaceVOList) {
        this.parkingSpaceVOList = parkingSpaceVOList;
    }

    @NonNull
    @Override
    public ParkingSpaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parking_slot_layout, parent, false);
        return new ParkingSpaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSpaceViewHolder holder, int position) {
        holder.bind(parkingSpaceVOList.get(position));
    }

    @Override
    public int getItemCount() {
        return parkingSpaceVOList.size();
    }
}
