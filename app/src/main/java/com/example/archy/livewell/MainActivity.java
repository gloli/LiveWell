package com.example.archy.livewell;

import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private Button goodButton;
    private Button badButton;
    private TextView motorText;

    private AppSQLiteHelper dbHelper;
    private Double MOOD_BAD = 0.0;
    private Double MOOD_GOOD = 1.0;

    AccelService mService;
    private Messenger messenger;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AppSQLiteHelper(this);
        motorText = (TextView) findViewById(R.id.motor_text);
        goodButton = (Button) findViewById(R.id.good_button);
        badButton =  (Button) findViewById(R.id.bad_button);

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertMoodData(MOOD_GOOD);
            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertMoodData(MOOD_BAD);
            }
        });

        // begin AccelService
        Intent accelIntent = new Intent(this, AccelService.class);
        messenger = new Messenger(new IncomingMessageHandler());
        accelIntent.putExtra("Messenger", messenger);

        startService(accelIntent);
        Log.d(TAG, "service started");
        bindService(accelIntent, mConnection, Context.BIND_AUTO_CREATE);

    }



/* =================== SERVICE FUNCTIONS =================== */

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected()");
            mService = ((AccelService.AccelBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // unexpected crash
            Log.d(TAG, "CRASH");
            mService = null;
            messenger = null;
            unbindService(mConnection);
        }
    };

    // handle messages from service
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handling message " + msg.what);
            // handle different types of messages from TrackingService
            Bundle bundle = msg.getData();
            double classification = bundle.getDouble("classification");

            switch ((int) classification) {
                case (0):
                    motorText.setText("Standing");
                    break;
                case (1):
                    motorText.setText("Walking");
                    break;
                case (2):
                    motorText.setText("Running");
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
