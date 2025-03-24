package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.handlers.ParkingSlotClickHandler;
import com.root.sms.viewHolders.ParkingSlotSelectorViewHolder;
import com.root.sms.viewHolders.RoomSelectorViewHolder;
import com.root.sms.vo.ParkingSpaceVO;
import com.root.sms.vo.RoomVO;

import java.util.List;

public class ParkingSlotSelectorAdapter extends RecyclerView.Adapter<ParkingSlotSelectorViewHolder> {

    private final List<ParkingSpaceVO> parkingList;
    private final ParkingSlotClickHandler slotClickHandler;

    public ParkingSlotSelectorAdapter(List<ParkingSpaceVO> parkingList, ParkingSlotClickHandler slotClickHandler) {
        this.parkingList = parkingList;
        this.slotClickHandler = slotClickHandler;
    }

    @NonNull
    @Override
    public ParkingSlotSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parking_slot_layout, parent, false);
        return new ParkingSlotSelectorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotSelectorViewHolder holder, int position) {
        ParkingSpaceVO parkingSpaceVO = parkingList.get(position);
        holder.bind(parkingSpaceVO);
        holder.card.setOnClickListener(view -> {
            slotClickHandler.onSlotClick(parkingSpaceVO);
        });
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }
}

