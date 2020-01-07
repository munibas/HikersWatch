package com.munibasiddiqi.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location!", location.toString());
                try {
                    updateLocationInfo(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else{
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            /*Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null){

                updateLocationInfo(lastKnownLocation);

            }*/
        }


    }

    private void updateLocationInfo(Location location) throws IOException {

        Log.i("Last Known Location", location.toString());

        TextView latTextView = (TextView) findViewById(R.id.LatTextView);
        TextView lngTextView = (TextView) findViewById(R.id.LngTextView);
        TextView accTextView = (TextView) findViewById(R.id.AccTextView);
        TextView altTextView = (TextView) findViewById(R.id.AltTextView);
        TextView addressTextView = (TextView) findViewById(R.id.AddressTextView);

        latTextView.setText("Latitude: "+Double.toString(location.getLatitude()));
        lngTextView.setText("Longitude: "+Double.toString(location.getLongitude()));
        altTextView.setText("Altitude: "+Double.toString(location.getAltitude()));
        accTextView.setText("Accuracy: "+Double.toString(location.getAccuracy()));

        String address = "Could not find address :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {

            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddress != null && listAddress.size()>0){

                Log.i("Address ",listAddress.toString());
                address = "Address: ";

                if (listAddress.get(0).getFeatureName() != null){
                    address += listAddress.get(0).getFeatureName()+" ";
                }
                if (listAddress.get(0).getThoroughfare() != null){
                    address += listAddress.get(0).getThoroughfare()+"\n";
                }
                if (listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality()+" ";
                }
                if (listAddress.get(0).getAdminArea() != null){
                    address += listAddress.get(0).getAdminArea()+" ";
                }
                if (listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode()+"\n";
                }
                if (listAddress.get(0).getCountryName() != null) {
                    address += listAddress.get(0).getCountryName();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

       addressTextView.setText(address);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }
}
