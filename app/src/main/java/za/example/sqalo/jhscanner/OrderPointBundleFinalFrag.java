package za.example.sqalo.jhscanner;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OrderPointBundleFinalFrag extends Fragment implements OnItemSelectedListener {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private Button accept;
    MainActivity activity = MainActivity.instance;
    ArrayAdapter adapter;
    Bundle bundle;
    Bundle bundles = new Bundle();
    Calendar c;
    ImageView callno;
    Connection con;
    private View currentSelectedView;
    String db;
    String email = "";
    String firstname = "";
    FragmentManager fragmentManager;
    String fulladdress_search;
    ImageView home;
    TextView hometxt;
    String ip;
    int isClicked = 0;
    String location = "";
    ImageView map;
    String minute;
    String mobileno = "";
    ImageView orderaccept;
    ImageView ordereject;
    int orderid;
    ImageView orderprep;
    TextView orderpreptxt;
    ImageView ordertransit;
    TextView ordertransittxt;
    String pass;
    String price;
    int qty;
    String ref = "";
    private Button reject;
    View rootView;
    String selectedpaymentmethod;
    Spinner spinnerdetails;
    String status = "";
    TextView txtback;
    TextView txtbundlename;
    TextView txtorderdelivery;
    TextView txtpaid;
    TextView txtreference;
    TextView txtthankname;
    TextView txttwarning,txtordertype;
    String un;
    int userid;
    ImageView vieworder;
    ImageView warning;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.orderpointfinal, container, false);
        this.txtthankname = (TextView) this.rootView.findViewById(R.id.txtthankname);
        this.txtreference = (TextView) this.rootView.findViewById(R.id.txtreference);
        this.txtorderdelivery = (TextView) this.rootView.findViewById(R.id.txtorderdelivery);
        this.txtpaid = (TextView) this.rootView.findViewById(R.id.txtpaid);
        this.txtbundlename = (TextView) this.rootView.findViewById(R.id.txtbundlename);
        this.orderpreptxt = (TextView) this.rootView.findViewById(R.id.orderpreptxt);
        this.ordertransittxt = (TextView) this.rootView.findViewById(R.id.ordertransittxt);
        this.hometxt = (TextView) this.rootView.findViewById(R.id.hometxt);
        this.txtback = (TextView) this.rootView.findViewById(R.id.txtback);
        this.txtordertype = (TextView) this.rootView.findViewById(R.id.txtordertype);
        this.txttwarning = (TextView) this.rootView.findViewById(R.id.txttwarning);
        this.spinnerdetails = (Spinner) this.rootView.findViewById(R.id.spinnerdetails);
        this.callno = (ImageView) this.rootView.findViewById(R.id.callno);
        this.map = (ImageView) this.rootView.findViewById(R.id.map);
        this.orderprep = (ImageView) this.rootView.findViewById(R.id.orderprep);
        this.ordertransit = (ImageView) this.rootView.findViewById(R.id.ordertransit);
        this.home = (ImageView) this.rootView.findViewById(R.id.home);
        this.orderaccept = (ImageView) this.rootView.findViewById(R.id.orderaccept);
        this.ordereject = (ImageView) this.rootView.findViewById(R.id.ordereject);
        this.accept = (Button) this.rootView.findViewById(R.id.btn_accept);
        this.reject = (Button) this.rootView.findViewById(R.id.btn_decline);
        this.warning = (ImageView) this.rootView.findViewById(R.id.warning);
        this.vieworder = (ImageView) this.rootView.findViewById(R.id.vieworder);

        this.fragmentManager = getFragmentManager();
        this.ip = "winsqls01.cpt.wa.co.za";
        this.db = "JHShopper";
        this.un = "sqaloits";
        this.pass = "422q5mfQzU";
        this.bundle = getArguments();
        this.con = new ConnectionClass().connectionclass(this.un, this.pass, this.db, this.ip);
        if (this.con == null) {
            Toast.makeText(this.rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        try {
            this.userid = this.bundle.getInt("userid");
            StringBuilder sb = new StringBuilder();
            sb.append("select * from [AppUser] where [id]='");
            sb.append(this.userid);
            sb.append("'");
            ResultSet rs = this.con.prepareStatement(sb.toString()).executeQuery();
            rs.next();
            this.firstname = rs.getString("firstname");
            this.location = rs.getString("location");
            this.mobileno = rs.getString("contact");
            this.email=rs.getString("email");
            //this.email = "sibusison@sqaloitsolutions.co.za";
            TextView textView = this.txtthankname;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("THANK YOU!");
            sb2.append(this.firstname);
            textView.setText(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.firstname);
            sb3.append(" ");
            sb3.append(this.mobileno);
            sb3.append(" ");
            sb3.append(this.email);
            Log.d("ReminderService In", sb3.toString());
        } catch (Exception ex) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(ex.getMessage());
            sb4.append("######");
            Log.d("ReminderService In", sb4.toString());
        }
        this.orderid = this.bundle.getInt("id");
        final String ordertype=bundle.getString("ordertype").toUpperCase();
        if(ordertype.equals("MAIN")){
            txtordertype.setText(ordertype+" Order Only");
        }else if(ordertype.equals("OPTIONAL")){
            txtordertype.setText(ordertype+" Order Only");
        }else{
            txtordertype.setText(ordertype+" Order");
        }

        Log.d("ReminderService In", "GGGG"+ this.orderid);
        FillData(this.orderid);
        this.spinnerdetails.setOnItemSelectedListener(this);
        this.orderaccept.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleFinalFrag.this.PushNotify();
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Accepted',[isread]='No' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is Accepted\nThank you for ordering with us,we hope you enjoy the delivery\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order Accepted");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Accepted!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.getFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFragmentOrderPoint()).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.ordereject.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Rejected',[isread]='No' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is Rejected\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order Rejected");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Rejected!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.getFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFragmentOrderPoint()).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.accept.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleFinalFrag.this.PushNotify();
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Accepted' ,[isread]='No' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is Accepted\nThank you for ordering with us,we hope you enjoy the delivery\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order Accepted");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Accepted!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.getFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFragmentOrderPoint()).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.reject.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Rejected',[isread]='No' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is Rejected\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order Rejected");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Rejected!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.getFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFragmentOrderPoint()).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.callno.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (OrderPointBundleFinalFrag.this.isGoogleMapsInstalled()) {
                    try {
                        if (!OrderPointBundleFinalFrag.this.fulladdress_search.equals("")) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("google.navigation:q=");
                            sb.append(OrderPointBundleFinalFrag.this.fulladdress_search);
                            Intent mapIntent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
                            mapIntent.setPackage("com.google.android.apps.maps");
                            OrderPointBundleFinalFrag.this.getActivity().startActivity(mapIntent);
                            return;
                        }
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("google.navigation:q=");
                            sb2.append(OrderPointBundleFinalFrag.this.fulladdress_search);
                            Intent mapIntent2 = new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString()));
                            mapIntent2.setPackage("com.google.android.apps.maps");
                            OrderPointBundleFinalFrag.this.startActivity(mapIntent2);
                        } catch (Exception e) {
                            Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e2) {
                        Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Builder builder = new Builder(OrderPointBundleFinalFrag.this.rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", OrderPointBundleFinalFrag.this.getGoogleMapsListener());
                    builder.create().show();
                }
            }
        });
        this.map.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OrderPointBundleFinalFrag orderPointBundleFinalFrag = OrderPointBundleFinalFrag.this;
                orderPointBundleFinalFrag.isClicked = 1;
                orderPointBundleFinalFrag.spinnerdetails.performClick();
            }
        });
        this.orderprep.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleFinalFrag.this.PushNotify();
                    String todaydate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Prep',[isread]='No',[prepdate]='");
                    sb.append(todaydate);
                    sb.append("',[transitdate]='-',[deliverdate]='-' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is being prepared\n");
                    sb2.append(todaydate);
                    sb2.append("\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order Prepared");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order in Preparation!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.bundles.putInt("id", orderid);
                    OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag.this.bundles.putInt("userid", userid);
                    OrderPointBundleFinalFrag.this.bundles.putString("ordertype", ordertype);

                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.ordertransit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleFinalFrag.this.PushNotify();
                    String todaydate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Transit',[isread]='No',[transitdate]='");
                    sb.append(todaydate);
                    sb.append("',[deliverdate]='-' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is on the way\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order in Transit");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order in Transit!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.bundles.putInt("id", orderid);
                    OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag.this.bundles.putInt("userid", userid);
                    OrderPointBundleFinalFrag.this.bundles.putString("ordertype", ordertype);
                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.home.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleFinalFrag.this.PushNotify();
                    String todaydate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append("update [BundleOrder] set [orderstatus]='Delivered',[isread]='No',[deliverdate]='");
                    sb.append(todaydate);
                    sb.append("' where [id]='");
                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                    sb.append("'");
                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Dear ");
                    sb2.append(OrderPointBundleFinalFrag.this.firstname);
                    sb2.append("\nYour Order Ref:\n");
                    sb2.append(OrderPointBundleFinalFrag.this.ref);
                    sb2.append(" is right at your doorstep\nLogin on your app and check.\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa");
                    String message = sb2.toString();
                    m.setTo(new String[]{OrderPointBundleFinalFrag.this.email});
                    m.setFrom("info@goingdots.com");
                    m.setSubject("Order at the Door");
                    m.setBody(message);
                    m.send();
                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Delivered!!!", Toast.LENGTH_LONG);
                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    OrderPointBundleFinalFrag.this.bundles.putInt("id", orderid);
                    OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                    OrderPointBundleFinalFrag.this.bundles.putInt("userid", userid);
                    OrderPointBundleFinalFrag.this.bundles.putString("ordertype", ordertype);
                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.warning.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    final CharSequence[] items = {"No Reported Possible Delay", "Possible Delay Due To Bad Weather", "Possible Delay Due To Vehicle Breakdown", "Possible Delay Due To Traffic Congession", "Possible Delay Due To Delivery Address", "No Respond/Receiver At Delivery Address", "Possible Delay Due To Ingredients Shortage", "Sorry, We Don't Deliver In Your Area"};
                    try {
                        Builder builder = new Builder(OrderPointBundleFinalFrag.this.rootView.getContext());
                        TextView title = new TextView(OrderPointBundleFinalFrag.this.rootView.getContext());
                        title.setPadding(10, 10, 10, 10);
                        title.setText("Select Warning");
                        title.setGravity(17);
                        title.setTextColor(OrderPointBundleFinalFrag.this.getResources().getColor(R.color.colorPrimary));
                        builder.setCustomTitle(title);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    OrderPointBundleFinalFrag.this.selectedpaymentmethod = items[which].toString();
                                    ConnectionClass cn = new ConnectionClass();
                                    OrderPointBundleFinalFrag.this.con = cn.connectionclass(OrderPointBundleFinalFrag.this.un, OrderPointBundleFinalFrag.this.pass, OrderPointBundleFinalFrag.this.db, OrderPointBundleFinalFrag.this.ip);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("update [BundleOrder] set [warning]='");
                                    sb.append(OrderPointBundleFinalFrag.this.selectedpaymentmethod);
                                    sb.append("' where [id]='");
                                    sb.append(OrderPointBundleFinalFrag.this.orderid);
                                    sb.append("'");
                                    OrderPointBundleFinalFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                                    Toast ToastMessage = Toast.makeText(OrderPointBundleFinalFrag.this.rootView.getContext(), "Order Warning Updated!!!", Toast.LENGTH_LONG);
                                    ToastMessage.getView().setBackgroundResource(R.drawable.toast_bground);
                                    ToastMessage.show();
                                    OrderPointBundleFinalFrag.this.bundles.putInt("id", orderid);
                                    OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                                    OrderPointBundleFinalFrag.this.bundles.putInt("userid", userid);
                                    OrderPointBundleFinalFrag.this.bundles.putString("ordertype", ordertype);

                                    OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }
                            }
                        });
                        builder.show();
                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                } catch (Exception ex2) {
                    Log.d("ReminderService In", ex2.getMessage().toString());
                }
            }
        });
        this.vieworder.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("select * from [Bundle] where [name]='");
                    sb.append(OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                    sb.append("'");
                    String query = sb.toString();
                    ResultSet rs = OrderPointBundleFinalFrag.this.con.prepareStatement(query).executeQuery();
                    Log.d("ReminderService In", query);
                    while (rs.next()) {
                        OrderPointBundleFinalFrag.this.bundles.putString("id", rs.getString("id").toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("price", rs.getString("price").toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("quantity", rs.getString("quantity").toString());
                    }
                    OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        this.txtback.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("select * from [Bundle] where [name]='");
                    sb.append(OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                    sb.append("'");
                    String query = sb.toString();
                    ResultSet rs = OrderPointBundleFinalFrag.this.con.prepareStatement(query).executeQuery();
                    Log.d("ReminderService In", query);
                    while (rs.next()) {
                        OrderPointBundleFinalFrag.this.bundles.putInt("bundleid", Integer.parseInt(rs.getString("id").toString()));
                        OrderPointBundleFinalFrag.this.bundles.putString("name", OrderPointBundleFinalFrag.this.txtbundlename.getText().toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("price", rs.getString("price").toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("qty", rs.getString("quantity").toString());
                        OrderPointBundleFinalFrag.this.bundles.putString("ordertype", txtordertype.getText().toString());
                    }
                    OrderPointBundleConfirmFrag fragment = new OrderPointBundleConfirmFrag();
                    fragment.setArguments(OrderPointBundleFinalFrag.this.bundles);
                    OrderPointBundleFinalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        return this.rootView;
    }

    public void PushNotify() {
        try {
            NotificationManager mgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(this.rootView.getContext(), SplashFragment.class);
            StringBuilder sb = new StringBuilder();
            sb.append("select * from [BundleOrder] where [userid]='");
            sb.append(this.userid);
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
                    Notification myNotication1 = builder.setStyle(new BigTextStyle().bigText(notificationbody1)).build();
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

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo applicationInfo = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 101) {
            return;
        }
        if (grantResults[0] == 0) {
            callPhoneNumber();
        } else {
            Log.e(GifHeaderParser.TAG, "Permission not Granted");
        }
    }

    public void callPhoneNumber() {
        try {
            if (VERSION.SDK_INT <= 22) {
                Intent callIntent = new Intent("android.intent.action.CALL");
                StringBuilder sb = new StringBuilder();
                sb.append("tel:");
                sb.append(this.mobileno);
                callIntent.setData(Uri.parse(sb.toString()));
                startActivity(callIntent);
            } else if (ActivityCompat.checkSelfPermission(this.rootView.getContext(), "android.permission.CALL_PHONE") != 0) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CALL_PHONE"}, 101);
            } else {
                Intent callIntent2 = new Intent("android.intent.action.CALL");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("tel:");
                sb2.append(this.mobileno);
                callIntent2.setData(Uri.parse(sb2.toString()));
                startActivity(callIntent2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OrderPointBundleFinalFrag.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
            }
        };
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (this.spinnerdetails.getSelectedItem().toString().startsWith("M:") && this.isClicked == 1) {
            try {
                callPhoneNumber();
                this.isClicked = 0;
            } catch (SecurityException ex) {
                StringBuilder sb = new StringBuilder();
                sb.append(ex.getMessage());
                sb.append(" ");
                sb.append(this.mobileno);
                Log.d("ReminderService In", sb.toString());
                Toast.makeText(this.rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
            }
        } else if (this.spinnerdetails.getSelectedItem().toString().startsWith("D:") && this.isClicked == 1) {
            Log.d("ReminderService In", "+++++D++++");
            try {
                if (isGoogleMapsInstalled()) {
                    try {
                        if (!this.fulladdress_search.equals("")) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("google.navigation:q=");
                            sb2.append(this.fulladdress_search);
                            Intent mapIntent = new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString()));
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else {
                            try {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("google.navigation:q=");
                                sb3.append(this.fulladdress_search);
                                Intent mapIntent2 = new Intent("android.intent.action.VIEW", Uri.parse(sb3.toString()));
                                mapIntent2.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent2);
                            } catch (Exception e) {
                                Toast.makeText(this.rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e2) {
                        Toast.makeText(this.rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Builder builder = new Builder(this.rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    builder.create().show();
                }
                this.isClicked = 0;
            } catch (SecurityException ex2) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(ex2.getMessage());
                sb4.append(" ");
                sb4.append(this.mobileno);
                Log.d("ReminderService In", sb4.toString());
                Toast.makeText(this.rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
            }
        } else if (this.spinnerdetails.getSelectedItem().toString().startsWith("R:") && this.isClicked == 1) {
            Log.d("ReminderService In", "+++++R++++");
            try {
                if (isGoogleMapsInstalled()) {
                    try {
                        if (!this.fulladdress_search.equals("")) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("google.navigation:q=");
                            sb5.append(this.location);
                            Intent mapIntent3 = new Intent("android.intent.action.VIEW", Uri.parse(sb5.toString()));
                            mapIntent3.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent3);
                        } else {
                            try {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("google.navigation:q=");
                                sb6.append(this.location);
                                Intent mapIntent4 = new Intent("android.intent.action.VIEW", Uri.parse(sb6.toString()));
                                mapIntent4.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent4);
                            } catch (Exception e3) {
                                Toast.makeText(this.rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e4) {
                        Toast.makeText(this.rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Builder builder2 = new Builder(this.rootView.getContext());
                    builder2.setMessage("Install Google Maps");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Install", getGoogleMapsListener());
                    builder2.create().show();
                }
                this.isClicked = 0;
            } catch (SecurityException ex3) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(ex3.getMessage());
                sb7.append(" ");
                sb7.append(this.mobileno);
                Log.d("ReminderService In", sb7.toString());
                Toast.makeText(this.rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void FillData(int id) {
        try {
            this.con = new ConnectionClass().connectionclass(this.un, this.pass, this.db, this.ip);
            if (this.con == null) {
                Toast.makeText(this.rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
                return;
            }
            Log.d("ReminderService In", "$$$$$$$$$");
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT name,b.image,bo.id,deliverydaydate,orderstatus,ordercost,deliverycost,ref,warning,prepdate,transitdate,deliverdate,paymentmethod,location,bo.quantity   FROM [Bundle] b   inner join [BundleOrder] bo on bo.bundleid=b.id where bo.id = '");
            sb.append(id);
            sb.append("' and bo.userid='");
            sb.append(this.userid);
            sb.append("'");
            String query = sb.toString();
            Log.d("ReminderService In", query);
            ResultSet rs = this.con.prepareStatement(query).executeQuery();
            while (rs.next()) {
                int payable = Integer.parseInt(rs.getString("ordercost")) + Integer.parseInt(rs.getString("deliverycost"));
                this.status = rs.getString("orderstatus");
                this.ref = rs.getString("ref");
                TextView textView = this.txtreference;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Your Order #:");
                sb2.append(rs.getString("ref"));
                textView.setText(sb2.toString());
                TextView textView2 = this.txtorderdelivery;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Order:R");
                sb3.append(rs.getString("ordercost"));
                sb3.append(" + R");
                sb3.append(rs.getString("deliverycost"));
                sb3.append(" Delivery");
                textView2.setText(sb3.toString());
                TextView textView3 = this.txtpaid;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Paid/Payable:R");
                sb4.append(String.valueOf(payable));
                textView3.setText(sb4.toString());
                this.txtbundlename.setText(rs.getString("name"));
                this.orderpreptxt.setText(rs.getString("prepdate"));
                this.ordertransittxt.setText(rs.getString("transitdate"));
                this.hometxt.setText(rs.getString("deliverdate"));
                this.txttwarning.setText(rs.getString("warning"));
                this.fulladdress_search = rs.getString("location").toString();
                ArrayList<String> category = new ArrayList<>();
                category.add("Select Option");
                StringBuilder sb5 = new StringBuilder();
                sb5.append("R:");
                sb5.append(this.location);
                category.add(sb5.toString());
                StringBuilder sb6 = new StringBuilder();
                sb6.append("M:");
                sb6.append(this.mobileno);
                category.add(sb6.toString());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("D:");
                sb7.append(this.fulladdress_search);
                category.add(sb7.toString());
                this.adapter = new ArrayAdapter(this.rootView.getContext(), R.layout.spinner_items, category);
                this.adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                this.spinnerdetails.setAdapter(this.adapter);
                if (this.userid != 3) {
                    this.orderprep.setClickable(false);
                    this.ordertransit.setClickable(false);
                    this.home.setClickable(false);
                    this.warning.setClickable(false);
                    this.orderaccept.setClickable(false);
                    this.ordereject.setClickable(false);
                    this.accept.setClickable(false);
                    this.reject.setClickable(false);
                }
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
    }
}
