package com.example.archy.livewell;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class AccelService extends Service implements SensorEventListener {

    AppSQLiteHelper db = new AppSQLiteHelper(this);

    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ArrayList<Double> featVect = new ArrayList<>();
    private int blockIndex = 0;
    private double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];

    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler()); // Target we publish for clients

    static AccelService service;

    public class TrackingBinder extends Binder {
        AccelService getService() {
            service = AccelService.this;
            return AccelService.this;
        }
    }
    private final IBinder binder = new TrackingBinder();

    /* instantiate in other classes */
    public static AccelService get() {
        return service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SERVICE", "ONBIND");

        return mMessenger.getBinder();
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
                    //Log.d("SENSOR", "adding to db!");
                    db.insertSensorData(classification);
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
}
