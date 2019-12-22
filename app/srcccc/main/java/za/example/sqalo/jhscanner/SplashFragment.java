package za.example.sqalo.jhscanner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_HALF_HOUR;

/**
 * Created by Owner on 2015-12-01.
 */
public class SplashFragment extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    ImageView img;

    Intent mServiceIntent;
    private SensorService mSensorService;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.splash);
        img = (ImageView) findViewById (R.id.imgLogo);
       animateImageView(img);

        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
       /* if (!isMyServiceRunning(mSensorService.getClass())) {
           startService(mServiceIntent);
        }*/
//============
       // setupAlarm(1);
        //setupAlarm();
//==============
        // slidingimage.startAnimation(rotateimage);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main fragment_mapy
                Intent i = new Intent(SplashFragment.this, MainActivity.class);
                startActivity(i);

                // close this fragment_mapy
                //finish();
            }
        }, SPLASH_TIME_OUT);

    }
    public void animateImageView(final ImageView v) {
        //final int orange = getResources().getColor(android.R.color.holo_orange_dark);
        final int orange = getResources().getColor(android.R.color.white);
        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(orange, mul);

                v.setColorFilter(alphaOrange, PorterDuff.Mode.ADD);


                if (mul == 0.0) {
                    v.setColorFilter(null);
                }
            }
        });

        colorAnim.setDuration(2000);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();

    }
    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    private void setupAlarm(int minutes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiverAutoStart.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( SplashFragment.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Getting current time and add the seconds in it
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, minutes);

        //alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Finish the currently running fragment_mapy
        //  MainActivity.this.finish();
    }
  /*  private void setupAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver1.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( SplashFragment.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Getting current time and add the seconds in it
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);

        if (now.after(alarmStartTime)) {
            Log.d("Hey","Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("Hey","Alarm set");
        // Finish the currently running fragment_mapy
        //  MainActivity.this.finish();
    }*/
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

}
