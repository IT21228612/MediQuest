package com.example.mediquest;

import static android.widget.Toast.LENGTH_SHORT;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//current geo location



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//current geo location end

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<MedicineItem> medicineList;

    double userLatitude ;
    double userLongitude ;



    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.searchView1);


        recyclerView = findViewById(R.id.recyclerView);

        // Initialize the adapter and set it to the RecyclerView
        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(medicineList);
        recyclerView.setAdapter(medicineAdapter);


        // Set an OnItemClickListener for the adapter
        medicineAdapter.setOnItemClickListener(new MedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here

                MedicineItem clickedItem = medicineList.get(position);
                // You can do something with the clicked item, like displaying details.
                // Display a toast message with item details
                String message = "Clicked on " + clickedItem.getName() + " at " + clickedItem.getPharmacy();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // Create an Intent to open a new activity
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                // Pass data to the new activity if needed
                intent.putExtra("itemName", clickedItem.getName());
                intent.putExtra("itemPharmacy", clickedItem.getPharmacy());
                intent.putExtra("itemLat", clickedItem.getLat());
                intent.putExtra("itemLon", clickedItem.getLon());
                intent.putExtra("itemPrice", clickedItem.getPrice());
                intent.putExtra("itemUnit", clickedItem.getUnit());
                intent.putExtra("itemGeoAddress", clickedItem.getGeoAddress());
                intent.putExtra("itemDistance", clickedItem.getDistance());
                // Add more data as needed

                // Start the new activity
                startActivity(intent);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the query when the user submits it
                if (!query.isEmpty()) {
                    // Display a toast message with the submitted query
                    //Toast.makeText(this, "Submitted Query: " + query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // When the query text changes, perform a Firestore query
                queryFirestoreForSearchResults(db, newText);
                return true;
            }
        });

        //current geo location start
        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
            }
        });
        //current geo location end

        // sort spinner
        Spinner sortSpinner = findViewById(R.id.sortSpinner);

        // Set an OnItemSelectedListener for the spinner
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the sorting option change
                sortMedicineList(); // Sort the list based on the selected option
                medicineAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

// Create an ArrayAdapter with custom sorting options
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.add("Price");
        adapter.add("Current Location");

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);

// Set "Current Location" as the default selected sort option
        sortSpinner.setSelection(adapter.getPosition("Current Location"));





    }

    private void queryFirestoreForSearchResults(FirebaseFirestore db, String searchQuery) {
        // Clear the existing medicine list
        medicineList.clear();

        // Create a query to search for documents where the "name" field contains the search query
        Query query = db.collection("medicine")
                .whereGreaterThanOrEqualTo("name", searchQuery)
                .whereLessThanOrEqualTo("name", searchQuery + "\uf8ff");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String name = document.getString("name");
                    String pharmacy = document.getString("pharmacy");
                    double price = document.getDouble("price");
                    String lat = document.getString("lat");
                    String lon = document.getString("lon");
                    String unit = document.getString("unit");

                    // Create a MedicineItem object with the retrieved data
                    MedicineItem medicineItem = new MedicineItem(name, pharmacy, price, lat, lon, unit);

                    String geoAddress = reverseGeocode(Double.parseDouble(medicineItem.getLat()),
                            Double.parseDouble(medicineItem.getLon()));


                    double distance2 = calculateDistance(getUserLatitude(), getUserLongitude(),
                            Double.parseDouble(medicineItem.getLat()), Double.parseDouble(medicineItem.getLon()));

                    medicineItem.setDistance(distance2);
                    medicineItem.setGeoAddress(geoAddress);

                    // Add the MedicineItem to the list
                    medicineList.add(medicineItem);
                }

                // Sort the list based on the selected sorting option
                sortMedicineList();

                // Notify the adapter that the data has changed
                medicineAdapter.notifyDataSetChanged();
            }

        });
    }

    //spinner start

    private void sortMedicineList() {
        // Get the selected sorting option from the Spinner
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        String selectedOption = sortSpinner.getSelectedItem().toString();




        // Sort the list based on the selected option
        if (selectedOption.equals("Price")) {
            Collections.sort(medicineList, new Comparator<MedicineItem>() {
                @Override
                public int compare(MedicineItem medicine1, MedicineItem medicine2) {
                    // Compare by price
                    return Double.compare(medicine1.getPrice(), medicine2.getPrice());
                }
            });
        } else if (selectedOption.equals("Current Location")) {
            // Sort by distance from the user's current location
            Collections.sort(medicineList, new Comparator<MedicineItem>() {
                @Override
                public int compare(MedicineItem medicine1, MedicineItem medicine2) {
                    // Calculate the distance of medicine1 and medicine2 from the user's location
                    double distance1 = calculateDistance(
                            getUserLatitude(), getUserLongitude(),
                            Double.parseDouble(medicine1.getLat()), Double.parseDouble(medicine1.getLon()));
                    double distance2 = calculateDistance(
                            getUserLatitude(), getUserLongitude(),
                            Double.parseDouble(medicine2.getLat()), Double.parseDouble(medicine2.getLon()));

                    // Compare by distance
                    return Double.compare(distance1, distance2);
                }
            });
        }
    }

    // Calculate the distance between two sets of latitude and longitude coordinates
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // The radius of the Earth in kilometers
        final double EARTH_RADIUS = 6371;

        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between latitudes and longitudes
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Calculate the Haversine distance
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;




        return distance;
    }

    //geocoder
    public String reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0); // Full address
                String city = address.getLocality(); // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country
                String postalCode = address.getPostalCode(); // Postal code

                return addressLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Location not found";
    }



    //current geo location start

    private TextView AddressText;
    private ImageButton LocationButton;
    private LocationRequest locationRequest;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        String CurrentAddress = reverseGeocode(latitude,longitude) ;

                                        AddressText.setText(CurrentAddress);

                                        setUserLatitude(latitude);
                                        setUserLongitude(longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


}

