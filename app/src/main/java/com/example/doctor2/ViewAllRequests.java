package com.example.doctor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.Arrays;

public class ViewAllRequests extends AppCompatActivity {

    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerAdapter<model, MyViewHolder> adapter;

    private DatabaseReference DoctorDbRef;
    private RecyclerView recyclerView;

    private Button filterButton;
    private String currentFilter = "Pending"; // Set "Pending" as the default filter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_requests);

        DoctorDbRef = FirebaseDatabase.getInstance().getReference().child("Doctor");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        TextView patientRequestsTextView = findViewById(R.id.patientRequestsTextView);
        patientRequestsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(ViewAllRequests.this, MainActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });

        filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        // Initial call to applyFilter with "Pending" as the default filter
        applyFilter(currentFilter);
    }

    private void applyFilter(String filter) {
        Query filteredRef = DoctorDbRef;

        if (!filter.equals("All")) {
            filteredRef = DoctorDbRef.orderByChild("status").equalTo(filter);
        }

        FirebaseRecyclerOptions<model> filteredOptions = new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(filteredRef, model.class)
                .build();

        if (adapter != null) {
            adapter.stopListening();
        }

        adapter = new FirebaseRecyclerAdapter<model, MyViewHolder>(filteredOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull model model) {
                final String name = model.getName();
                final String age = model.getAge();
                final String gender = model.getSex();
                final String alt = model.getAlt();
                final String symp = model.getSymp();
                final String status = model.getStatus();

                model.setKey(getRef(position).getKey());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(getApplicationContext(), Activity2.class);
                        intent2.putExtra("name", name);
                        intent2.putExtra("age", age);
                        intent2.putExtra("sex", gender);
                        intent2.putExtra("alt", alt);
                        intent2.putExtra("symp", symp);
                        intent2.putExtra("key", model.getKey());

                        Intent intent3 = new Intent(getApplicationContext(), Activity3.class);
                        intent3.putExtra("name", name);
                        intent3.putExtra("age", age);
                        intent3.putExtra("sex", gender);
                        intent3.putExtra("alt", alt);
                        intent3.putExtra("symp", symp);
                        intent3.putExtra("key", model.getKey());

                        startActivity(intent2);
                    }
                });

                holder.textviewName.setText(name);
                holder.textviewAlt.setText(alt);

                if (status != null && !status.isEmpty()) {
                    holder.textviewStatus.setText("Status: " + status);
                } else {
                    holder.textviewStatus.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_layout, parent, false);
                return new MyViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showFilterDialog() {
        final String[] filterOptions = {"Pending", "Accepted", "Rejected", "All"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Filter");
        builder.setSingleChoiceItems(filterOptions, Arrays.asList(filterOptions).indexOf(currentFilter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentFilter = filterOptions[which];
                dialog.dismiss();
                applyFilter(currentFilter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        public SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String key = adapter.getRef(position).getKey();

            // Handle the swipe to delete action here
            deleteItem(key);
        }
    }

    private void deleteItem(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Patient's Request");
        builder.setMessage("Are you sure you want to delete this  Patient's Request?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "Yes," so delete the item
                DoctorDbRef.child(key).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ViewAllRequests.this, " Patient's Request deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ViewAllRequests.this, "Error deleting", Toast.LENGTH_SHORT).show();
                        });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "No," so finish the activity and immediately restart it
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
        builder.show();
    }


}


