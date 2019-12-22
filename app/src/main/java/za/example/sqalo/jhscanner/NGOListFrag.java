package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class NGOListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    ListView lstgross;
    String currentid;
    ImageView productimage,retailerimage;
    TextView txtTheKitchen,txtselect;
double total=0;

    ArrayList <Bitmap> logoimage= new ArrayList<Bitmap>();
    ArrayList <String> name= new ArrayList<String>();
    ArrayList <String> distance= new ArrayList<String>();


    Bundle bundles = new Bundle();

    ArrayAdapter adapter1;


    ImageButton donestatus;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
String  useridorder;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.ngo_list, container, false);


      lstgross = (ListView)rootView.findViewById(R.id.lstgross);
        txtTheKitchen = (TextView)rootView.  findViewById(R.id.txtTheKitchen);
        txtselect = (TextView)rootView.  findViewById(R.id.txtselect);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



        bundle = this.getArguments();

        FillDataOrder();

        try{
            if (!activity.edthidenuserid.getText().toString().equals("")) {

               // donestatus.setVisibility(View.GONE);

            }

        }catch (Exception ex){

        }
        donestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {

                    // donestatus.setVisibility(View.GONE);
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                }else {
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("assist", "assistant");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }

            }
        });


          return rootView;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

       // spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

          //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);


    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }


    public void FillDataOrder() {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Con null!!",Toast.LENGTH_LONG).show();
               // Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {

                    // Co-user login
                    String  query = "select * from [Ngo] order by [id] asc";
                    name.clear();
                    distance.clear();
                    logoimage.clear();



                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {


                            txtTheKitchen.setText("Donation Pointer - Charity Partners");
                           // txtselect.setText("For More Details, Tap Dish Image"+dayOfTheWeek);
                        txtselect.setText("For More Details, Tap Charity");
                        String lat=rs.getString("lonlat").toString().substring(10,rs.getString("lonlat").toString().indexOf(','));
                        String lon=rs.getString("lonlat").toString().substring(rs.getString("lonlat").toString().indexOf(',')+1,rs.getString("lonlat").toString().indexOf(')'));
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

                            double distance1;
                            Location locationA = new Location("");
                            locationA.setLatitude(latitude);
                            locationA.setLongitude(longitude);

                            Location locationB = new Location("");
                            locationB.setLatitude(latdouble);
                            locationB.setLongitude(londouble);

                            // distance = locationA.distanceTo(locationB);   // in meters
                            distance1 = locationA.distanceTo(locationB)/1000;   // in km

                           // txtdistance.setText();
                            distance.add((double)Math.round(distance1)+"km Away");
                        }



                        name.add(rs.getString("name").toString());

                        byte[] decodeString1 = Base64.decode(rs.getString("imagelogo"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1,0, decodeString1.length);
                        logoimage.add(decodebitmap1);




                    }

                    NGOListAdapter adapter=new NGOListAdapter(this.getActivity(), logoimage, name, distance);
                    lstgross.setAdapter(adapter);
                    lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub


                            try {


                                String selectedname = name.get(position);


                                String query = "select * from [Ngo] where [name]='"+selectedname+ "'";
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {

                                     Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                    bundles.putString("id", rs.getString("id").toString());
                                    bundles.putString("regno", rs.getString("regno").toString());
                                    bundles.putString("name", rs.getString("name").toString());
                                    bundles.putString("address", rs.getString("address").toString());
                                    bundles.putString("lonlat", rs.getString("lonlat").toString());
                                    bundles.putString("telephone", rs.getString("telephone").toString());
                                    bundles.putString("website", rs.getString("website").toString());
                                    bundles.putString("contactperson", rs.getString("contactperson").toString());
                                    bundles.putString("about", rs.getString("about").toString());
                                    bundles.putString("categoryacceptable", rs.getString("categoryacceptable").toString());
                                    bundles.putString("imageprofile", rs.getString("imageprofile").toString());
                                    bundles.putString("imagelogo", rs.getString("imagelogo").toString());
                                    bundles.putString("userid", rs.getString("userid").toString());


                                    NGODetailsFrag fragment = new NGODetailsFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();



                                }

                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            }
                        }
                    });




            }
        } catch (Exception ex) {
              Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }






}


