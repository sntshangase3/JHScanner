package za.example.sqalo.jhscanner;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiverAutoStart extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar now =Calendar.getInstance();
        //if ((now.get(Calendar.HOUR_OF_DAY)==4 && now.get(Calendar.MINUTE)==7 && now.get(Calendar.SECOND)==0)){
            Log.d(TAG, "AlarmReceiverAutoStart, app started...");
            // Start the MainActivity
            Intent i = new Intent(context, SplashFragment.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
       // }


    }

}
