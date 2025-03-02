package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.handlers.RoomClickHandler;
import com.root.sms.viewHolders.RoomSelectorViewHolder;
import com.root.sms.vo.RoomVO;

import java.util.List;

public class RoomSelectorAdapter extends RecyclerView.Adapter<RoomSelectorViewHolder> {

    private final List<RoomVO> roomList;
    private RoomClickHandler roomClickHandler;

    public RoomSelectorAdapter(List<RoomVO> roomList, RoomClickHandler roomClickHandler) {
        this.roomList = roomList;
        this.roomClickHandler = roomClickHandler;
    }

    @NonNull
    @Override
    public RoomSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_layout, parent, false);
        return new RoomSelectorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomSelectorViewHolder holder, int position) {
        RoomVO roomVO = roomList.get(position);
        holder.bind(roomVO);
        holder.card.setOnClickListener(view -> {
            roomClickHandler.onRoomClick(roomVO);
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

