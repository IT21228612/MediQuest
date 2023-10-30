package com.example.pharmacyapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Medicine> list;
    DatabaseReference databaseReference;

    public MyAdapter(Context context, ArrayList<Medicine> list) {
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference("medicine");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.medicineentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medicine medicine = list.get(position);
        holder.medicineName.setText(medicine.getMedName());
        holder.medicineDosage.setText(medicine.getMedDosage());
        holder.medicinemanufacturer.setText(medicine.getMedManu());
        holder.medicineQuentity.setText(medicine.getMedQuentity());
        holder.medicinePricePerUnit.setText(medicine.getMedPrice());
        holder.medicineMdate.setText(medicine.getMedManuDate());
        holder.medicineEdate.setText(medicine.getMedExDate());

        // Set the onClick listener for the Delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the item clicked
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Call the deleteMedicine method with the position
                    deleteMedicine(adapterPosition);
                }
            }
        });
        holder.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement report functionality here
                Intent intent = new Intent(context, ReportActivity.class);
                Medicine medicine = list.get(position);
                intent.putExtra("MedicineName", medicine.getMedName());
                intent.putExtra("MedicineDosage", medicine.getMedDosage());
                intent.putExtra("MedicineManufacturer", medicine.getMedManu());
                intent.putExtra("MedicineQuentity", medicine.getMedQuentity());
                intent.putExtra("MedicinePrice", medicine.getMedPrice());
                intent.putExtra("MedicineManufactureDate", medicine.getMedManuDate());
                intent.putExtra("MedicineExpireDate", medicine.getMedExDate());



                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Method to delete a specific medicine
    public void deleteMedicine(int position) {
        Medicine deletedMedicine = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);

        // Delete the medicine from the Firebase Realtime Database
        DatabaseReference medicineRef = databaseReference.child(deletedMedicine.getMedName());
        medicineRef.removeValue();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicineDosage, medicinemanufacturer, medicineQuentity, medicinePricePerUnit, medicineMdate, medicineEdate;
        Button btnDelete,btnReport;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicName);
            medicineDosage = itemView.findViewById(R.id.medicDosage);
            medicinemanufacturer = itemView.findViewById(R.id.medicManu);
            medicineQuentity = itemView.findViewById(R.id.medicQuentity);
            medicinePricePerUnit = itemView.findViewById(R.id.medicPrice);
            medicineMdate = itemView.findViewById(R.id.medicMDate);
            medicineEdate = itemView.findViewById(R.id.medicEDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}
