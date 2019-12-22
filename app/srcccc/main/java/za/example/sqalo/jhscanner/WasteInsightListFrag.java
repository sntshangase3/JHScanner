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

import org.joda.time.LocalDate;

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
public class WasteInsightListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

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

    TextView back, txtprevalue,txtprepercent,txtpostvalue,txtpostpercent,txtannualwaste,txtwastelevelpercent,txtwastecurrent,txtwastecurrentmonth,txtwastecurrentmonthdaystogo;
    int yearlyinventory = 0;
    int yearlycosumption = 0;
    int yearlydonation = 0;
    int yearlywaste = 0;
    double totalvalueall;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.waste_insight, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        back = (TextView) rootView. findViewById(R.id.txtback);
        txtprevalue = (TextView)rootView.  findViewById(R.id.txtprevalue);
        txtprepercent = (TextView)rootView.  findViewById(R.id.txtprepercent);
        txtpostvalue = (TextView)rootView.  findViewById(R.id.txtpostvalue);
        txtpostpercent = (TextView)rootView.  findViewById(R.id.txtpostpercent);
        txtannualwaste = (TextView)rootView.  findViewById(R.id.txtannualwaste);
        txtwastelevelpercent = (TextView) rootView.findViewById(R.id.txtwastelevelpercent);

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


                        //-------- Yearly Invenstory currently in Product

                        String query1 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + "  and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";

                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                            yearlyinventory = yearlyinventory + Integer.parseInt(rs1.getString("totatitemvalue").toString());

                        }


                        //-------- Yearly Consumption

                        String query2 = "select * from [UserProductConsumption] where [userid]=" + activity.edthidenuserid.getText().toString() + " and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";

                        PreparedStatement ps2 = con.prepareStatement(query2);
                        ResultSet rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            yearlycosumption = yearlycosumption + Integer.parseInt(rs2.getString("totatitemvalue").toString());
                                               }

                        //-------- Yearly Donation

                        String query3 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + "  and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";

                        PreparedStatement ps3 = con.prepareStatement(query3);
                        ResultSet rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            yearlydonation = yearlydonation + Integer.parseInt(rs3.getString("totatitemvalue").toString());

                        }


                        String query4 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        PreparedStatement ps4 = con.prepareStatement(query4);
                        ResultSet rs4 = ps4.executeQuery();
                        while (rs4.next()) {
                            yearlywaste = yearlywaste + Integer.parseInt(rs4.getString("totatitemvalue").toString());

                        }
                        //Monthly waste
                int monthlywaste=0;
                String query5 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and MONTH(Cast([purchasedate] as date))=MONTH(getDate())";
                PreparedStatement ps5 = con.prepareStatement(query5);
                ResultSet rs5 = ps5.executeQuery();
                while (rs5.next()) {
                    monthlywaste = monthlywaste + Integer.parseInt(rs5.getString("totatitemvalue").toString());

                }
                txtwastecurrent.setText("R"+String.valueOf(monthlywaste));
                         totalvalueall=yearlyinventory+yearlycosumption+yearlydonation+yearlywaste;

                txtwastelevelpercent.setText(String.valueOf((int)Math.round((yearlywaste/totalvalueall)*100))+"%");



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
double preyearlywaste=0;
                double postyearlywaste=0;
              //Co-user login
                String query = "select [image] from [UserProductWaste] where [userid] = '" + userid + "' and YEAR(Cast([purchasedate] as date))=YEAR(getDate()) group by [image]";
                expiration.clear();
                proimage.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);

                    //===========


                        double wastevalueperitem=0;
                        int expire = 0;
                        int spoil = 0;
                        int prepare = 0;
                        int cook = 0;
                        int qualty = 0;
                        int use = 0;
                        String query1 = "select * from [UserProductWaste] where [userid] = '" + userid + "' and [image]='" + rs.getString("image")+"' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            wastevalueperitem = wastevalueperitem + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            if (rs1.getString("wastereason").equals("Expiration Date")) {
                                expire += 1;
                                preyearlywaste = preyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("wastereason").equals("Food Spoilage")) {
                                spoil += 1;
                                preyearlywaste = preyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("wastereason").equals("Badly Prepared")) {
                                prepare += 1;
                                postyearlywaste = postyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("wastereason").equals("Badly Cooked")) {
                                cook += 1;
                                postyearlywaste = postyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("wastereason").equals("Poor Quality")) {
                                qualty += 1;
                                preyearlywaste = preyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            } else if (rs1.getString("wastereason").equals("Partially Used")) {
                                use += 1;
                                postyearlywaste = postyearlywaste + Double.parseDouble(rs1.getString("totatitemvalue").toString());
                            }

                        }

                        wastevalue.add("\t\tWaste Value R"+ String.valueOf((int)Math.round(wastevalueperitem))+" "+String.valueOf((int)Math.round((wastevalueperitem/yearlywaste)*100))+"%");
                        expiration.add("\t\t"+String.valueOf(expire) + " Counts");
                        spoilage.add("\t\t"+String.valueOf(spoil) + " Counts");
                        preparation.add("\t\t"+String.valueOf(prepare) + " Counts");
                        cooked.add("\t\t"+String.valueOf(cook) + " Counts");
                        quality.add("\t\t"+String.valueOf(qualty) + " Counts");
                        used.add("\t\t"+String.valueOf(use) + " Counts");


                }





                   double totalvalue=preyearlywaste+postyearlywaste;





                txtprevalue.setText("R" + String.valueOf((int)Math.round(preyearlywaste)));
                txtprepercent.setText(String.valueOf((int)Math.round((preyearlywaste/totalvalue)*100))+"%");
                txtpostvalue.setText("R" + String.valueOf((int)Math.round(postyearlywaste)));
                txtpostpercent.setText(String.valueOf((int)Math.round((postyearlywaste/totalvalue)*100))+"%");

                txtannualwaste.setText("Annual Waste Value R" + String.valueOf((int)Math.round(totalvalue)));

                WasteInsightListAdapter adapter = new WasteInsightListAdapter(this.getActivity(), proimage,wastevalue,expiration,spoilage,preparation,cooked,quality,used);
                lstgross.setAdapter(adapter);


            }
        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }


}


