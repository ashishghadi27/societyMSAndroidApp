package com.root.sms.viewHolders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.ParkingSpaceVO;
import com.root.sms.vo.RoomVO;

public class ParkingSlotSelectorViewHolder extends RecyclerView.ViewHolder {

    TextView slotId;
    public RelativeLayout card;

    public ParkingSlotSelectorViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        slotId = itemView.findViewById(R.id.slotId);
    }

    public void bind(ParkingSpaceVO parkingSpaceVO) {
        slotId.setText(parkingSpaceVO.getParkingId());
    }
}
