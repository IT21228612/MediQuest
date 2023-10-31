package com.example.doctor2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView textviewName,textviewAge,textviewAlt,textviewStatus;
    View view;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        textviewName=itemView.findViewById(R.id.textviewName);
        textviewAge=itemView.findViewById(R.id.textviewAge);
        textviewAlt=itemView.findViewById(R.id.textviewAlt);
        textviewStatus = itemView.findViewById(R.id.textviewStatus);
        view=itemView;
    }


}
