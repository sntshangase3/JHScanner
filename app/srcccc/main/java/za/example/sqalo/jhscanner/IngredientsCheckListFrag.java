package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class IngredientsCheckListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    int userid;
    final CharSequence[] items = {"Shopping List", "GroRequest", "Recipe Idea"};
    ListView lstgross;


    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> description = new ArrayList<String>();
    ArrayList<String> qty = new ArrayList<String>();


    int total=0;
    int count=0;
   Button btn_done;

    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    EditText edtsearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ingredientchecklist, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ArrayList<ItemData> list = new ArrayList<>();
        // list.add(new ItemData("Select Retailer",R.drawable.blackproduct));
        list.add(new ItemData("Checkers", R.drawable.logocheckers));
        list.add(new ItemData("OK Food", R.drawable.logook));
        list.add(new ItemData("Pick n Pay", R.drawable.logopicknpay));
        list.add(new ItemData("Shoprite", R.drawable.logoshoprite));
        list.add(new ItemData("Spar", R.drawable.logospar));
        list.add(new ItemData("USave", R.drawable.logousaveshoprite));

        list.add(new ItemData("Makro", R.drawable.logomakro));
        list.add(new ItemData("Food Lovers", R.drawable.logofoodlovers));
        list.add(new ItemData("Woolworth Food", R.drawable.logowoolworth));
        list.add(new ItemData("Boxer", R.drawable.logoboxer));
        list.add(new ItemData("Game", R.drawable.logogame));
        list.add(new ItemData("Fruits & Veg", R.drawable.logofruitsandveg));
        list.add(new ItemData("Other Retailers", R.drawable.logootherretailers));




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
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            }

        } catch (Exception ex) {

        }




        try{
            if(bundle != null) {
                if (!bundle.getString("name").equals("")) {
                    Log.d("ReminderService In", bundle.getString("name") + "######");
                    edtsearch.setText(bundle.getString("Loading Please Wait..."));
                    FillDataOrderByStatus(bundle.getString("keyingredient"));
                    edtsearch.setText(bundle.getString("name"));
                }
            }
        }catch (Exception ex){

        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new ChefDishDetailsFragment();
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });




        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {




    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }



    public void FillDataOrderByStatus(String name_Or_ingredients) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {
               // String[] words = name_Or_ingredients.split("\\W+");
                description.clear();
                proimage.clear();
                qty.clear();

                String ingredients=name_Or_ingredients.toString().replace(", ",",");
                StringTokenizer tokenizer = new StringTokenizer(ingredients, ",");
                while (tokenizer.hasMoreTokens()) {
                    String descriptions = tokenizer.nextToken();
                    String query = "select * from [UserProduct] where [userid]='" + userid + "' and [description] ='" + descriptions + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    // if(rs.next() ){
                    while (rs.next()) {
                        count=count+1;

                    }

                    Log.d("ReminderService In", query);
                    if(count>0){
                        Log.d("ReminderService In", "Yes######"+count);
                        Log.d("ReminderService In", "Yes######"+descriptions);
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {



                            description.add(rs.getString("description").toString());
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            proimage.add(decodebitmap);
                            qty.add(rs.getString("quantity").toString());
                        }
                    }else{
                        Log.d("ReminderService In", "No######"+descriptions.trim());
                        query = "select * from [UserProduct] where  [isscanned]='No' and [description] ='" + descriptions + "'";
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            description.add(rs.getString("description").toString());
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            proimage.add(decodebitmap);
                            qty.add("0");
                        }

                    }





                    count=0;


                }


                IngredientsListAdapter adapter = new IngredientsListAdapter(this.getActivity(), proimage, description,qty);

                 lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {


//#######################

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

                           /* TextView title = new TextView(rootView.getContext());
                            title.setPadding(10, 10, 10, 10);
                            title.setText(description.get(position).toString());
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(getResources().getColor(R.color.colorPrimary));
                            builder.setCancelable(true);
                            builder.setCustomTitle(title);
                            builder.setMessage("                  Select Action?");
                           // builder.setIcon(rootView.getResources().getDrawable(R.drawable.add));*/
                            builder.setTitle(description.get(position).toString()).setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (items[which].equals("Shopping List")) {
                                        try {
                                            String selecteddescription = description.get(position).toString();
                                            String query = "select * from [UserProduct] where [description]='" + selecteddescription + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundles.putString("id", rs.getString("id").toString());
                                                bundles.putString("bestbefore", rs.getString("bestbefore").toString());
                                                bundles.putString("name", rs.getString("name").toString());
                                                bundles.putString("description", rs.getString("description").toString());
                                                bundles.putString("website", rs.getString("website").toString());
                                                bundles.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                                                bundles.putString("size", rs.getString("size").toString());
                                                bundles.putString("price", rs.getString("price").toString());
                                                bundles.putString("storage", rs.getString("storage").toString());
                                                bundles.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundles.putString("quantity", rs.getString("quantity").toString());

                                                bundles.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundles.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundles.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundles.putString("isscanned", rs.getString("isscanned").toString());
                                                bundles.putString("image", rs.getString("image").toString());
                                                bundles.putString("categoryid", rs.getString("categoryid").toString());
                                                bundles.putString("retailerid", rs.getString("retailerid").toString());
                                                bundles.putString("barcode", rs.getString("Barcode").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                AddProductFragOcr fragment = new AddProductFragOcr();
                                                fragment.setArguments(bundles);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    } else if (items[which].equals("GroRequest")) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(rootView.getContext());

                                        builder1.setTitle("GroRequest");
                                        builder1.setMessage("Grocery Request:");
                                        builder1.setIcon(rootView.getResources().getDrawable(R.drawable.request));
                                        final EditText input = new EditText(rootView.getContext());
                                        input.setGravity(Gravity.CENTER);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                        input.setHint("Enter Quantity");
                                        input.setHintTextColor(Color.GRAY);
                                        builder1.setView(input);

                                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                try {

                                                    String selecteddescription = description.get(position).toString();
                                                    String query = "select * from [UserProduct] where [description]='" + selecteddescription + "'";
                                                    PreparedStatement ps = con.prepareStatement(query);
                                                    ResultSet rs = ps.executeQuery();
                                                    while (rs.next()) {

                                                        Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();

                                                        Date today = new Date();
                                                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                                        String todaydate = date_format.format(today);

                                                        String refno = rs.getString("name").toString().substring(0, 3).toUpperCase() + today.getHours() + "" + today.getMinutes() + "" + today.getSeconds();
                                                        String message = "GroRequest Ref_No:" + refno + " by " + firstname + " to purchase Product:" + rs.getString("name").toString() + "\nQuantity:" + m_Text_donate + "\nDescription:" + rs.getString("description").toString() + "\nLogin GoingDots App";
                                                        String commands = "insert into [UserProductShoppingList] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[isread],[Barcode],[website],[image_name],[image_type])" +
                                                                "values ('" + rs.getString("bestbefore").toString() + "','" + rs.getString("name").toString() + "','" + rs.getString("description").toString() + "','" +
                                                                rs.getString("bestbeforeStatus").toString() + "','" + rs.getString("size").toString() + "','" + Double.parseDouble(rs.getString("price").toString()) + "','" +
                                                                rs.getString("storage").toString() + "','" + Integer.parseInt(rs.getString("preferbestbeforedays").toString()) + "','" + Integer.parseInt(m_Text_donate) + "','" + Integer.parseInt(rs.getString("quantityperbulk").toString()) + "','" + Integer.parseInt(rs.getString("reorderpoint").toString()) + "','" + Double.parseDouble(rs.getString("totatitemvalue").toString()) +
                                                                "','" + rs.getString("isscanned").toString() + "','" + todaydate + "','" + rs.getString("image").toString() + "','" + userid + "','" + Integer.parseInt(rs.getString("categoryid").toString()) + "','" + Integer.parseInt(rs.getString("retailerid").toString()) + "','No','" + rs.getString("Barcode").toString() + "','" + rs.getString("website").toString() + "','" + refno + "','" + message + "')";
                                                        PreparedStatement preStmt = con.prepareStatement(commands);
                                                        preStmt.executeUpdate();
                                                        //Search contact and send message
                                                        // sendSMSwithDeliveryReport("+27795357393","Testing");
                                                        Intent i = new Intent(rootView.getContext(), MainActivitySms.class);
                                                        Bundle extras = new Bundle();
                                                        extras.putString("message", message);
                                                        i.putExtras(extras);
                                                        startActivity(i);

                                                    }


                                                } catch (Exception ex) {
                                                    Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                                                    Log.d("ReminderService In", ex.getMessage());
                                                }

                                            }
                                        });
                                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder1.show();

                                    } else {
                                        try {
                                            String selecteddescription = description.get(position).toString();
                                            String query = "select * from [UserProduct] where [description]='" + selecteddescription + "'";
                                            PreparedStatement ps = con.prepareStatement(query);
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {

                                                //   Toast.makeText(rootView.getContext(), rs.getString("Barcode").toString(), Toast.LENGTH_SHORT).show();


                                                bundles.putString("id", rs.getString("id").toString());
                                                bundles.putString("bestbefore", rs.getString("bestbefore").toString());
                                                bundles.putString("name", rs.getString("name").toString());
                                                bundles.putString("description", rs.getString("description").toString());
                                                bundles.putString("website", rs.getString("website").toString());
                                                bundles.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                                                bundles.putString("size", rs.getString("size").toString());
                                                bundles.putString("price", rs.getString("price").toString());
                                                bundles.putString("storage", rs.getString("storage").toString());
                                                bundles.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                                bundles.putString("quantity", rs.getString("quantity").toString());

                                                bundles.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                                bundles.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                                bundles.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                                bundles.putString("isscanned", rs.getString("isscanned").toString());
                                                bundles.putString("image", rs.getString("image").toString());
                                                bundles.putString("categoryid", rs.getString("categoryid").toString());
                                                bundles.putString("retailerid", rs.getString("retailerid").toString());
                                                bundles.putString("barcode", rs.getString("Barcode").toString());

                                                // bundles.putString("password",activity.edtpass.getText().toString());


                                                ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                fragment.setArguments(bundles);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                }

                            });
                            builder.show();



                        } catch (Exception ex) {
                            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString() + " NGOAllD");
                        }
                    }
                });


            }
        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }

  


}


