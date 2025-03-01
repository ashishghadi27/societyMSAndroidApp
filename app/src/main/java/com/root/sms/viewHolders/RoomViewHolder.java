package com.root.sms.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.RoomVO;

public class RoomViewHolder extends RecyclerView.ViewHolder {

    private TextView roomNumber, roomSize;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        roomNumber = itemView.findViewById(R.id.roomNumber);
        roomSize = itemView.findViewById(R.id.roomSize);
    }

    public void bind(RoomVO roomVO) {
        roomNumber.setText(String.format("Room No: %s", roomVO.getRoomNo()));
        roomSize.setText(String.format("%s Square Ft", roomVO.getRoomSize()));
    }
}
