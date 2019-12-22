package za.example.sqalo.jhscanner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import java.util.Calendar;

import javax.mail.Quota;

public class SplashFragment extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    Context ctx;
    ImageView img;
    private SensorService mSensorService;
    Intent mServiceIntent;

    public Context getCtx() {
        return this.ctx;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.splash);
        this.img = (ImageView) findViewById(R.id.imgLogo);
        animateImageView(this.img);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashFragment.this.startActivity(new Intent(SplashFragment.this, MainActivity.class));
            }
        }, (long) SPLASH_TIME_OUT);
    }

    public void animateImageView(final ImageView v) {
        final int orange = getResources().getColor(R.color.colorWhite);
        ValueAnimator colorAnim = ObjectAnimator.ofFloat(new float[]{0.0f, 1.0f});
        colorAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = ((Float) animation.getAnimatedValue()).floatValue();
                v.setColorFilter(SplashFragment.this.adjustAlpha(orange, mul), Mode.ADD);
                if (((double) mul) == 0.0d) {
                    v.setColorFilter(null);
                }
            }
        });
        colorAnim.setDuration(1000);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();
    }

    public int adjustAlpha(int color, float factor) {
        return Color.argb(Math.round(((float) Color.alpha(color)) * factor), Color.red(color), Color.green(color), Color.blue(color));
    }


    public void onDestroy() {
        stopService(this.mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
    }
}
