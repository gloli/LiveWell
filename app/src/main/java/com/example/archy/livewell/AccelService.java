package com.example.archy.livewell;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.location.LocationListener;

import com.example.archy.livewell.weather.AppEntry;
import com.example.archy.livewell.weather.JSONWeatherParser;
import com.example.archy.livewell.weather.WeatherHttpClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

public class AccelService extends Service implements SensorEventListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public AppSQLiteHelper db = new AppSQLiteHelper(this);
    private Messenger mClient;
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_MOTOR_UPDATE = 3;

    // sensor & classifier vars
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ArrayList<Double> featVect = new ArrayList<Double>();
    private int blockIndex = 0;
    private double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];

    // location vars (for weather)
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler()); // Target we publish for clients
    static AccelService service;
    NotificationManager notifManager;


/* =================== INSTANTIATE =================== */
    @Override
    public void onCreate() {
        super.onCreate();
        setupNotification();
        startGoogleClient();
    }

    public class AccelBinder extends Binder {
        AccelService getService() {
            service = AccelService.this;
            return AccelService.this;
        }
    }
    private final IBinder binder = new AccelBinder();

    public static AccelService get() {
        return service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("AccelService", "ONBIND");
        //return mMessenger.getBinder();
        return binder;
    }

    @Override
    // set up sensor and messenger
    public int onStartCommand(Intent intent, int flags, int startId) {
        mClient = intent.getParcelableExtra(MainActivity.INTENT_MSG);
        startSensorUpdate();

        return START_NOT_STICKY;
    }

    // display in status bar
    public void setupNotification() {
        Log.d("AccelService", "setupNotification()");
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(MapDisplayActivity.class)));
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        //PendingIntent. FLAG_UPDATE_CURRENT);
        // set up notification UI
        notifManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(this).setContentTitle("LiveWell")
                .setContentText("Recording your mood now")//.setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
        notif.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        //notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notifManager.notify(0, notif);
    }

/* =================== MESSAGE HANDLING =================== */

    private void sendMessageToActivity(int messageType, Double type) {
        Log.d("AccelService", "sending msg to activity...");
        if (mClient != null) {
            try {
                Message msg;
                Bundle bundle = new Bundle();
                switch (messageType) {
                    case MSG_MOTOR_UPDATE:
                        msg = Message.obtain(null, MSG_MOTOR_UPDATE);
                        bundle.putDouble("classification", type);
                        msg.setData(bundle);
                        break;
                    default:
                        msg = Message.obtain(null, MSG_MOTOR_UPDATE);
                        break;
                }
                mClient.send(msg);
            // dead client
            } catch (RemoteException e) {
                mClient = null;
            }
        }
    }

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClient = msg.replyTo;
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClient = null;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

/* ================== SENSOR METHODS ================== */

    // set up sensor manager
    public void startSensorUpdate() {
        Log.d("SENSOR", "setting up in service!");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double m = Math.sqrt(event.values[0] * event.values[0]
                    + event.values[1] * event.values[1] + event.values[2]
                    * event.values[2]);
            accBlock[blockIndex] = m;
            blockIndex++;
            if (blockIndex == ACCELEROMETER_BLOCK_CAPACITY) {
                createFeatureVector();

                // classify feature vector and input to db
                try {
                    double classification = WekaClassifier.classify(featVect.toArray());
                    Log.d("SENSOR", "adding to db!");
                    db.insertSensorData(classification);

                    // send to Activity
                    sendMessageToActivity(MSG_MOTOR_UPDATE, classification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                blockIndex = 0;
                featVect.clear();
            }
        }
    }

    private void createFeatureVector() {
        FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
        double[] re = accBlock;
        double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];

        double max = Double.MIN_VALUE;

        for (double val : accBlock) {
            if (max < val) {
                max = val;
            }
        }

        fft.fft(re, im);

        for (int i = 0; i < re.length; i++) {
            // Compute each coefficient
            double mag = Math.sqrt(re[i] * re[i] + im[i] * im[i]);
            // Adding the computed FFT coefficient to the
            // featVect
            featVect.add(Double.valueOf(mag));
            // Clear the field
            im[i] = .0;
        }
        // Finally, append max after frequency components
        featVect.add(Double.valueOf(max));
        accBlock = new double[64];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // ==================== Google Map API Functions ====================

    protected synchronized void startGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("UPDATING LOCATIONS", "ONCONNECTED");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(600000); // Update location every 10 mins
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        /*
        try {

            // get weather data
            String data = ((new WeatherHttpClient()).getWeatherData(location));
            AppEntry entry = JSONWeatherParser.getWeather(data, location);

            // add to db
            //db.insertWeatherData(entry);
        } catch (JSONException e) {
            e.printStackTrace();
        } */

            // TODO: helper function
            db.getLastMood();                   // returns 0.0 if bad, 1.0 if good
            db.getListMode(db.getMotorData());  // returns most common activity - 0.0 = standing, 1.0 = walk, 2.0 = run


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

}
