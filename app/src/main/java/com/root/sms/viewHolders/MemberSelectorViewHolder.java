package com.root.sms.viewHolders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.MemberVO;

public class MemberSelectorViewHolder extends RecyclerView.ViewHolder {

    TextView name, roomNumber;
    public RelativeLayout card;

    public MemberSelectorViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        name = itemView.findViewById(R.id.memberName);
        roomNumber = itemView.findViewById(R.id.roomNumber);
    }

    public void bind(MemberVO memberVO) {
        name.setText(String.format("%s %s", memberVO.getFirstName(), memberVO.getLastName()));
        roomNumber.setText(String.format("Room No. %s", memberVO.getRoomId()));
    }
}
