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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class NGOGroceryInsightListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

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
    ArrayList <String> price= new ArrayList<String>();
    ArrayList <String> date= new ArrayList<String>();
     ArrayList<Bitmap> statusimage=new ArrayList<Bitmap>();

    Bundle bundles = new Bundle();

    ArrayAdapter adapter1;


    ImageButton donestatus,btn_status;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
String  useridorder;
    String filterstatusselect;
    int statusclick=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.ngo_grocery_insight_list, container, false);


      lstgross = (ListView)rootView.findViewById(R.id.lstgross);
        txtTheKitchen = (TextView)rootView.  findViewById(R.id.txtTheKitchen);
        txtselect = (TextView)rootView.  findViewById(R.id.txtselect);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        btn_status = (ImageButton) rootView. findViewById(R.id.filtercategory);
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

                //Get donation details
                    String  query = "select * FROM [NgoProductDonation]  where [ngouserid]='" + activity.edthidenuserid.getText().toString() + "'";
                     PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                price.clear();
                date.clear();
                logoimage.clear();
                statusimage.clear();
                    while (rs.next()) {
                        total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                        price.add("R"+rs.getString("totatitemvalue").toString());
                        date.add(rs.getString("donationdate").toString());
                        if(rs.getString("donationstatus").toString().equals("Accepted")||rs.getString("donationstatus").toString().equals("Received")){
                            Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_received);
                            statusimage.add(im);
                        }else if(rs.getString("donationstatus").toString().equals("Postponed")||rs.getString("donationstatus").toString().equals("Pending")){
                            Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_pending);
                            statusimage.add(im);
                        }else if(rs.getString("donationstatus").toString().equals("Declined")){
                            Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_decline);
                            statusimage.add(im);
                        }


                        //Get ngo image
                        String  query1 = "select * from [Ngo] where [userid]='" + rs.getString("userid").toString() + "'";
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            byte[] decodeString1 = Base64.decode(rs1.getString("imagelogo"), Base64.DEFAULT);
                            Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1,0, decodeString1.length);
                            logoimage.add(decodebitmap1);
                        }

                    }



                txtselect.setText("Donated Grocery R"+String.valueOf(total));


                    NGOGroceryInsightListAdapter adapter=new NGOGroceryInsightListAdapter(this.getActivity(), logoimage, price, date,statusimage);
                    lstgross.setAdapter(adapter);
                  /*  lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
*/



            }
        } catch (Exception ex) {
              Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }

    public void FillDataOrderByStatus(String filterstatus) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Con null!!",Toast.LENGTH_LONG).show();
                // Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {

                // Co-user login

                //Get donation details

                String  query;

                if(filterstatus.equals("Accepted")||filterstatus.equals("Received")){
                    query = "select * FROM [NgoProductDonation]  where [donationstatus]='Accepted' or [donationstatus]='Received' and  [ngouserid]='" + activity.edthidenuserid.getText().toString() + "'";
                }else if(filterstatus.equals("Pending")||filterstatus.equals("Postponed")){
                    query = "select * FROM [NgoProductDonation]  where [donationstatus]='Pending' or [donationstatus]='Postponed' and  [ngouserid]='" + activity.edthidenuserid.getText().toString() + "'";
              }else if(filterstatus.equals("Declined")){
                    query = "select * FROM [NgoProductDonation]  where [donationstatus]='Declined' and  [ngouserid]='" + activity.edthidenuserid.getText().toString() + "'";
              }else{
                    query = "select * FROM [NgoProductDonation]  where [ngouserid]='" + activity.edthidenuserid.getText().toString() + "'";

                }



                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total=0;
                price.clear();
                date.clear();
                logoimage.clear();
                statusimage.clear();
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    price.add("R"+rs.getString("totatitemvalue").toString());
                    date.add(rs.getString("donationdate").toString());
                    if(rs.getString("donationstatus").toString().equals("Accepted")||rs.getString("donationstatus").toString().equals("Received")){
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_received);
                        statusimage.add(im);
                    }else if(rs.getString("donationstatus").toString().equals("Postponed")||rs.getString("donationstatus").toString().equals("Pending")){
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_pending);
                        statusimage.add(im);
                    }else if(rs.getString("donationstatus").toString().equals("Declined")){
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.donation_decline);
                        statusimage.add(im);
                    }


                    //Get ngo image
                    String  query1 = "select * from [Ngo] where [userid]='" + rs.getString("userid").toString() + "'";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        byte[] decodeString1 = Base64.decode(rs1.getString("imagelogo"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1,0, decodeString1.length);
                        logoimage.add(decodebitmap1);
                    }

                }



                txtselect.setText("Donated Grocery R"+String.valueOf(total));


                NGOGroceryInsightListAdapter adapter=new NGOGroceryInsightListAdapter(this.getActivity(), logoimage, price, date,statusimage);
                lstgross.setAdapter(adapter);
                  /*  lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
*/



            }
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }




}


