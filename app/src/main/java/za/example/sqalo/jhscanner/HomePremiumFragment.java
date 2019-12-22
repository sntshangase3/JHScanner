package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owner on 2015-10-04.
 */
public class HomePremiumFragment extends Fragment{
    View rootView;
    ImageView homev;

    Visualizer visual;
    int formattedVizData[];
    byte rawWaveForm[];
    int cont = 0xFF;
    EditText edttest;
    ImageView  b1, b2,b3,lock;
    Button btn_schedule,btn_logout;
    MainActivity activity =   MainActivity.instance;
    Connection con;
    String un,pass,db,ip;
    int totalnoticeonceoff=0;
    int totalnoticeweekly=0;
    int totalnoticedish=0;

    TextView txttotalnoticeonceoff,txttotalnoticeweekly,txttotalnoticedish;
    ImageView edtlogoImage,edtprofileImage;
    Bundle bundle;
     String onceoffOrWeek;
    public HomePremiumFragment(){

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

  rootView = inflater.inflate(R.layout.fragment_homee_premium, container, false);

        b1 = (ImageView) rootView.findViewById(R.id.b1);
        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        lock = (ImageView) rootView.findViewById(R.id.lock);


        txttotalnoticeonceoff = (TextView)rootView.  findViewById(R.id.txtonceoffnotice);
        txttotalnoticeweekly = (TextView)rootView.  findViewById(R.id.txtweeklynotice);
        txttotalnoticedish = (TextView)rootView.  findViewById(R.id.txtdishnotice);

        edtlogoImage = (ImageView) rootView. findViewById(R.id.imgLogo);
        edtprofileImage = (ImageView) rootView. findViewById(R.id.profileImage);
        btn_schedule = (Button) rootView. findViewById(R.id.btn_schedule);
        btn_logout = (Button) rootView. findViewById(R.id.btn_logout);

     //activity.edthidenuserid.getText().toString();

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

        bundle = this.getArguments();
        try{
            if(bundle != null){
                if(bundle.getString("assist").equals("assistant")){
                    btn_schedule.setVisibility(View.GONE);
                    btn_logout.setVisibility(View.VISIBLE);
                }

            }else if(bundle.getString("couser").equals("couser")){
                if(bundle.getString("share").equals("share")){
                    Toast.makeText(rootView.getContext(), "Dish Shared Successfully!!", Toast.LENGTH_LONG).show();
                }
                btn_schedule.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.GONE);
            }else {
                btn_schedule.setVisibility(View.GONE);
                btn_logout.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){

        }

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //----------Show day pass


                    try {
                        final NotificationManager mgr1 = (NotificationManager) rootView.getContext().getSystemService(NOTIFICATION_SERVICE);
                        final Intent notificationIntent1 = new Intent(rootView.getContext(), SplashFragment.class);
                        //++++++++++Show notification for passcode once
                        // String query111 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "' and [isdaypasscodesent]='No'";
                        String query111 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                        PreparedStatement ps111 = con.prepareStatement(query111);
                        ResultSet rs111 = ps111.executeQuery();
                        rs111.next();
                        // if ( rs111.getRow() != 0) {
                        String notificationbody1 = "";//
                        final Notification.Builder builder1 = new Notification.Builder(rootView.getContext());
                        Notification myNotication1;
                        int id = 0;
                        PendingIntent pendingIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent1, PendingIntent.FLAG_ONE_SHOT);

                        builder1.setAutoCancel(false);

                        notificationbody1= "Your Day Pass Code:"+rs111.getString("assistancedaypasscode").toString() ;
                        builder1.setTicker("Pass Code");
                        builder1.setSmallIcon(R.drawable.logos);
                        builder1.setContentIntent(pendingIntent);
                        builder1.setContentTitle("Pass Code");
                        builder1.setContentText(notificationbody1);
                        myNotication1 = builder1.setStyle(new Notification.BigTextStyle().bigText(notificationbody1)).build();
                        myNotication1.defaults |= Notification.DEFAULT_VIBRATE;
                        mgr1.notify(id, myNotication1);
                        //Update [AppUserAssistance] isdaypasscode Yes after viewed
                       /* String commands1 = "update [AppUserAssistance] set [isdaypasscodesent]='Yes' where [assistancename]='" + rs111.getString("assistancename").toString()+ "'";
                        PreparedStatement preStmt1 = con.prepareStatement(commands1);
                        preStmt1.executeUpdate();*/
                        // }

                        //++++++++++++++++
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        builder.setTitle("Welcome:"+rs111.getString("assistancename").toString());
                        builder.setIcon(rootView.getResources().getDrawable(R.drawable.profil));
                    /*final EditText input = new EditText(rootView.getContext());
                    input.setGravity(Gravity.CENTER);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                    input.setHint("Enter Day Pass Code");
                    input.setHintTextColor(Color.GRAY);
                    builder.setView(input);*/

                        builder.setMessage("Your Day Pass Code:"+rs111.getString("assistancedaypasscode").toString());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //############################
                                try {
                                    String query111 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                                    PreparedStatement ps111 = con.prepareStatement(query111);
                                    ResultSet rs111 = ps111.executeQuery();
                                    rs111.next();
                                    final String assistancedaypasscode=rs111.getString("assistancedaypasscode").toString() ;//

                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    builder.setTitle("House Assistant");
                                    builder.setIcon(rootView.getResources().getDrawable(R.drawable.locki));
                                    final EditText input = new EditText(rootView.getContext());
                                    input.setGravity(Gravity.CENTER);
                                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                    input.setHint("Enter Day Pass Code");
                                    input.setHintTextColor(Color.GRAY);
                                    builder.setView(input);
                                    builder.setMessage("Day Pass Code?");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if( input.getText().toString().equals(assistancedaypasscode)){
                                                if(onceoffOrWeek.equals("onceoff")){
                                                    Fragment frag = new MyListFragNotificationAllCategory();
                                                    FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction()
                                                            .replace(R.id.mainFrame, frag).commit();
                                                }else if(onceoffOrWeek.equals("weekly")){
                                                    Fragment frag =   new MyListFragNotification();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("week", "week");
                                                    frag.setArguments(bundle);
                                                    FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction()
                                                            .replace(R.id.mainFrame, frag).commit();
                                                }else{
                                                    Fragment frag = new ChefDishListAllRequestCategory();
                                                    FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction()
                                                            .replace(R.id.mainFrame, frag).commit();
                                                }

                                            }else{
                                                Toast.makeText(rootView.getContext(), "Incorrect Day Pass Code!!!",Toast.LENGTH_LONG).show();
                                                dialog.cancel();
                                            }

                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();



                                } catch (Exception ex) {
                                    // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    Log.d("ReminderService In", " Ex 2 "+ex.getMessage() );
                                }
                                //############################
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                      /// con.close();

                    } catch (Exception ex) {
                        Log.d("ReminderService In", " Ex 2 "+ex.getMessage() );
                    }
                    //------------






            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_schedule.getVisibility() == View.GONE && (!txttotalnoticeonceoff.getText().toString().equals("0"))) {
                    onceoffOrWeek="onceoff";
                    lock.setVisibility(View.VISIBLE);
                }else if(btn_schedule.getVisibility() == View.VISIBLE && (!txttotalnoticeonceoff.getText().toString().equals("0"))){
                    Fragment frag = new MyListFragNotificationAllCategory();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "No Request Scheduled!!!",Toast.LENGTH_LONG).show();
                }



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_schedule.getVisibility() == View.GONE && (!txttotalnoticeweekly.getText().toString().equals("0"))) {
                    onceoffOrWeek="weekly";
                    lock.setVisibility(View.VISIBLE);
                }else if(btn_schedule.getVisibility() == View.VISIBLE && (!txttotalnoticeweekly.getText().toString().equals("0"))){
                    Fragment frag =   new MyListFragNotification();
                    Bundle bundle = new Bundle();
                    bundle.putString("week", "week");
                    frag.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }else{
                    Toast.makeText(rootView.getContext(), "No Request Scheduled!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_schedule.getVisibility() == View.GONE ) {
                    onceoffOrWeek="share";
                    lock.setVisibility(View.VISIBLE);
                }else if(btn_schedule.getVisibility() == View.VISIBLE ){
                    Fragment frag = new ChefDishListAllRequestCategory();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                }
               /* Fragment frag = new ChefDishListAllRequestCategory();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();*/




            }
        });
       btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {

                        Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
                    } else {
                        String query1 = "select * from [AppUserAssistance]  where [userid]=" + activity.edthidenuserid.getText().toString();
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        if ( rs1.getRow() == 0) {
                            //Toast.makeText(rootView.getContext(),  "", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle("No Assistant Available");
                            builder.setMessage("Subscribe & Add House Assistant");
                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.profil));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment = new HomeFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, fragment).commit();
                                }
                            });

                            builder.show();
                        }else{
                            Fragment frag =   new MyListFrag();
                            Bundle bundle = new Bundle();
                            bundle.putString("ProdIDingredients", "ProdIDingredients");
                            frag.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                        }



                    }
                } catch (Exception ex) {

                }


            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     System.exit(0);
            }
        });




        OnceOffNoticeNumber();



        return rootView;
    }

    public void OnceOffNoticeNumber() {
        //==============Initialize list=

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null)
            {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            }

                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();
                // rs11.getString("userid").toString();

                //OnceOffNoticeTotal
                if (activity.edthidenuserid.getText().toString().equals("")) {
                    //Assistant
                    String  query = "select * from [UserProductNotification] where [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]!='1' and [isread]='No'  order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        totalnoticeonceoff=totalnoticeonceoff+1;
                    }
                    String  query3 = "select *  from [UserProductNotification] where [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [requesttypeid]='1' and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        totalnoticeweekly=totalnoticeweekly+1;
                    }
                    //-------- Invenstory currently about to expire in Product
                 String  query4  = "select *  from [UserProduct] where [userid]="+rs11.getString("userid").toString()+" and  [isscanned]='No' and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";
                    PreparedStatement ps4 = con.prepareStatement(query4);
                    ResultSet rs4 = ps4.executeQuery();
                    while (rs4.next()) {

                        //+++++++++++++++++++++++++++++++
                        String query5 = "select * from [Dish]  order by [id] asc";
                        PreparedStatement ps5 = con.prepareStatement(query5);
                        ResultSet rs5 = ps5.executeQuery();

                        while (rs5.next()) {
                            if(rs5.getString("keyingredient").toString().toLowerCase().contains(rs4.getString("name").toString().toLowerCase())){
                                //Check if already recommended and inserted to DishIdeas
                                String query6 = "select * from [DishIdeas] where [keyingredient]='"+rs5.getString("keyingredient").toString()+"'";
                                PreparedStatement ps6 = con.prepareStatement(query6);
                                ResultSet rs6 = ps6.executeQuery();
                                rs6.next();
                                if (rs6.getRow() == 0) {
                                    String query7 = "insert into [DishIdeas]([name],[image],[keyingredient],[recipe],[preptime],[cost],[isread],[userid]) " +
                                            "values ('" + rs5.getString("name").toString() + "','" + rs5.getString("image").toString() + "','" + rs5.getString("keyingredient").toString() + "','" + rs5.getString("recipe").toString() + "','" + rs5.getString("preptime").toString() + "','" + rs5.getString("cost").toString() + "','No','" + Integer.parseInt(rs11.getString("userid").toString()) + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query7);
                                    preparedStatement.executeUpdate();
                                }

                            }

                        }
                        //+++++++++++++++++++++++++++++++++++
                    }
                    // Ideas
                    String  query1_ideas = "select *  from [DishIdeas] where  [userid]='"+rs11.getString("userid").toString()+"' and [isread]='No'";
                    PreparedStatement ps1_ideas = con.prepareStatement(query1_ideas);
                    ResultSet rs1_ideas = ps1_ideas.executeQuery();
                    totalnoticedish=0;
                    while (rs1_ideas.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }
                    //Dish shared
                    String  query_share = "select *  from [DishAssistantShare] where [userid]='"+rs11.getString("userid").toString()+"' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps_share = con.prepareStatement(query_share);
                    ResultSet rs_share = ps_share.executeQuery();

                    while (rs_share.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }
                    // Recipes
                    String  query2 = "select *  from [Dish] where [userid]='" + rs11.getString("userid").toString() + "' and [recipe]!='Not Specified' order by [id] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();

                    while (rs2.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }
                    txttotalnoticedish.setText(String.valueOf(totalnoticedish));
                }else {//########################
                   //co-user
                    String  query = "select *  from [UserProductNotification] where [userid]='"+activity.edthidenuserid.getText().toString()+"'  and [requesttypeid]!='1' and  [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        totalnoticeonceoff=totalnoticeonceoff+1;
                           }

                    String  query3 = "select *  from [UserProductNotification] where [userid]='"+activity.edthidenuserid.getText().toString()+"' and [requesttypeid]='1' and [isread]='No' order by [categoryid] asc";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        totalnoticeweekly=totalnoticeweekly+1;
                    }
/*
                    //@@@@@@@@@@ Invenstory currently about to expire in Product and insert to DishIdeas
                    String  query4  = "select *  from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  [isscanned]='No' and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";
                    PreparedStatement ps4 = con.prepareStatement(query4);
                    ResultSet rs4 = ps4.executeQuery();
                    while (rs4.next()) {


                        //+++++++++++++++++++++++++++++++
                        String query5 = "select * from [Dish]  order by [id] asc";
                        PreparedStatement ps5 = con.prepareStatement(query5);
                        ResultSet rs5 = ps5.executeQuery();

                       while (rs5.next()) {
                            if(rs5.getString("keyingredient").toString().toLowerCase().contains(rs4.getString("name").toString().toLowerCase())){
                                //Check if already recommended and inserted to DishIdeas
                                String query6 = "select * from [DishIdeas] where [keyingredient]='"+rs5.getString("keyingredient").toString()+"'";
                                PreparedStatement ps6 = con.prepareStatement(query6);
                                ResultSet rs6 = ps6.executeQuery();
                                rs6.next();
                                if (rs6.getRow() == 0) {
                                    String query7 = "insert into [DishIdeas]([name],[image],[keyingredient],[recipe],[preptime],[cost],[isread],[userid]) " +
                                            "values ('" + rs5.getString("name").toString() + "','" + rs5.getString("image").toString() + "','" + rs5.getString("keyingredient").toString() + "','" + rs5.getString("recipe").toString() + "','" + rs5.getString("preptime").toString() + "','" + rs5.getString("cost").toString() + "','No','" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query7);
                                    preparedStatement.executeUpdate();
                                }

                            }

                        }
                        //+++++++++++++++++++++++++++++++++++

                    }
                    //@@@@@@
                    */
                    // Orders
                    String  query_orders = "select *  from [DishOrder] where [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps_orders = con.prepareStatement(query_orders);
                    ResultSet rs_orders = ps_orders.executeQuery();
                    totalnoticedish=0;
                    while (rs_orders.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }

                    // Orderscustomers
                    String  query3_orderscustomers = "select *  from [DishOrder] where [useridchef]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'  order by [id] asc";
                    PreparedStatement ps3_orderscustomers = con.prepareStatement(query3_orderscustomers);
                    ResultSet rs3_orderscustomers = ps3_orderscustomers.executeQuery();
                    while (rs3_orderscustomers.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }

                    // Ideas
                    String  query1_ideas = "select * from [DishIdeas] where  [userid]='"+activity.edthidenuserid.getText().toString()+"' and [isread]='No'";
                    PreparedStatement ps1_ideas = con.prepareStatement(query1_ideas);
                    ResultSet rs1_ideas = ps1_ideas.executeQuery();

                    while (rs1_ideas.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }
                    // Dish shared
                    String query_share = "select *  from [DishAssistantShare] where [userid]='" + activity.edthidenuserid.getText().toString() + "' order by [id] asc";
                    PreparedStatement ps_share = con.prepareStatement(query_share);
                    ResultSet rs_share = ps_share.executeQuery();
                   while (rs_share.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }
                    // Recipes
                    String  query2 = "select *  from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [recipe]!='Not Specified' order by [id] asc";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();

                    while (rs2.next()) {
                        totalnoticedish=totalnoticedish+1;
                    }



                }
            Log.d("ReminderService In", " Ex  "+totalnoticeweekly+" "+totalnoticeonceoff+" "+totalnoticedish );



                txttotalnoticeonceoff.setText(String.valueOf(totalnoticeonceoff));
                txttotalnoticeweekly.setText(String.valueOf(totalnoticeweekly));
                txttotalnoticedish.setText(String.valueOf(totalnoticedish));


        } catch (Exception ex) {
           // Toast.makeText(rootView.getContext(), " Ex 2 "+ex.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", " Ex 2 "+ex.getMessage() );
        }

//===========

    }















}
