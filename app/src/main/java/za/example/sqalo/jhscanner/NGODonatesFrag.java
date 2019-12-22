
package za.example.sqalo.jhscanner;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NGODonatesFrag extends Fragment {
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


    private Button mConfirmButton,mDateButton;

    String fulladdress="";
    String lonlat="";


    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c,mCalendar;


    //---------con--------
    Connection con;
    String un, pass, db, ip;

    int ngouserid;

    MainActivity activity = MainActivity.instance;

    Bundle bundle;

    String productID="";
    FragmentManager fragmentManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ngo_donate, container, false);


        mCalendar = Calendar.getInstance();
        mDateButton = (Button)rootView. findViewById(R.id.reminder_date);
        mConfirmButton = (Button)rootView. findViewById(R.id.confirm);

        // Set the date button text based upon the value from the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);




        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();

        try {
            if (bundle != null) {
                if (!bundle.getString("id").toString().equals("")){
                    ngouserid = bundle.getInt("ngouserid");

                }

            }
        } catch (Exception ex) {

        }


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


        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = "";
                String body = "";




                        //selected Correctly
                        //Insert into  NgoProductDonation
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


                                           /* Date today = new Date();
                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                            String todaydate = date_format.format(today);*/
                                            String todaydate = mDateButton.getText().toString();

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

                                            LatLon();

                                            String commands = "insert into [NgoProductDonation] ([bestbefore],[name],[address],[lonlat],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[ngouserid],[userid],[categoryid],[retailerid],[isread],[donationdate],[donationstatus])" +
                                                    "values ('" + rs.getString("bestbefore").toString() + "','" + rs.getString("name").toString() + "','"+fulladdress+"','"+lonlat+"','" + rs.getString("description").toString() + "','" +
                                                    rs.getString("bestbeforeStatus").toString() + "','" + rs.getString("size").toString() + "','" + Double.parseDouble(rs.getString("price").toString()) + "','" +
                                                    rs.getString("storage").toString() + "','" + Integer.parseInt(rs.getString("preferbestbeforedays").toString()) + "','" + Integer.parseInt(outputStrArrqty[i].toString()) + "','" + Integer.parseInt(rs.getString("quantityperbulk").toString()) + "','" + Integer.parseInt(rs.getString("reorderpoint").toString()) + "','" + totaldonation +
                                                    "','" + rs.getString("isscanned").toString() + "','" + rs.getString("purchasedate").toString() + "','" + rs.getString("image").toString() + "','" + ngouserid + "','" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "','" + Integer.parseInt(rs.getString("categoryid").toString()) + "','" + Integer.parseInt(rs.getString("retailerid").toString()) + "','No','"+todaydate+"','Pending')";
                                            // encodedImage which is the Base64 String
                                            PreparedStatement preStmt = con.prepareStatement(commands);
                                            preStmt.executeUpdate();
//------------------
                                            //Update [UserProduct] qty and total
                                            String commands1 = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + outputStrArr[i].toString() + "'";
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

                                            Toast.makeText(rootView.getContext(), "Donation Successfully", Toast.LENGTH_LONG).show();


                                            Fragment frag =   new HomePremiumFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.mainFrame, frag).commit();

                                        }


                                    } catch (Exception ex) {
                                     //   Toast.makeText(ReminderEditScheduleFrag.this, ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                                        Log.d("ReminderService In", ex.getMessage().toString());
                                    }

                                    //============
                                }

                            }

                        }
                    } catch (Exception ex) {
                       // Toast.makeText(ReminderEditScheduleFrag.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }






            }

        });




        return rootView;
    }

    public void LatLon(){
        try{
            // create class object
            GPSTracker mGPS = new GPSTracker(rootView.getContext());
            if (mGPS.canGetLocation) {
                mGPS.getLocation();
                double latitude = mGPS.getLatitude();
                double longitude = mGPS.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                 fulladdress= address;
                lonlat="lat/lng: ("+String.valueOf(latitude)+","+String.valueOf(longitude)+")";
            }



        }catch (Exception e){
            Toast.makeText(rootView.getContext(), "Slow network connection,wait...!!", Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", e.getMessage().toString());
        }

    }


}

