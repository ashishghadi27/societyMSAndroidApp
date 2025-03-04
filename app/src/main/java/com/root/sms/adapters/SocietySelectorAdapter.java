package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.handlers.RoomClickHandler;
import com.root.sms.handlers.SocietyClickHandler;
import com.root.sms.viewHolders.RoomSelectorViewHolder;
import com.root.sms.viewHolders.SocietySelectorViewHolder;
import com.root.sms.vo.RoomVO;
import com.root.sms.vo.SocietyVO;

import java.util.List;

public class SocietySelectorAdapter extends RecyclerView.Adapter<SocietySelectorViewHolder> {

    private final List<SocietyVO> societyList;
    private SocietyClickHandler societyClickHandler;

    public SocietySelectorAdapter(List<SocietyVO> societyList, SocietyClickHandler societyClickHandler) {
        this.societyList = societyList;
        this.societyClickHandler = societyClickHandler;
    }

    @NonNull
    @Override
    public SocietySelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.society_selector_layout, parent, false);
        return new SocietySelectorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocietySelectorViewHolder holder, int position) {
        SocietyVO societyVO = societyList.get(position);
        holder.bind(societyVO);
        holder.card.setOnClickListener(view -> {
            societyClickHandler.onSocietyClick(societyVO);
        });
    }

    @Override
    public int getItemCount() {
        return societyList.size();
    }
}
