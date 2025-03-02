package com.root.sms.viewHolders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.RoomVO;

public class RoomSelectorViewHolder extends RecyclerView.ViewHolder {

    TextView roomNumber, roomSize;
    public RelativeLayout card;

    public RoomSelectorViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        roomNumber = itemView.findViewById(R.id.roomNumber);
        roomSize = itemView.findViewById(R.id.roomSize);
    }

    public void bind(RoomVO roomVO) {
        roomNumber.setText(String.format("Room No: %s", roomVO.getRoomNo()));
        roomSize.setText(String.format("%s Square Ft", roomVO.getRoomSize()));
    }
}
