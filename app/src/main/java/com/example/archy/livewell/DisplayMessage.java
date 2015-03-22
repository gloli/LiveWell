package com.example.archy.livewell;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessage extends ActionBarActivity {

    AppSQLiteHelper dbHelper = new AppSQLiteHelper(this);
    TextView activity;
    Messenger messenger;
    AccelService mService;
    public static final String INTENT_MSG = "Messenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        messenger = new Messenger(new IncomingMessageHandler());

        TextView mood = (TextView) findViewById(R.id.mood_textview);
        activity = (TextView) findViewById(R.id.sensor_textview);
        TextView weather = (TextView) findViewById(R.id.weather_textview);

        if (dbHelper.getLastMood() != -100.0) {
            switch ((int) dbHelper.getLastMood()) {
                case (0):
                    mood.setText("Sad");
                    break;
                case (1):
                    mood.setText("Happy");
                    break;
                default:
                    mood.setText("Happy");
                    break;
            }
        }
        else
            mood.setText("Nothing Yet!");


        // begin AccelService
        Intent accelIntent = new Intent(this, AccelService.class);
        messenger = new Messenger(new IncomingMessageHandler());
        accelIntent.putExtra(INTENT_MSG, messenger);

        startService(accelIntent);
        Log.d("DisplayMessage", "service started");
        bindService(accelIntent, mConnection, Context.BIND_AUTO_CREATE);

        // intent
        Intent i = getIntent();
        if (i.getBooleanExtra("dialog", false)) {
            FragmentManager fm = getFragmentManager();
            MainActivity.StateDialogFrag dialog = new MainActivity.StateDialogFrag();
            dialog.setRetainInstance(false);
            dialog.show(fm, "advice_popup");

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Log.d(TAG, "onServiceConnected()");
            mService = ((AccelService.AccelBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // unexpected crash
            //Log.d(TAG, "CRASH");
            mService = null;
            messenger = null;
            unbindService(mConnection);
        }
    };

    // handle messages from service
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //Log.d(TAG, "handling message " + msg.what);
            // handle different types of messages from TrackingService
            switch (msg.what) {
                case (AccelService.MSG_MOTOR_UPDATE):
                    Bundle bundle = msg.getData();
                    double classification = bundle.getDouble("classification");

                    switch ((int) classification) {
                        case (0):
                            activity.setText("Standing");
                            break;
                        case (1):
                            activity.setText("Walking");
                            break;
                        case (2):
                            activity.setText("Running");
                            break;
                    }
                    break;
                case (AccelService.MSG_WELLNESS_UPDATE):
                    FragmentManager fm = getFragmentManager();
                    MainActivity.StateDialogFrag dialog = new MainActivity.StateDialogFrag();
                    dialog.setRetainInstance(true);
                    dialog.show(fm, "advice_popup");
            }
        }
    }

}
