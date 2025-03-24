package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.handlers.MemberClickHandler;
import com.root.sms.handlers.ParkingSlotClickHandler;
import com.root.sms.viewHolders.MemberSelectorViewHolder;
import com.root.sms.viewHolders.ParkingSlotSelectorViewHolder;
import com.root.sms.vo.MemberVO;
import com.root.sms.vo.ParkingSpaceVO;

import java.util.List;

public class MemberSelectorAdapter extends RecyclerView.Adapter<MemberSelectorViewHolder> {

    private final List<MemberVO> memberVOList;
    private final MemberClickHandler memberClickHandler;

    public MemberSelectorAdapter(List<MemberVO> memberVOList, MemberClickHandler memberClickHandler) {
        this.memberVOList = memberVOList;
        this.memberClickHandler = memberClickHandler;
    }

    @NonNull
    @Override
    public MemberSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_layout, parent, false);
        return new MemberSelectorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberSelectorViewHolder holder, int position) {
        MemberVO memberVO = memberVOList.get(position);
        holder.bind(memberVO);
        holder.card.setOnClickListener(view -> {
            memberClickHandler.onMemberClick(memberVO);
        });
    }

    @Override
    public int getItemCount() {
        return memberVOList.size();
    }
}

