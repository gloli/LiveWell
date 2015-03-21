package com.example.archy.livewell;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Archy on 3/21/15.
 */
public class AppSQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

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
    private static final String COLUMN_WEATHER_DATA = "weather_type";
    private String[] weather_columns = new String[] {COLUMN_WEATHER_ID, COLUMN_WEATHER_TIMESTAMP, COLUMN_WEATHER_DATA};

    // create tables
    private static final String DB_CREATE_SENSOR = "create table "
            + TABLE_SENSOR + "("
            + COLUMN_SENSOR_ID + "integer primary key autoincrement, "
            + COLUMN_SENSOR_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_SENSOR_CLASSIFIER + " double" + ");";

    private static final String DB_CREATE_MOOD = "create table "
            + TABLE_MOOD + "("
            + COLUMN_MOOD_ID + "integer primary key autoincrement, "
            + COLUMN_MOOD_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_MOOD_DATA + " double" + ");";

    private static final String DB_CREATE_WEATHER = "create table "
            + TABLE_WEATHER + "("
            + COLUMN_WEATHER_ID + "integer primary key autoincrement, "
            + COLUMN_WEATHER_TIMESTAMP + "timestamp not null default current_timestamp, "
            + COLUMN_WEATHER_DATA + " double" + ");";



    public AppSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    public long insertWeatherData(double temperature) {
        db = getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_WEATHER_DATA, temperature);
        long insert = db.insert(AppSQLiteHelper.TABLE_WEATHER, null, vals);
        db.close();
        return insert;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
