package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleFinalFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;



    private Button accept,reject;
    //---------con--------
    Connection con;
    String un, pass, db, ip,deliverydaydate,selecteddeliverycost, day,hour,minute,fulladdress_search,price,selectedpaymentmethod,latlon;
    String firstname = "";


    int userid,qty,orderid,selectedquantity;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    Calendar c;
    TextView txtthankname,txtreference,txtorderdelivery,txtpaid,txtbundlename,orderpreptxt,ordertransittxt,hometxt,txttwarning;
    String mobileno = "",status="";
    ImageView  callno, map,orderprep,ordertransit,home,orderaccept,ordereject,vieworder,warning;
    Bundle bundle;
    Bundle bundles = new Bundle();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.orderpointfinal, container, false);


        txtthankname = (TextView) rootView. findViewById(R.id.txtthankname);
        txtreference = (TextView) rootView. findViewById(R.id.txtreference);
        txtorderdelivery = (TextView) rootView.findViewById(R.id.txtorderdelivery);
        txtpaid = (TextView) rootView. findViewById(R.id.txtpaid);
        txtbundlename = (TextView) rootView. findViewById(R.id.txtbundlename);
        orderpreptxt = (TextView) rootView.findViewById(R.id.orderpreptxt);
        ordertransittxt = (TextView) rootView. findViewById(R.id.ordertransittxt);
        hometxt = (TextView) rootView. findViewById(R.id.hometxt);
        txttwarning = (TextView) rootView.findViewById(R.id.txttwarning);


        callno = (ImageView) rootView.findViewById(R.id.callno);
        map = (ImageView) rootView.findViewById(R.id.map);

        orderprep = (ImageView) rootView.findViewById(R.id.orderprep);
        ordertransit = (ImageView) rootView.findViewById(R.id.ordertransit);
        home = (ImageView) rootView.findViewById(R.id.home);

        orderaccept = (ImageView) rootView.findViewById(R.id.orderaccept);
        ordereject = (ImageView) rootView.findViewById(R.id.ordereject);
        accept = (Button)rootView. findViewById(R.id.btn_accept);
        reject = (Button)rootView. findViewById(R.id.btn_decline);

        warning = (ImageView) rootView.findViewById(R.id.warning);
        vieworder = (ImageView) rootView.findViewById(R.id.vieworder);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        bundle = this.getArguments();

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }
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
                firstname = rs.getString("firstname");
mobileno=rs.getString("contact");
            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]='" + userid+"'";
                Log.d("ReminderService In", query);
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
                mobileno=rs.getString("contact");
                txtthankname.setText("THANK YOU!"+firstname);
                Log.d("ReminderService In", firstname+" "+mobileno);
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }
        orderid = Integer.parseInt(bundle.getString("id"));
        FillData(orderid);

        orderaccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                try {

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Accepted' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Accepted!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    Fragment frag = new HomeFragmentOrderPoint();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }


        });
        ordereject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Rejected' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Rejected!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    Fragment frag = new HomeFragmentOrderPoint();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                    try {

                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        String commands = "update [BundleOrder] set [orderstatus]='Accepted' where [id]='" + orderid + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();

                        Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Accepted!!!",Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();

                        Fragment frag = new HomeFragmentOrderPoint();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();

                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }


        }


    });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Rejected' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Rejected!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    Fragment frag = new HomeFragmentOrderPoint();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });

        callno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:" + mobileno));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGoogleMapsInstalled()) {
                    try {
                       if (!fulladdress_search.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else {

                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
        orderprep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Prep',[prepdate]='"+todaydate+"',[transitdate]='-',[deliverdate]='-' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order in Preparation!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(orderid));
                    bundles.putString("name", txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        ordertransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Transit',[transitdate]='"+todaydate+"',[deliverdate]='-' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order in Transit!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(orderid));
                    bundles.putString("name", txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [BundleOrder] set [orderstatus]='Delivered',[deliverdate]='"+todaydate+"' where [id]='" + orderid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Delivered!!!",Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(orderid));
                    bundles.putString("name", txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                   final CharSequence[] items = {"No Reported Possible Delay",
                    "Possible Delay Due To Bad Weather",
                    "Possible Delay Due To Vehicle Breakdown",
                    "Possible Delay Due To Traffic Congession",
                    "Possible Delay Due To Delivery Address",
                    "No Respond/Receiver At Delivery Address",
                    "Possible Delay Due To Ingredients Shortage",
                    "Sorry, We Don't Deliver In Your Area"};
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        TextView title = new TextView(rootView.getContext());
                        title.setPadding(10, 10, 10, 10);
                        title.setText("Select Warning");
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(getResources().getColor(R.color.colorPrimary));
                        builder.setCustomTitle(title);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                try{
                                    selectedpaymentmethod=items[which].toString();
                                    ConnectionClass cn = new ConnectionClass();
                                    con = cn.connectionclass(un, pass, db, ip);
                                    String commands = "update [BundleOrder] set [warning]='"+selectedpaymentmethod+"' where [id]='" + orderid + "'";
                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                    preStmt.executeUpdate();

                                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Order Warning Updated!!!",Toast.LENGTH_LONG);
                                    View toastView = ToastMessage.getView();
                                    toastView.setBackgroundResource(R.drawable.toast_bground);
                                    ToastMessage.show();
                            } catch (Exception ex) {
                                Log.d("ReminderService In", ex.getMessage().toString());
                            }
                            }

                        });
                        builder.show();


                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    String query = "select * from [Bundle] where [name]='" + txtbundlename.getText().toString() + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    Log.d("ReminderService In",query);
                    while (rs.next()) {
                        bundles.putString("id", rs.getString("id").toString());
                        bundles.putString("name", txtbundlename.getText().toString());
                        bundles.putString("price", rs.getString("price").toString());
                        bundles.putString("quantity", rs.getString("quantity").toString());


                    }
                        OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        return rootView;

}
    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private View currentSelectedView;
    public void FillData( int id) {
        //==============Fill Data=FillScheduleData

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                Log.d("ReminderService In", "$$$$$$$$$");
                String query = "SELECT name,b.image,bo.id,deliverydaydate,orderstatus,ordercost,deliverycost,ref,warning,prepdate,transitdate,deliverdate,paymentmethod,location,bo.quantity " +
                        "  FROM [Bundle] b " +
                        "  inner join [BundleOrder] bo on bo.bundleid=b.id where bo.id = '" + id + "' and bo.userid='" + userid +"'";
                Log.d("ReminderService In", query);
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                 while (rs.next()) {
                     int payable=Integer.parseInt(rs.getString("ordercost"))+Integer.parseInt(rs.getString("deliverycost"));
                     status=rs.getString("orderstatus");

                     txtreference.setText("Your Order #:"+rs.getString("ref"));
                     txtorderdelivery.setText("Order:R"+rs.getString("ordercost")+" + R"+rs.getString("deliverycost")+" Delivery");
                     txtpaid.setText("Paid/Payable:R"+String.valueOf(payable));
                     txtbundlename.setText(rs.getString("name"));

                     orderpreptxt.setText(rs.getString("prepdate"));
                     ordertransittxt.setText(rs.getString("transitdate"));
                     hometxt.setText(rs.getString("deliverdate"));

                     txttwarning.setText(rs.getString("warning"));
                     fulladdress_search = rs.getString("location").toString().replace(" ", "+");
                     if(userid!=3){
                         orderprep.setClickable(false);
                         ordertransit.setClickable(false);
                         home.setClickable(false);

                         warning.setClickable(false);

                         orderaccept.setClickable(false);
                         ordereject.setClickable(false);
                         accept.setClickable(false);
                         reject.setClickable(false);
                     }

                }
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }

    }
}


