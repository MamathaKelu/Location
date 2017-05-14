/*Mamatha Kelu
* Assignment 4*/
package lwtech.itad230.location1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    static final int READ_BLOCK_SIZE = 100;
    TextView textView;
    private static final String KEY_EDITTEXTSTRING = "editTextString";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.locations);

        /*Restore the values when activity is recreated*/
        String strEditText = "";
        if (savedInstanceState != null) {
            strEditText = savedInstanceState.getString(KEY_EDITTEXTSTRING, "<default string>");
            textView.setText(strEditText);
        }

    }
    /**
     * onSaveInstanceState function saves the values for activity recreation
     * @param savedInstanceState values save din the bundle
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // get the string data from the EditText widget
        String strEditText = textView.getText().toString();

        // store the string data in the savedInstanceState bundle
        savedInstanceState.putString(KEY_EDITTEXTSTRING, strEditText);
    }
    /*
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient);

        if( mLastLocation == null ) {
            return;
        }

        mLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
        mLongitude.setText( String.valueOf(mLastLocation.getLongitude()));
        String dat = DateFormat.getTimeInstance().format(new Date()).toString();
        mLastUpdate.setText( dat );

        createLocationRequest();
        if( mLocationRequest != null){
            startLocationUpdates();
        }
    }
    */

    @Override
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * startButtonClick - handler for START button
     * @param view
     */
    public void startButtonClick(View view) {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    /**
     * stopButtonClick - handler for STOP button
     * @param view
     */
    public void stopButtonClick(View view) {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    /**
     * locsButtonClick - handler for LOCS button
     * @param view
     */
    public void locsButtonClick(View view) {
        String str = "";
        Intent intent = new Intent (this, CoordinateToFile.class);

        for (String s : LocationService.locations) {
            str += (s + "\n");
        }
        /*Call another service to store the location values to a file*/
        intent.putExtra(CoordinateToFile.EXTRA_MESSAGE,
                str);
        startService(intent);
    }
    /**
     * FileReadButtonClick - handler for FILE button
     * @param view
     */
    public void FileReadButtonClick(View view) {
        try {

            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;
            /*Read from the file*/
            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            /*File close*/
            InputRead.close();
            /*Display on textview*/
            textView.setText(s);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ClearButtonClick - handler for CLEAR button
     * @param view
     */
    public void ClearButtonClick(View view)
    {
        /*Clear the text*/
        textView.setText(null);
    }
}
