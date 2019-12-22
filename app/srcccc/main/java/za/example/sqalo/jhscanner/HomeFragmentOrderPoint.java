package za.example.sqalo.jhscanner;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Owner on 2015-10-04.
 */
public class HomeFragmentOrderPoint extends Fragment {
    View rootView;

    ImageView b1, b2,b3;
    Button btn_premium;
    MainActivity activity = MainActivity.instance;
    Connection con;
    String un, pass, db, ip;
    int totalnoticeonceoff = 0;
    int totalnoticeweekly = 0;
    int total=0;
    TextView txttotalnoticeonceoff;
    TextView txttotalnoticeweekly;
    ImageView edtlogoImage, edtprofileImage,moretap;
    Bundle bundle;
    Intent mServiceIntent;
    private SensorService mSensorService;
    public HomeFragmentOrderPoint() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_homee_orderpoint, container, false);

        b1 = (ImageView) rootView.findViewById(R.id.b1);
        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        txttotalnoticeonceoff = (TextView) rootView.findViewById(R.id.txtnotice1);
        txttotalnoticeweekly = (TextView) rootView.findViewById(R.id.txtnotice2);
        edtlogoImage = (ImageView) rootView.findViewById(R.id.imgLogo);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);
        moretap = (ImageView) rootView.findViewById(R.id.moretap);
        btn_premium = (Button) rootView.findViewById(R.id.btn_premium);


        //fragment_mapy.edthidenuserid.getText().toString();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn=new ConnectionClass();
        con =cn.connectionclass(un, pass, db, ip);
        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }


        bundle = this.getArguments();
        try {
            if (bundle != null) {
                if (!bundle.getString("assist").equals("")) {
                    btn_premium.setVisibility(View.GONE);
                }

            }
        } catch (Exception ex) {

        }

        try {
            if(isGoogleMapsInstalled())
            {
                try {


                    try {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=1+Poole+St,+Brooklyn,+Cape+Town,+7405,+South+Africa");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        // startActivity(mapIntent);
                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Google Maps Not Found...", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex) {
                    Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setMessage("Install Google Maps");
                builder.setCancelable(false);
                builder.setPositiveButton("Install", getGoogleMapsListener());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } catch (Exception ex) {

        }


       /* mSensorService = new SensorService(rootView.getContext());
        mServiceIntent = new Intent(rootView.getContext(), mSensorService.getClass());
        if (isMyServiceRunning(mSensorService.getClass())) {
            rootView.getContext().stopService(mServiceIntent);
            Log.i("EXIT", "Service stopped in Home!");
        }*/
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_premium.getVisibility() == View.GONE) {
//Assistant click to premium hom
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("assist", "assistant");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } else {

                    Fragment frag = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    frag.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new MyListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment frag = new OrderPointBundleListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        btn_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new RegisterPremiumFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });


        try {
          NoticeWeekly_ProductDaysUpdate();
            NoticeOnceoff();
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }

        moretap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Fragment frag = new SearchListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
                return false;
            }
        });
        return rootView;
    }
    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
    public DialogInterface.OnClickListener getGoogleMapsListener()
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }


    public void NoticeWeekly_ProductDaysUpdate() {
        //==============Initialize list=

        try {


            //================= Update [UserProduct] left days if have products
            if (!activity.edthidenuserid.getText().toString().equals("")) {
              /*  String query1 = "select top 5 * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                // ps1.setQueryTimeout(120*1000);
                Log.d("ReminderService In", "Here");
                String dates = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                while (rs1.next()) {
                    String daysleftmessage = "";
                    try {

                        Date bestbefore = new SimpleDateFormat("yyyy-MM-dd").parse(rs1.getString("bestbefore").toString());
                        Date today = new SimpleDateFormat("yyyy-MM-dd").parse(dates);
                        long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        if (daysleft <= 0) {
                            daysleftmessage = "0 Days";
                        } else {
                            daysleftmessage = daysleft + " Days";
                        }

                        String query2 = "update [UserProduct] set [bestbeforeStatus]='" + daysleftmessage + "' where [id]=" + rs1.getString("id").toString();
                        PreparedStatement preparedStatement = con.prepareStatement(query2);
                        preparedStatement.executeUpdate();
                    } catch (ParseException e) {
                        // Toast.makeText(rootView.getContext(), " Ex 1 "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }


                }*/
                //Update notice numbers
                Log.d("ReminderService In", "Here");
                //String query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";
                String query = "select  [id] from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                // ps.setQueryTimeout(120*1000);
                while (rs.next()) {
                    total=total+1;
                }
                txttotalnoticeweekly.setText(String.valueOf(total));
            } else if (!activity.edthidenuserrole.getText().toString().equals("")) {
                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();
/*
                String query1 = "select * from [UserProduct] where [userid]=" + rs11.getString("userid").toString();
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                // ps1.setQueryTimeout(120*1000);
                String dates = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                while (rs1.next()) {
                    String daysleftmessage = "";
                    try {

                        Date bestbefore = new SimpleDateFormat("yyyy-MM-dd").parse(rs1.getString("bestbefore").toString());
                        Date today = new SimpleDateFormat("yyyy-MM-dd").parse(dates);
                        long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        if (daysleft <= 0) {
                            daysleftmessage = "0 Days";
                        } else {
                            daysleftmessage = daysleft + " Days";
                        }

                        String query2 = "update [UserProduct] set [bestbeforeStatus]='" + daysleftmessage + "' where [id]=" + rs1.getString("id").toString();
                        PreparedStatement preparedStatement = con.prepareStatement(query2);
                        preparedStatement.executeUpdate();
                    } catch (ParseException e) {
                        //  Toast.makeText(rootView.getContext(), " Ex 1 " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }*/
                String query = "select [id] from [UserProduct] where [userid]="+rs11.getString("userid").toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    total=total+1;
                }
                txttotalnoticeweekly.setText(String.valueOf(total));
            }


        } catch (Exception ex) {

            // Toast.makeText(rootView.getContext(), " Ex 2 " + ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage());
        }
        try {


            //====Update frofile pic from co-user
            String queryprofile = "select * from [AppUser] where [id]= '" + activity.edthidenuserid.getText().toString() + "'";
            PreparedStatement psp = con.prepareStatement(queryprofile);
            ResultSet rsp = psp.executeQuery();
            rsp.next();
            if (rsp.getRow() != 0) {
                byte[] decodeString = Base64.decode(rsp.getString("image"), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                //Set rounded corner
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                edtprofileImage.setImageDrawable(roundedBitmapDrawable);
            } else {
                //====Update frofile pic from assistance

                String query1 = "select * from [AppUserAssistance] where [id]= '" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();


                if (rs1.getRow() != 0) {

                    byte[] decodeString1 = Base64.decode(rs1.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                    //Set rounded corner
                    RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap1);
                    roundedBitmapDrawable1.setCornerRadius(50.0f);
                    roundedBitmapDrawable1.setAntiAlias(true);
                    edtprofileImage.setImageDrawable(roundedBitmapDrawable1);
                }

            }





        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage()+"Here",Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage());
        }

//===========

    }

    public void NoticeOnceoff() {
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                if (activity.edthidenuserid.getText().toString().equals("")) {
                    //Assistant login
                    //OnceOffNoticeTotal
                    String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                    ResultSet rs11 = ps11.executeQuery();
                    rs11.next();
                    // rs11.getString("userid").toString();

                    String query = "select [id] from [UserProductNotification] where [userid]='" + rs11.getString("userid").toString() + "' and  [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        totalnoticeonceoff = totalnoticeonceoff + 1;
                    }

                }else {
                    //Co-user login
                    //OnceOffNoticeTotal
                    /*String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                    ResultSet rs11 = ps11.executeQuery();
                    rs11.next();*/
                    // rs11.getString("userid").toString();

                    String query = "select [id] from [UserProductNotification] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and  [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        totalnoticeonceoff = totalnoticeonceoff + 1;
                    }

                }
                txttotalnoticeonceoff.setText(String.valueOf(totalnoticeonceoff));
            }

            con.close();


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), " Ex 2 " + ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage());
        }
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) rootView.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }




}
