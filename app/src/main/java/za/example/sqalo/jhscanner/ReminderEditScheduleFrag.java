
package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;


public class ReminderEditScheduleFrag extends Fragment {
    View rootView;
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

    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;


    private Calendar mCalendar;


    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;


    //---------con--------
    Connection con;
    String un, pass, db, ip;
    Spinner spinnerassistant, spinnerrequesttype;
    SpinnerAdapter adapter = null;
    ArrayAdapter adapterassistant, adapterrequesttype;
    MainActivity activity = MainActivity.instance;
    String assistantid, comanagerdaypasscode, assistancename, assistancedaypasscode;
    Bundle bundle;
    int assistanceuserid;
    String productID="";
    FragmentManager fragmentManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reminder_edit_schedule, container, false);




        mDateButton = (Button)rootView. findViewById(R.id.reminder_date);
        mTimeButton = (Button)rootView. findViewById(R.id.reminder_time);
        mConfirmButton = (Button)rootView. findViewById(R.id.confirm);

        spinnerassistant = (Spinner)rootView. findViewById(R.id.spinnerassistant);
        spinnerrequesttype = (Spinner)rootView. findViewById(R.id.spinnerrequesttype);


        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 8);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();


        FillAppUserAssistance();
        FillRequestType();

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
                String title = "";
                String body = "";

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);


                    if ((spinnerassistant.getSelectedItem().toString().trim().equals("Select Assistant Name Here")) || (spinnerrequesttype.getSelectedItem().toString().trim().equals("Select Request Type Here"))) {
                        if ((spinnerassistant.getSelectedItem().toString().trim().equals("Select Assistant Name Here"))) {
                            spinnerassistant.setBackground(errorbg);
                        } else {
                            spinnerassistant.setBackground(bg);
                        }

                        if ((spinnerrequesttype.getSelectedItem().toString().trim().equals("Select Request Type Here"))) {
                            spinnerrequesttype.setBackground(errorbg);
                        } else {
                            spinnerrequesttype.setBackground(bg);
                        }
                    } else {
                        //selected Correctly
                        //Insert into  notifications
                    try {
                        if (bundle != null) {
                            if (!bundle.getStringArray("outputStrArr").equals("")) {
                                ConnectionClass cn = new ConnectionClass();
                                con = cn.connectionclass(un, pass, db, ip);
                                String[] outputStrArr = bundle.getStringArray("outputStrArr");
                                String[] outputStrArrqty = bundle.getStringArray("outputStrArrqty");
                                for (int i = 0; i < outputStrArr.length; i++) {
                                    //================
                                    try {


                                        if (con == null) {

                                            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
                                        } else {

                                           /* String querya = "select * from [AppUserAssistance]  where [userid]=" + fragment_mapy.edthidenuserid.getText().toString();
                                            PreparedStatement psa = con.prepareStatement(querya);
                                            ResultSet rsa = psa.executeQuery();
                                            while (rsa.next()) {
                                                if (rsa.getString("assistancename").equals(spinnerassistant.getSelectedItem().toString()))
                                                    assistanceuserid = Integer.parseInt(rsa.getString("id"));
                                            }*/


                                            Date today = new Date();
                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                            String todaydate = date_format.format(today);

                                            String query = "select * from [UserProduct] where [id]='" + outputStrArr[i].toString() + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            rs.next();

                                            //==000000000
                                            int diff;
                                            double total;
                                            double totaldonation;

                                            if (!rs.getString("quantityperbulk").toString().equals("0")) {

                                                diff = Integer.parseInt(rs.getString("quantity").toString()) - Integer.parseInt(outputStrArrqty[i].toString());
                                                total = diff * (Double.valueOf(rs.getString("price").toString()) / Integer.valueOf(rs.getString("quantityperbulk").toString()));
                                                totaldonation = Integer.parseInt(outputStrArrqty[i].toString()) * (Double.valueOf(rs.getString("price").toString()) / Integer.valueOf(rs.getString("quantityperbulk").toString()));
                                            } else {
                                                diff = Integer.parseInt(rs.getString("quantity").toString()) - Integer.parseInt(outputStrArrqty[i].toString());
                                                total = diff * Double.parseDouble(rs.getString("price").toString());
                                                totaldonation = Integer.parseInt(outputStrArrqty[i].toString()) * Double.parseDouble(rs.getString("price").toString());
                                            }
                                            //==0000000


                                            String commands = "insert into [UserProductNotification] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[noticedate],[issent],[isread],[userid],[categoryid],[retailerid],[assistancename],[requesttypeid])" +
                                                    "values ('" + rs.getString("bestbefore").toString() + "','" + rs.getString("name").toString() + "','" + rs.getString("description").toString() + "','" +
                                                    rs.getString("bestbeforeStatus").toString() + "','" + rs.getString("size").toString() + "','" + Double.parseDouble(rs.getString("price").toString()) + "','" +
                                                    rs.getString("storage").toString() + "','" + Integer.parseInt(rs.getString("preferbestbeforedays").toString()) + "','" + Integer.parseInt(outputStrArrqty[i].toString()) + "','" + Integer.parseInt(rs.getString("quantityperbulk").toString()) + "','" + Integer.parseInt(rs.getString("reorderpoint").toString()) + "','" + totaldonation +
                                                    "','" + rs.getString("isscanned").toString() + "','" + todaydate + "','" + rs.getString("image").toString() + "','" + mDateButton.getText().toString() + "','No','No','" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "','" + Integer.parseInt(rs.getString("categoryid").toString()) + "','" + Integer.parseInt(rs.getString("retailerid").toString()) + "','" + spinnerassistant.getSelectedItem().toString() + "','" + spinnerrequesttype.getSelectedItemPosition() + "')";
                                            // encodedImage which is the Base64 String
                                            PreparedStatement preStmt = con.prepareStatement(commands);
                                            preStmt.executeUpdate();
//------------------
                                            //Update [UserProduct] qty and total
                                            //#########No longer in needed
                                           /* String commands1 = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + outputStrArr[i].toString() + "'";
                                            PreparedStatement preStmt1 = con.prepareStatement(commands1);
                                            preStmt1.executeUpdate();*/
                                            //Update [AppUserAssistance] isdaypasscode no
                                              String commands1 = "update [AppUserAssistance] set [isdaypasscodesent]='No' where [assistancename]='" + spinnerassistant.getSelectedItem().toString()+ "'";
                                            PreparedStatement preStmt1 = con.prepareStatement(commands1);
                                            preStmt1.executeUpdate();

                                            productID=outputStrArr[i].toString();
                                            //Check if need notifications as R.O.P
                                            if (diff <= Integer.valueOf(rs.getString("reorderpoint").toString()) && !rs.getString("reorderpoint").toString().trim().equals("0")) {
                                                //trigger notification
                                                //**********
                                                try {
                                                    String notificationbody = "You have " + String.valueOf(diff) + " " + rs.getString("name").toString() + " left";

                                                    //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                                    NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                                    Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                                    PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builderd.setContentIntent(contentIntent);
                                                    builderd.setAutoCancel(false);
                                                    // Add as notification
                                                    builderd.build();

                                                    NotificationManager manager = (NotificationManager) rootView.getContext().getSystemService(NOTIFICATION_SERVICE);

                                                    Notification myNotication;

                                                    myNotication = builderd.getNotification();
                                                    myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                                    myNotication.defaults |= Notification.DEFAULT_SOUND;
                                                    myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                                    manager.notify(0, myNotication);
                                                } catch (Exception ex) {
                                                    Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                                                }
                                                //**********
                                            }


                                            //--------

                                            Toast.makeText(rootView.getContext(), "Request set Successfully", Toast.LENGTH_LONG).show();


                                            Fragment frag =   new HomePremiumFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.mainFrame, frag).commit();

                                        }


                                    } catch (Exception ex) {
                                     //   Toast.makeText(ReminderEditScheduleFrag.this, ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                                    }

                                    //============
                                }

                            }

                        }
                    } catch (Exception ex) {
                       // Toast.makeText(ReminderEditScheduleFrag.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    //=========
                    try {
                        Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                        //String to = "jabun@ngobeniholdings.co.za";
                        // String to = "sntshangase3@gmail.com";
                        // m.setFrom("Info@ngobeniholdings.co.za");
                        // String to = "SibusisoN@sqaloitsolutions.co.za";
                        String to = "SibusisoN@sqaloitsolutions.co.za";
                        String from = "info@goingdots.com";
                        String subject = "Co-User Notification "+productID;
                        String message = "Dear Sibusiso\nCo-User created notification" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                        String[] toArr = {to};
                        m.setTo(toArr);
                        m.setFrom(from);
                        m.setSubject(subject);
                        m.setBody(message);

                        m.send();


                    } catch (Exception e) {

                       // Toast.makeText(ReminderEditScheduleFrag.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //==========

                    //  scheduleAlarm();
                   // finish();
                }


            }

        });

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mSeconds = c.get(Calendar.SECOND);
                mMSeconds = c.get(Calendar.MILLISECOND);


                datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        //textValue.setText();


                        Date bestbefore = null;
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);
                        try {
                            Date date = new Date(date_format.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth).getTime());
                            Date date2 = new Date(date_format.parse(todaydate).getTime());
                            bestbefore = date;
                            today = date2;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        // daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);


                        mDateButton.setText(date_format.format(bestbefore));


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        return rootView;
    }

    //======Load All AppUserAssistance

    public void FillAppUserAssistance() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String query = "select * from [AppUserAssistance]  where [userid]=" + activity.edthidenuserid.getText().toString();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> assistant = new ArrayList<String>();
                assistant.add("Select Assistant Name Here");
                while (rs.next()) {
                    assistant.add(rs.getString("assistancename"));

                }
                adapterassistant = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, assistant);
                adapterassistant.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerassistant.setAdapter(adapterassistant);

                // Toast.makeText(ReminderEditScheduleFrag.this, fragment_mapy.edtuseremail.getText().toString(),Toast.LENGTH_LONG).show();
                // spinnerassistant.setSelection(adapterassistant.getPosition("gg"));
                // z = "Loaded Successfully";

                con.close();

            }
        } catch (Exception ex) {
            // Toast.makeText(ReminderEditScheduleFrag.this,"Excp"+ ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            // z = "Invalid data input!";
        }


    }

    public void FillRequestType() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {
                String query = "select * from [RequestType]";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> requesttype = new ArrayList<String>();
                requesttype.add("Select Request Type Here");
                while (rs.next()) {

                    requesttype.add(rs.getString("requestType"));
                }
                adapterrequesttype = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, requesttype);
                adapterrequesttype.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerrequesttype.setAdapter(adapterrequesttype);
                //spinnerassistant.setSelection(adapter1.getPosition(rs.getString("birthyear")));
                // z = "Loaded Successfully";

                con.close();

            }
        } catch (Exception ex) {
            // Toast.makeText(ReminderEditScheduleFrag.this,"Excp"+ ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            // z = "Invalid data input!";
        }


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

