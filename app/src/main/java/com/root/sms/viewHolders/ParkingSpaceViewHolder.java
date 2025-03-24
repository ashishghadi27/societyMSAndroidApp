package com.root.sms.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.ParkingSpaceVO;

public class ParkingSpaceViewHolder extends RecyclerView.ViewHolder {

    private TextView slotId;

    public ParkingSpaceViewHolder(@NonNull View itemView) {
        super(itemView);
        slotId = itemView.findViewById(R.id.slotId);
    }

    public void bind(ParkingSpaceVO parkingSpaceVO) {
        slotId.setText(parkingSpaceVO.getParkingId());
    }
}
