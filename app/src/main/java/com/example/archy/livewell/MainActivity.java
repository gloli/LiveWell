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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    public static class Splash extends Activity {

        /** Duration of wait **/
        private final int SPLASH_DISPLAY_LENGTH = 2000;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.splash_layout);
            findViewById(android.R.id.content).postDelayed (new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        }
    }

    View viewToAnimate;

    private ImageButton goodButton;
    private ImageButton badButton;
    private TextView motorText;

    private AppSQLiteHelper dbHelper;
    private Double MOOD_BAD = 0.0;
    private Double MOOD_GOOD = 1.0;

    AccelService mService;
    private Messenger messenger;
    private final String TAG = "MainActivity";
    public static final String INTENT_MSG = "Messenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AppSQLiteHelper(this);
        goodButton = (ImageButton) findViewById(R.id.good_button);
        badButton =  (ImageButton) findViewById(R.id.bad_button);

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertMoodData(MOOD_GOOD);
                Toast.makeText(MainActivity.this, "Mood recorded!", Toast.LENGTH_SHORT).show();

            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertMoodData(MOOD_BAD);

                Toast.makeText(MainActivity.this, "Mood recorded!", Toast.LENGTH_SHORT).show();
                startDialog();
            }
        });

    }

    public void startDialog() {
        Intent i = new Intent(this, DisplayMessage.class);
        i.putExtra("dialog", true);
        startActivity(i);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessage.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        intent.putExtra("dialog", false);
        startActivity(intent);
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


    public static class StateDialogFrag extends DialogFragment {


        View v;

        private static final String INACTIVITY_MESSAGE = "Take a walk, have fun, and get active!";
        private static final String COLD_WEATHER = "It's chilly out; grab a jacket and some hot chocolate!";
        private static final String INSPIRATION = "Every moment is a fresh beginning; never, never, never give up!";
        private static final String COURAGE = "No one can make you feel inferior without your consent!";

        public StateDialogFrag() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            Random rand = new Random();
            int num = rand.nextInt(4);
            String info = "";
            switch(num) {
                case 0:  info += INACTIVITY_MESSAGE;
                    break;
                case 1: info+= COLD_WEATHER;
                    break;
                case 2: info += INSPIRATION;
                    break;
                default: info += COURAGE;
                    break;
            }

            // display stats
            builder.setTitle("LiveWell, Be Happy!").setMessage(info);
            info = "";
            return builder.create();
        }
    }
}
