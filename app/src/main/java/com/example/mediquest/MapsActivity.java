package com.example.mediquest;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.DecimalFormat;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Calls onMapReady when the map is loaded

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You don't have the permission; request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            // You already have the permission
            // Perform location-related operations here
        }

        Intent intent = getIntent();

        String name = intent.getStringExtra("itemName");
        String pharmacy = intent.getStringExtra("itemPharmacy");
        double lat = Double.parseDouble(intent.getStringExtra("itemLat"));
        double lon = Double.parseDouble(intent.getStringExtra("itemLon"));
        double price = intent.getDoubleExtra("itemPrice", 0.0); // Provide a default value if needed
        String unit = intent.getStringExtra("itemUnit");
        String geoAddress = intent.getStringExtra("itemGeoAddress");
        double distance = intent.getDoubleExtra("itemDistance", 0.0); // Provide a default value if needed

        TextView nameTextView = findViewById(R.id.nameView);
        TextView pharmacyTextView = findViewById(R.id.pharmacyView);
        TextView priceTextView = findViewById(R.id.priceView);
        // latTextView = findViewById(R.id.medicineLat);
        // lonTextView = findViewById(R.id.medicineLon);
        //TextView unitTextView = findViewById(R.id.medicineUnit);
        TextView geoAddressTextView = findViewById(R.id.addressView);
        TextView DistanceTextView = findViewById(R.id.distanceView);

        nameTextView.setText(name);
        nameTextView.setTextSize(24); // Change 24 to desired text size
        pharmacyTextView.setText("Pharmacy: " + pharmacy);
        pharmacyTextView.setTextSize(20);
        priceTextView.setText("Price: $" + price);
        geoAddressTextView.setText("Address: " + geoAddress);

        DecimalFormat df = new DecimalFormat("0.###"); // This format will show up to three decimal places
        String formattedDistance = df.format(distance);
        DistanceTextView.setText("Distance: " + formattedDistance + " KM");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; perform location-related operations
            } else {
                // Permission denied; handle the denial or inform the user
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();

        double lat = Double.parseDouble(intent.getStringExtra("itemLat"));
        double lon = Double.parseDouble(intent.getStringExtra("itemLon"));
        String geoAddress = intent.getStringExtra("itemGeoAddress");
        String pharmacy = intent.getStringExtra("itemPharmacy");

        LatLng location = new LatLng(lat, lon);

        googleMap.addMarker(new MarkerOptions().position(location).title(pharmacy+" - "+geoAddress));

        // Adjust the zoom level here
        float zoomLevel = 15.0f; // change this value as needed

        // Create a CameraPosition object
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)      // Sets the center of the map to the given location
                .zoom(zoomLevel)       // Sets the zoom level
                .build();

        // Move the camera to the specified position
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}