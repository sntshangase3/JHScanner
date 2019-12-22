package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class GroceryInsightsFrag extends Fragment  {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    TextView txt1,txt2,txt3,txt4,txt5,txt6;
    ImageView b1,b2,b3,b4,b5,b6;
    double yearlycosumption=0;
    double yearlyspending=0;
    double yearlydonation=0;
    double yearlywaste=0;
    double monthlyspending=0;

    int totalnoticeonceoff = 0;
    TextView txttotalnoticeonceoff;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    String checkifpremiumuser="No";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.grocery_insight, container, false);

        txt1 = (TextView)rootView.  findViewById(R.id.txt1);
        txt2 = (TextView)rootView.  findViewById(R.id.txt2);
        txt3 = (TextView)rootView.  findViewById(R.id.txt3);
        txt4 = (TextView)rootView.  findViewById(R.id.txt4);
        txt5 = (TextView)rootView.  findViewById(R.id.txt5);
        txt6 = (TextView)rootView.  findViewById(R.id.txt6);

        b1=(ImageView) rootView.findViewById(R.id.b1);
        b2=(ImageView) rootView.findViewById(R.id.b2);
        b3=(ImageView) rootView.findViewById(R.id.b3);
        b4=(ImageView) rootView.findViewById(R.id.b4);
        b5=(ImageView) rootView.findViewById(R.id.b5);
        b6=(ImageView) rootView.findViewById(R.id.b6);
        txttotalnoticeonceoff = (TextView) rootView.findViewById(R.id.txtnotice1);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        FillDataYearlyCosumption();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();*/
                if(checkifpremiumuser.equals("Yes")){

                    Fragment mainFragment = new ConsumeInsightListFrag();
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
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();*/

                if(checkifpremiumuser.equals("Yes")){

                    Fragment mainFragment = new WasteInsightListFrag();
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
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();*/
                Fragment frag =   new MyListSavvyFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
               /* Fragment frag =   new ReportsBarChartFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();*/
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* Toast ToastMessage = Toast.makeText(rootView.getContext(),"Premium Service Coming Soon!!!",Toast.LENGTH_SHORT);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_bground);
                ToastMessage.show();*/
                Fragment frag =   new NGOGroceryInsightListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });

          return rootView;
    }






    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }


    public void FillDataYearlyCosumption() {
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
                //Check if AppUser info already subcribes and populates accordingly
                String queryx = "select * from [AppUserAssistance] where [userid]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement psx = con.prepareStatement(queryx);
                ResultSet rsx = psx.executeQuery();
                rsx.next();

                if (rsx.getRow() != 0) {
                    checkifpremiumuser="Yes";
                }

                String querya = "select COUNT (DISTINCT [ngouserid])  FROM [NgoProductDonation]  where [ngouserid]='" + activity.edthidenuserid.getText().toString() + "' and  [isread]='No'";
                PreparedStatement psa = con.prepareStatement(querya);
                ResultSet rsa = psa.executeQuery();
                while (rsa.next()) {
                    totalnoticeonceoff = totalnoticeonceoff + 1;
                }
                txttotalnoticeonceoff.setText(String.valueOf(totalnoticeonceoff));
                //#########################Yearly spending##############
                //====== Yearly spending Invenstory
               double yearlyspendinginventory=0;
                //-------- Yearly spending Invenstory currently in Product
                String query11 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                while (rs11.next()) {
                    yearlyspendinginventory = yearlyspendinginventory + Double.parseDouble(rs11.getString("totatitemvalue").toString());
                }
                //-------- Yearly spending Invenstory in Donation
                String query12 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps12 = con.prepareStatement(query12);
                ResultSet rs12 = ps12.executeQuery();
                while (rs12.next()) {
                    yearlyspendinginventory = yearlyspendinginventory + Double.parseDouble(rs12.getString("totatitemvalue").toString());
                }
                //-------- Yearly spending Invenstory in Waste
                String query13 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps13 = con.prepareStatement(query13);
                ResultSet rs13 = ps13.executeQuery();
                while (rs13.next()) {
                    yearlyspendinginventory = yearlyspendinginventory + Double.parseDouble(rs13.getString("totatitemvalue").toString());
                }

                //====== Yearly spending Shopping
                double yearlyspendingshopping=0;
                //-------- Yearly spending shopping currently in Product
                String query14 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps14 = con.prepareStatement(query14);
                ResultSet rs14 = ps14.executeQuery();
                while (rs14.next()) {
                    yearlyspendingshopping = yearlyspendingshopping + Double.parseDouble(rs14.getString("totatitemvalue").toString());
                }
                //-------- Yearly spending shopping in Donation
                String query15 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps15 = con.prepareStatement(query15);
                ResultSet rs15 = ps15.executeQuery();
                while (rs15.next()) {
                    yearlyspendingshopping = yearlyspendingshopping + Double.parseDouble(rs15.getString("totatitemvalue").toString());
                }
                //-------- Yearly spending shopping in Waste
                String query16 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps16 = con.prepareStatement(query16);
                ResultSet rs16 = ps16.executeQuery();
                while (rs16.next()) {
                    yearlyspendingshopping = yearlyspendingshopping + Double.parseDouble(rs16.getString("totatitemvalue").toString());
                }

                //#########################End Yearly spending##############
                //====== Yearly Donations
                String query1 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    yearlydonation = yearlydonation + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                }
                //====== Yearly Waste
                String query2 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    yearlywaste = yearlywaste + Double.parseDouble(rs2.getString("totatitemvalue").toString());
                }
                //===== Yearly Consumption
                String query161 = "select * from [UserProductConsumption] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                PreparedStatement ps161 = con.prepareStatement(query161);
                ResultSet rs161 = ps161.executeQuery();
                while (rs161.next()) {
                    yearlycosumption = yearlycosumption + Double.parseDouble(rs161.getString("totatitemvalue").toString());
                }
                //#########################Monthly spending###################
                double monthlyspendinginventory=0;

                //====== Monthly spending inventory currently in product
                String query3 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps3 = con.prepareStatement(query3);
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    monthlyspendinginventory = monthlyspendinginventory + Double.parseDouble(rs3.getString("totatitemvalue").toString());
                }
                //====== Monthly spending inventory in Donation
                String query31 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps31 = con.prepareStatement(query31);
                ResultSet rs31 = ps31.executeQuery();
                while (rs31.next()) {
                    monthlyspendinginventory = monthlyspendinginventory + Double.parseDouble(rs31.getString("totatitemvalue").toString());
                }

                //====== Monthly spending inventory in Waste
                String query32 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps32 = con.prepareStatement(query32);
                ResultSet rs32 = ps32.executeQuery();
                while (rs32.next()) {
                    monthlyspendinginventory = monthlyspendinginventory + Double.parseDouble(rs32.getString("totatitemvalue").toString());
                }
                //------------------------------------
                double monthlyspendingshopping=0;
                //====== Monthly spending shopping currently in product
                String query33 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps33 = con.prepareStatement(query33);
                ResultSet rs33 = ps33.executeQuery();
                while (rs33.next()) {
                    monthlyspendingshopping = monthlyspendingshopping + Double.parseDouble(rs33.getString("totatitemvalue").toString());
                }
                //====== Monthly spending shopping in Donation
                String query34 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps34 = con.prepareStatement(query34);
                ResultSet rs34 = ps34.executeQuery();
                while (rs34.next()) {
                    monthlyspendingshopping = monthlyspendingshopping + Double.parseDouble(rs34.getString("totatitemvalue").toString());
                }

                //====== Monthly spending shopping in Waste
                String query35 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes' and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps35 = con.prepareStatement(query35);
                ResultSet rs35 = ps35.executeQuery();
                while (rs35.next()) {
                    monthlyspendingshopping = monthlyspendingshopping + Double.parseDouble(rs35.getString("totatitemvalue").toString());
                }
                //######################### End Monthly spending###################
                yearlyspending=yearlyspendinginventory-yearlyspendingshopping;
                monthlyspending=monthlyspendinginventory-monthlyspendingshopping;


                if(yearlyspending<0){
                    yearlyspending=yearlyspending*-1;
                }
                if(monthlyspending<0){
                    monthlyspending=monthlyspending*-1;
                }

                txt1.setText("R" + String.valueOf(yearlycosumption));
                txt2.setText("R" + String.valueOf(yearlywaste));
                txt3.setText("R" + String.valueOf(monthlyspending));
                txt4.setText("R" + String.valueOf(yearlyspending));
               // txt5.setText("By Retail Brand");
                txt6.setText("R" + String.valueOf(yearlydonation));
            }
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
//===========

    }


}


