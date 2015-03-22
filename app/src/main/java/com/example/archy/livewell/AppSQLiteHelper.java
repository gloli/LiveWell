package com.example.archy.livewell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.archy.livewell.weather.AppEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Archy on 3/21/15.
 */
public class AppSQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static AppSQLiteHelper helper;

    private static final String DATABASE_NAME = "livewell.db";
    private static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    // accelerometer table
    private static final String TABLE_SENSOR = "sensor_data";
    private static final String COLUMN_SENSOR_ID = "sensor_id";
    private static final String COLUMN_SENSOR_TIMESTAMP = "sensor_timestamp";
    private static final String COLUMN_SENSOR_CLASSIFIER = "sensor_feat_vect";
    private String[] sensor_columns = new String[] {COLUMN_SENSOR_ID, COLUMN_SENSOR_TIMESTAMP, COLUMN_SENSOR_CLASSIFIER};

    // user mood table
    private static final String TABLE_MOOD = "mood_data";
    private static final String COLUMN_MOOD_ID = "mood_id";
    private static final String COLUMN_MOOD_TIMESTAMP = "mood_timestamp";
    private static final String COLUMN_MOOD_DATA = "mood_input";
    private String[] mood_columns = new String[] {COLUMN_MOOD_ID, COLUMN_MOOD_TIMESTAMP, COLUMN_MOOD_DATA};

    // weather table
    private static final String TABLE_WEATHER = "weather_data";
    private static final String COLUMN_WEATHER_ID = "weather_id";
    private static final String COLUMN_WEATHER_TIMESTAMP = "weather_timestamp";
    private static final String COLUMN_WEATHER_TEMP = "weather_temp";
    private static final String COLUMN_WEATHER_CLOUDS = "weather_clouds";
    private String[] weather_columns = new String[] {COLUMN_WEATHER_ID, COLUMN_WEATHER_TIMESTAMP, COLUMN_WEATHER_TEMP, COLUMN_WEATHER_CLOUDS};

    // create tables
    private static final String DB_CREATE_SENSOR = "create table "
            + TABLE_SENSOR + "("
            + COLUMN_SENSOR_ID + " integer primary key autoincrement, "
            + COLUMN_SENSOR_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_SENSOR_CLASSIFIER + " double" + ");";

    private static final String DB_CREATE_MOOD = "create table "
            + TABLE_MOOD + "("
            + COLUMN_MOOD_ID + " integer primary key autoincrement, "
            + COLUMN_MOOD_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_MOOD_DATA + " double" + ");";

    private static final String DB_CREATE_WEATHER = "create table "
            + TABLE_WEATHER + "("
            + COLUMN_WEATHER_ID + " integer primary key autoincrement, "
            + COLUMN_WEATHER_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_WEATHER_TEMP + " float, "
            + COLUMN_WEATHER_CLOUDS + " int" + ");";



    public AppSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AppSQLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new AppSQLiteHelper(context.getApplicationContext());
        }
        return helper;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_SENSOR);
        db.execSQL(DB_CREATE_MOOD);
        db.execSQL(DB_CREATE_WEATHER);
    }


/* ===================== insert into tables ===================== */

    public long insertSensorData(double classification) {
        db = getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_SENSOR_CLASSIFIER, classification);
        long insert = db.insert(AppSQLiteHelper.TABLE_SENSOR, null, vals);
        db.close();
        return insert;
    }

    public long insertMoodData(double mood) {
        db = getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_MOOD_DATA, mood);
        long insert = db.insert(AppSQLiteHelper.TABLE_MOOD, null, vals);
        db.close();
        return insert;
    }

    // input temp double
    public long insertWeatherData(AppEntry entry) {
        db = getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_WEATHER_TEMP, entry.temp);
        vals.put(COLUMN_WEATHER_CLOUDS, entry.clouds);
        long insert = db.insert(AppSQLiteHelper.TABLE_WEATHER, null, vals);
        db.close();
        return insert;
    }

/* ===================== fetch from tables ===================== */

    // get last hr of sensor data
    public ArrayList<Double> getMotorData() {
        ArrayList<Double> classes = new ArrayList<Double>();
        db = getReadableDatabase();
        Calendar c = getHourBefore();
        Cursor cursor = db.query(TABLE_SENSOR, sensor_columns,
                COLUMN_SENSOR_TIMESTAMP + " >= '" + DATE_FORMAT.format(c.getTime()) + "'",
                        null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                double data = cursor.getDouble(2);
                classes.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return classes;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Calendar getHourBefore() {
        Calendar c = Calendar.getInstance();
        Calendar UTCTime = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        UTCTime.setTimeInMillis(c.getTimeInMillis());
        UTCTime.add(Calendar.HOUR_OF_DAY, -1);
        return UTCTime;
    }
}
