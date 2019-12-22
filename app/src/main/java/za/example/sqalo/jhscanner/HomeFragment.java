package za.example.sqalo.jhscanner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owner on 2015-10-04.
 */
public class HomeFragment extends Fragment {
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
    TextView txttotalnoticeweekly,txtbundleno;
    ImageView edtlogoImage, edtprofileImage,moretap;
    Bundle bundle;
    Intent mServiceIntent;
    private SensorService mSensorService;
    public HomeFragment() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_homee, container, false);

        b1 = (ImageView) rootView.findViewById(R.id.b1);
        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        txttotalnoticeonceoff = (TextView) rootView.findViewById(R.id.txtnotice1);
        txttotalnoticeweekly = (TextView) rootView.findViewById(R.id.txtnotice2);
        txtbundleno = (TextView) rootView.findViewById(R.id.txtnotice3);
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
        ClearOldGrorequest();
        bundle = this.getArguments();
        try {
            String query2 = "select id from [Bundle]";
            PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs2 = ps2.executeQuery();
            int total=0;
            while (rs2.next()) {
                total=total+1;
            }
            txtbundleno.setText(String.valueOf(total));

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

                Fragment frag = new HomeFragmentOrderPoint();
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
        if (this.activity.hometimes == 0) {
            new UpdateRemainDays().execute(new String[]{""});
            this.activity.hometimes++;
            Log.d("ReminderService In", "First time from Home");
        } else {
            this.activity.hometimes++;
            Log.d("ReminderService In", "From Some where");
        }
        LoadProfilePicture();
        AboutToExpiryNotification();
        ClearOldGrorequest();
       NoticeWeekly();
        NoticeOnceoff();
        PushNotify();
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
public void LoadProfilePicture(){
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
}
    public void ClearOldGrorequest() {
        try {
            if (!this.activity.edthidenuserid.getText().toString().equals("")) {
                StringBuilder sb = new StringBuilder();
                sb.append("select id,Datediff(D,convert(varchar,getDate(),112),Cast([purchasedate] as date)) as daysleft from [UserProductShoppingList] where [userid]=");
                sb.append(this.activity.edthidenuserid.getText().toString());
                ResultSet rsa = this.con.prepareStatement(sb.toString()).executeQuery();
                while (rsa.next()) {
                    if (Integer.parseInt(rsa.getString("daysleft").toString()) >= 2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("delete from [UserProductShoppingList]  where [id]='");
                        sb2.append(rsa.getString("id").toString());
                        sb2.append("'");
                        this.con.prepareStatement(sb2.toString()).executeUpdate();
                    }
                }
                Log.d("Remindersaervice In", "CLEARGROSSReq");
            }
        } catch (Exception e) {
        }
    }
    public void NoticeWeekly() {
        try {
            if (!this.activity.edthidenuserid.getText().toString().equals("")) {
                StringBuilder sb = new StringBuilder();
                sb.append("select [id] from [UserProduct] where [userid]='");
                sb.append(this.activity.edthidenuserid.getText().toString());
                sb.append("' and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=0 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7");
                ResultSet rs = this.con.prepareStatement(sb.toString()).executeQuery();
                while (rs.next()) {
                    this.total++;
                }
                this.txttotalnoticeweekly.setText(String.valueOf(this.total));
            } else if (!this.activity.edthidenuserrole.getText().toString().equals("")) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("select userid from [AppUserAssistance] where [id]='");
                sb2.append(this.activity.edthidenuserrole.getText().toString());
                sb2.append("'");
                ResultSet rs11 = this.con.prepareStatement(sb2.toString()).executeQuery();
                rs11.next();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("select [id] from [UserProduct] where [userid]=");
                sb3.append(rs11.getString("userid").toString());
                sb3.append(" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=0 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7");
                ResultSet rs2 = this.con.prepareStatement(sb3.toString()).executeQuery();
                while (rs2.next()) {
                    this.total++;
                }
                this.txttotalnoticeweekly.setText(String.valueOf(this.total));
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
    }

    public void NoticeOnceoff() {
        try {
            if (this.activity.edthidenuserid.getText().toString().equals("")) {
                StringBuilder sb = new StringBuilder();
                sb.append("select userid from [AppUserAssistance] where [id]='");
                sb.append(this.activity.edthidenuserrole.getText().toString());
                sb.append("'");
                ResultSet rs11 = this.con.prepareStatement(sb.toString()).executeQuery();
                rs11.next();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("select id from [UserProductNotification] where [userid]='");
                sb2.append(rs11.getString("userid").toString());
                sb2.append("' and  [isread]='No' order by [categoryid] asc");
                ResultSet rs = this.con.prepareStatement(sb2.toString()).executeQuery();
                while (rs.next()) {
                    this.totalnoticeonceoff++;
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("select id from [UserProductNotification] where [userid]='");
                sb3.append(this.activity.edthidenuserid.getText().toString());
                sb3.append("' and  [isread]='No' order by [categoryid] asc");
                ResultSet rs2 = this.con.prepareStatement(sb3.toString()).executeQuery();
                while (rs2.next()) {
                    this.totalnoticeonceoff++;
                }
            }
            this.txttotalnoticeonceoff.setText(String.valueOf(this.totalnoticeonceoff));
            Log.i("HOME", "@ HOME after everything");
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
    }

    public void AboutToExpiryNotification() {
        Notification.Builder builder;
        NotificationManager mgr;
        Intent notificationIntent;
        PreparedStatement psu;
        String queryu;
        HomeFragment homeFragment = this;
        try {
            if (!homeFragment.activity.edthidenuserid.getText().toString().equals("")) {
                Log.d("ReminderService In", "IN@@@@@@@@@@ CORE USER About to expire items");
                NotificationManager mgr2 = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                Intent notificationIntent2 = new Intent(homeFragment.rootView.getContext(), SplashFragment.class);
                Notification.Builder builder2 = new Notification.Builder(homeFragment.rootView.getContext());
                StringBuilder sb = new StringBuilder();
                sb.append("select firstname,email from [AppUser] where [id]='");
                sb.append(homeFragment.activity.edthidenuserid.getText().toString());
                sb.append("'");
                String queryu2 = sb.toString();
                PreparedStatement psu2 = homeFragment.con.prepareStatement(queryu2);
                ResultSet rsu = psu2.executeQuery();
                rsu.next();
               // String to = "sibusison@sqaloitsolutions.co.za";
                String to = rsu.getString("email");
                String from = "info@goingdots.com";
                String firstname = rsu.getString("firstname");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("select id,name,quantity,preferbestbeforedays,Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date)) as daysleft from [UserProduct] where [userid]='");
                sb2.append(homeFragment.activity.edthidenuserid.getText().toString());
                sb2.append("' and [isread]='No' and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=0");
                ResultSet rs = homeFragment.con.prepareStatement(sb2.toString()).executeQuery();
                while (rs.next()) {
                    String notificationbody = "";
                    if (Integer.parseInt(rs.getString("daysleft").toString()) == Integer.parseInt(rs.getString("preferbestbeforedays").toString())) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(notificationbody);
                        String str = notificationbody;
                        sb3.append(rs.getString("quantity").toString());
                        sb3.append(" ");
                        sb3.append(rs.getString("name").toString());
                        sb3.append("\n");
                        String notificationbody2 = sb3.toString();
                        StringBuilder sb4 = new StringBuilder();
                        queryu = queryu2;
                        sb4.append("update [UserProduct] set [isread]='Yes' where [id]='");
                        sb4.append(rs.getString("id").toString());
                        sb4.append("'");
                        String commands = sb4.toString();
                        homeFragment.con.prepareStatement(commands).executeUpdate();
                        String str2 = commands;
                        psu = psu2;
                        PendingIntent pendingIntent = PendingIntent.getActivity(homeFragment.rootView.getContext(), 0, notificationIntent2, PendingIntent.FLAG_ONE_SHOT);
                        Log.d("ReminderService In", "Notified");
                        builder2.setAutoCancel(false);
                        builder2.setTicker("Item about to expire");
                        builder2.setSmallIcon(R.drawable.logos);
                        builder2.setContentIntent(pendingIntent);
                        builder2.setContentTitle("Item about to expire");
                        builder2.setContentText(notificationbody2);
                        Notification myNotication = builder2.setStyle(new Notification.BigTextStyle().bigText(notificationbody2)).build();
                        myNotication.defaults |= 2;
                        mgr2.notify(0, myNotication);
                        String subject = "Item about to expire";

                        try {
                            StringBuilder sb5 = new StringBuilder();
                            mgr = mgr2;
                            try {
                                sb5.append("Dear ");
                                sb5.append(firstname);
                                sb5.append("\nYou have item/s about to expire:\n");
                                sb5.append(notificationbody2);
                                sb5.append("\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                                String message = sb5.toString();
                                String[] toArr = {to};
                                notificationIntent = notificationIntent2;
                                try {

                                    try {
                                        Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                                        m.setTo(toArr);
                                        m.setFrom(from);
                                        m.setSubject(subject);
                                        m.setBody(message);
                                        m.send();
                                    } catch (Exception e) {

                                    }
                                } catch (Exception e2) {

                                    builder = builder2;
                                    String str3 = "ReminderService In";
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("Time");
                                    sb6.append("");
                                    Log.d(str3, sb6.toString());
                                    queryu2 = queryu;
                                    psu2 = psu;
                                    notificationIntent2 = notificationIntent;
                                    mgr2 = mgr;
                                    builder2 = builder;
                                    homeFragment = this;
                                }
                            } catch (Exception e3) {

                                notificationIntent = notificationIntent2;
                                builder = builder2;
                                String str32 = "ReminderService In";
                                StringBuilder sb62 = new StringBuilder();
                                sb62.append("Time");
                                sb62.append("");
                                Log.d(str32, sb62.toString());
                                queryu2 = queryu;
                                psu2 = psu;
                                notificationIntent2 = notificationIntent;
                                mgr2 = mgr;
                                builder2 = builder;
                                homeFragment = this;
                            }
                        } catch (Exception e4) {


                        }
                    }

                }

            }
        } catch (Exception ex) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Timerrrrr&&&&&&&&&&&&&");
            sb7.append(ex.getMessage());
            Log.d("ReminderService In", sb7.toString());
        }
    }

    private class UpdateRemainDays extends AsyncTask<String, Integer, Void> {
        Boolean isSuccess=false;
        private ProgressDialog progressDialog;
        private String z="";

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(HomeFragment.this.rootView.getContext(), "Please wait", "Updating Products Expiry Days Left...", true, false);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            this.progressDialog.dismiss();
            if (!this.isSuccess.booleanValue()) {
                Toast.makeText(HomeFragment.this.rootView.getContext(), this.z, Toast.LENGTH_LONG).show();
            }
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... params) {
            String daysleftmessage;
            String daysleftmessage2;
            try {
                if (HomeFragment.this.con == null) {
                    this.z = "Check Your Internet Access!";
                } else if (!activity.edthidenuserid.getText().toString().equals("")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("select id ,Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date)) as daysleft from [UserProduct] where [userid]='");
                    sb.append(HomeFragment.this.activity.edthidenuserid.getText().toString());
                    sb.append("' and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=0 order by id asc");
                    ResultSet rs1 = HomeFragment.this.con.prepareStatement(sb.toString()).executeQuery();
                    while (rs1.next()) {
                        String str = "";
                        int daysleft = Integer.parseInt(rs1.getString("daysleft").toString());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(rs1.getString("id").toString());
                        sb2.append("###Remain Days");
                        sb2.append(daysleft);
                        Log.d("ReminderService In", sb2.toString());
                        if (daysleft <= 0) {
                            daysleftmessage2 = "0 Days";
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(daysleft);
                            sb3.append(" Days");
                            daysleftmessage2 = sb3.toString();
                        }
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("update [UserProduct] set [bestbeforeStatus]='");
                        sb4.append(daysleftmessage2);
                        sb4.append("' where [id]='");
                        sb4.append(rs1.getString("id").toString());
                        sb4.append("'");
                        HomeFragment.this.con.prepareStatement(sb4.toString()).executeUpdate();
                    }
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("select userid from [AppUserAssistance] where [id]='");
                    sb5.append(HomeFragment.this.activity.edthidenuserrole.getText().toString());
                    sb5.append("'");
                    ResultSet rs11 = HomeFragment.this.con.prepareStatement(sb5.toString()).executeQuery();
                    rs11.next();
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("select [id],Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date)) as daysleft from [UserProduct] where [userid]=");
                    sb6.append(rs11.getString("userid").toString());
                    ResultSet rs12 = HomeFragment.this.con.prepareStatement(sb6.toString()).executeQuery();
                    while (rs12.next()) {
                        String str2 = "";
                        int daysleft2 = Integer.parseInt(rs12.getString("daysleft").toString());
                        if (daysleft2 <= 0) {
                            daysleftmessage = "0 Days";
                        } else {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append(daysleft2);
                            sb7.append(" Days");
                            daysleftmessage = sb7.toString();
                        }
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("update [UserProduct] set [bestbeforeStatus]='");
                        sb8.append(daysleftmessage);
                        sb8.append("' where [id]='");
                        sb8.append(rs12.getString("id").toString());
                        sb8.append("'");
                        HomeFragment.this.con.prepareStatement(sb8.toString()).executeUpdate();
                    }
                }
                this.isSuccess = Boolean.valueOf(true);
            } catch (Exception e) {
                this.isSuccess = Boolean.valueOf(false);
                this.z = "Check your network connection!!!";
            }
            return null;
        }
    }

    public void PushNotify() {
        try {
            NotificationManager mgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(this.rootView.getContext(), SplashFragment.class);
            StringBuilder sb = new StringBuilder();
            sb.append("select * from [BundleOrder] where [userid]='");
            sb.append( activity.edthidenuserid.getText().toString());
            sb.append("' and [isread]='No'");
            ResultSet rs1 = this.con.prepareStatement(sb.toString()).executeQuery();
            String notificationbody1 = "";
            while (rs1.next()) {
                String status2 = rs1.getString("orderstatus").toString();
                if (status2.equals("Prep")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Order is being prepared\n");
                    sb2.append("E.T.D "+rs1.getString("prepdate").toString());
                    notificationbody1 = sb2.toString();
                } else if (status2.equals("Transit")) {
                    notificationbody1 = "Order is on the way";
                } else if (status2.equals("Delivered")) {
                    notificationbody1 = "Order is right at your doorstep";
                } else if (status2.equals("Accepted")) {
                    notificationbody1 = "Thank you for ordering with us\nWe hope you enjoyed the delivery";
                } else if (status2.equals("Rejected")) {
                    notificationbody1 = "Order is Rejected";
                }
                if (!notificationbody1.equals("")) {
                    Log.d("ReminderService In", notificationbody1);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(this.rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                    Notification.Builder builder = new Notification.Builder(this.rootView.getContext());
                    builder.setAutoCancel(false);
                    builder.setTicker("Order Status");
                    builder.setSmallIcon(R.drawable.logos);
                    builder.setContentIntent(pendingIntent1);
                    builder.setContentTitle(getResources().getString(R.string.notify_new_task_title));
                    builder.setContentText(notificationbody1);
                    Notification myNotication1 = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody1)).build();
                    myNotication1.defaults |= 2;
                    mgr.notify(0, myNotication1);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("update [BundleOrder] set [isread]='Yes' where [id]='");
                    sb3.append(rs1.getString("id").toString());
                    sb3.append("'");
                    this.con.prepareStatement(sb3.toString()).executeUpdate();
                }
            }
        } catch (Exception ex) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(ex.getMessage());
            sb4.append("######");
            Log.d("ReminderService In", sb4.toString());
        }
    }



}
