package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ReportsBarChartFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;
    MainActivity activity =   MainActivity.instance;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reportbarchart, container, false);

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        double total1=0;double total2=0;double total3=0;double total4=0;double total5=0;double total6=0;
        double total7=0;double total8=0;double total9=0;double total10=0;double total11=0;double total12=0;double total13=0;

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {



                String query11 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() ;
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                while (rs11.next()) {


                    if(Integer.parseInt(rs11.getString("retailerid").toString())==1){
                        total1=total1+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==2){
                        total2=total2+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==3){
                        total3=total3+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==4){
                        total4=total4+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==5){
                        total4=total5+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==6){
                        total6=total6+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==7){
                        total7=total7+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==8){
                        total8=total8+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==9){
                        total9=total9+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==10){
                        total10=total10+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==11){
                        total11=total11+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==12){
                        total2=total2+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs11.getString("retailerid").toString())==13){
                        total3=total3+Double.parseDouble(rs11.getString("totatitemvalue").toString());
                    }

                }

                String query12 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString();
                PreparedStatement ps12 = con.prepareStatement(query12);
                ResultSet rs12 = ps12.executeQuery();
                while (rs12.next()) {
                    if(Integer.parseInt(rs12.getString("retailerid").toString())==1){
                        total1=total1+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==2){
                        total2=total2+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==3){
                        total3=total3+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==4){
                        total4=total4+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==5){
                        total4=total5+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==6){
                        total6=total6+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==7){
                        total7=total7+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==8){
                        total8=total8+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==9){
                        total9=total9+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==10){
                        total10=total10+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==11){
                        total11=total11+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==12){
                        total2=total2+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs12.getString("retailerid").toString())==13){
                        total3=total3+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }
                }

                String query13 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString();
                PreparedStatement ps13 = con.prepareStatement(query13);
                ResultSet rs13 = ps13.executeQuery();
                while (rs13.next()) {
                    if(Integer.parseInt(rs13.getString("retailerid").toString())==1){
                        total1=total1+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==2){
                        total2=total2+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==3){
                        total3=total3+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==4){
                        total4=total4+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==5){
                        total4=total5+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==6){
                        total6=total6+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==7){
                        total7=total7+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==8){
                        total8=total8+Double.parseDouble(rs12.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==9){
                        total9=total9+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==10){
                        total10=total10+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==11){
                        total11=total11+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==12){
                        total2=total2+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }else if(Integer.parseInt(rs13.getString("retailerid").toString())==13){
                        total3=total3+Double.parseDouble(rs13.getString("totatitemvalue").toString());
                    }
                }

            }
        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
//===========



        BarChart chart = (BarChart) rootView.findViewById(R.id.barchart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        int index=0;
        if((float)total1>0.0f){
            entries.add(new BarEntry((float)total1, index));
            index++;
        }
        if((float)total2>0.0f){
            entries.add(new BarEntry((float)total2, index));
            index++;
        }
        if((float)total3>0.0f){
            entries.add(new BarEntry((float)total3, index));
            index++;
        }
        if((float)total4>0.0f){
            entries.add(new BarEntry((float)total4, index));
            index++;
        }
        if((float)total5>0.0f){
            entries.add(new BarEntry((float)total5, index));
            index++;
        }
        if((float)total6>0.0f){
            entries.add(new BarEntry((float)total6, index));
            index++;
        }
        if((float)total7>0.0f){
            entries.add(new BarEntry((float)total7, index));
            index++;
        }
        if((float)total8>0.0f){
            entries.add(new BarEntry((float)total8, index));
            index++;
        }
        if((float)total9>0.0f){
            entries.add(new BarEntry((float)total9, index));
            index++;
        }
        if((float)total10>0.0f){
            entries.add(new BarEntry((float)total10, index));
            index++;
        }
        if((float)total11>0.0f){
            entries.add(new BarEntry((float)total1, index));
            index++;
        }
        if((float)total12>0.0f){
            entries.add(new BarEntry((float)total2, index));
            index++;
        }
        if((float)total13>0.0f){
            entries.add(new BarEntry((float)total3, index));
            index++;
        }


        BarDataSet dataset = new BarDataSet(entries, "Retail Brand");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<String> labels = new ArrayList<String>();

        if((float)total1>0.0f){
            labels.add("Checkers");
        }
        if((float)total2>0.0f){
            labels.add("OK Food");
        }
        if((float)total3>0.0f){
            labels.add("Pick n Pay");
        }
        if((float)total4>0.0f){
            labels.add("Shoprite");
        }
        if((float)total5>0.0f){
            labels.add("Spar");
        }
        if((float)total6>0.0f){
            labels.add("USave");
        }
        if((float)total7>0.0f){
            labels.add("Makro");
        }
        if((float)total8>0.0f){
            labels.add("Food Lovers");
        }
        if((float)total9>0.0f){
            labels.add("Woolworth Food");
        }
        if((float)total10>0.0f){
            labels.add("Boxer");
        }
        if((float)total11>0.0f){
            labels.add("Game");
        }
        if((float)total12>0.0f){
            labels.add("Fruits & Veg");
        }
        if((float)total13>0.0f){
            labels.add("Other Retailers");
        }



        BarData data = new BarData(labels, dataset);

        chart.getXAxis().setLabelsToSkip(0);
        chart.setData(data);
        chart.setDrawGridBackground(false);
        chart.setDescription("Grocery Analytic");
        chart.animateY(3000);



       /* 1
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));        // gap of 2f
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setDrawIcons(true);


        BarData data = new BarData(set,set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setDrawGridBackground(false);*/

        //=========

       /* 2
        float barWidth = 0.9f; // x4 dataset
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

            yVals1.add(new BarEntry(0f, 30f));
            yVals2.add(new BarEntry(1f, 80f));
            yVals3.add(new BarEntry(2f, 60f));
            yVals4.add(new BarEntry(3f, 50f));


        BarDataSet  set1 = new BarDataSet(yVals1, "Company A");
        set1.setColor(Color.rgb(104, 241, 175));
        set1.setDrawIcons(true);
        BarDataSet  set2 = new BarDataSet(yVals2, "Company B");
        set2.setColor(Color.rgb(164, 228, 251));
        BarDataSet  set3 = new BarDataSet(yVals3, "Company C");
        set3.setColor(Color.rgb(242, 247, 158));
        BarDataSet set4 = new BarDataSet(yVals4, "Company D");
        set4.setColor(Color.rgb(255, 102, 0));

        BarData data = new BarData(set1, set2, set3, set4);
        data.setValueFormatter(new LargeValueFormatter());
        data.setBarWidth(barWidth);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setDrawGridBackground(false);


        chart.getXAxis().setDrawGridLines(false);

        //==========

        chart.animateXY(2000, 2000);
        chart.invalidate();*/

        return rootView;
    }






}


