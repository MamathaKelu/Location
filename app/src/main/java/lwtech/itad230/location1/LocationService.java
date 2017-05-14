/*Mamatha Kelu
* Assignment 4*/
package lwtech.itad230.location1;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    static protected ArrayList<String> locations = new ArrayList<String>();
    boolean firstPosition = false;

    private static double M_THRESHOLD = 10.0;
    double prevLat = 0.0, prevLon = 0.0;
    String prevDat = "";

    // leave alone for now
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( mGoogleApiClient == null ) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onConnected(Bundle bundle) {
        int permissionCheck;
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if( permissionCheck == PackageManager.PERMISSION_DENIED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if( mLastLocation == null ) {
            return;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(4000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if( mLocationRequest != null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, (LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        String dat = DateFormat.getDateTimeInstance().format(new Date());
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        if(firstPosition != true)
        {
            locations.add(dat);
            locations.add(String.valueOf(lon));
            locations.add(String.valueOf(lat));

            /*first position is computed*/
            if(dat != "")
            {
                firstPosition = true;
                /*save the position*/
                prevDat = dat;
                prevLon = lon;
                prevLat = lat;
            }
        }
        else
        {
            /*check whether current position is within threshold*/
            if (computeDistance(lat,prevLat,lon,prevLon)> M_THRESHOLD) {
                /*if position is beyond threshold then send the previous valid position*/
                locations.add(prevDat);
                locations.add(String.valueOf(prevLon));
                locations.add(String.valueOf(prevLat));
            }
            else
            {
                locations.add(dat);
                locations.add(String.valueOf(lon));
                locations.add(String.valueOf(lat));
                prevDat = dat;
                prevLon = lon;
                prevLat = lat;
            }
        }
    }

    /**
     * computeDistance - method computes the distance between current and previous instance position
     * @param lat1 - latitude of current position
     * @param lat2 - latitude of previous position
     * @param lon1 - longitude of current position
     * @param lon2 - longitude of previous position
     * @return - distance travelled in miles
     */
    public double computeDistance(double lat1, double lat2, double lon1, double lon2)
    {
        double R = 6378.137; //radius of earth in Km
        double dLat = (lat2-lat1)*Math.PI/180;
        double dLon = (lon2-lon1)*Math.PI/180;
        double a = Math.pow(Math.sin((dLat/2)),2.0)+ Math.cos(lat1*Math.PI/180)*Math.cos(lat2*Math.PI/180)*Math.pow((Math.sin(dLon/2)),2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d =  (R * c)/1.6; /*distance in miles*/
        return d;

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }
}
