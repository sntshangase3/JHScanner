package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyListFragNotificationAllCategory extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;


int total=0;




    private Button cooking,quarantine,disposal,donation;
    TextView txtcook,txtquarantin,txtdipose,txtdonate,txthome;
    ImageButton donestatus;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.mylistnotificationallcategory, container, false);

        txtcook = (TextView) rootView.findViewById(R.id.cook);
        txtquarantin = (TextView) rootView.findViewById(R.id.quarantin);
        txtdipose = (TextView) rootView.findViewById(R.id.dispose);
        txtdonate = (TextView) rootView.findViewById(R.id.donate);
        txthome = (TextView) rootView.findViewById(R.id.txtselect);

        cooking = (Button) rootView.findViewById(R.id.cooking);
        quarantine = (Button) rootView.findViewById(R.id.quarantine);
        disposal = (Button) rootView.findViewById(R.id.disposal);
        donation = (Button) rootView.findViewById(R.id.donation);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



        bundle = this.getArguments();
        try{
            if (!activity.edthidenuserid.getText().toString().equals("")) {
                txthome.setVisibility(View.GONE);
                donestatus.setVisibility(View.GONE);

            }

        }catch (Exception ex){

        }

        cooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag =   new MyListFragNotification();
                Bundle bundle = new Bundle();
                bundle.putString("type", "cooking");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });
        quarantine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag =   new MyListFragNotification();
                Bundle bundle = new Bundle();
                bundle.putString("type", "quarantine");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });
        disposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag =   new MyListFragNotification();
                Bundle bundle = new Bundle();
                bundle.putString("type", "disposal");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag =   new MyListFragNotification();
                Bundle bundle = new Bundle();
                bundle.putString("type", "donation");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
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
        FillDataOrderAllNotice();

          return rootView;
    }




    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }






    public void FillDataOrderAllNotice() {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {

                if (activity.edthidenuserid.getText().toString().equals("")) {
                    //Assistance loging
                    String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                    ResultSet rs11 = ps11.executeQuery();
                    rs11.next();
                    // Cooking
                    String  query = "select * from [UserProductNotification] where  [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]=2  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {
                        total=total+1;
                    }
                    txtcook.setText(String.valueOf(total));
                    // Quarantine
                    String  query1 = "select * from [UserProductNotification] where  [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]=3  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    total=0;
                    while (rs1.next()) {
                        total=total+1;
                    }
                    txtquarantin.setText(String.valueOf(total));
                    // Disposal
                    String  query2 = "select * from [UserProductNotification] where  [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]=4  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    total=0;
                    while (rs2.next()) {
                        total=total+1;
                    }
                    txtdipose.setText(String.valueOf(total));
                    // Donation
                    String  query3 = "select * from [UserProductNotification] where  [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]=5  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    total=0;
                    while (rs3.next()) {
                        total=total+1;
                    }
                   txtdonate.setText(String.valueOf(total));


                }else {
                    // Co-user login
                 // Cooking
                    String  query = "select * from [UserProductNotification] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [requesttypeid]=2  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {
                        total=total+1;
                    }
                    txtcook.setText(String.valueOf(total));
                    // Quarantine
                    String  query1 = "select * from [UserProductNotification] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [requesttypeid]=3  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    total=0;
                    while (rs1.next()) {
                        total=total+1;
                    }
                    txtquarantin.setText(String.valueOf(total));
                    // Disposal
                    String  query2 = "select * from [UserProductNotification] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [requesttypeid]=4  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    total=0;
                    while (rs2.next()) {
                        total=total+1;
                    }
                    txtdipose.setText(String.valueOf(total));
                    // Donation
                    String  query3 = "select * from [UserProductNotification] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [requesttypeid]=5  and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    total=0;
                    while (rs3.next()) {
                        total=total+1;
                    }
                    txtdonate.setText(String.valueOf(total));


                }



            }
        } catch (Exception ex) {
          //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }






}


