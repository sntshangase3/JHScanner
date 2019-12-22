package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefDishListAllRequestCategory extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;


int total=0;




    private Button dishorders,dishorderscustomer,dishshare,dishideas,dishrecipe;
    TextView txtorders,txtorderscustomer,txtshare,txtideas,txtrecipes,txthome;
    ImageButton donestatus;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.mylistdishallcategory, container, false);

        txtorders = (TextView) rootView.findViewById(R.id.orders);
        txtorderscustomer = (TextView) rootView.findViewById(R.id.orderscustomer);
        txtshare = (TextView) rootView.findViewById(R.id.share);
        txtideas = (TextView) rootView.findViewById(R.id.ideas);
        txtrecipes = (TextView) rootView.findViewById(R.id.recipes);

        txthome = (TextView) rootView.findViewById(R.id.txtselect);

        dishorders = (Button) rootView.findViewById(R.id.dishorders);
        dishorderscustomer = (Button) rootView.findViewById(R.id.dishorderscustomer);
        dishshare = (Button) rootView.findViewById(R.id.dishshare);
        dishideas = (Button) rootView.findViewById(R.id.dishideas);
        dishrecipe = (Button) rootView.findViewById(R.id.dishrecipe);

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
               // txthome.setVisibility(View.GONE);
               // donestatus.setVisibility(View.GONE);

            }



        }catch (Exception ex){

        }

        dishorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!activity.edthidenuserid.getText().toString().equals("")) {
                    Fragment frag =   new MyOrdersOwnListFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "Co-User Only!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        dishorderscustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {
                    Fragment frag =   new MyOrdersCustomerListFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "Co-User Only!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        dishshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Fragment frag =   new ChefSharedListFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();

            }
        });
        dishideas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!activity.edthidenuserid.getText().toString().equals("")) {
                    Fragment frag =   new ChefIdeasListFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "Co-User Only!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        dishrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {
                    Fragment frag =   new RegisterChefPartnershipFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "Co-User Only!!", Toast.LENGTH_LONG).show();
                }

            }
        });

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
                    // Ordersown
                    String  query = "select * from [DishOrder] where [userid]='"+rs11.getString("userid").toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {
                        total=total+1;
                    }
                    txtorders.setText(String.valueOf(total));

                    // Orderscustomers
                    String  query3 = "select * from [DishOrder] where [useridchef]='"+rs11.getString("userid").toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    total=0;
                    while (rs3.next()) {
                        total=total+1;
                    }
                    txtorderscustomer.setText(String.valueOf(total));

                    // Ideas
                    String  query1 = "select * from [DishIdeas] where  [userid]='"+rs11.getString("userid").toString()+"' and [isread]='No'";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    total=0;
                    while (rs1.next()) {
                        total=total+1;
                    }
                    txtideas.setText(String.valueOf(total));
                    // Dish shared
                    String  query4 = "select * from [DishAssistantShare] where [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps4 = con.prepareStatement(query4);
                    ResultSet rs4 = ps4.executeQuery();
                    total=0;
                    while (rs4.next()) {
                        total=total+1;
                    }
                    txtshare.setText(String.valueOf(total));

                    // Recipes
                    String  query2 = "select * from [Dish] where [userid]='" + rs11.getString("userid").toString() + "' and [recipe]!='Not Specified' order by [id] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    total=0;
                    while (rs2.next()) {
                        total=total+1;
                    }
                    txtrecipes.setText(String.valueOf(total));

                }else {
                    // Co-user login
                 // Ordersown
                    String  query = "select * from [DishOrder] where [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total=0;
                    while (rs.next()) {
                        total=total+1;
                    }
                    txtorders.setText(String.valueOf(total));
                    // Orderscustomers
                    String  query3 = "select * from [DishOrder] where [useridchef]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    total=0;
                    while (rs3.next()) {
                        total=total+1;
                    }
                    txtorderscustomer.setText(String.valueOf(total));
                    // Ideas
                    String  query1 = "select * from [DishIdeas] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    total=0;
                    while (rs1.next()) {
                        total=total+1;
                    }
                    txtideas.setText(String.valueOf(total));
                    // Dish shared
                    String  query4 = "select * from [DishAssistantShare] where [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps4 = con.prepareStatement(query4);
                    ResultSet rs4 = ps4.executeQuery();
                    total=0;
                    while (rs4.next()) {
                        total=total+1;
                    }
                   txtshare.setText(String.valueOf(total));
                    // Recipes
                    String  query2 = "select * from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [recipe]!='Not Specified' order by [id] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    total=0;
                    while (rs2.next()) {
                        total=total+1;
                    }
                    txtrecipes.setText(String.valueOf(total));



                }



            }
        } catch (Exception ex) {
          //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
//===========

    }






}


