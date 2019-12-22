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
import android.widget.ImageButton;
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


    Button btn_order_now;
    MainActivity activity = MainActivity.instance;
    Connection con;
    String un, pass, db, ip;

    int total=0;
    TextView txttotalnoticeonceoff,txttotalnoticeweekly,txtorderno,txtbundleno,terms;

    ImageView edtlogoImage, edtprofileImage,moretap;
    Bundle bundle;
    Bundle bundles = new Bundle();
    Intent mServiceIntent;
    private SensorService mSensorService;
    ImageButton myoders,create_bundle;
    int userid;
    public HomeFragmentOrderPoint() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_homee_orderpoint, container, false);
        myoders = (ImageButton) rootView.findViewById(R.id.myoders);
        create_bundle = (ImageButton) rootView.findViewById(R.id.create_bundle);

        txttotalnoticeonceoff = (TextView) rootView.findViewById(R.id.txtnotice1);
        terms = (TextView) rootView.findViewById(R.id.terms);
        txttotalnoticeweekly = (TextView) rootView.findViewById(R.id.txtnotice2);
        edtlogoImage = (ImageView) rootView.findViewById(R.id.imgLogo);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);

        btn_order_now = (Button) rootView.findViewById(R.id.btn_order_now);
        txtorderno = (TextView) rootView.findViewById(R.id.txtorderno);
        txtbundleno = (TextView) rootView.findViewById(R.id.txtbundleno);

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
            if (activity.edthidenuserid.getText().toString().equals("")) {

                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();

                userid = Integer.parseInt(rs11.getString("userid").toString());

                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if(userid!=3){
                    create_bundle.setVisibility(View.GONE);
                }
                if (rs.getRow() != 0) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    //Set rounded corner
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    edtprofileImage.setImageDrawable(roundedBitmapDrawable);
                }

            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if(userid!=3){
                    create_bundle.setVisibility(View.GONE);
                }
                if (rs.getRow() != 0) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    //Set rounded corner
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    edtprofileImage.setImageDrawable(roundedBitmapDrawable);
                }
            }


        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }

        try {
            String query2 = "select * from [Bundle]";
           PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs2 = ps2.executeQuery();
            int total=0;
            while (rs2.next()) {
                total=total+1;
            }
            txtbundleno.setText(String.valueOf(total));

            String query1 = "select id from [BundleOrder] where [userid]="+activity.edthidenuserid.getText().toString();
            PreparedStatement ps1 = con.prepareStatement(query1);
            ResultSet rs1 = ps1.executeQuery();
            total=0;
            while (rs1.next()) {
                total=total+1;
            }
            txtorderno.setText(String.valueOf(total));


        } catch (Exception ex) {

        }

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new TermsDeliveryFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });

        btn_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new OrderPointBundleListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });

        create_bundle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundles.putString("extras", "");
                Fragment frag = new OrderPointBundleCreateFrag();
                frag.setArguments(bundles);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        myoders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    OrderPointBundleListAllOrdersFrag fragment = new OrderPointBundleListAllOrdersFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });


        return rootView;
    }





}
