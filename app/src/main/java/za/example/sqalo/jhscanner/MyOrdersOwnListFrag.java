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
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyOrdersOwnListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    ListView lstgross;
    String currentid;
    ImageView productimage,retailerimage;
    TextView txtTheKitchen,txtselect;
double total=0;
    int catergoryclick=0;
    int statusclick=0;
    int shoppingclick=0;
    int ropclick=0;
    String filterstatusselect,filtershoppingselect,filterropselect;
    String Describtion="";
    ArrayList <Bitmap> proimage= new ArrayList<Bitmap>();


    ArrayList <String> name= new ArrayList<String>();
    ArrayList <String> quantity= new ArrayList<String>();
    ArrayList <String> orderdate= new ArrayList<String>();
    ArrayList <String> chefname= new ArrayList<String>();
    ArrayList <String> schedule= new ArrayList<String>();
    Bundle bundles = new Bundle();

    ArrayAdapter adapter1;


    ImageButton donestatus;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.mylistownorder, container, false);


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
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {

                if (activity.edthidenuserid.getText().toString().equals("")) {
                    //Assistatnce login
                    String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                  final  ResultSet rs11 = ps11.executeQuery();
                    rs11.next();

                    String  query = "select * from [DishOrder] where [userid]='"+rs11.getString("userid").toString()+"' and [isread]='No'  order by [id] asc";
                    name.clear();
                    quantity.clear();
                    orderdate.clear();

                    proimage.clear();
                    chefname.clear();


                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {

                        Date noticedate = null;
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

                        try {
                            noticedate = new Date(date_format.parse(rs.getString("orderdate").toString()).getTime());

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        String dayOfTheWeek = sdf.format(noticedate);

                        if (!activity.edthidenuserid.getText().toString().equals("")) {
                            txtTheKitchen.setText("The Kitchen - Dish Own Orders");
                        }else{
                            txtTheKitchen.setText("The Kitchen - Shared List");
                        }
                        //txtselect.setText("For More Details, Tap Dish Image"+dayOfTheWeek);
                        txtselect.setText("For More Details, Tap Dish Image");

                        quantity.add(rs.getString("quantity").toString());
                        name.add(rs.getString("name").toString());
                        orderdate.add(rs.getString("orderdate").toString());

                        String query2 = "select * from [Chef] where [userid]=" + rs.getString("useridchef").toString();
                        PreparedStatement ps2 = con.prepareStatement(query2);
                        ResultSet rs2 = ps2.executeQuery();
                        rs2.next();
                        chefname.add(rs2.getString("name"));


                        byte[] decodeString1 = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1,0, decodeString1.length);
                        proimage.add(decodebitmap1);




                    }

                    OrderListAdapter adapter=new OrderListAdapter(this.getActivity(), proimage, name, quantity,orderdate, chefname);
                    lstgross.setAdapter(adapter);
                    lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub


                            try {


                                String selectedname = name.get(position);
                                String selectedquantity = quantity.get(position);
                                String selectedstatus = orderdate.get(position);
                                String query = "select * from [DishOrder] where [userid]='" + rs11.getString("userid").toString()+ "' and [name]='"+selectedname+ "'  and [quantity]='"+selectedquantity+ "'  and [orderdate]='"+selectedstatus+"'";
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {

                                    Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                    bundles.putString("id", rs.getString("id").toString());
                                    bundles.putString("useridchef", rs.getString("useridchef").toString());
                                    bundles.putString("name", selectedname);
                                    bundles.putString("keyingredient", rs.getString("keyingredient").toString());
                                    bundles.putString("image", rs.getString("image").toString());


                                    ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();



                                }

                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {

                    // Co-user login
                    String  query = "select * from [DishOrder] where [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    name.clear();
                    quantity.clear();
                    orderdate.clear();

                    proimage.clear();
                    chefname.clear();


                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {

                            Date noticedate = null;
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

                            try {
                                noticedate = new Date(date_format.parse(rs.getString("orderdate").toString()).getTime());

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            String dayOfTheWeek = sdf.format(noticedate);
                            txtTheKitchen.setText("The Kitchen - Dish Own Orders");
                            //txtselect.setText("For More Details, Tap Dish Image"+dayOfTheWeek);
                        txtselect.setText("For More Details, Tap Dish Image");

                        quantity.add(rs.getString("quantity").toString());
                        name.add(rs.getString("name").toString());
                        orderdate.add(rs.getString("orderdate").toString());

                        String query2 = "select * from [Chef] where [userid]=" + rs.getString("useridchef").toString();
                        PreparedStatement ps2 = con.prepareStatement(query2);
                        ResultSet rs2 = ps2.executeQuery();
                        rs2.next();
                        chefname.add(rs2.getString("name"));


                        byte[] decodeString1 = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1,0, decodeString1.length);
                        proimage.add(decodebitmap1);




                    }

                    OrderListAdapter adapter=new OrderListAdapter(this.getActivity(), proimage, name, quantity,orderdate, chefname);
                    lstgross.setAdapter(adapter);
                    lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub


                            try {


                                String selectedname = name.get(position);
                                String selectedquantity = quantity.get(position);
                                String selectedstatus = orderdate.get(position);
                                String query = "select * from [DishOrder] where [userid]='" + activity.edthidenuserid.getText().toString()+ "' and [name]='"+selectedname+ "'  and [quantity]='"+selectedquantity+ "'  and [orderdate]='"+selectedstatus+"'";
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {

                                     Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                    bundles.putString("id", rs.getString("id").toString());
                                    bundles.putString("useridchef", rs.getString("useridchef").toString());
                                    bundles.putString("name", selectedname);
                                    bundles.putString("keyingredient", rs.getString("keyingredient").toString());
                                    bundles.putString("image", rs.getString("image").toString());


                                    ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();



                                }

                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }



            }
        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }






}


