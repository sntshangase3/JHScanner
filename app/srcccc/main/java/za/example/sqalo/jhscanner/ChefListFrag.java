package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefListFrag extends Fragment implements AdapterView.OnItemSelectedListener  {
    public int counter=1;
    int useridchef;
    View rootView;
    ViewPager viewPager;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bitmap decodebitmap1;
    Spinner spinnercategory;
    ImageView prev,imageView,next, b2,b3,b4,service,btn_signupi;
    TextView txtname,txtstorelocation,txtdistance,txtabout,txtdishnumber,btn_signup;
    int catergoryclick=0;
    int totaldish=0;
    ArrayList <String> catergory= new ArrayList<String>();
    String chefmobileno = "";
    String chefwebsite = "";
    Bundle bundle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chef_list, container, false);

        prev = (ImageView) rootView.findViewById(R.id.previous);
        imageView = (ImageView) rootView.findViewById(R.id.chefprofileImage);
        next = (ImageView) rootView.findViewById(R.id.next);

        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        b4 = (ImageView) rootView.findViewById(R.id.b4);
        service = (ImageView) rootView.findViewById(R.id.service);
        btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signupi = (ImageView) rootView.findViewById(R.id.btn_signupi);
        txtname = (TextView) rootView.findViewById(R.id.txtname);
        txtstorelocation = (TextView) rootView.findViewById(R.id.txtstorelocation);
        txtdistance = (TextView) rootView.findViewById(R.id.txtdistance);
        txtdishnumber = (TextView) rootView.findViewById(R.id.txtdishnumber);

        txtabout = (TextView) rootView.findViewById(R.id.txtabout);
        spinnercategory = (Spinner) rootView. findViewById(R.id.spinnercategory);
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        //startTimer();

        bundle = this.getArguments();

            try {
            if (bundle != null) {
                if (!bundle.getString("useridchef").toString().equals("")){
                    useridchef = Integer.parseInt(bundle.getString("useridchef"));
                    UpdateProfilee updatePro = new UpdateProfilee();
                    updatePro.execute("");
                    Log.d("ReminderService In", "if");
                }

            }
        } catch (Exception ex) {

        }

        try {
            if (bundle == null) {
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                Log.d("ReminderService In", "budle null ");
            }
        } catch (Exception ex) {

        }



        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                return false;
            }
        });

        prev.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                return false;
            }
        });
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new ChefServicesFrag();
                Bundle bundle = new Bundle();
                bundle.putString("useridchef", String.valueOf(useridchef));
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(rootView.getContext(), "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Fragment frag = new ChefDishesFrag();
                Bundle bundle = new Bundle();
                bundle.putString("useridchef", String.valueOf(useridchef));
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:" + chefmobileno));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Chef Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(chefwebsite));
                startActivity(intent);

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment mainFragment = new RegisterChefPartnershipFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, mainFragment)
                        .commit();
            }
        });
        btn_signupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment mainFragment = new RegisterChefPartnershipFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, mainFragment)
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
        //Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();
        catergory.add(spinnercategory.getSelectedItem().toString());
        //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);


    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    public Bitmap setProfile(int position)  {



        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {



                String query = "select * from [AppUser]  where [id]="+position+" and [id] in(select [userid] from [Chef])";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if(rs.getRow()!=0){


                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        decodebitmap1=decodebitmap;

                    } else {
                        decodebitmap1=BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profilephoto);
                    }

                      useridchef=Integer.parseInt(rs.getString("id"));
                   /* if(useridchef==0){
                        useridchef=Integer.parseInt(rs.getString("id"));
                    }*/
                    // Set Total Dish No
                    totaldish=0;
                    String query22 = "select * from [Dish] where [userid]=" + useridchef;
                    PreparedStatement ps22 = con.prepareStatement(query22);
                    ResultSet rs22 = ps22.executeQuery();
                    while (rs22.next()) {
                        totaldish=totaldish+1;
                    }
                    txtdishnumber.setText(String.valueOf(totaldish));

                    //Fill according to Chef Table
                    String query2 = "select * from [Chef] where [userid]=" + useridchef;
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    txtname.setText(rs2.getString("name"));
                    txtabout.setText(rs2.getString("about"));
                    chefmobileno = rs2.getString("telephone").toString();
                    chefwebsite=rs2.getString("website").toString();

                    String lat=rs2.getString("lonlat").toString().substring(10,rs2.getString("lonlat").toString().indexOf(','));
                    String lon=rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',')+1,rs2.getString("lonlat").toString().indexOf(')'));
                    double latdouble=Double.parseDouble(lat);
                    double londouble=Double.parseDouble(lon);

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
                        String fulladdress= address;
                        // Get reults

                        double distance;
                        Location locationA = new Location("");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("");
                        locationB.setLatitude(latdouble);
                        locationB.setLongitude(londouble);

                        // distance = locationA.distanceTo(locationB);   // in meters
                        distance = locationA.distanceTo(locationB)/1000;   // in km

                        String  addressdisplayfinal="";
                        String  addressdisplay = rs2.getString("address").toString();
                        addressdisplayfinal=addressdisplay.substring(0,addressdisplay.indexOf(','));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));

                        txtstorelocation.setText(addressdisplayfinal);
                        txtdistance.setText((double)Math.round(distance)+"km Away");

                    }


                }else{

                    counter=1;
                    String query1 = "select * from [AppUser] where [id]in(select [userid] from [Chef])";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    rs1.next();

                    byte[] decodeString = Base64.decode(rs1.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    decodebitmap1=decodebitmap;
                     useridchef=Integer.parseInt(rs1.getString("id"));
                   /* if(useridchef==0){
                        useridchef=Integer.parseInt(rs.getString("id"));
                    }*/
                    // Set Total Dish No
                    totaldish=0;
                    String query22 = "select * from [Dish] where [userid]=" + useridchef;
                    PreparedStatement ps22 = con.prepareStatement(query22);
                    ResultSet rs22 = ps22.executeQuery();
                    while (rs22.next()) {
                        totaldish=totaldish+1;
                    }
                    txtdishnumber.setText(String.valueOf(totaldish));
                    //Fill according to Chef Table
                    String query2 = "select * from [Chef] where [userid]=" + useridchef;
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    txtname.setText(rs2.getString("name"));
                    txtabout.setText(rs2.getString("about"));
                    chefmobileno = rs2.getString("telephone").toString();
                    chefwebsite=rs2.getString("website").toString();

                    String lat=rs2.getString("lonlat").toString().substring(10,rs2.getString("lonlat").toString().indexOf(','));
                    String lon=rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',')+1,rs2.getString("lonlat").toString().indexOf(')'));
                    double latdouble=Double.parseDouble(lat);
                    double londouble=Double.parseDouble(lon);

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
                        String fulladdress= address;
                        // Get reults


                        double distance;
                        Location locationA = new Location("");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("");
                        locationB.setLatitude(latdouble);
                        locationB.setLongitude(londouble);

                        // distance = locationA.distanceTo(locationB);   // in meters
                        distance = locationA.distanceTo(locationB)/1000;   // in km

                        String  addressdisplayfinal="";
                        String  addressdisplay = rs2.getString("address").toString();
                        addressdisplayfinal=addressdisplay.substring(0,addressdisplay.indexOf(','));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));

                        txtstorelocation.setText(addressdisplayfinal);
                        txtdistance.setText((double)Math.round(distance)+"km Away");

                    }

                }

            }
           // con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
        return decodebitmap1;
    }

    public Bitmap setProfilee()  {



        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {



                String query = "select * from [AppUser]  where [id]="+useridchef;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if(rs.getRow()!=0){


                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        decodebitmap1=decodebitmap;

                    } else {
                        decodebitmap1=BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profilephoto);
                    }


                    // Set Total Dish No
                    totaldish=0;
                    String query22 = "select * from [Dish] where [userid]=" + useridchef;
                    PreparedStatement ps22 = con.prepareStatement(query22);
                    ResultSet rs22 = ps22.executeQuery();
                    while (rs22.next()) {
                        totaldish=totaldish+1;
                    }
                    txtdishnumber.setText(String.valueOf(totaldish));

                    //Fill according to Chef Table
                    String query2 = "select * from [Chef] where [userid]=" + useridchef;
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    txtname.setText(rs2.getString("name"));
                    txtabout.setText(rs2.getString("about"));
                    chefmobileno = rs2.getString("telephone").toString();
                    chefwebsite=rs2.getString("website").toString();

                    String lat=rs2.getString("lonlat").toString().substring(10,rs2.getString("lonlat").toString().indexOf(','));
                    String lon=rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',')+1,rs2.getString("lonlat").toString().indexOf(')'));
                    double latdouble=Double.parseDouble(lat);
                    double londouble=Double.parseDouble(lon);

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
                        String fulladdress= address;
                        // Get reults

                        double distance;
                        Location locationA = new Location("");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("");
                        locationB.setLatitude(latdouble);
                        locationB.setLongitude(londouble);

                        // distance = locationA.distanceTo(locationB);   // in meters
                        distance = locationA.distanceTo(locationB)/1000;   // in km

                        String  addressdisplayfinal="";
                        String  addressdisplay = rs2.getString("address").toString();
                        addressdisplayfinal=addressdisplay.substring(0,addressdisplay.indexOf(','));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));

                        txtstorelocation.setText(addressdisplayfinal);
                        txtdistance.setText((double)Math.round(distance)+"km Away");

                    }


                }else{

                    counter=1;
                    String query1 = "select * from [AppUser] where [id]in(select [userid] from [Chef])";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    rs1.next();

                    byte[] decodeString = Base64.decode(rs1.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    decodebitmap1=decodebitmap;
                   // useridchef=Integer.parseInt(rs1.getString("id"));

                    // Set Total Dish No
                    totaldish=0;
                    String query22 = "select * from [Dish] where [userid]=" + useridchef;
                    PreparedStatement ps22 = con.prepareStatement(query22);
                    ResultSet rs22 = ps22.executeQuery();
                    while (rs22.next()) {
                        totaldish=totaldish+1;
                    }
                    txtdishnumber.setText(String.valueOf(totaldish));
                    //Fill according to Chef Table
                    String query2 = "select * from [Chef] where [userid]=" + useridchef;
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    txtname.setText(rs2.getString("name"));
                    txtabout.setText(rs2.getString("about"));
                    chefmobileno = rs2.getString("telephone").toString();
                    chefwebsite=rs2.getString("website").toString();

                    String lat=rs2.getString("lonlat").toString().substring(10,rs2.getString("lonlat").toString().indexOf(','));
                    String lon=rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',')+1,rs2.getString("lonlat").toString().indexOf(')'));
                    double latdouble=Double.parseDouble(lat);
                    double londouble=Double.parseDouble(lon);

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
                        String fulladdress= address;
                        // Get reults


                        double distance;
                        Location locationA = new Location("");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("");
                        locationB.setLatitude(latdouble);
                        locationB.setLongitude(londouble);

                        // distance = locationA.distanceTo(locationB);   // in meters
                        distance = locationA.distanceTo(locationB)/1000;   // in km

                        String  addressdisplayfinal="";
                        String  addressdisplay = rs2.getString("address").toString();
                        addressdisplayfinal=addressdisplay.substring(0,addressdisplay.indexOf(','));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));
                        addressdisplay=addressdisplay.substring(addressdisplay.indexOf(',')+1);
                        addressdisplayfinal=addressdisplayfinal+(addressdisplay.substring(0,addressdisplay.indexOf(',')));

                        txtstorelocation.setText(addressdisplayfinal);
                        txtdistance.setText((double)Math.round(distance)+"km Away");

                    }

                }

            }
            // con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
        return decodebitmap1;
    }

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;
        // Integer.parseInt(edtappuserId.getText().toString());


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), setProfile(counter));
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
                //  b1.setImageDrawable(roundedBitmapDrawable);
                counter=counter+1;
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {
                    z = "Check your network connection!!";

                } else {
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;

                //  z = "Check your network connection!!";
                z= ex.getMessage().toString();
                Log.d("ReminderService In", ex.getMessage().toString());

            }

            return z;
        }
    }
    class UpdateProfilee extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;
        // Integer.parseInt(edtappuserId.getText().toString());


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), setProfilee());
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
                //  b1.setImageDrawable(roundedBitmapDrawable);
               // counter=counter+1;
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {
                    z = "Check your network connection!!";

                } else {
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                //  z = "Check your network connection!!";
                z= ex.getMessage().toString();
                Log.d("ReminderService In", ex.getMessage().toString());

            }

            return z;
        }
    }


}


