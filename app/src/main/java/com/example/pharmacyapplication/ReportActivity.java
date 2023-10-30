package com.example.pharmacyapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Bundle extras = getIntent().getExtras();

        String reportText = generateReport();

        TextView textViewMedicineName = findViewById(R.id.textViewMedicineName);
        TextView textViewMedicineDosage = findViewById(R.id.textViewMedicineDosage);
        TextView textViewMedicineManu = findViewById(R.id.textViewMedicineManu);
        TextView textViewMedicineQuantity = findViewById(R.id.textViewMedicineQuantity);
        TextView textViewMedicinePrice = findViewById(R.id.textViewMedicinePrice);
        TextView textViewMedicineManuDate = findViewById(R.id.textViewMedicineManuDate);
        TextView textViewMedicineExDate = findViewById(R.id.textViewMedicineExDate);

        textViewMedicineName.setText(extras.getString("MedicineName"));
        textViewMedicineDosage.setText(extras.getString("MedicineDosage"));
        textViewMedicineManu.setText(extras.getString("MedicineManufacturer"));
        textViewMedicineQuantity.setText(extras.getString("MedicineQuentity"));
        textViewMedicinePrice.setText(extras.getString("MedicinePrice"));
        textViewMedicineManuDate.setText(extras.getString("MedicineManufactureDate"));
        textViewMedicineExDate.setText(extras.getString("MedicineExpireDate"));
    }
    private String generateReport() {
        StringBuilder report = new StringBuilder();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String medicineName = extras.getString("MedicineName");
            String medicineDosage = extras.getString("MedicineDosage");
            String medicineManufacturer = extras.getString("MedicineManufacturer");
            String medicineQuentity = extras.getString("MedicineQuentity");
            String medicinePrice = extras.getString("MedicinePrice");
            String medicineManufactureDate = extras.getString("MedicineManufactureDate");
            String medicineExpireDate = extras.getString("MedicineExpireDate");

            // Append medicine information to the report
            report.append("Medicine Report:\n\n");
            report.append("Medicine Name: ").append(medicineName).append("\n");
            report.append("Medicine Dosage: ").append(medicineDosage).append("\n");
            report.append("Medicine Manufacturer: ").append(medicineManufacturer).append("\n");
            report.append("Medicine Quentity: ").append(medicineQuentity).append("\n");
            report.append("Medicine Price: ").append(medicinePrice).append("\n");
            report.append("Medicine Manufacture Date: ").append(medicineManufactureDate).append("\n");
            report.append("Medicine Expiry Date: ").append(medicineExpireDate).append("\n");


        }
        return report.toString();

    }
}
