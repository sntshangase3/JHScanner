package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefServicesFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;

    ListView lstdish;
    String currentid;
    ImageView productimage;
    TextView txtTheKitchen, txtselect;
    double total = 0;

    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();

    Bundle bundles = new Bundle();


    Button request_date;
    ImageButton request;
    Spinner spinnerservice;

    byte[] byteArray;
    String encodedImage;

    ArrayList<String> services = new ArrayList<String>();
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    LinearLayout layoutdishlist;

    int PLACE_PICKER_REQUEST = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String API_KEY = "AIzaSyA1RX5FgK6qKIuHOkQP6D40uEEaL5eLi0w";
    String lonlat="";
int useridchef;

    ArrayList <String> schedule= new ArrayList<String>();
    ArrayList <String> scheduleqty= new ArrayList<String>();
    String m_Text_donate = "";
    String m_Text_delivery="";
    private View currentSelectedView;
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;
    private Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chef_service, container, false);

        layoutdishlist = (LinearLayout) rootView.findViewById(R.id.dishlist);
        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        spinnerservice = (Spinner)rootView. findViewById(R.id.spinnerservice);
        radioSexGroup=(RadioGroup)rootView.findViewById(R.id.radioGroup);

        request_date = (Button) rootView.findViewById(R.id.request_date);
        request = (ImageButton) rootView.findViewById(R.id.request);

        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 8);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();
        try{
            if(bundle != null){
                if(!bundle.getString("useridchef").equals("")){
                    useridchef=Integer.parseInt( bundle.getString("useridchef")) ;
                    FillDataUser();
                    FillDataDish();
                }


            }
        }catch (Exception ex){

        }
       // userid=fragment_mapy.edthidenuserid.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        request_date.setText(dateForButton);

        radioSexGroup.clearCheck();
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioSexButton=(RadioButton)rootView.findViewById(checkedId);
                if(radioSexButton.getText().toString().trim().equals("Pick-Up")){

                }else if(radioSexButton.getText().toString().trim().equals("Deliver")){
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        builder.setTitle("Enter Delivery Address");
                        builder.setIcon(rootView.getResources().getDrawable(R.drawable.profil));
                        final EditText input = new EditText(rootView.getContext());
                        input.setGravity(Gravity.CENTER);
                        input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                        input.setHint("e.g. 27 Limoniet Rd,Croydon,Kempton Park,1619");
                        input.setHintTextColor(Color.GRAY);
                        builder.setView(input);

                        builder.setMessage("Delivery on this Address?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text_delivery = input.getText().toString();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();



                    } catch (Exception ex) {
                       // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((spinnerservice.getSelectedItem().toString().trim().equals("Select Services")) || ((radioSexButton.getText().toString().trim().equals("")))) {
                    if ((spinnerservice.getSelectedItem().toString().trim().equals("Select Services"))) {
                        spinnerservice.setBackground(errorbg);
                    } else {
                        spinnerservice.setBackground(bg);
                    }

                    if ((!(radioSexButton.getText().toString().trim().equals("")))) {
                        radioSexGroup.setBackground(errorbg);
                    } else {
                        radioSexGroup.setBackground(bg);

                    }
                } else {
                    //selected Correctly
                    //Insert into  notifications



                    AddDish updateDish = new AddDish();
                    updateDish.execute("");
                }

            }
        });
        request_date.setOnClickListener(new View.OnClickListener() {
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


                        request_date.setText(date_format.format(bestbefore));


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        return rootView;
    }





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        // spinnerservice.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

        //FillDataOrderBy(spinnerservice.getSelectedItemPosition()+1);



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    public void FillDataUser() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                //AppUser already Chef
                String query = "select * from [Chef] where [userid]=" + useridchef;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (rs.getRow() != 0) {

                    services.add("Select Services");
                    if (rs.getString("services").contains("Cook Meal Orders")) {
                      services.add("Meal Kit Orders");
                    }
                    if (rs.getString("services").contains("Meal Kit Orders")) {
                        services.add("Meal Kit Orders");
                    }
                    if (rs.getString("services").contains("Catering Services")) {
                        services.add("Catering Services");
                    }
                    if (rs.getString("services").contains("Recipe Designs")) {
                             services.add("Recipe Designs");
                    }
                    if (rs.getString("services").contains("Private Chef Services")) {
                         services.add("Private Chef Services");
                    }
                    ArrayAdapter adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, services);
                    adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinnerservice.setAdapter(adapter1);



                }


            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here", Toast.LENGTH_LONG).show();
        }

    }

    public void FillDataDish() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String query = "select * from [Dish] where [userid]='" + useridchef + "' order by [id] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                name.clear();
                keyingredient.clear();
                recipe.clear();
                proimage.clear();

                while (rs.next()) {

                    name.add(rs.getString("name").toString());
                    keyingredient.add(rs.getString("keyingredient").toString());
                    recipe.add(rs.getString("recipe").toString());

                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);


                }

                DishListAdapter adapter = new DishListAdapter(this.getActivity(), proimage, name, keyingredient, recipe);
                lstdish.setAdapter(adapter);
                lstdish.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {
                            final String selectedname = name.get(position);
                            final String selectedkeyingredient = keyingredient.get(position);
                            final String selectedrecipe = recipe.get(position);
                            String query1 = "select top 1 * from [Dish] where [userid]='" + useridchef + "' and [name]='" + selectedname + "'  and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query1);
                            final ResultSet rs = ps.executeQuery();
                            rs.next();
                            final String dishId=rs.getString("id").toString();
                            if(schedule.contains(dishId)){
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    builder.setTitle("Dish exist");
                                    builder.setMessage("Remove dish from request?");
                                    builder.setIcon(rootView.getResources().getDrawable(R.drawable.removedish));


                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // m_Text_donate = input.getText().toString();
                                            schedule.remove(dishId);
                                            scheduleqty.remove(m_Text_donate);


                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();





                                } catch (Exception ex) {
                                   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                }

                            }else {

                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    builder.setTitle("New Dish");
                                    builder.setIcon(rootView.getResources().getDrawable(R.drawable.adddish));
                                    final EditText input = new EditText(rootView.getContext());
                                    input.setGravity(Gravity.CENTER);
                                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                    input.setHint("Enter Quantity");
                                    input.setHintTextColor(Color.GRAY);
                                    builder.setView(input);

                                    builder.setMessage("Add dish to request?");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            m_Text_donate = input.getText().toString();
                                            schedule.add(dishId);
                                            scheduleqty.add(m_Text_donate);
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();



                                } catch (Exception ex) {
                                   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                }
                            }
                            if (currentSelectedView != null && currentSelectedView != view ) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);


                        } catch (Exception ex) {
                           // Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


        } catch (Exception ex) {
           // Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here0", Toast.LENGTH_LONG).show();
        }
//===========

    }
    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor( R.color.colorPrimary));

    }
    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }

    public class AddDish extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;

        int userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
        String schedules = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    //String to = "jabun@ngobeniholdings.co.za";
                    // String to = "sntshangase3@gmail.com";
                    // m.setFrom("Info@ngobeniholdings.co.za");
                    // String to = "SibusisoN@sqaloitsolutions.co.za";
                    String to = "SibusisoN@sqaloitsolutions.co.za";
                    String from = "info@goingdots.com";
                    String subject = "New order";
                    String message = "Dear Sibusiso\nNew order created" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);

                    m.send();


                } catch (Exception e) {


                }
                //==========
                Fragment frag = new MyOrdersOwnListFrag();
                Bundle bundle = new Bundle();
                bundle.putString("couser", "couser");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            } else {
                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();



            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                for (String temp : schedule) {
                    schedules = schedules + temp;
                }
                if (schedules.trim().equals("")||m_Text_delivery.equals("") ) {
                    z = "Please fill in required details...";
                } else {

                    try {
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        if (con == null) {
                            z = "Check your network connection!!";
                        } else {

                            int count=0;
                            for (String temp : schedule) {


                                String query = "select * from [Dish] where [id]='" + temp + "'" ;
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();
                                rs.next();

                                String quantity=scheduleqty.get(count).toString();
                                String namedish = rs.getString("name").toString();
                                String keyingredient = rs.getString("keyingredient").toString();
                                String recipe = rs.getString("recipe").toString();
                                String image=rs.getString("image").toString();

                                String service=spinnerservice.getSelectedItem().toString();
                                String deliverytype=radioSexButton.getText().toString();
                                String orderdate=request_date.getText().toString();
                                String issent="No";
                                String isread="No";

                               String address = m_Text_delivery+", South Africa";
                                LatLon(address);
                                if(!lonlat.equals("")&&m_Text_delivery.length()>6){
                                    String query1 = "insert into [DishOrder]([quantity],[name],[image],[keyingredient],[recipe],[service],[deliverytype],[deliveryaddress],[lonlat],[orderdate],[issent],[isread],[useridchef],[userid]) " +
                                            "values ('" + quantity + "','" + namedish + "','" + image + "','" + keyingredient + "','" + recipe + "','" + service + "','" + deliverytype + "','" + m_Text_delivery + "','" + lonlat + "','" + orderdate + "','" + issent + "','" + isread + "','" + useridchef + "','" + activity.edthidenuserid.getText().toString() + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query1);
                                    preparedStatement.executeUpdate();
                                    count=count+1;
                                    z = "Dish Order Requested Successfully";
                                    isSuccess = true;
                                }else {
                                   /* String query2 = "select * from [AppUser]  where [id]='" + activity.edthidenuserid.getText().toString()+"'";
                                    PreparedStatement ps1 = con.prepareStatement(query2);
                                    ResultSet rs1 = ps1.executeQuery();
                                    rs1.next();
                                    String address1 = rs1.getString("location").trim();
                                    LatLon(address1);

                                    String query1 = "insert into [DishOrder]([quantity],[name],[image],[keyingredient],[recipe],[service],[deliverytype],[deliveryaddress],[lonlat],[orderdate],[issent],[isread],[useridchef],[userid]) " +
                                            "values ('" + quantity + "','" + namedish + "','" + image + "','" + keyingredient + "','" + recipe + "','" + service + "','" + deliverytype + "','" + m_Text_delivery + "','" + lonlat + "','" + orderdate + "','" + issent + "','" + isread + "','" + useridchef + "','" + activity.edthidenuserid.getText().toString() + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query1);
                                    preparedStatement.executeUpdate();
                                    count=count+1;*/
                                    z = "Invalid Address!!!";
                                   // isSuccess = true;
                                }




                            }

                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                         z = "Check your network connection!!";
                        z = ex.getMessage();
                        Log.d("ReminderService In", "An error occurred1: "+ex.getMessage());
                    }
                }

            } catch (Exception ex) {
                z = "Check your network connection!!";
                Log.d("ReminderService In", "An error occurred2: "+ex.getMessage());
//                Toast.makeText(rootView.getContext(), "Upload Dish Image", Toast.LENGTH_LONG).show();
            }

            return z;
        }
    }

    public void LatLon(String address){
        try{
            Geocoder geocoder;
            geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
            double Lat = geocoder.getFromLocationName(address, 1).get(0).getLatitude();
            double Lon = geocoder.getFromLocationName(address, 1).get(0).getLongitude();
            lonlat="lat/lng: ("+String.valueOf(Lat)+","+String.valueOf(Lon)+")";

        }catch (Exception e){
            Toast.makeText(rootView.getContext(), "Slow network connection,wait...!!", Toast.LENGTH_LONG).show();
        }

    }


}


