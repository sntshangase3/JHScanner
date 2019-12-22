package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
public class OrderPointBundleListAllOrdersFrag extends Fragment implements AdapterView.OnItemSelectedListener {


    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid;

    int userid,qty,id;


    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> orderdeliverydate = new ArrayList<String>();
    ArrayList<String> orderstatus = new ArrayList<String>();





    ImageButton search;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    EditText edtsearch;
    TextView txtorderno;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundlelistallorders, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        txtorderno = (TextView) rootView.findViewById(R.id.txtorderno);
        search = (ImageButton) rootView.findViewById(R.id.search);


        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }


        try {
            if (activity.edthidenuserid.getText().toString().equals("")) {

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
                if(userid!=3){
                  //  btn_schedule.setVisibility(View.GONE);
                }

            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
                if(userid!=3){
                   // btn_schedule.setVisibility(View.GONE);
                }
            }

        } catch (Exception ex) {

        }

        try {
            FillDataOrderByStatus(search_product);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");

        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                    FillDataOrderByStatus(search_product);
            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(rootView.getContext(), "You've selected " + name.get(position) , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    private View currentSelectedView;
    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {

                //Co-user login
            String query1 = "select  * from [BundleOrder] where [userid]="+activity.edthidenuserid.getText().toString();
            PreparedStatement ps1 = con.prepareStatement(query1);
            ResultSet rs1 = ps1.executeQuery();
            int total=0;
            while (rs1.next()) {
                total=total+1;
            }
            txtorderno.setText(String.valueOf(total));

            String query = "SELECT name,b.image,deliverydaydate,orderstatus,ordercost,deliverycost,paymentmethod,location,bo.quantity " +
                    "  FROM [Bundle] b " +
                    "  inner join [BundleOrder] bo on bo.bundleid=b.id where bo.userid='" + userid +"'";
                if (!search.equals("")) {
                    query = "SELECT name,b.image,deliverydaydate,orderstatus,ordercost,deliverycost,paymentmethod,location,bo.quantity " +
                            "  FROM [Bundle] b " +
                            "  inner join [BundleOrder] bo on bo.bundleid=b.id where [name] like '%" + search + "%' and bo.userid='" + userid +"'";
                }
            Log.d("ReminderService In",  query);
                name.clear();
                proimage.clear();
                price.clear();
                quantity.clear();
                orderdeliverydate.clear();
                orderstatus.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    double cost=Double.valueOf(rs.getString("ordercost").toString())+Integer.valueOf(rs.getString("deliverycost").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    orderdeliverydate.add(rs.getString("deliverydaydate").toString());
                    orderstatus.add(rs.getString("orderstatus").toString());
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);
                    price.add("R"+cost);
                }
                OrderPointBundleListAllOrdersAdapter adapter = new OrderPointBundleListAllOrdersAdapter(this.getActivity(), proimage, name,quantity,orderdeliverydate,orderstatus,price);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub

                        try {

                            String selectedname = name.get(position);
                            String selectedprice= price.get(position);
                            String query = "SELECT name,b.image,bo.id,deliverydaydate,orderstatus,ordercost,deliverycost,paymentmethod,location,bo.quantity " +
                                    "  FROM [Bundle] b " +
                                    "  inner join [BundleOrder] bo on bo.bundleid=b.id where name like '%" + selectedname + "%' and bo.userid='" + userid +"'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            Log.d("ReminderService In",query);
                            while (rs.next()) {


                                bundles.putString("id", rs.getString("id").toString());
                                bundles.putString("name", selectedname);
                                OrderPointBundleFinalFrag fragment = new OrderPointBundleFinalFrag();
                                fragment.setArguments(bundles);
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                                if (currentSelectedView != null && currentSelectedView != view) {
                                    unhighlightCurrentRow(currentSelectedView);
                                }
                                currentSelectedView = view;
                                highlightCurrentRow(currentSelectedView);

                        }
                        } catch(Exception ex) {
                            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }

                    }


                });

        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


