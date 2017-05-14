/*Mamatha Kelu
* Assignment 4*/
package lwtech.itad230.location1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CoordinateToFile extends IntentService {

    public static final String EXTRA_MESSAGE = "message";

    public CoordinateToFile() {
        super("CoordinateToFile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String filename = "myfile";
        String string = "Hello world!";
        //FileOutputStream outputStream;
        //File file = new File(getApplicationContext().getFilesDir(), filename);

        synchronized(this) {
            try {
                /*Write position to mytextfile.txt file */
                String messageText = intent.getStringExtra(EXTRA_MESSAGE);
                FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write(messageText);
                outputWriter.close();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        String text = intent.getStringExtra(EXTRA_MESSAGE);
        /*Displaying coordinates for debug purpose*/
        showText(text);
    }

    private void showText(final String text) {
        Log.v("Co-ordinates", " " + text);
    }

    /* onStartCommand() gets called every time the intent service is started.
     * It runs on the main thread, and runs before onHandleIntent(). If we
     * create a handler in onStartCommand(), we can use it in onHandleIntent
     * to post code to the main thread.
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
