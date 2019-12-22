package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by sibusison on 2017/07/30.
 */
public class AddProductFragOcr extends Fragment implements View.OnClickListener {

    View rootView;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    private CompoundButton autoFocus;
    private CompoundButton useFlash;

    TextView textValue, statusMessage, textValueMesage, contentTxt,description,web,txtcountinginvent,txtcountshopping;

    String daysleftmessage_pass = "";
    FragmentManager fragmentManager;
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;

    Button btnscan;
    Bundle bundle;
    String extractdate, extractformat;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "AddProductFragOcr";
    public long daysleft;
    ImageButton search,search_idea;
    ImageView productimage,moretap;;
    final CharSequence[] items = {"Product Insight","Shopping List", "GroRequest", "Recipe Idea"};
    Bundle bundles = new Bundle();
    String m_Text_donate = "";
    String firstname = "";
    int userid;
    MainActivity activity = MainActivity.instance;
    int countinventory=0;
    int countshoppinglist=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main_ocr, container, false);
        fragmentManager = getFragmentManager();
        statusMessage = (TextView) rootView.findViewById(R.id.status_message);
        textValue = (TextView) rootView.findViewById(R.id.text_value);
        txtcountinginvent = (TextView) rootView.findViewById(R.id.txtcountinginvent);
        txtcountshopping = (TextView) rootView.findViewById(R.id.txtcountshopping);
        description = (TextView) rootView.findViewById(R.id.description);
        web = (TextView) rootView.findViewById(R.id.website);
        productimage = (ImageView) rootView. findViewById(R.id.productimage);
        textValueMesage = (TextView) rootView.findViewById(R.id.text_value_message);
        search_idea = (ImageButton) rootView. findViewById(R.id.search_idea);
        search = (ImageButton) rootView. findViewById(R.id.search);
        moretap = (ImageView) rootView.findViewById(R.id.moretap);
        contentTxt = (TextView) rootView.findViewById(R.id.scan_content);
        btnscan = (Button) rootView.findViewById(R.id.btn_scan);

        autoFocus = (CompoundButton) rootView.findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) rootView.findViewById(R.id.use_flash);


        rootView.findViewById(R.id.read_text).setOnClickListener(this);
        rootView.findViewById(R.id.btn_select_action).setOnClickListener(this);
        //rootView.findViewById(R.id.search).setOnClickListener(this);



        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();

        try {
            if (activity.edthidenuserid.getText().toString().equals("")) {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
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
            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            }

        } catch (Exception ex) {

        }



        try {
            if (bundle != null) {

                // if(!bundle.getString("prod").toString().equals("prod")){
                  /*  if (!bundle.getString("barcode").toString().equals("")) {
                        contentTxt.setText(bundle.getString("barcode").toString());
                        bundle.clear();
                    }*/
                // contentTxt.setText(bundle.getString("barcode").toString());

                // }else {
                if (!bundle.getString("barcode").toString().equals("")) {

                    textValue.setText(bundle.getString("bestbefore").toString());
                    Date bestbefore = null;
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                    String todaydate = date_format.format(today);
                    try {
                        Date date = new Date(date_format.parse(textValue.getText().toString()).getTime());
                        Date date2 = new Date(date_format.parse(todaydate).getTime());
                        bestbefore = date;
                        today = date2;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                    String daysleftmessage = "";
                    if (daysleft < 0) {
                        //daysleftmessage = "Date already passed.";
                        daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                        daysleftmessage_pass = 0 + " Days";
                    } else {
                        daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                        daysleftmessage_pass = daysleft + " Days";
                    }

                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(date_format.format(bestbefore));
                    textValueMesage.setText(Html.fromHtml(daysleftmessage));


                    contentTxt.setText("Barcode :"+bundle.getString("barcode").toString());



                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                    productimage.setImageBitmap(decodebitmap);

                  ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        description.setText(rs.getString("description").toString());
                        web.setText(rs.getString("website").toString());

                        bundle.putString("id", rs.getString("id").toString());
                        bundle.putString("bestbefore", rs.getString("bestbefore").toString());
                        bundle.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                        bundle.putString("barcode", rs.getString("Barcode").toString());
                        bundle.putString("name", rs.getString("name").toString());
                        bundle.putString("description", rs.getString("description").toString());
                        bundle.putString("website", rs.getString("website").toString());
                        bundle.putString("size", rs.getString("size").toString());
                        bundle.putString("price", rs.getString("price").toString());
                        bundle.putString("storage", rs.getString("storage").toString());
                        bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                        bundle.putString("quantity", rs.getString("quantity").toString());
                        bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                        bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                        bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                        bundle.putString("isscanned", rs.getString("isscanned").toString());
                        bundle.putString("image", rs.getString("image").toString());
                        bundle.putString("categoryid", rs.getString("categoryid").toString());
                        bundle.putString("retailerid", rs.getString("retailerid").toString());



                    }
                   // countinventory=0;
                    //countshoppinglist=0;
                    String query0 = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "' and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query0);
                    PreparedStatement ps0 = con.prepareStatement(query0);
                    ResultSet rs0 = ps0.executeQuery();
                    while (rs0.next()) {

                        countshoppinglist = countshoppinglist + Integer.parseInt(rs0.getString("quantity").toString());

                    }
                    String query1 = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "' and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query1);
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        countinventory = countinventory + Integer.parseInt(rs1.getString("quantity").toString());

                    }
                    txtcountinginvent.setText(String.valueOf(countinventory));
                    txtcountshopping.setText(String.valueOf(countshoppinglist));
                       /* AddProductFrag fragment = new AddProductFrag();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/


                }


            }
        } catch (Exception ex) {

            try {
                if (bundle != null) {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        description.setText(rs.getString("description").toString());
                        web.setText(rs.getString("website").toString());

                        bundle.putString("id", rs.getString("id").toString());
                        bundle.putString("bestbefore", rs.getString("bestbefore").toString());
                        bundle.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                        bundle.putString("barcode", rs.getString("Barcode").toString());
                        bundle.putString("name", rs.getString("name").toString());
                        bundle.putString("description", rs.getString("description").toString());
                        bundle.putString("website", rs.getString("website").toString());
                        bundle.putString("size", rs.getString("size").toString());
                        bundle.putString("price", rs.getString("price").toString());
                        bundle.putString("storage", rs.getString("storage").toString());
                        bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                        bundle.putString("quantity", rs.getString("quantity").toString());
                        bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                        bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                        bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                        bundle.putString("isscanned", rs.getString("isscanned").toString());
                        bundle.putString("image", rs.getString("image").toString());
                        bundle.putString("categoryid", rs.getString("categoryid").toString());
                        bundle.putString("retailerid", rs.getString("retailerid").toString());

                        contentTxt.setText("Barcode :"+bundle.getString("barcode").toString());
                        textValue.setText(rs.getString("bestbefore").toString());
                        Date bestbefore = null;
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);
                        try {
                            Date date = new Date(date_format.parse(textValue.getText().toString()).getTime());
                            Date date2 = new Date(date_format.parse(todaydate).getTime());
                            bestbefore = date;
                            today = date2;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        String daysleftmessage = "";
                        if (daysleft < 0) {
                            //daysleftmessage = "Date already passed.";
                            daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                            daysleftmessage_pass = 0 + " Days";
                        } else {
                            daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                            daysleftmessage_pass = daysleft + " Days";
                        }

                        statusMessage.setText(R.string.ocr_success);
                        textValue.setText(date_format.format(bestbefore));
                        textValueMesage.setText(Html.fromHtml(daysleftmessage));

                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                        productimage.setImageBitmap(decodebitmap);

                      //  countinventory=0;
                       // countshoppinglist=0;
                        String query0 = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "' and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        Log.d("ReminderService In", query0);
                        PreparedStatement ps0 = con.prepareStatement(query0);
                        ResultSet rs0 = ps0.executeQuery();
                        while (rs0.next()) {

                            countshoppinglist = countshoppinglist + Integer.parseInt(rs0.getString("quantity").toString());

                        }
                        String query1 = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "' and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        Log.d("ReminderService In", query1);
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            countinventory = countinventory + Integer.parseInt(rs1.getString("quantity").toString());

                        }
                        txtcountinginvent.setText(countinventory);
                        txtcountshopping.setText(countshoppinglist);

                    }

                }
            }catch (Exception e){

            }

        }

        search_idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (bundle != null) {

                        // if(!bundle.getString("prod").toString().equals("prod")){
                  /*  if (!bundle.getString("barcode").toString().equals("")) {
                        contentTxt.setText(bundle.getString("barcode").toString());
                        bundle.clear();
                    }*/
                        // contentTxt.setText(bundle.getString("barcode").toString());

                        // }else {
                        if (!bundle.getString("barcode").toString().equals("")) {

                            try {
                                if (bundle != null) {
                                    ConnectionClass cn = new ConnectionClass();
                                    con = cn.connectionclass(un, pass, db, ip);
                                    String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                                    PreparedStatement ps = con.prepareStatement(query);
                                    ResultSet rs = ps.executeQuery();

                                    while (rs.next()) {
                                        bundle.clear();
                                        Log.d("ReminderService In", rs.getString("name") + "@@@");
                                        Log.d("ReminderService In", rs.getString("description") + "@@@");
                                        description.setText(rs.getString("description").toString());
                                        web.setText(rs.getString("website").toString());
                                        bundle.putString("id", rs.getString("id").toString());
                                        bundle.putString("bestbefore", rs.getString("bestbefore").toString());
                                        bundle.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                                        bundle.putString("barcode", rs.getString("Barcode").toString());
                                        bundle.putString("name", rs.getString("name").toString());
                                        bundle.putString("description", rs.getString("description").toString());
                                        bundle.putString("website", rs.getString("website").toString());
                                        bundle.putString("size", rs.getString("size").toString());
                                        bundle.putString("price", rs.getString("price").toString());
                                        bundle.putString("storage", rs.getString("storage").toString());
                                        bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                        bundle.putString("quantity", rs.getString("quantity").toString());
                                        bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                        bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                        bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                        bundle.putString("isscanned", rs.getString("isscanned").toString());
                                        bundle.putString("image", rs.getString("image").toString());
                                        bundle.putString("categoryid", rs.getString("categoryid").toString());
                                        bundle.putString("retailerid", rs.getString("retailerid").toString());



                                    }

                                }
                            }catch (Exception e){

                            }
                            Fragment fragment = new SearchListFrag();
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                        }
                    }
                }catch (Exception ex){

                }

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Fragment fragment = new SearchListFrag();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }
        });
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();*/

                Intent i = new Intent(rootView.getContext(), ScanBarcode.class);
                startActivity(i);
              /* Fragment fragment = new ScanFrag();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/
            }
        });


        textValue.setOnClickListener(new View.OnClickListener() {
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

                        textValue.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Date bestbefore = null;
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);
                        try {
                            Date date = new Date(date_format.parse(textValue.getText().toString()).getTime());
                            Date date2 = new Date(date_format.parse(todaydate).getTime());
                            bestbefore = date;
                            today = date2;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        String daysleftmessage = "";
                        if (daysleft < 0) {
                            daysleftmessage = "Date already passed.";
                        } else {
                            daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                            daysleftmessage_pass = daysleft + " Days";
                        }

                        statusMessage.setText(R.string.ocr_success);
                        textValue.setText(date_format.format(bestbefore));
                        textValueMesage.setText(Html.fromHtml(daysleftmessage));


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        moretap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

                        textValue.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Date bestbefore = null;
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);
                        try {
                            Date date = new Date(date_format.parse(textValue.getText().toString()).getTime());
                            Date date2 = new Date(date_format.parse(todaydate).getTime());
                            bestbefore = date;
                            today = date2;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        String daysleftmessage = "";
                        if (daysleft < 0) {
                            daysleftmessage = "Date already passed.";
                        } else {
                            daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                            daysleftmessage_pass = daysleft + " Days";
                        }

                        statusMessage.setText(R.string.ocr_success);
                        textValue.setText(date_format.format(bestbefore));
                        textValueMesage.setText(Html.fromHtml(daysleftmessage));


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                return false;
            }
        });
        return rootView;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {



        if (v.getId() == R.id.read_text) {
            // launch Ocr capture fragment_mapy.
            Intent intent = new Intent(rootView.getContext(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
        if (v.getId() == R.id.btn_select_action) {
            // Bundle bundle = new Bundle();
            if (statusMessage.getText().toString().equals("Date scanned successfully")) {
               // Bundle bundle = this.getArguments();
                try {
                    if (bundle != null) {

                        bundle.putString("bestbefore", textValue.getText().toString());
                        bundle.putString("bestbeforeStatus", daysleftmessage_pass);
                        bundle.putLong("days", daysleft);
                        bundle.putString("barcode", bundle.getString("barcode").toString());
                      //  bundle.putString("option", bundle.getString("option").toString());

                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);





                        try {


//#######################

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
//builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                            builder.setTitle(description.getText().toString()+"\nInventory:" + countinventory + " & Shopping:"+countshoppinglist).setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (items[which].equals("Shopping List")) {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", bundle.getString("barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                AddProductFrag fragment = new AddProductFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    else if (items[which].equals("Product Insight")) {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", bundle.getString("barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                ProductInsightFrag fragment = new ProductInsightFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    else if (items[which].equals("GroRequest")) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(rootView.getContext());

                                        builder1.setTitle("GroRequest");
                                        builder1.setMessage("Grocery Request:");
                                        builder1.setIcon(rootView.getResources().getDrawable(R.drawable.request));
                                        final EditText input = new EditText(rootView.getContext());
                                        input.setGravity(Gravity.CENTER);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                        input.setHint("Enter Quantity");
                                        input.setHintTextColor(Color.GRAY);
                                        builder1.setView(input);

                                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                try {


                                                    String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                                                    PreparedStatement ps = con.prepareStatement(query);
                                                    ResultSet rs = ps.executeQuery();
                                                    while (rs.next()) {

                                                        Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();

                                                        Date today = new Date();
                                                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                                        String todaydate = date_format.format(today);

                                                        String refno = rs.getString("name").toString().substring(0, 3).toUpperCase() + today.getHours() + "" + today.getMinutes() + "" + today.getSeconds();
                                                        String message = "GroRequest Ref_No:" + refno + " by " + firstname + " to purchase Product:" + rs.getString("name").toString() + "\nQuantity:" + m_Text_donate + "\nDescription:" + rs.getString("description").toString() + "\nLogin GoingDots App";
                                                        String commands = "insert into [UserProductShoppingList] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website],[image_name],[image_type])" +
                                                                "values ('" + rs.getString("bestbefore").toString() + "','" + rs.getString("name").toString() + "','" + rs.getString("description").toString() + "','" +
                                                                rs.getString("bestbeforeStatus").toString() + "','" + rs.getString("size").toString() + "','" + Double.parseDouble(rs.getString("price").toString()) + "','" +
                                                                rs.getString("storage").toString() + "','" + Integer.parseInt(rs.getString("preferbestbeforedays").toString()) + "','" + Integer.parseInt(m_Text_donate) + "','" + Integer.parseInt(rs.getString("quantityperbulk").toString()) + "','" + Integer.parseInt(rs.getString("reorderpoint").toString()) + "','" + Double.parseDouble(rs.getString("totatitemvalue").toString()) +
                                                                "','" + rs.getString("isscanned").toString() + "','" + todaydate + "','" + rs.getString("image").toString() + "','" + userid + "','" + Integer.parseInt(rs.getString("categoryid").toString()) + "','" + Integer.parseInt(rs.getString("retailerid").toString()) + "','No','" + rs.getString("Barcode").toString() + "','" + rs.getString("website").toString() + "','" + refno + "','" + message + "')";
                                                        PreparedStatement preStmt = con.prepareStatement(commands);
                                                        preStmt.executeUpdate();
                                                        //Search contact and send message
                                                        // sendSMSwithDeliveryReport("+27795357393","Testing");
                                                        Intent i = new Intent(rootView.getContext(), MainActivitySms.class);
                                                        Bundle extras = new Bundle();
                                                        extras.putString("message", message);
                                                        i.putExtras(extras);
                                                        startActivity(i);

                                                    }


                                                } catch (Exception ex) {
                                                    Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                                                    Log.d("ReminderService In", ex.getMessage());
                                                }

                                            }
                                        });
                                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder1.show();

                                    } else {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", bundle.getString("barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                }

                            });
                            builder.show();



                        } catch (Exception ex) {
                            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString() + " NGOAllD");
                        }
                        //##################
                       /* String query = "select * from [UserProduct] where [Barcode]='" + bundle.getString("barcode").toString() + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {


                            bundle.putString("id", rs.getString("id").toString());
                            bundle.putString("name", rs.getString("name").toString());
                            bundle.putString("barcode", bundle.getString("barcode").toString());
                            bundle.putString("MylistFrag", "Ocr");
                            bundle.putString("description", rs.getString("description").toString());
                            bundle.putString("size", rs.getString("size").toString());
                            bundle.putString("website", rs.getString("website").toString());
                            bundle.putString("price", rs.getString("price").toString());
                            bundle.putString("storage", rs.getString("storage").toString());
                            bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                            bundle.putString("quantity", rs.getString("quantity").toString());
                            bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                            bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                            bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                            bundle.putString("isscanned", rs.getString("isscanned").toString());
                            bundle.putString("image", rs.getString("image").toString());
                            bundle.putString("categoryid", rs.getString("categoryid").toString());
                            bundle.putString("retailerid", rs.getString("retailerid").toString());
                            bundle.putString("userid", rs.getString("userid").toString());


                        }*/


                    } else {

                        bundle = new Bundle();
                        bundle.putString("bestbefore", textValue.getText().toString());
                        bundle.putString("bestbeforeStatus", daysleftmessage_pass);
                        bundle.putLong("days", daysleft);

                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);

                        int countinventory=0;
                        int countshoppinglist=0;
                        String query0 = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "' and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        Log.d("ReminderService In", query0);
                        PreparedStatement ps0 = con.prepareStatement(query0);
                        ResultSet rs0 = ps0.executeQuery();
                        while (rs0.next()) {

                            countshoppinglist = countshoppinglist + Integer.parseInt(rs0.getString("quantity").toString());

                        }
                        String query1 = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "' and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        Log.d("ReminderService In", query1);
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            countinventory = countinventory + Integer.parseInt(rs1.getString("quantity").toString());

                        }

                        try {


//#######################

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
//builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                            builder.setTitle(description.getText().toString()+"\nInventory:" + countinventory + " & Shopping:"+countshoppinglist).setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (items[which].equals("Shopping List")) {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", rs.getString("Barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                AddProductFrag fragment = new AddProductFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    else if (items[which].equals("Product Insight")) {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", rs.getString("Barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                ProductInsightFrag fragment = new ProductInsightFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    else if (items[which].equals("GroRequest")) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(rootView.getContext());

                                        builder1.setTitle("GroRequest");
                                        builder1.setMessage("Grocery Request:");
                                        builder1.setIcon(rootView.getResources().getDrawable(R.drawable.request));
                                        final EditText input = new EditText(rootView.getContext());
                                        input.setGravity(Gravity.CENTER);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                        input.setHint("Enter Quantity");
                                        input.setHintTextColor(Color.GRAY);
                                        builder1.setView(input);

                                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                try {


                                                    String query = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "'";
                                                    PreparedStatement ps = con.prepareStatement(query);
                                                    ResultSet rs = ps.executeQuery();
                                                    while (rs.next()) {

                                                        Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();

                                                        Date today = new Date();
                                                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                                        String todaydate = date_format.format(today);

                                                        String refno = rs.getString("name").toString().substring(0, 3).toUpperCase() + today.getHours() + "" + today.getMinutes() + "" + today.getSeconds();
                                                        String message = "GroRequest Ref_No:" + refno + " by " + firstname + " to purchase Product:" + rs.getString("name").toString() + "\nQuantity:" + m_Text_donate + "\nDescription:" + rs.getString("description").toString() + "\nLogin GoingDots App";
                                                        String commands = "insert into [UserProductShoppingList] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website],[image_name],[image_type])" +
                                                                "values ('" + rs.getString("bestbefore").toString() + "','" + rs.getString("name").toString() + "','" + rs.getString("description").toString() + "','" +
                                                                rs.getString("bestbeforeStatus").toString() + "','" + rs.getString("size").toString() + "','" + Double.parseDouble(rs.getString("price").toString()) + "','" +
                                                                rs.getString("storage").toString() + "','" + Integer.parseInt(rs.getString("preferbestbeforedays").toString()) + "','" + Integer.parseInt(m_Text_donate) + "','" + Integer.parseInt(rs.getString("quantityperbulk").toString()) + "','" + Integer.parseInt(rs.getString("reorderpoint").toString()) + "','" + Double.parseDouble(rs.getString("totatitemvalue").toString()) +
                                                                "','" + rs.getString("isscanned").toString() + "','" + todaydate + "','" + rs.getString("image").toString() + "','" + userid + "','" + Integer.parseInt(rs.getString("categoryid").toString()) + "','" + Integer.parseInt(rs.getString("retailerid").toString()) + "','No','" + rs.getString("Barcode").toString() + "','" + rs.getString("website").toString() + "','" + refno + "','" + message + "')";
                                                        PreparedStatement preStmt = con.prepareStatement(commands);
                                                        preStmt.executeUpdate();
                                                        //Search contact and send message
                                                        // sendSMSwithDeliveryReport("+27795357393","Testing");
                                                        Intent i = new Intent(rootView.getContext(), MainActivitySms.class);
                                                        Bundle extras = new Bundle();
                                                        extras.putString("message", message);
                                                        i.putExtras(extras);
                                                        startActivity(i);

                                                    }


                                                } catch (Exception ex) {
                                                    Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                                                    Log.d("ReminderService In", ex.getMessage());
                                                }

                                            }
                                        });
                                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder1.show();

                                    } else {
                                        try {

                                            String query = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundle.putString("id", rs.getString("id").toString());
                                                bundle.putString("name", rs.getString("name").toString());
                                                bundle.putString("barcode", rs.getString("Barcode").toString());
                                                bundle.putString("MylistFrag", "Ocr");
                                                bundle.putString("description", rs.getString("description").toString());
                                                bundle.putString("website", rs.getString("website").toString());
                                                bundle.putString("size", rs.getString("size").toString());
                                                bundle.putString("price", rs.getString("price").toString());
                                                bundle.putString("storage", rs.getString("storage").toString());
                                                bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundle.putString("quantity", rs.getString("quantity").toString());
                                                bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundle.putString("isscanned", rs.getString("isscanned").toString());
                                                bundle.putString("image", rs.getString("image").toString());
                                                bundle.putString("categoryid", rs.getString("categoryid").toString());
                                                bundle.putString("retailerid", rs.getString("retailerid").toString());
                                                bundle.putString("userid", rs.getString("userid").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                fragment.setArguments(bundle);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                }

                            });
                            builder.show();



                        } catch (Exception ex) {
                            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString() + " NGOAllD");
                        }
                        //##################
                        /*String query = "select * from [UserProduct] where [Barcode]='" + contentTxt.getText().toString().substring(contentTxt.getText().toString().indexOf(":")+1) + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {


                            bundle.putString("id", rs.getString("id").toString());
                            bundle.putString("name", rs.getString("name").toString());
                            bundle.putString("barcode", rs.getString("Barcode").toString());
                            bundle.putString("MylistFrag", "Ocr");
                            bundle.putString("description", rs.getString("description").toString());
                            bundle.putString("website", rs.getString("website").toString());
                            bundle.putString("size", rs.getString("size").toString());
                            bundle.putString("price", rs.getString("price").toString());
                            bundle.putString("storage", rs.getString("storage").toString());
                            bundle.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                            bundle.putString("quantity", rs.getString("quantity").toString());
                            bundle.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                            bundle.putString("reorderpoint", rs.getString("reorderpoint").toString());
                            bundle.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                            bundle.putString("isscanned", rs.getString("isscanned").toString());
                            bundle.putString("image", rs.getString("image").toString());
                            bundle.putString("categoryid", rs.getString("categoryid").toString());
                            bundle.putString("retailerid", rs.getString("retailerid").toString());
                            bundle.putString("userid", rs.getString("userid").toString());
                        }*/

                    }


                } catch (Exception ex) {
                    Toast.makeText(rootView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("ReminderService In", "exxxe"+ex.getMessage());

                }

              /*  AddProductFrag fragment = new AddProductFrag();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/
            } else {

                Toast.makeText(rootView.getContext(), "Please Scan correct date!!!", Toast.LENGTH_SHORT).show();
            }


        }
    }


    /**
     * Called when an fragment_mapy you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * fragment_mapy is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child fragment_mapy
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult

     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);

                    if (text.contains("Best Before") || text.contains("Best Before :")) {
                        text = text.replace(":", "").replace("Best Before", "").trim();
                    } else if (text.contains("Best before") || text.contains("Best before :")) {
                        text = text.replace(":", "").replace("Best before", "").trim();
                    } else if (text.contains("BEST BEFORE") || text.contains("BEST BEFORE :")) {
                        text = text.replace(":", "").replace("BEST BEFORE", "").trim();
                    } else if (text.contains("EXP") || text.contains("EXP :")) {
                        text = text.replace(":", "").replace("EXP", "").trim();
                    } else if (text.contains("Exp") || text.contains("Exp :")) {
                        text = text.replace(":", "").replace("Exp", "").trim();
                    } else if (text.contains("BB") || text.contains("BB :")) {
                        text = text.replace(":", "").replace("BB", "").trim();
                    } else if (text.contains("BBE") || text.contains("BBE :")) {
                        text = text.replace(":", "").replace("BBE", "").trim();
                    }
                    text = text.replaceAll(" ", "").trim();


                    try {

                        Parser parser = new Parser();
                        List<LocalDateModel> dates = parser.parse(text);
                        if (dates != null) {
                            extractdate = dates.get(0).getDateTimeString();

                            if (extractdate.contains("/")) {
                                extractdate = extractdate.replaceAll(" ", "");
                            }
                        }


                        Date bestbefore = new SimpleDateFormat("yyyy-MM-dd").parse(extractdate);
                        Date today = new Date();

                        long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                        String daysleftmessage = "";

                        if (daysleft < 0) {
                            daysleftmessage = "Date already passed.";
                        } else {
                            daysleftmessage = "Best/Use: " + String.valueOf(daysleft) + " Days Left";
                            daysleftmessage_pass = daysleft + " Days";
                        }


                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        statusMessage.setText(R.string.ocr_success);
                        textValue.setText(dateFormat.format(bestbefore));
                        textValueMesage.setText(Html.fromHtml(daysleftmessage));


                    } catch (Exception e) {

                        extractdate = "Selection [" + text + "] conflicts expected date format,alternatively touch below... ";
                        statusMessage.setText(extractdate);
                        textValue.setText("");
                        textValueMesage.setText("");


                    }


                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    public Date extractDateFormat(String strdate) {

        List<String> dateformats = Arrays.asList("M d yyyy", "d M yyyy", "yyyy M d",
                "dd-MM-yy", "dd MMM yy", "MMM dd yy", "yy MMM dd", "dd-MM-yyyy", "MM-dd-yyyy", "yyyy-MM-dd", "dd/MM/yy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd",
                "yyyy MMMMM d", "yyyy d MMMMM", "MMMMM d yyyy", "d MMMMM yyyy", "yyyy MMM d", "yyyy d MMM", "d MMM yy", "MMM d yy", "MMM d yyyy", "d MMM yyyy");
        // List<String> dateformats= Arrays.asList("M/y","M/d/y","M-d-y","d/M/y","d-M-y","MMM yyyy");
        for (String format : dateformats) {
            try {

                return new SimpleDateFormat(format).parse(strdate);
            } catch (ParseException e) {
                extractdate = "Selection " + strdate + " conflicts expected date format,alternatively touch below... ";
            }
        }
        return null;

    }


}
