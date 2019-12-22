package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class NGOAllDonationListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    ListView lstgross;
    String currentid;
    ImageView productimage,retailerimage;

double total=0;

    ArrayList <Bitmap> logoimage= new ArrayList<Bitmap>();
    ArrayList <String> name= new ArrayList<String>();
    ArrayList <String> distance= new ArrayList<String>();




    ImageButton donestatus,btn_status;

    String filterstatusselect,filtershoppingselect,filterropselect;
    String TotalDescribtion="";


    int statusclick=0;
    String m_Text_donate = "";
    Bundle bundles = new Bundle();



    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
String  useridorder;
    int totalnoticeonceoff = 0;
    TextView txttotalnoticeonceoff;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.ngo_all_donations, container, false);


      lstgross = (ListView)rootView.findViewById(R.id.lstgross);

        txttotalnoticeonceoff = (TextView) rootView.findViewById(R.id.txtnotice1);
        btn_status = (ImageButton) rootView. findViewById(R.id.filtercategory);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



        bundle = this.getArguments();

        FillDataOrderByStatus("All");


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
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statusclick==0){
                    filterstatusselect="Accepted";
                    statusclick=1;
                }else if(statusclick==1){
                    filterstatusselect="Pending";
                    statusclick=2;
                }else if(statusclick==2){
                    filterstatusselect="Declined";
                    statusclick=3;
                }
                else{
                    filterstatusselect="All";
                    statusclick=0;
                }

                FillDataOrderByStatus(filterstatusselect);
                Toast.makeText(rootView.getContext(), filterstatusselect+" items." , Toast.LENGTH_SHORT).show();
            }
        });

          return rootView;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

      // spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

          //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);
       // FillDataOrderByStatus(spinnercategory.getSelectedItem().toString());

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }


    public void FillDataOrderByStatus(String filterstatus) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {


                //Co-user login
                String query2 = "select COUNT (DISTINCT [ngouserid])  FROM [NgoProductDonation]  where [ngouserid]='" + activity.edthidenuserid.getText().toString() + "' and  [isread]='No'";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                totalnoticeonceoff=0;
                while (rs2.next()) {
                    totalnoticeonceoff = totalnoticeonceoff + 1;
                }


            txttotalnoticeonceoff.setText(String.valueOf(totalnoticeonceoff));
                    //Get Co-user login
                /*String query2 = "select * from [Ngo] where [userid]='"+activity.edthidenuserid.getText().toString()+ "'";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
rs2.next();*/
               // String  query="select * from [NgoProductDonation]  where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";// = "select * from [NgoProductDonation] where [donationstatus]='Pending'  and [isread]='No'  order by [id] asc";
                String  query;

                 if(filterstatus.equals("Accepted")){
                    query = "select distinct * from [NgoProductDonation] where [donationstatus]='Accepted' and [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                }else if(filterstatus.equals("Pending")){
                    query = "select * from [NgoProductDonation]   where [donationstatus]='Pending' and [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                }else if(filterstatus.equals("Declined")){
                    query = "select * from [NgoProductDonation]  where [donationstatus]='Declined' and [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                }else{
                    query = "select * from [NgoProductDonation]  where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                }
              /*  if(filterstatus.equals("Accepted")){
                    query = "select * from [NgoProductDonation] pd" +
                            " inner join [Ngo] ngo on ngo.[id]=pd.[ngoid]  where pd.[donationstatus]='Accepted' and ngo.[userid]='"+activity.edthidenuserid.getText().toString()+"' and pd.[isread]='No'  order by pd.[id] asc";
                }else if(filterstatus.equals("Pending")){
                    query = "select * from [NgoProductDonation] pd" +
                            " inner join [Ngo] ngo on ngo.[id]=pd.[ngoid]  where pd.[donationstatus]='Pending' and ngo.[userid]='"+activity.edthidenuserid.getText().toString()+"' and pd.[isread]='No'  order by pd.[id] asc";
                }else if(filterstatus.equals("Declined")){
                    query = "select * from [NgoProductDonation] pd" +
                            " inner join [Ngo] ngo on ngo.[id]=pd.[ngoid]  where pd.[donationstatus]='Declined' and ngo.[userid]='"+activity.edthidenuserid.getText().toString()+"' and pd.[isread]='No'  order by pd.[id] asc";
                }else{
                    query = "select * from [NgoProductDonation] pd" +
                            " inner join [Ngo] ngo on ngo.[id]=pd.[ngoid] where ngo.[userid]='"+activity.edthidenuserid.getText().toString()+"' and pd.[isread]='No'  order by pd.[id] asc";
                }*/
                    name.clear();
                    distance.clear();
                    logoimage.clear();



                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {



                        String lat = rs.getString("lonlat").toString().substring(10, rs.getString("lonlat").toString().indexOf(','));
                        String lon = rs.getString("lonlat").toString().substring(rs.getString("lonlat").toString().indexOf(',') + 1, rs.getString("lonlat").toString().indexOf(')'));
                        double latdouble = Double.parseDouble(lat);
                        double londouble = Double.parseDouble(lon);

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
                            String fulladdress = address;
                            // Get reults

                            double distance1;
                            Location locationA = new Location("");
                            locationA.setLatitude(latitude);
                            locationA.setLongitude(longitude);

                            Location locationB = new Location("");
                            locationB.setLatitude(latdouble);
                            locationB.setLongitude(londouble);

                            // distance = locationA.distanceTo(locationB);   // in meters
                            distance1 = locationA.distanceTo(locationB) / 1000;   // in km

                            // txtdistance.setText();
                            distance.add((double) Math.round(distance1) + "km Away");
                        }

                        String query1 = "select * from [AppUser]  where [id]="+activity.edthidenuserid.getText().toString();
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        Bitmap decodebitmap;
                        rs1.next();
                            if (rs1.getString("image") != null) {
                                byte[] decodeString = Base64.decode(rs1.getString("image"), Base64.DEFAULT);
                                decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            } else {
                                decodebitmap=BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profilephoto);
                            }
                        String donationstatus=rs.getString("donationstatus").toString();
                        if(donationstatus.equals("Accepted")){
                            if(!name.contains("Accepted "+rs1.getString("firstname").toString()))
                         name.add("Accepted "+rs1.getString("firstname").toString());
                        }else if(donationstatus.equals("Pending")){
                            if(!name.contains("Pending "+rs1.getString("firstname").toString()))
                          name.add("Pending "+rs1.getString("firstname").toString());
                        }else if(donationstatus.equals("Declined")){
                           if(!name.contains("Declined "+rs1.getString("firstname").toString()))
                         name.add("Declined "+rs1.getString("firstname").toString());
                        }

                        logoimage.add(decodebitmap);




                    }

                NGOAllDonationListAdapter adapter=new NGOAllDonationListAdapter(this.getActivity(), logoimage, name, distance);
                    lstgross.setAdapter(adapter);
                    lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub


                            try {


                               // String selectedname = name.get(position);
                                String selectedname=name.get(position).substring(name.get(position).indexOf(" "));
                                Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_LONG).show();
                                String query = "select * from [AppUser]  where [firstname]='"+selectedname.trim()+"'";
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();

                                  while (rs.next()) {
                                    bundles.putString("userid", rs.getString("id").toString());
                                      bundles.putString("image", rs.getString("image").toString());
                                    NGODonationPerUserFrag fragment = new NGODonationPerUserFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




                                }

                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", ex.getMessage().toString()+" NGOAllD");
                            }
                        }
                    });




            }
        } catch (Exception ex) {
              Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }






}


