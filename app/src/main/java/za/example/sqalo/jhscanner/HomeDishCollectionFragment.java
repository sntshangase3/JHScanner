package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by sibusison on 2017/07/30.
 */
public class HomeDishCollectionFragment extends Fragment  {
    public int counter1=0;
    View rootView;
    ViewPager viewPager;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    MainActivity activity =   MainActivity.instance;

    FragmentManager fragmentManager;
    Bitmap decodebitmap1;

    ImageView cheflist,b1;

    Spinner spinnercategory;
    TextView txtname,txtstorelocation,txtdistance,txtyourlocation,txtabout;
   public int click=0;

String checkifpremiumuser="No";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_home_dish, container, false);

        cheflist = (ImageView) rootView.findViewById(R.id.cheflist);
        b1 = (ImageView) rootView.findViewById(R.id.b1);

        spinnercategory = (Spinner) rootView. findViewById(R.id.spinnercategory);
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        //startTimer();
        FillCategory();


       cheflist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkifpremiumuser.equals("Yes")){

                Fragment frag = new ChefListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }else{
                Toast.makeText(rootView.getContext(), "Only Premium Subscribed Users!!",Toast.LENGTH_LONG).show();
                Fragment frag = new RegisterPremiumFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }



        }
    });
    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(checkifpremiumuser.equals("Yes")){

                Fragment mainFragment = new ChefListInventoryAllFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, mainFragment)
                        .commit();
            }else{
                Toast.makeText(rootView.getContext(), "Only Premium Subscribed Users!!",Toast.LENGTH_LONG).show();
                Fragment frag = new RegisterPremiumFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }



        }
    });




         return rootView;
    }




    public void FillCategory() {

        try
        {
            ConnectionClass cn=new ConnectionClass();
            con =cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            }
            else
            {
                String query = "select * from [Category]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();
                category.clear();
                while(rs.next()) {

                    category.add(rs.getString("productCategory"));
                }
                ArrayAdapter adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnercategory.setAdapter(adapter1);

                //Check if AppUser info already subcribes and populates accordingly
                String query1 = "select * from [AppUserAssistance] where [userid]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();

                if (rs1.getRow() != 0) {
                    checkifpremiumuser="Yes";
                }
                /*String query2 = "select * from [Chef] where [userid]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                if (rs2.getRow() != 0) {
                    checkifsubcribechef="Yes";
                }*/

               // con.close();

            }
        }
        catch (Exception ex)
        {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            // z = "Invalid data input!";
        }



    }



}


