package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ConsumeInsightListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    final CharSequence[] items = {"Product Insight", "Shopping List", "GroRequest", "Recipe Idea"};
    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid;
    ImageView productimage;
    int userid;

    ArrayList<String> wastevalue = new ArrayList<String>();
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> expiration = new ArrayList<String>();
    ArrayList<String> spoilage = new ArrayList<String>();
    ArrayList<String> preparation = new ArrayList<String>();
    ArrayList<String> cooked = new ArrayList<String>();
    ArrayList<String> quality = new ArrayList<String>();
    ArrayList<String> used = new ArrayList<String>();


    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    TextView back, txtbreakfastvalue,txtbreakfastpercent,txtlunchvalue,txtlunchpercent,txtannualconsume,txtdinnersuppervalue;
    TextView txtdinnersupperpercent,txtsnackothervalue,txtsnackotherpercent,txtwastecurrent,txtwastecurrentmonth,txtwastecurrentmonthdaystogo;


    int yearlycosumption = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.consume_insight, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        back = (TextView) rootView. findViewById(R.id.txtback);
        txtbreakfastvalue = (TextView)rootView.  findViewById(R.id.txtbreakfastvalue);
        txtbreakfastpercent = (TextView)rootView.  findViewById(R.id.txtbreakfastpercent);
        txtlunchvalue = (TextView)rootView.  findViewById(R.id.txtlunchvalue);
        txtlunchpercent = (TextView)rootView.  findViewById(R.id.txtlunchpercent);

        txtdinnersuppervalue = (TextView) rootView.findViewById(R.id.txtdinnersuppervalue);
        txtdinnersupperpercent = (TextView)rootView.  findViewById(R.id.txtdinnersupperpercent);
        txtsnackothervalue = (TextView)rootView.  findViewById(R.id.txtsnackothervalue);
        txtsnackotherpercent = (TextView) rootView.findViewById(R.id.txtsnackotherpercent);

        txtannualconsume = (TextView)rootView.  findViewById(R.id.txtannualconsume);

        txtwastecurrent = (TextView) rootView.findViewById(R.id.txtwastecurrent);
        txtwastecurrentmonth = (TextView) rootView.findViewById(R.id.txtwastecurrentmonth);
        txtwastecurrentmonthdaystogo = (TextView) rootView.findViewById(R.id.txtwastecurrentmonthdaystogo);
        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";


        Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date FirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date LastDay = calendar.getTime();

        Date today = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Format formatter = new SimpleDateFormat("MMM dd");
        txtwastecurrentmonth.setText(formatter.format(FirstDay)+"-"+formatter.format(LastDay));
        String todaydate = date_format.format(today);
        try {

          today = new Date(date_format.parse(todaydate).getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long daysleft = getDateDiff( LastDay,today, TimeUnit.DAYS);
txtwastecurrentmonthdaystogo.setText(String.valueOf(daysleft)+" Days To Go");

        bundle = this.getArguments();


        try {
            if (activity.edthidenuserid.getText().toString().equals("")) {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();

                userid = Integer.parseInt(rs11.getString("userid").toString());

                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            } else {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            }


            try {




                        //-------- Yearly Consumption

                        String query2 = "select * from [UserProductConsumption] where [userid]=" + activity.edthidenuserid.getText().toString() + " and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";

                        PreparedStatement ps2 = con.prepareStatement(query2);
                        ResultSet rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            yearlycosumption = yearlycosumption + Integer.parseInt(rs2.getString("totatitemvalue").toString());
                                               }


                int monthlywaste=0;
                String query5 = "select * from [UserProductConsumption] where [userid]=" + activity.edthidenuserid.getText().toString() + " and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps5 = con.prepareStatement(query5);
                ResultSet rs5 = ps5.executeQuery();
                while (rs5.next()) {
                    monthlywaste = monthlywaste + Integer.parseInt(rs5.getString("totatitemvalue").toString());

                }
                txtwastecurrent.setText("R"+String.valueOf(monthlywaste));




            } catch (Exception ex) {

                Log.d("ReminderService In", "Stack"+ex.getStackTrace().toString()+"Loca"+ex.getLocalizedMessage()+ex.getMessage());
            }

        } catch (Exception ex) {

        }

        FillDataWaste();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryInsightsFrag fragment = new GroceryInsightsFrag();
                 fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




            }
        });
        return rootView;
    }
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date1.getTime() - date2.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void FillDataWaste() {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {
               double breakfast=0;
                double lunch=0;
                double dinner=0;
                double snack=0;
              //Co-user login
                String query = "select [image] from [UserProductConsumption] where [userid] = '" + userid + "' and YEAR(Cast([purchasedate] as date))=YEAR(getDate()) group by [image]";
                expiration.clear();
                proimage.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);

                    //===========


                        double consumevalueperitem=0;
                        int expire = 0;
                        int spoil = 0;
                        int prepare = 0;
                        int cook = 0;
double volume =0;String unit="";

                        String query1 = "select * from [UserProductConsumption] where [userid] = '" + userid + "' and [image]='" + rs.getString("image")+"' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            consumevalueperitem = consumevalueperitem + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            volume = volume + Double.parseDouble(rs1.getString("size").toString().replaceAll("[^0-9]", ""));
                            unit = rs1.getString("size").toString().replaceAll("[^A-Za-z]+", "");
                            if (rs1.getString("consumereason").equals("Breakfast")) {
                                expire += 1;
                                breakfast = breakfast + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("consumereason").equals("Lunch")) {
                                spoil += 1;
                                lunch = lunch + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("consumereason").equals("Dinner/Supper")) {
                                prepare += 1;
                                dinner = dinner + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("consumereason").equals("Snack/Other")) {
                                cook += 1;
                                snack = snack + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            }

                        }

                        wastevalue.add("\t\tConsumed  "+String.valueOf((int)Math.round(volume))+unit+"  R"+ String.valueOf((int)Math.round(consumevalueperitem))+"  "+String.valueOf((int)Math.round((consumevalueperitem/yearlycosumption)*100))+"%");
                        expiration.add("\t\t"+String.valueOf(expire) + " Counts");
                        spoilage.add("\t\t"+String.valueOf(spoil) + " Counts");
                        preparation.add("\t\t"+String.valueOf(prepare) + " Counts");
                        cooked.add("\t\t"+String.valueOf(cook) + " Counts");



                }





                   double totalvalue=breakfast+lunch+dinner+snack;





                txtbreakfastvalue.setText("R" + String.valueOf((int)Math.round(breakfast)));
                txtbreakfastpercent.setText(String.valueOf((int)Math.round((breakfast/totalvalue)*100))+"%");
                txtlunchvalue.setText("R" + String.valueOf((int)Math.round(lunch)));
                txtlunchpercent.setText(String.valueOf((int)Math.round((lunch/totalvalue)*100))+"%");
                txtdinnersuppervalue.setText("R" + String.valueOf((int)Math.round(dinner)));
                txtdinnersupperpercent.setText(String.valueOf((int)Math.round((dinner/totalvalue)*100))+"%");
                txtsnackothervalue.setText("R" + String.valueOf((int)Math.round(snack)));
                txtsnackotherpercent.setText(String.valueOf((int)Math.round((snack/totalvalue)*100))+"%");

                txtannualconsume.setText("R" + String.valueOf((int)Math.round(totalvalue)));

                ConsumeInsightListAdapter adapter = new ConsumeInsightListAdapter(this.getActivity(), proimage,wastevalue,expiration,spoilage,preparation,cooked);
                lstgross.setAdapter(adapter);


            }
        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }


}


