package com.root.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.viewHolders.RoomViewHolder;
import com.root.sms.vo.RoomVO;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {

    private final List<RoomVO> roomList;

    public RoomAdapter(List<RoomVO> roomList) {
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_layout, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        holder.bind(roomList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
