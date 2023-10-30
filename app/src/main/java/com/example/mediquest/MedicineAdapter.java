package com.example.mediquest;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    //making items clickable
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private List<MedicineItem> medicineList;

    public MedicineAdapter(List<MedicineItem> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item_layout, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineItem medicineItem = medicineList.get(position);
        holder.bind(medicineItem);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView pharmacyTextView;
        private TextView priceTextView;

        private TextView latTextView;

        private TextView lonTextView;
        private TextView unitTextView;

        private TextView geoAddressTextView;

        private TextView DistanceTextView;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicineName);
            pharmacyTextView = itemView.findViewById(R.id.medicinePharmacy);
            priceTextView = itemView.findViewById(R.id.medicinePrice);
           // latTextView = itemView.findViewById(R.id.medicineLat);
           // lonTextView = itemView.findViewById(R.id.medicineLon);
            unitTextView = itemView.findViewById(R.id.medicineUnit);
            geoAddressTextView = itemView.findViewById(R.id.medicineAddress);
           DistanceTextView = itemView.findViewById(R.id.medicineDistance);

            // Set an OnClickListener for the entire item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }

        public void bind(MedicineItem medicineItem) {
            nameTextView.setText(medicineItem.getName());
            pharmacyTextView.setText("Pharmacy: " + medicineItem.getPharmacy());
            priceTextView.setText("Price: $" + medicineItem.getPrice());
            //latTextView.setText("Latitude: " + medicineItem.getLat()); // Assuming you have a String representation of location
            //lonTextView.setText("Longitude: " + medicineItem.getLon());
            unitTextView.setText("Unit: " + medicineItem.getUnit());
            geoAddressTextView.setText("Address: " + medicineItem.getGeoAddress());

            double distance = medicineItem.getDistance(); // Replace this with your actual distance calculation
            DecimalFormat df = new DecimalFormat("0.###"); // This format will show up to three decimal places
            String formattedDistance = df.format(distance);

            DistanceTextView.setText("Distance: " + formattedDistance + " KM");


        }
    }
}

