package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
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
public class NGODetailsFrag extends Fragment implements AdapterView.OnItemSelectedListener  {
    public int counter=1;
    int ngouserid;
    View rootView;
    ViewPager viewPager;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bitmap decodebitmap1;

    ImageView prev,imageView,next, b2,b3,b4,map;
    TextView txtname,txtstorelocation,txtdistance,txtabout,txtdishnumber,txtregno;
    int catergoryclick=0;
    int totaldish=0;
    ArrayList <String> catergory= new ArrayList<String>();
    String chefmobileno = "";
    String chefwebsite = "";
    Bundle bundle;
    ArrayAdapter adapter1;
    Spinner spinnercategory;

    String latlon = "";
    String fulladdress_search = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ngo_details, container, false);

        prev = (ImageView) rootView.findViewById(R.id.previous);
        imageView = (ImageView) rootView.findViewById(R.id.chefprofileImage);
        next = (ImageView) rootView.findViewById(R.id.next);

        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        b4 = (ImageView) rootView.findViewById(R.id.b4);
        map = (ImageView) rootView.findViewById(R.id.map);
        txtname = (TextView) rootView.findViewById(R.id.txtname);
        txtstorelocation = (TextView) rootView.findViewById(R.id.txtstorelocation);
        txtdistance = (TextView) rootView.findViewById(R.id.txtdistance);
        txtdishnumber = (TextView) rootView.findViewById(R.id.txtdishnumber);
        txtregno= (TextView) rootView.findViewById(R.id.txtregno);
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
                if (!bundle.getString("userid").toString().equals("")){
                    ngouserid = Integer.parseInt(bundle.getString("userid"));

                    UpdateProfile updatePro = new UpdateProfile();
                    updatePro.execute("");

                }

            }
        } catch (Exception ex) {

        }

        FillCategoryData();
        spinnercategory.setOnItemSelectedListener(this);





        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catergoryclick = 1;
                spinnercategory.performClick();

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ngouserid==Integer.parseInt(activity.edthidenuserid.getText().toString())){
                    Fragment fragment = new NGOAllDonationListFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    bundle.putString("donationpinter", "donationpinter");
                    bundle.putInt("ngouserid", ngouserid);
                    bundle.putString("ProdIDingredients", "ProdIDingredients");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }else{
                    Toast.makeText(rootView.getContext(), bundle.getString("name")+" is not your profile!!!", Toast.LENGTH_LONG).show();
                }
               /* Fragment fragment = new HomePremiumFragment();
                Bundle bundle = new Bundle();
                bundle.putString("couser", "couser");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyListFrag();
                Bundle bundle = new Bundle();
                bundle.putString("couser", "couser");
                bundle.putString("donationpinter", "donationpinter");
                bundle.putInt("ngouserid", ngouserid);
                bundle.putString("ProdIDingredients", "ProdIDingredients");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



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
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isGoogleMapsInstalled())
                {
                    try {
                        if (!latlon.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlon);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else if (!fulladdress_search.equals("")) {
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
    public void FillCategoryData() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                String query = "select * from [Category]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

               /* category.add("Food");
                category.add("Status");
                category.add("Retailer");*/
                while (rs.next()) {
                    if(bundle.getString("categoryacceptable").toLowerCase().contains(rs.getString("productCategory").toLowerCase())){
                        category.add(rs.getString("productCategory"));
                   }
                    Log.d("ReminderService In", bundle.getString("categoryacceptable").toLowerCase()+"  "+rs.getString("productCategory").toLowerCase());
                }
                adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnercategory.setAdapter(adapter1);


            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
       // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

        //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);


    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    public Bitmap setProfile()  {



        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {



                //Fill according to Ngo Table
                String query2 = "select * from [Ngo] where [userid]=" + ngouserid;
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                if(rs2.getRow()!=0){


                    if (rs2.getString("imagelogo") != null) {
                        byte[] decodeString = Base64.decode(rs2.getString("imagelogo"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        decodebitmap1=decodebitmap;

                    } else {
                        decodebitmap1=BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profilephoto);
                    }


                    totaldish=0;

                    //Get New Donations Number
                    String query22 = "select COUNT (DISTINCT [ngouserid]) from [NgoProductDonation] where [ngouserid]=" + activity.edthidenuserid.getText().toString();
                    PreparedStatement ps22 = con.prepareStatement(query22);
                    ResultSet rs22 = ps22.executeQuery();
                    while (rs22.next()) {
                        totaldish=totaldish+1;
                    }
                    txtdishnumber.setText(String.valueOf(totaldish));



                    fulladdress_search = rs2.getString("address").toString().replace(" ", "+");//Making use of address if lonlat fails
                    txtname.setText(rs2.getString("name"));
                    txtregno.setText("NGO Reg: "+rs2.getString("regno"));
                    txtabout.setText(rs2.getString("about"));
                    chefmobileno = rs2.getString("telephone").toString();
                    chefwebsite=rs2.getString("website").toString();

                    String lat=rs2.getString("lonlat").toString().substring(10,rs2.getString("lonlat").toString().indexOf(','));
                    String lon=rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',')+1,rs2.getString("lonlat").toString().indexOf(')'));
                    latlon = lat + "," + lon;
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

                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), setProfile());
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                b2.setImageDrawable(roundedBitmapDrawable);
                //  b1.setImageDrawable(roundedBitmapDrawable);

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
                z = "Check your network connection!!";

            }

            return z;
        }
    }



}


