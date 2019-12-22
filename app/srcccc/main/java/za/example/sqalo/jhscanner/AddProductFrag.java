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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.provider.MediaStore;


/**
 * Created by sibusison on 2017/07/30.
 */
public class AddProductFrag extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,AdapterView.OnItemSelectedListener {

    View rootView;
    //---------con--------
    Connection con;
    String un,pass,db,ip;
    ConnectionClass connectionClass;
    EditText edtproductId,edtname,edtdescription,edtbestbeforeStatus,edtbestbeforedate,edtsize,edtprice,edtquantity,edtquantityperbulk,edtquantitybulktotal,edtreorderpoint,edttotal;
    EditText edtpreferbestbeforedays,edtbarcode,edtstorage,edtisscanned,edtuserid,edtcategoryid,edtretailerid,edtweb;

    Spinner spinnerretailer,spinnercategory;
   SpinnerAdapter adapter = null;

    Button btnsearchproduct, btnScanbestBeforeDate,btnScanImage;
    ImageView edtproductImage;
    ImageButton btn_add_mylist,btn_update_mylist,btn_remove_mylist,donestatus;

    ListView lstproduct;
    private int mYear, mMonth, mDay, mHour, mMinute,mSeconds,mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;
    //---------con--------
    Calendar cal;


    LinearLayout loadminbutton,louserbutton,layoutreorderpoint,layoutbulk,layoutbulktotalquantity,layouthome;
    private static final int CAMERA_REQUEST = 1888;

    Bitmap productphoto;
    byte[] byteArray;
    String encodedImage,selectedretailer;

    ArrayAdapter adapter1;
    Bundle bundle;
    CheckBox chkisShopping,chkisBulk;
    int previousquantity=0;
    String m_Text_waste = "";
    String m_Text_donate = "";

    MainActivity activity =   MainActivity.instance;
    static AddProductFrag instance;
    public AddProductFrag(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.addproducts, container, false);

       // loadminbutton = (LinearLayout) rootView. findViewById(R.id.layoutadminbutton);
        louserbutton = (LinearLayout) rootView. findViewById(R.id.layoutuserbutton);
        layoutreorderpoint = (LinearLayout) rootView. findViewById(R.id.layoutreorderpoint);
        layoutbulk = (LinearLayout) rootView. findViewById(R.id.layoutbulk);
        layoutbulktotalquantity = (LinearLayout) rootView. findViewById(R.id.layoutbulktotalquantity);
        layouthome = (LinearLayout) rootView. findViewById(R.id.layouthome);

        edtproductId = (EditText)rootView. findViewById(R.id.edtproductid);
        // int foreign
        edtuserid = (EditText)rootView. findViewById(R.id.edtuserid);
        edtcategoryid = (EditText)rootView. findViewById(R.id.edtproductCategory);
        edtretailerid = (EditText)rootView. findViewById(R.id.edtretailerName);
        //--------------------------------
        edtname = (EditText)rootView. findViewById(R.id.edtname);
        edtdescription = (EditText) rootView. findViewById(R.id.edtdescription);
        edtweb = (EditText) rootView. findViewById(R.id.edtweb);
        edtbestbeforeStatus = (EditText)rootView.  findViewById(R.id.edtbestbeforeStatus);
        edtbestbeforedate = (EditText)rootView.  findViewById(R.id.edtbestbeforedate);
        edtbarcode = (EditText)rootView.  findViewById(R.id.edtbarcode);

        edtsize = (EditText)rootView.  findViewById(R.id.edtsize);
        edtprice = (EditText)rootView.  findViewById(R.id.edtprice);

        edtquantity = (EditText)rootView.  findViewById(R.id.edtquantity);
        edtquantityperbulk = (EditText)rootView.  findViewById(R.id.edtquantityperbulk);
        edtquantitybulktotal = (EditText)rootView.  findViewById(R.id.edtquantitybulktotal);

        edtreorderpoint = (EditText)rootView.  findViewById(R.id.edtreorderpoint);
        edttotal = (EditText)rootView.  findViewById(R.id.edttotal);
        edtpreferbestbeforedays = (EditText)rootView.  findViewById(R.id.edtnotifydays);
        edtstorage = (EditText)rootView.  findViewById(R.id.edtstorage);
        edtisscanned = (EditText)rootView.  findViewById(R.id.edtisscanned);
        chkisShopping = (CheckBox) rootView.  findViewById(R.id.chkisshopping);
        chkisBulk = (CheckBox) rootView.  findViewById(R.id.chkisbulk);
        spinnerretailer = (Spinner) rootView. findViewById(R.id.spinnerretailer);
        spinnercategory = (Spinner) rootView. findViewById(R.id.spinnercategory);

        edtproductImage = (ImageView) rootView. findViewById(R.id.productimage);


        edtproductId.setVisibility(View.GONE);
instance=this;

        btnsearchproduct = (Button) rootView. findViewById(R.id.btn_search_product);
        btnScanbestBeforeDate = (Button) rootView. findViewById(R.id.btn_scan_bestbeforedate);
        btnScanImage = (Button) rootView. findViewById(R.id.btn_scan_image);

         btn_add_mylist = (ImageButton)  rootView.findViewById(R.id.btn_add_mylist);
        btn_update_mylist = (ImageButton) rootView. findViewById(R.id.btn_update_mylist);
        btn_remove_mylist = (ImageButton)  rootView.findViewById(R.id.btn_remove_mylist);


        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);

//==========
       /* if(fragment_mapy.edthidenuserid.getText().toString().equals("1")){ //Admin
            louserbutton.setVisibility(View.GONE);
        }else{
            //loadminbutton.setVisibility(View.GONE);
            //btnScanImage.setVisibility(View.GONE);
        }*/
        ArrayList<ItemData> list=new ArrayList<>();
       // list.add(new ItemData("Select Retailer",R.drawable.blackproduct));
        list.add(new ItemData("Checkers",R.drawable.logocheckers));
        list.add(new ItemData("OK Food",R.drawable.logook));
        list.add(new ItemData("Pick n Pay",R.drawable.logopicknpay));
        list.add(new ItemData("Shoprite",R.drawable.logoshoprite));
        list.add(new ItemData("Spar",R.drawable.logospar));
        list.add(new ItemData("USave",R.drawable.logousaveshoprite));

        list.add(new ItemData("Makro",R.drawable.logomakro));
        list.add(new ItemData("Food Lovers",R.drawable.logofoodlovers));
        list.add(new ItemData("Woolworth Food",R.drawable.logowoolworth));
        list.add(new ItemData("Boxer",R.drawable.logoboxer));
        list.add(new ItemData("Game",R.drawable.logogame));
        list.add(new ItemData("Fruits & Veg",R.drawable.logofruitsandveg));
        list.add(new ItemData("Other Retailers",R.drawable.logootherretailers));


        spinnerretailer=(Spinner)rootView.findViewById(R.id.spinnerretailer);
   adapter=new SpinnerAdapter(this.getActivity(), R.layout.spinner_layout,R.id.txt,list);
        spinnerretailer.setAdapter(adapter);
        spinnerretailer.setOnItemSelectedListener(this);

       // edtdescription.setText(list.get(2).getText());

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";


        FillCategory();

        try{
            if (activity.edthidenuserid.getText().toString().equals("")) {
                ConnectionClass cn=new ConnectionClass();
                con =cn.connectionclass(un, pass, db, ip);
                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();
                edtuserid.setText( rs11.getString("userid").toString());


            }else {
                edtuserid.setText(activity.edthidenuserid.getText().toString());
                layouthome.setVisibility(View.GONE);
            }

        }catch (Exception ex){

        }



        bundle = this.getArguments();

        btnsearchproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(rootView.getContext(), MainActivityOcr.class);
                //startActivity(i);

                Fragment frag =   new SearchListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });

        btnScanbestBeforeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(rootView.getContext(), MainActivityOcr.class);
                //startActivity(i);

                Fragment frag =   new AddProductFragOcr();
                try{
                    if(bundle != null){
                        bundle.putString("prod", "prod");
                       frag.setArguments(bundle);
                    }
                }catch (Exception ex){

                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });

        edtbestbeforedate .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag =   new AddProductFragOcr();

                try{
                    if(bundle != null){
                        bundle.putString("prod", "prod");
                        frag.setArguments(bundle);
                    }
                }catch (Exception ex){

                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });




        chkisShopping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edtisscanned.setText("Yes");
                }else{
                    edtisscanned.setText("No");
                }
            }
        } );
        chkisBulk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   layoutbulk.setVisibility(View.VISIBLE);


                    }else{
                    layoutbulk.setVisibility(View.GONE);

                }
            }
        } );




        try{
            if(bundle != null){
                if(!bundle.getString("id").equals("")){


                    previousquantity=Integer.valueOf(bundle.getString("quantity"));
                    edtproductId.setText(bundle.getString("id"));
                    edtbestbeforedate.setText(bundle.getString("bestbefore"));
                    edtname.setText(bundle.getString("name"));
                    edtbarcode.setText(bundle.getString("barcode"));
                    edtdescription.setText(bundle.getString("description"));
                    edtweb.setText(bundle.getString("website"));
                    edtbestbeforeStatus.setText(bundle.getString("bestbeforeStatus"));
                    edtsize.setText(bundle.getString("size"));
                    edtprice.setText(bundle.getString("price"));
                    edtstorage.setText(bundle.getString("storage"));
                    edtpreferbestbeforedays.setText(bundle.getString("preferbestbeforedays"));
                    edtquantity.setText(bundle.getString("quantity"));
                    edtquantityperbulk.setText(bundle.getString("quantityperbulk"));
                    edtreorderpoint.setText(bundle.getString("reorderpoint"));
                    edttotal.setText(bundle.getString("totatitemvalue"));
                    edtisscanned.setText(bundle.getString("isscanned"));



                    if(edtisscanned.getText().toString().equals("Yes")){
                        chkisShopping.setChecked(true);

                    }else{
                        chkisShopping.setChecked(false);

                    }

                    if(!edtquantityperbulk.getText().toString().equals("0")){
                        chkisBulk.setChecked(true);
                        layoutbulk.setVisibility(View.VISIBLE);
                    }else{
                        chkisBulk.setChecked(false);
                        layoutbulk.setVisibility(View.GONE);
                    }



                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                    edtproductImage.setImageBitmap(decodebitmap);
                     encodedImage=bundle.getString("image");
                    spinnercategory.setSelection(Integer.parseInt(bundle.getString("categoryid"))-1);

                    spinnerretailer.setSelection(Integer.parseInt(bundle.getString("retailerid"))-1);


                }

            }
        }catch (Exception ex){

        }

        try{
            if(bundle != null){
                edtbestbeforedate.setText(bundle.getString("bestbefore"));
                edtbestbeforeStatus.setText(bundle.getString("bestbeforeStatus"));
                edtbarcode.setText(bundle.getString("barcode"));

            }
        }catch (Exception ex){

        }

        try{
            if(bundle != null){
                if(!bundle.getString("MylistFrag").equals("MylistFrag")){
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground_highlight);
                    edtstorage.setBackground(bg);
                    edtpreferbestbeforedays.setBackground(bg);
                    edtprice.setBackground(bg);
                    edtquantity.setBackground(bg);
                    edtreorderpoint.setBackground(bg);
                    spinnerretailer.setBackground(bg);
                    edtstorage.setBackground(bg);
                    btnScanbestBeforeDate.setVisibility(View.INVISIBLE);
                }
            }
        }catch (Exception ex){

        }


//=============

        edtprice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try{
                    if((s.length() != 0) && (! edtquantity.getText().toString().equals("")) &&!chkisBulk.isChecked()){
                        double total=Integer.valueOf(edtquantity.getText().toString())* Double.valueOf(edtprice.getText().toString());
                        edttotal.setText(String.valueOf(total));
                    }else if((s.length() != 0) && (! edtquantity.getText().toString().equals(""))&& (! edtquantityperbulk.getText().toString().equals("")) && chkisBulk.isChecked()){
                        int totalquantity=Integer.valueOf(edtquantity.getText().toString())*Integer.valueOf(edtquantityperbulk.getText().toString());
                        edtquantitybulktotal.setText(String.valueOf(totalquantity));
                        double total=Integer.valueOf(edtquantitybulktotal.getText().toString())* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                        edttotal.setText(String.valueOf(total));
                    }
                    else{
                        edttotal.setText("");
                    }
                }catch (Exception ex){

                }
            }
        });

        edtquantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try{
                    if((s.length() != 0) && (! edtprice.getText().toString().equals("")) && !chkisBulk.isChecked()){
                        double total=Integer.valueOf(edtquantity.getText().toString())* Double.valueOf(edtprice.getText().toString());
                        edttotal.setText(String.valueOf(total));
                    }else if((s.length() != 0) && (! edtprice.getText().toString().equals(""))&& (! edtquantityperbulk.getText().toString().equals("")) && chkisBulk.isChecked()){
                        int totalquantity=Integer.valueOf(edtquantity.getText().toString())*Integer.valueOf(edtquantityperbulk.getText().toString());
                        edtquantitybulktotal.setText(String.valueOf(totalquantity));
                        double total=Integer.valueOf(edtquantitybulktotal.getText().toString())* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                        edttotal.setText(String.valueOf(total));
                    } else{
                        edttotal.setText("");
                    }
                }catch (Exception ex){

                }

            }
        });
        edtquantityperbulk.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try{
                    if((s.length() != 0) && (! edtprice.getText().toString().equals("")) && !chkisBulk.isChecked()){
                        double total=Integer.valueOf(edtquantity.getText().toString())* Double.valueOf(edtprice.getText().toString());
                        edttotal.setText(String.valueOf(total));
                    }else if((s.length() != 0) && (! edtprice.getText().toString().equals(""))&& (! edtquantity.getText().toString().equals("")) && chkisBulk.isChecked()){
                        int totalquantity=Integer.valueOf(edtquantity.getText().toString())*Integer.valueOf(edtquantityperbulk.getText().toString());
                        edtquantitybulktotal.setText(String.valueOf(totalquantity));
                        double total=Integer.valueOf(edtquantitybulktotal.getText().toString())* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                        edttotal.setText(String.valueOf(total));
                    } else{
                        edttotal.setText("");
                    }
                }catch (Exception ex){

                }



            }
        });

        edtpreferbestbeforedays.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {

                try {
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                    if ((s.length() != 0) && (!edtpreferbestbeforedays.getText().toString().trim().equals("")) && (!edtbestbeforeStatus.getText().toString().trim().equals(""))) {
                        long preferdays = Long.parseLong(edtpreferbestbeforedays.getText().toString());
                        long daysleft = Long.parseLong(edtbestbeforeStatus.getText().toString().substring(0, edtbestbeforeStatus.getText().toString().indexOf(" ")));
                        if (daysleft < preferdays) {
                            Toast.makeText(rootView.getContext(), "This item does not meet your Prefered Best Before Days", Toast.LENGTH_SHORT).show();
                            edtbestbeforeStatus.setBackground(errorbg);

                        } else if (daysleft == preferdays) {
                            edtbestbeforeStatus.setBackground(errorbg);
                            Toast.makeText(rootView.getContext(), String.valueOf(preferdays) + " Days Before expiry is Today!!!", Toast.LENGTH_SHORT).show();
                            if ((!edtname.getText().toString().trim().equals("")) && (!edtdescription.getText().toString().trim().equals(""))) {
                                Intent i = new Intent(rootView.getContext(), ReminderEditActivity.class);
                                startActivity(i);

                            } else {

                                if ((edtname.getText().toString().trim().equals(""))) {
                                    edtname.setBackground(errorbg);
                                } else {
                                    edtname.setBackground(bg);
                                }

                                if ((edtdescription.getText().toString().trim().equals(""))) {
                                    edtdescription.setBackground(errorbg);
                                } else {
                                    edtdescription.setBackground(bg);
                                }

                                Toast.makeText(rootView.getContext(), "Field are required...", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            edtbestbeforeStatus.setBackground(bg);

                            if ((!edtname.getText().toString().trim().equals("")) && (!edtdescription.getText().toString().trim().equals(""))) {
                                Intent i = new Intent(rootView.getContext(), ReminderEditActivity.class);
                                startActivity(i);

                            } else {

                                if ((edtname.getText().toString().trim().equals(""))) {
                                    edtname.setBackground(errorbg);
                                } else {
                                    edtname.setBackground(bg);
                                }

                                if ((edtdescription.getText().toString().trim().equals(""))) {
                                    edtdescription.setBackground(errorbg);
                                } else {
                                    edtdescription.setBackground(bg);
                                }

                                Toast.makeText(rootView.getContext(), "Field are required...", Toast.LENGTH_LONG).show();
                            }

                        }


                    }
                } catch (Exception e) {

                }
            }



        });
        //==========




        btnScanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });



        btn_add_mylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(bundle != null) {
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);

                        if (!bundle.getString("id").equals("")) {

                            String query = "select  * from [UserProduct] where [description]='" + bundle.getString("description")+ "' and [userid]='" + edtuserid.getText().toString()+ "'  and [name]='"+bundle.getString("name")+ "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();

                          /* if (bundle.getString("option").equals("1")) {
                                AddProShoppingList();
                                Log.d("ReminderService In", "From search...adding shopping list");

                            }else{
                                if(rs.next()) {
                                    Toast.makeText(rootView.getContext(), "Product already exit !!! click Update", Toast.LENGTH_SHORT).show();
                                    Log.d("ReminderService In", bundle.getString("description")+" name:"+bundle.getString("name")+" userid:"+edtuserid.getText().toString());
                                }else{
                                    Log.d("ReminderService In", bundle.getString("description")+" name:"+bundle.getString("name")+" userid:"+edtuserid.getText().toString());
                                    AddPro();
                                }
                            }*/
                          //  if (bundle.getString("option").equals("1")) {
                               // Log.d("ReminderService In", "Add Shopping List");
                                if(rs.next()) {
                                    Toast.makeText(rootView.getContext(), "Product already exit !!! click Update", Toast.LENGTH_SHORT).show();
                                    Log.d("ReminderService In", bundle.getString("description")+" name:"+bundle.getString("name")+" userid:"+edtuserid.getText().toString());
                                }else{
                                    Log.d("ReminderService In", bundle.getString("description")+" name:"+bundle.getString("name")+" userid:"+edtuserid.getText().toString());
                                    AddPro();
                                }

                          //  }else{
                               // Log.d("ReminderService In", "Send GroRequest");
                           // }




                        }
                    }
                }catch (Exception ex){

                    AddPro();
                    Log.d("ReminderService In", ex.getMessage());
                }


            }
        });
        btn_update_mylist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                 UpdatePro();

            }
        });

        btn_remove_mylist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                  DeletePro();

            }
        });

        donestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomePremiumFragment();
                Bundle bundle = new Bundle();
                bundle.putString("assist", "assistant");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }
        });

        return rootView;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        ItemData c = adapter.getItem(position);
       // spinnerretailer.setSelection(adapter.getPosition(c));
       // Toast.makeText(rootView.getContext(), "You've selected " + c.getText() + " " + c.imageId, Toast.LENGTH_SHORT).show();
        selectedretailer=c.getText();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

                     if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                         productphoto = (Bitmap) data.getExtras().get("data");
                         edtproductImage.setImageBitmap(productphoto);

                         if (productphoto != null) {
                             ByteArrayOutputStream stream = new ByteArrayOutputStream();
                             productphoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                             byteArray = stream.toByteArray();

                             encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            // Toast.makeText(rootView.getContext(), "Conversion Done", Toast.LENGTH_SHORT).show();
                         }
                     }






    }
    //======Sync FillCategory


       public void FillCategory() {

                try
                {
                    ConnectionClass cn=new ConnectionClass();
                    con =cn.connectionclass(un, pass, db, ip);
                    if (con == null)
                    {

                        Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String query = "select * from [Category]";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        ArrayList<String> category = new ArrayList<String>();
                         while(rs.next()) {

                            category.add(rs.getString("productCategory"));
                        }
                        adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnercategory.setAdapter(adapter1);
                        //spinnercategory.setSelection(adapter1.getPosition(rs.getString("birthyear")));
                       // z = "Loaded Successfully";

                        con.close();

                    }
                }
                catch (Exception ex)
                {
                   // Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                   // z = "Invalid data input!";
                }



        }




        public void AddPro() {
            try {
             edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
                edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
                //---------
                if ((edtbestbeforedate.getText().toString().trim().equals("") || edtname.getText().toString().trim().equals("") || edtbestbeforeStatus.getText().toString().trim().equals("") || edtretailerid.getText().toString().trim().equals("")|| edtcategoryid.getText().toString().trim().equals("")|| edtprice.getText().toString().trim().equals("") || edtquantity.getText().toString().trim().equals("") || edtstorage.getText().toString().trim().equals("") || edtsize.getText().toString().trim().equals("") || edtpreferbestbeforedays.getText().toString().trim().equals("") || encodedImage.trim().equals("") )){

                    Toast.makeText(rootView.getContext(), "Please complete all details...",Toast.LENGTH_LONG).show();
                } else {

                    try {


                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        if (con == null) {

                            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
                        } else {
                            Date today = new Date();
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            String todaydate = date_format.format(today);



                            if(edtreorderpoint.getText().toString().trim().equals("")){
                                edtreorderpoint.setText("0");
                            }
                            if(edtquantitybulktotal.getText().toString().trim().equals("")){
                                edtquantitybulktotal.setText("1");
                            }
                            if(edtweb.getText().toString().trim().equals("") || edtweb.getText().toString()==null){
                                edtweb.setText("www.goingdots.com");
                            }

                            String ed_text = edtweb.getText().toString().trim();
                            if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                            {
                                edtweb.setText("www.goingdots.com");
                            }

                            if(chkisBulk.isChecked()){

                                String commands = "insert into [UserProduct] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantitybulktotal.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "','No','" + edtbarcode.getText().toString() + "','" + edtweb.getText().toString() + "')";
                                // encodedImage which is the Base64 String
                                Log.d("ReminderService In", "if########"+commands);
                                  PreparedStatement preStmt = con.prepareStatement(commands);
                                 preStmt.executeUpdate();
                            }else{

                                edtquantityperbulk.setText("0");
                                String commands = "insert into [UserProduct] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantity.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "','No','" + edtbarcode.getText().toString() + "','" + edtweb.getText().toString() + "')";
                                // encodedImage which is the Base64 String
                                Log.d("ReminderService In", "else######"+commands);
                                 PreparedStatement preStmt = con.prepareStatement(commands);
                                  preStmt.executeUpdate();
                            }



                            Toast.makeText(rootView.getContext(), "Product Added Successfully",Toast.LENGTH_LONG).show();



                            if (!activity.edthidenuserid.getText().toString().equals("")) {
                                Fragment frag =   new MyListFrag();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.mainFrame, frag).commit();
                            }else {
                                Fragment frag =   new HomePremiumFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.mainFrame, frag).commit();
                            }
                        }


                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage());
                    }


                }

            } catch (Exception ex) {
                   Toast.makeText(rootView.getContext(), "Take Product Photo",Toast.LENGTH_LONG).show();

            }
            /*
            //=========
            try {
                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                //String to = "jabun@ngobeniholdings.co.za";
                // String to = "sntshangase3@gmail.com";
                // m.setFrom("Info@ngobeniholdings.co.za");
                // String to = "SibusisoN@sqaloitsolutions.co.za";
                String to = "SibusisoN@sqaloitsolutions.co.za";
                String from = "info@goingdots.com";
                String subject = "Product added";
                String message = "Dear Sibusiso\nNew product added" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                String[] toArr = {to};
                m.setTo(toArr);
                m.setFrom(from);
                m.setSubject(subject);
                m.setBody(message);

                m.send();


            } catch (Exception e) {


            }
            //==========
            */
        }
    public void AddProShoppingList() {
        try {
            edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
            edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
            //---------
            if ((edtbestbeforedate.getText().toString().trim().equals("") || edtname.getText().toString().trim().equals("") || edtbestbeforeStatus.getText().toString().trim().equals("") || edtretailerid.getText().toString().trim().equals("")|| edtcategoryid.getText().toString().trim().equals("")|| edtprice.getText().toString().trim().equals("") || edtquantity.getText().toString().trim().equals("") || edtstorage.getText().toString().trim().equals("") || edtsize.getText().toString().trim().equals("") || edtpreferbestbeforedays.getText().toString().trim().equals("") || encodedImage.trim().equals("") )){

                Toast.makeText(rootView.getContext(), "Please complete all details...",Toast.LENGTH_LONG).show();
            } else {

                try {


                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {

                        Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
                    } else {
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);



                        if(edtreorderpoint.getText().toString().trim().equals("")){
                            edtreorderpoint.setText("0");
                        }
                        if(edtquantitybulktotal.getText().toString().trim().equals("")){
                            edtquantitybulktotal.setText("1");
                        }

                        if(chkisBulk.isChecked()){

                            String commands = "insert into [UserProductShoppingList] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website])" +
                                    "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                    edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                    edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantitybulktotal.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                    "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "','No','" + edtbarcode.getText().toString() + "','" + edtweb.getText().toString() + "')";
                            // encodedImage which is the Base64 String
                            Log.d("ReminderService In", "if########"+commands);
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }else{

                            edtquantityperbulk.setText("0");
                            String commands = "insert into [UserProductShoppingList] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website])" +
                                    "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                    edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                    edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantity.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                    "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "','No','" + edtbarcode.getText().toString() + "','" + edtweb.getText().toString() + "')";
                            // encodedImage which is the Base64 String
                            Log.d("ReminderService In", "else######"+commands);
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }



                        Toast.makeText(rootView.getContext(), "Product Added Successfully",Toast.LENGTH_LONG).show();



                        if (!activity.edthidenuserid.getText().toString().equals("")) {
                            Fragment frag =   new MyListFrag();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                        }else {
                            Fragment frag =   new HomePremiumFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                        }
                    }


                } catch (Exception ex) {
                    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                    Log.d("ReminderService In", ex.getMessage());
                }


            }

        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Take Product Photo",Toast.LENGTH_LONG).show();

        }
            /*
            //=========
            try {
                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                //String to = "jabun@ngobeniholdings.co.za";
                // String to = "sntshangase3@gmail.com";
                // m.setFrom("Info@ngobeniholdings.co.za");
                // String to = "SibusisoN@sqaloitsolutions.co.za";
                String to = "SibusisoN@sqaloitsolutions.co.za";
                String from = "info@goingdots.com";
                String subject = "Product added";
                String message = "Dear Sibusiso\nNew product added" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                String[] toArr = {to};
                m.setTo(toArr);
                m.setFrom(from);
                m.setSubject(subject);
                m.setBody(message);

                m.send();


            } catch (Exception e) {


            }
            //==========
            */
    }


    //======Sync Update


    public void UpdatePro () {

            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {

                    Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();

                } else {
                    if(edtreorderpoint.getText().toString().trim().equals("")){
                        edtreorderpoint.setText("0");
                    }
                    if(edtweb.getText().toString().trim().equals("")){
                        edtweb.setText("www.goingdots.com");
                    }

                    String ed_text = edtweb.getText().toString().trim();
                    if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                    {
                        edtweb.setText("www.goingdots.com");
                    }
                    edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
                    edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
                    String commands = "update [UserProduct] set [website]='" + edtweb.getText().toString() + "',[Barcode]='" + edtbarcode.getText().toString() + "',[bestbefore]='" + edtbestbeforedate.getText().toString() + "',[name]='" + edtname.getText().toString()+ "',[description]='" + edtdescription.getText().toString() + "',[bestbeforeStatus]='" +edtbestbeforeStatus.getText().toString() + "',[size]='" + edtsize.getText().toString() + "',[price]='" + Double.parseDouble(edtprice.getText().toString())  + "',[storage]='" +
                            edtstorage.getText().toString() + "',[preferbestbeforedays]='" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "',[quantity]='" +  Integer.parseInt(edtquantity.getText().toString()) + "',[quantityperbulk]='" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "',[reorderpoint]='" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "',[totatitemvalue]='" +  Double.parseDouble(edttotal.getText().toString()) +
                            "',[isscanned]='" + edtisscanned.getText().toString() + "',[image]='" + encodedImage + "',[categoryid]='" + Integer.parseInt(edtcategoryid.getText().toString()) + "',[retailerid]='" + Integer.parseInt(edtretailerid.getText().toString()) + "' where [id]='"+edtproductId.getText().toString()+"'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();
                    Toast.makeText(rootView.getContext(), "Product Updated Successfully",Toast.LENGTH_LONG).show();

                    if(Integer.valueOf(edtquantity.getText().toString())<= Integer.valueOf(edtreorderpoint.getText().toString()) && !edtreorderpoint.getText().toString().trim().equals("0") ){
                        //trigger notification
                        //**********
                        try{
                            String notificationbody="You have "+edtquantity.getText().toString()+" "+edtname.getText().toString()+" left, bought from "+selectedretailer+" retailer";
                            byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                            //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                            Intent notificationIntent = new Intent(rootView.getContext(),SplashFragment.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(contentIntent);
                            builder.setAutoCancel(false);
                            // Add as notification
                            builder.build();

                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                            Notification myNotication;

                            myNotication = builder.getNotification();
                            myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                            myNotication.defaults |= Notification.DEFAULT_SOUND;
                            myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                            manager.notify(0, myNotication);
                        } catch (Exception ex) {
                          //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                        }
                        //**********
                    }


                      /*  Fragment frag =   new HomePremiumFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();*/
                    if (!activity.edthidenuserid.getText().toString().equals("")) {
                        Fragment frag =   new MyListFrag();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }else {
                        Fragment frag =   new HomePremiumFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }


                }


            } catch (Exception ex) {
                               Toast.makeText(rootView.getContext(), "Invalid data input!",Toast.LENGTH_LONG).show();
               // Toast.makeText(rootView.getContext(), ex.getMessage(),Toast.LENGTH_LONG).show();
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
            String subject = "Item updated "+edtproductId.getText().toString();
            String message = "Dear Sibusiso\nItem Updated" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========
        }

    public void ConsumePro () {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
              /* TextView title=new TextView(rootView.getContext());
               // title.setPadding(10,10,10,10);
                title.setText("Donation");
                title.setGravity(Gravity.CENTER);
                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                builder.setCustomTitle("Donation");*/

                builder.setTitle("Consumption");
                builder.setMessage("Grocery Consumed:");
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.cosumption));
                final EditText input = new EditText(rootView.getContext());
                input.setGravity(Gravity.CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                input.setHint("Enter Quantity");
                input.setHintTextColor(Color.GRAY);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text_donate = input.getText().toString();
                        try{
                            edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
                            edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
                            if (Integer.parseInt(m_Text_donate) == Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate all
                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductConsumption] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantity.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (!edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have 0 "  + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                //Remove completely from [UserProduct]
                                String commands = "delete from [UserProduct]  where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();
                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }

                                Toast.makeText(rootView.getContext(), "Product Consumed", Toast.LENGTH_LONG).show();
                            } else if (Integer.parseInt(m_Text_donate) < Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate not all

                                int diff;
                                double total ;
                                double totaldonation;

                                if(chkisBulk.isChecked()){

                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total=diff* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                    totaldonation=Integer.parseInt(m_Text_donate)*(Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                }else{
                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total = diff * Double.parseDouble(edtprice.getText().toString());
                                    totaldonation=Integer.parseInt(m_Text_donate)*Double.parseDouble(edtprice.getText().toString());
                                }

                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductConsumption] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(m_Text_donate) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  totaldonation +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Update [UserProduct] qty and total
                                String commands = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (diff <= Integer.valueOf(edtreorderpoint.getText().toString()) && !edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have " + String.valueOf(diff) + " " + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                    //    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }
                                Toast.makeText(rootView.getContext(), "Partially Consumed", Toast.LENGTH_LONG).show();
                            } else {
                                // Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)
                                Toast.makeText(rootView.getContext(), "Can not consume this quantity(Consumption must be above 0, less or equal your available quantity)", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception ex){
                            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here2",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Invalid data input!", Toast.LENGTH_LONG).show();
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
            String subject = "Item consumed"+edtproductId.getText().toString();
            String message = "Dear Sibusiso\nItem Consumed" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========

    }

    public void DonatePro () {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
              /* TextView title=new TextView(rootView.getContext());
               // title.setPadding(10,10,10,10);
                title.setText("Donation");
                title.setGravity(Gravity.CENTER);
                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                builder.setCustomTitle("Donation");*/

                builder.setTitle("Donation");
                builder.setMessage("Thank you for Donating:");
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.yearlydonation));
                final EditText input = new EditText(rootView.getContext());
                input.setGravity(Gravity.CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                input.setHint("Enter Quantity");
                input.setHintTextColor(Color.GRAY);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text_donate = input.getText().toString();
                        try{
                            edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
                            edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
                            if (Integer.parseInt(m_Text_donate) == Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate all
                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                   String commandsd = "insert into [UserProductDonation] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantity.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (!edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have 0 "  + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                //Remove completely from [UserProduct]
                                String commands = "delete from [UserProduct]  where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();
                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }

                                Toast.makeText(rootView.getContext(), "Product Donated Successfully", Toast.LENGTH_LONG).show();
                            } else if (Integer.parseInt(m_Text_donate) < Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate not all

                                int diff;
                                double total ;
                             double totaldonation;

                                if(chkisBulk.isChecked()){

                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total=diff* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                    totaldonation=Integer.parseInt(m_Text_donate)*(Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                }else{
                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total = diff * Double.parseDouble(edtprice.getText().toString());
                                    totaldonation=Integer.parseInt(m_Text_donate)*Double.parseDouble(edtprice.getText().toString());
                                }

                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductDonation] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(m_Text_donate) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  totaldonation +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Update [UserProduct] qty and total
                                String commands = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (diff <= Integer.valueOf(edtreorderpoint.getText().toString()) && !edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have " + String.valueOf(diff) + " " + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                      //   Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }
                                Toast.makeText(rootView.getContext(), "Partially Donation Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                // Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)
                                Toast.makeText(rootView.getContext(), "Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception ex){
                           // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here2",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Invalid data input!", Toast.LENGTH_LONG).show();
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
            String subject = "Item donated"+edtproductId.getText().toString();
            String message = "Dear Sibusiso\nItem donated" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========

    }

    public void WastePro () {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {


                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
              /*  TextView title=new TextView(rootView.getContext());
               title.setPadding(10,10,10,10);
                title.setText("Waste");
                title.setGravity(Gravity.CENTER);
                title.setTextSize(25);
                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                 builder.setCustomTitle(title);*/
                builder.setTitle("Waste");
                builder.setMessage("Sorry for the Waste:");
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.waste));
                final EditText input = new EditText(rootView.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                input.setGravity(Gravity.CENTER);
                input.setHint("Enter Quantity");
                input.setHintTextColor(Color.GRAY);
                builder.setView(input);
                 builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        m_Text_donate = input.getText().toString();
                        try{
                            edtretailerid.setText(String.valueOf(spinnerretailer.getSelectedItemPosition()+1));
                            edtcategoryid.setText(String.valueOf(spinnercategory.getSelectedItemPosition()+1));
                            if (Integer.parseInt(m_Text_donate) == Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate all
                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductWaste] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(edtquantity.getText().toString()) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  Double.parseDouble(edttotal.getText().toString()) +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String

                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (!edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have 0 "  + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                //Remove completely from [UserProduct]
                                String commands = "delete from [UserProduct]  where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();
                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }

                                Toast.makeText(rootView.getContext(), "Product Waste Successfully", Toast.LENGTH_LONG).show();
                            } else if (Integer.parseInt(m_Text_donate) < Integer.parseInt(edtquantity.getText().toString())) {
                                //Donate not all
                                int diff;
                                double total ;
                                double totaldonation;

                                if(chkisBulk.isChecked()){

                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total=diff* (Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                    totaldonation=Integer.parseInt(m_Text_donate)*(Double.valueOf(edtprice.getText().toString())/Integer.valueOf(edtquantityperbulk.getText().toString()));
                                }else{
                                    diff = Integer.parseInt(edtquantity.getText().toString()) - Integer.parseInt(m_Text_donate);
                                    total = diff * Double.parseDouble(edtprice.getText().toString());
                                    totaldonation=Integer.parseInt(m_Text_donate)*Double.parseDouble(edtprice.getText().toString());
                                }

                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductWaste] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate.getText().toString() + "','" + edtname.getText().toString()+ "','" + edtdescription.getText().toString() + "','" +
                                        edtbestbeforeStatus.getText().toString() + "','" + edtsize.getText().toString() + "','" + Double.parseDouble(edtprice.getText().toString())  + "','" +
                                        edtstorage.getText().toString() + "','" + Integer.parseInt(edtpreferbestbeforedays.getText().toString()) + "','" +  Integer.parseInt(m_Text_donate) + "','" +  Integer.parseInt(edtquantityperbulk.getText().toString()) + "','" +  Integer.parseInt(edtreorderpoint.getText().toString()) + "','" +  totaldonation +
                                        "','" + edtisscanned.getText().toString() + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid.getText().toString()) + "','" + Integer.parseInt(edtcategoryid.getText().toString()) + "','" + Integer.parseInt(edtretailerid.getText().toString()) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Update [UserProduct] qty and total
                                String commands = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + edtproductId.getText().toString() + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (diff <= Integer.valueOf(edtreorderpoint.getText().toString()) && !edtreorderpoint.getText().toString().trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have " + String.valueOf(diff) + " " + edtname.getText().toString() + " left, bought from " + selectedretailer + " retailer";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                if (!activity.edthidenuserid.getText().toString().equals("")) {
                                    Fragment frag =   new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }else {
                                    Fragment frag =   new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }
                                Toast.makeText(rootView.getContext(), "Partially Waste Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                // Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)
                                Toast.makeText(rootView.getContext(), "Can not waste with this quantity(Waste must be above 0, less or equal your available quantity)", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();




            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Invalid data input!", Toast.LENGTH_LONG).show();
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
            String subject = "Item waste"+edtproductId.getText().toString();;
            String message = "Dear Sibusiso\nItem wasted" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========

    }




        public void DeletePro() {
            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {

                    Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
                } else {


                    String commands = "delete from [UserProduct]  where [id]='"+edtproductId.getText().toString()+"'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast.makeText(rootView.getContext(), "Product Removed Successfully",Toast.LENGTH_LONG).show();
                    if (!activity.edthidenuserid.getText().toString().equals("")) {
                        Fragment frag =   new MyListFrag();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }else {
                        Fragment frag =   new HomePremiumFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }
                              //  con.close();
                }


            } catch (Exception ex) {
                             Toast.makeText(rootView.getContext(), "Invalid data input!",Toast.LENGTH_LONG).show();
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
                String subject = "Item removed"+edtproductId.getText().toString();
                String message = "Dear Sibusiso\nItem deleted" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                String[] toArr = {to};
                m.setTo(toArr);
                m.setFrom(from);
                m.setSubject(subject);
                m.setBody(message);

                m.send();


            } catch (Exception e) {


            }
            //==========


    }







}


