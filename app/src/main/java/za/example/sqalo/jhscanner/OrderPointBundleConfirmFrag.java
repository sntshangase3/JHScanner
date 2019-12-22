package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleConfirmFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    EditText edtquant, edtaddress;
    private Button confirm, selectdeliveryaddrress;
    private Button cancel;
    //---------con--------
    Connection con;
    String un, pass, db, ip, deliverydaydate, selecteddeliverycost, day, hour, minute, fulladdress_search, price, selectedpaymentmethod = "", ordertype;
    String firstname = "";

    Calendar date;
    int userid, qty, bundleid, selectedquantity;
    String m_Text_donate = "";

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    ArrayList<String> scheduleid = new ArrayList<String>();
    ArrayList<String> deleiveryday = new ArrayList<String>();
    ArrayList<String> deliveryhourrange = new ArrayList<String>();
    ArrayList<String> cost = new ArrayList<String>();
    ArrayList<Bitmap> timer = new ArrayList<Bitmap>();
    ListView lstgross;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    Date bestbefore = null;
    Bundle bundles = new Bundle();

    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    Calendar c;
    TextView edtprice, txtback;
    ImageButton btn_select_action;
    // final CharSequence[] items = {"Cash","EFT/Credit Card"};
    final CharSequence[] items = {"Cash"};
    final CharSequence[] itemsbanks = {"Absa", "Capitec", "FNB", "Nedbank", "Standard Bank"};
    ImageView callno, map;
    Spinner spinnerdeliveryaddres;
    ArrayAdapter adapter1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundleconfirm, container, false);

        selectdeliveryaddrress = (Button) rootView.findViewById(R.id.selectdeliveryaddrress);
        edtaddress = (EditText) rootView.findViewById(R.id.edtaddress);
        edtquant = (EditText) rootView.findViewById(R.id.edtquantity);
        edtprice = (TextView) rootView.findViewById(R.id.edtprice);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        btn_select_action = (ImageButton) rootView.findViewById(R.id.btn_select_action);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        callno = (ImageView) rootView.findViewById(R.id.callno);
        map = (ImageView) rootView.findViewById(R.id.map);
        txtback = (TextView) rootView.findViewById(R.id.txtback);
        spinnerdeliveryaddres = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerdeliveryaddres.setOnItemSelectedListener(this);
        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        bundle = this.getArguments();
        FillScheduleData();

        try {
            bundleid = bundle.getInt("bundleid");
            selectedquantity = bundle.getInt("qty");
            price = bundle.getString("price");
            ordertype = bundle.getString("ordertype");
            FillAddressData(bundleid);


            edtprice.setText("How you want to pay? Order R" + price);
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
                //fulladdress_search = rs.getString("location").toString();
                // edtaddress.setText(fulladdress_search);

            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
                fulladdress_search = rs.getString("location").toString();
                //  edtaddress.setText(fulladdress_search);
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);

                if ((edtaddress.getText().toString().trim().equals(""))) {
                    edtaddress.setBackground(errorbg);
                } else if ((edtquant.getText().toString().trim().equals(""))) {
                    edtquant.setBackground(errorbg);
                } else if (selectedpaymentmethod.equals("")) {
                    btn_select_action.setBackground(errorbg);
                    Toast.makeText(rootView.getContext(), "Select Payment Method!!", Toast.LENGTH_LONG).show();
                } else if (deliverydaydate.equals("") || selecteddeliverycost.equals("")) {
                    Toast.makeText(rootView.getContext(), "Select Delivery Day!!", Toast.LENGTH_LONG).show();
                } else {

                    try {
                        m_Text_donate = edtquant.getText().toString();
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        int diff = selectedquantity - Integer.parseInt(m_Text_donate);
                        if (diff <= 0) {
                            Toast ToastMessage = Toast.makeText(rootView.getContext(), "Oops! all items snapped up!!! " + selectedquantity + " Left", Toast.LENGTH_LONG);
                            View toastView = ToastMessage.getView();
                            toastView.setBackgroundResource(R.drawable.toast_bground);
                            ToastMessage.show();

                        } else {
                            Date today = new Date();
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            String todaydate = date_format.format(today);

                            String refno = "JH" + String.valueOf(bundleid) + "G" + today.getHours() + "" + today.getMinutes() + "" + today.getSeconds();
                            String command = "insert into [BundleOrder]([bundleid],[userid],[createddate],[deliverydaydate],[orderstatus],[ordercost],[deliverycost],[paymentmethod],[location],[quantity],[ref],[warning] ,[prepdate]\n" +
                                    "      ,[transitdate]\n" +
                                    "      ,[deliverdate] ,[isread], [ordertype]) " +
                                    "values ('" + bundleid + "','" + userid + "','" + todaydate + "','" + deliverydaydate + "','New','" + Integer.parseInt(price) * Integer.parseInt(m_Text_donate) + "','" + selecteddeliverycost + "','" + selectedpaymentmethod + "','" + edtaddress.getText().toString() + "','" + edtquant.getText().toString() + "','" + refno + "','No Reported Possible Delay','#####','#####','#####','#####','" + ordertype + "')";
                            PreparedStatement preparedStatement = con.prepareStatement(command);
                            preparedStatement.executeUpdate();
if(ordertype.equals("full") ||ordertype.equals("main")){


    String commands = "update [Bundle] set [quantity]='" + diff + "' where [id]='" + bundleid + "'";
    PreparedStatement preStmt = con.prepareStatement(commands);
    preStmt.executeUpdate();

}


                            Toast ToastMessage = Toast.makeText(rootView.getContext(), "Order created Successfully!!!", Toast.LENGTH_LONG);
                            View toastView = ToastMessage.getView();
                            toastView.setBackgroundResource(R.drawable.toast_bground);
                            ToastMessage.show();

                            String query1 = "select  * from [BundleOrder] where [id] in(select max(id) as id from [BundleOrder] ) and [userid]=" + activity.edthidenuserid.getText().toString() +
                                    " and [bundleid]=" + bundleid;
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();

                            while (rs1.next()) {
                                bundles.putInt("id", Integer.parseInt(rs1.getString("id").toString()));
                            }
                            bundles.putInt("userid", userid);
                            bundles.putString("ordertype", ordertype);

                            OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                        }


                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }

            }


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new HomeFragmentOrderPoint();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        selectdeliveryaddrress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerdeliveryaddres.performClick();

            }
        });
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String query = "select * from [Bundle] where [name]='" + bundle.getString("name") + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    Log.d("ReminderService In", query);
                    while (rs.next()) {
                        bundles.putString("id", rs.getString("id").toString());
                        bundles.putString("name", bundle.getString("name"));
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
        btn_select_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    TextView title = new TextView(rootView.getContext());
                    title.setPadding(10, 10, 10, 10);
                    title.setText("Select Payment Option");
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                    builder.setCustomTitle(title);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (items[which].equals("Cash")) {
                                try {
                                    selectedpaymentmethod = "Cash";
                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage());
                                }
                            } else if (items[which].equals("EFT/Credit Card")) {
                                try {
                                    try {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                        TextView title = new TextView(rootView.getContext());
                                        title.setPadding(10, 10, 10, 10);
                                        title.setText("Select Bank");
                                        title.setGravity(Gravity.CENTER);
                                        title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        builder.setCustomTitle(title);
                                        builder.setItems(itemsbanks, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (itemsbanks[which].equals("Absa")) {
                                                    selectedpaymentmethod = "EFT-Absa";
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                    intent.setData(Uri.parse("https://www.absa.africa/absaafrica/"));
                                                    startActivity(intent);
                                                } else if (itemsbanks[which].equals("Capitec")) {
                                                    selectedpaymentmethod = "EFT-Capitec";
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                    intent.setData(Uri.parse("http://www.capitecbank.co.za/"));
                                                    startActivity(intent);
                                                } else if (itemsbanks[which].equals("FNB")) {
                                                    selectedpaymentmethod = "EFT-FNB";
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                    intent.setData(Uri.parse("https://www.fnb.co.za/"));
                                                    startActivity(intent);
                                                } else if (itemsbanks[which].equals("Nedbank")) {
                                                    selectedpaymentmethod = "EFT-Nedbank";
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                    intent.setData(Uri.parse("https://secured.nedbank.co.za/#/login"));
                                                    startActivity(intent);
                                                } else if (itemsbanks[which].equals("Standard Bank")) {
                                                    selectedpaymentmethod = "EFT-Sandard Bank";
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                    intent.setData(Uri.parse("https://onlinebanking.standardbank.co.za/#/login"));
                                                    startActivity(intent);
                                                }
                                            }

                                        });
                                        builder.show();


                                    } catch (Exception ex) {
                                        Log.d("ReminderService In", ex.getMessage().toString());
                                    }

                                } catch (Exception ex) {

                                }
                            }
                        }

                    });
                    builder.show();


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });


        return rootView;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        fulladdress_search = spinnerdeliveryaddres.getSelectedItem().toString();
        edtaddress.setText(fulladdress_search);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void FillAddressData(int bundleid) {
        //==============Fill Data=
        try {

            String query = "select * from [BundleDeliveryAddress] where [bundleid]='" + bundleid + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Delivery Area");
            while (rs.next()) {
                category.add(rs.getString("address"));
            }
            adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerdeliveryaddres.setAdapter(adapter1);

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }

    public void showDateTimePicker() {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);

                        try {

                            deliverydaydate = date_format.format(date.getTime());
                            Log.d("ReminderService In", selecteddeliverycost + "     " + deliverydaydate);
                        } catch (Exception e) {
                            Log.d("ReminderService In", e.getMessage().toString());
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private View currentSelectedView;

    public void FillScheduleData() {
        //==============Fill Data=FillScheduleData

        try {

            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                String query = "select * from [BundleDeliverySchedule]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

                scheduleid.clear();
                deleiveryday.clear();
                deliveryhourrange.clear();
                timer.clear();
                cost.clear();
                while (rs.next()) {
                    scheduleid.add(rs.getString("id"));
                    deleiveryday.add(rs.getString("deliveryday"));
                    deliveryhourrange.add(rs.getString("deliveryhourrange"));
                    cost.add(rs.getString("cost"));
                    Bitmap preptime = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.prep_time);
                    timer.add(preptime);

                }
                OrderPointBundleConfirmAdapter adapter = new OrderPointBundleConfirmAdapter(this.getActivity(), scheduleid, deleiveryday, deliveryhourrange, cost, timer);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {
                            selecteddeliverycost = cost.get(position).toString().substring(1);
                            day = deleiveryday.get(position).toString();
                            hour = deliveryhourrange.get(position).toString();
                            hour = hour.substring(hour.indexOf("-") + 1);
                            hour = hour.substring(0, hour.indexOf("H")).trim();
                            minute = deliveryhourrange.get(position).toString();
                            minute = minute.substring(minute.length() - 2).trim();
                            Log.d("ReminderService In", selecteddeliverycost + " " + day + " " + hour + " " + minute + " " + deliverydaydate);
                            String message = "Specific delivery time?";
                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle("Select");

                            builder.setMessage(message);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        // Get Current Date
                                        showDateTimePicker();


                                    } catch (Exception ex) {
                                        Log.d("ReminderService In", ex.getMessage().toString());
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dialog.cancel();
                                    c = Calendar.getInstance();


                                    switch (day) {
                                        case "Sunday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Monday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Tuesday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Wednesday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Thursday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Friday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Saturday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;

                                    }


                                    c.set(Calendar.SECOND, 0);
                                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                                    deliverydaydate = date_format.format(c.getTime());
                                }
                            });
                            builder.show();

                            Log.d("ReminderService In", selecteddeliverycost + " " + day + " " + hour + " " + minute + " " + deliverydaydate);

                        } catch (Exception ex) {
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }


                    }


                });


            }


        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }
}


