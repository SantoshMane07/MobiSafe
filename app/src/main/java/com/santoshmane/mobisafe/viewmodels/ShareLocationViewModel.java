package com.santoshmane.mobisafe.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.Manifest;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.lang.*;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import com.santoshmane.mobisafe.ContactModel;
import com.santoshmane.mobisafe.DataBaseHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
//
import com.google.android.gms.location.LocationRequest;


public class ShareLocationViewModel extends ViewModel {

    //
    LocationRequest locationRequest;

    public ShareLocationViewModel() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
    }
    //

    public boolean shareEmergLocationAndMessageToAllContacts(ArrayList<ContactModel> arrEmergContacts, String emergMssg, Context context) {
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    if (isGPSEnabled(context)) {
                        //...
                        //...
                        LocationServices.getFusedLocationProviderClient(context)
                                .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);

                                        LocationServices.getFusedLocationProviderClient(context)
                                                .removeLocationUpdates(this);

                                        if (locationResult != null && locationResult.getLocations().size() > 0) {

                                            int index = locationResult.getLocations().size() - 1;
                                            double latitude = locationResult.getLocations().get(index).getLatitude();
                                            double longitude = locationResult.getLocations().get(index).getLongitude();
                                            //Sending...to contacts
                                            shareLocationToContacts(latitude, longitude, emergMssg, arrEmergContacts);
                                            //
                                        }
                                    }
                                }, Looper.getMainLooper());
                        //...
                        return true;
                    } else {
                        turnOnGPS(context, locationRequest);
                        return false;
                    }
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 2);
                    return false;
                }
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        //
        return true;
    }

    // Get All Emerg Contacts List
    public ArrayList<ContactModel> getAllEmergContacts(Context context) {
        ArrayList<ContactModel> arrEmergContacts = new ArrayList<>();
        //
        arrEmergContacts = (ArrayList<ContactModel>) DataBaseHelper.getDB(context).contactsDao().getAllContacts();
        //
        return arrEmergContacts;
    }

    // Share Custom Mssg And Location To Specific Contact
    public boolean shareCustomMssgAndLocationToSpecificContact(String phone, String Custommssg, Context context) {
        //
        boolean flag = sendMssgToSingleContact(phone, Custommssg, context);
        //
        return flag;
    }

    // Share Only Emerg Mssg And Location To Specific Contact
    public boolean shareOnlyEmergMssgAndLocationToSpecificContact(String phone, String emergMssg, Context context) {
        //
        boolean flag = sendMssgToSingleContact(phone, emergMssg, context);
        //
        return flag;
    }

    //Helper Functions
    //
    //Is GPS enabled
    private boolean isGPSEnabled(Context context) {
        LocationManager locationManager = null;
        boolean isEnabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    //Turn On GPS
    public void turnOnGPS(Context context, LocationRequest locationRequest) {
        //
        //
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText((Activity) context, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult((Activity) context, 2);
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
        //
    }

    //Sending Locations To All Contacts
    public void shareLocationToContacts(double latitude, double longitude, String emergMssg, ArrayList<ContactModel> arrEmergContacts) {
        //Log.d("LLL", "shareLocationToContacts: "+arrEmergContacts.size());
        SmsManager smsManager = SmsManager.getDefault();
        for (ContactModel contactDetails : arrEmergContacts) {
            //Log.d("LL", "shareLocationToContacts: " +latitude+" "+longitude+" "+emergMssg+" "+contactDetails.getUserPhone());
            String location = "https://www.google.com/maps/search/?api=1&query=" + latitude + "%2C" + longitude;
            smsManager.sendTextMessage(contactDetails.getUserPhone(), null, emergMssg + " My Location is: " + location, null, null);
        }
    }

    //Sending location to Single Contact
    public boolean sendMssgToSingleContact(String phone, String mssg, Context context) {
        //..
        boolean result = false;
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled(context)) {
                    //...
                    //...
                    LocationServices.getFusedLocationProviderClient(context)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(context)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        //Sending...to contact
                                        SmsManager smsManager = SmsManager.getDefault();
                                        String location = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                                        try {
                                            smsManager.sendTextMessage(phone, null, mssg + " My Location is: " + location, null, null);
                                        } catch (SecurityException e) {
                                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 3);
                                            Toast.makeText((Activity) context, "Sorry Failed! Please give SMS Permission to MobiSafe and then Try again", Toast.LENGTH_SHORT).show();
                                        }
                                        //.........
                                        //
                                    }
                                }
                            }, Looper.getMainLooper());
                    //...
                    return true;
                } else {
                    turnOnGPS(context, locationRequest);
                    return false;
                }
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        //
        //..
        return true;
    }
}
