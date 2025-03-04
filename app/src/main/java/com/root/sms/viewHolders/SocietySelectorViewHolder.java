package com.root.sms.viewHolders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.sms.R;
import com.root.sms.vo.SocietyVO;

import org.apache.commons.lang3.StringUtils;

public class SocietySelectorViewHolder extends RecyclerView.ViewHolder {

    public TextView societyName, plotNumber, address;
    public RelativeLayout card;

    public SocietySelectorViewHolder(@NonNull View itemView) {
        super(itemView);
        societyName = itemView.findViewById(R.id.societyName);
        plotNumber = itemView.findViewById(R.id.societyPlotNo);
        address = itemView.findViewById(R.id.address);
        card = itemView.findViewById(R.id.card);
    }

    public void bind(SocietyVO societyVO) {
        plotNumber.setText(String.format("Plot No. %s", societyVO.getPlotNumber()));
        societyName.setText(societyVO.getName());
        String addressLine1 = StringUtils.isEmpty(societyVO.getAddressLine1()) ? "" : societyVO.getAddressLine1();
        String addressLine2 = StringUtils.isEmpty(societyVO.getAddressLine2()) ? "" : societyVO.getAddressLine2();
        String addressLine = addressLine1.trim()+ " " + addressLine2.trim();
        address.setText(addressLine);
    }
}
