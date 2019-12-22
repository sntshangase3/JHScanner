
package za.example.sqalo.jhscanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ReminderEditActivity extends Activity {

    //
    // Dialog Constants
    //
    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;

    //
    // Date Format
    //
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    EditText mTitleText;
    EditText mBodyText;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;
    private Long mRowId;

    private Calendar mCalendar;
    AddProductFrag addProductFrag =   AddProductFrag.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.reminder_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateButton = (Button) findViewById(R.id.reminder_date);
        mTimeButton = (Button) findViewById(R.id.reminder_time);
        mConfirmButton = (Button) findViewById(R.id.confirm);


        mTitleText.setText(addProductFrag.edtname.getText().toString());
        mBodyText.setText(addProductFrag.edtdescription.getText().toString());

        mCalendar = Calendar.getInstance();

        try {
            String dt = addProductFrag.edtbestbeforedate.getText().toString();  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            mCalendar.setTime(sdf.parse(dt));
            mCalendar.set(Calendar.HOUR_OF_DAY, 8);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            if(!addProductFrag.edtpreferbestbeforedays.getText().toString().equals("0")) {
                mCalendar.add(Calendar.DATE, -Integer.parseInt(addProductFrag.edtpreferbestbeforedays.getText().toString()));  // number of days to add
            }
            dt = sdf.format(mCalendar.getTime());  // dt is now the new date

        } catch (ParseException e) {
            //	e.printStackTrace();
        }

        // Set the time button text based upon the value from the database
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mTimeButton.setText(timeForButton);

        // Set the date button text based upon the value from the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);


        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = mTitleText.getText().toString();
                String body = mBodyText.getText().toString();

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
                // if (mRowId == null) {

                //  long id = mDbHelper.createReminder(title, body, reminderDateTime);
                // if (id > 0) {
                //   mRowId = id;
                // }
                // } else {
                //     mDbHelper.updateReminder(mRowId, title, body, reminderDateTime);
                // }

                // new ReminderManager(this).setReminder(mRowId, mCalendar);
                Toast.makeText(ReminderEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
              //  scheduleAlarm();
                finish();
            }

        });



    }
    // Setup a recurring alarm every half hour
   /* public void scheduleAlarm() {
        // Construct an intent that will execute the SensorRestarterBroadcastReceiver
        Intent intent = new Intent(getApplicationContext(), SensorRestarterBroadcastReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, SensorRestarterBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,5000, pIntent);
    }*/


}

