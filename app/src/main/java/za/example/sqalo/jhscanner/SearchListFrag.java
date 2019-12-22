package za.example.sqalo.jhscanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class SearchListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    final CharSequence[] items = {"Member Allergens","Product Insight", "Shopping List", "GroRequest", "Recipe Idea"};
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

    int catergoryclick = 0;
    int retailerclick = 0;
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> description = new ArrayList<String>();


    ArrayAdapter adapter1;
    Spinner spinnerretailer, spinnercategory;


    ImageButton search, btn_category, btn_retailer;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    EditText edtsearch;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_product, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        search = (ImageButton) rootView.findViewById(R.id.search);
        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);

        btn_category = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_retailer = (ImageButton) rootView.findViewById(R.id.filterretailer);

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


        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        adapter = new SpinnerAdapter(this.getActivity(), R.layout.spinner_layout, R.id.txt, list);
        spinnerretailer.setAdapter(adapter);
        spinnerretailer.setOnItemSelectedListener(this);

        bundle = this.getArguments();

        FillCategoryData();
        spinnercategory.setOnItemSelectedListener(this);


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

        try {
            if (bundle != null) {
                if (!bundle.getString("name").equals("")) {
                    Log.d("ReminderService In", bundle.getString("name") + "######");
                    edtsearch.setText(bundle.getString("name"));
                    search_product = edtsearch.getText().toString();
                    FillDataOrderByStatus(search_product);
                }
            }
        } catch (Exception ex) {

        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                if (!search_product.equals("")) {
                    FillDataOrderByStatus(search_product);
                } else {
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Toast.makeText(rootView.getContext(), "Please enter search value!!!", Toast.LENGTH_LONG).show();
                    edtsearch.setBackground(errorbg);

                }


            }
        });

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catergoryclick = 1;
                spinnercategory.performClick();
                // FillDataOrderByCategory();

            }
        });
        btn_retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerclick = 1;
                spinnerretailer.performClick();
                // FillDataOrderByCategory();

            }
        });


        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
        //Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString() , Toast.LENGTH_LONG).show();
        if (catergoryclick == 1 || retailerclick == 1) {
            FillDataOrderByCategory();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    public void FillCategoryData() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                String query = "select * from [Category]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

               /* category.add("Food");
                category.add("Status");
                category.add("Retailer");*/
                while (rs.next()) {

                    category.add(rs.getString("productCategory"));
                }
                adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnercategory.setAdapter(adapter1);


            }


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }

    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {


                //Co-user login
                String query = "select * from [UserProduct] where [description] like '%" + search + "%'";
                if (catergoryclick == 1 && retailerclick == 1) {
                    query = "select * from [UserProduct] where [description] like '%" + search + "%' and [categoryid]=" + (spinnercategory.getSelectedItemPosition() + 1) + " and [retailerid]=" + (spinnerretailer.getSelectedItemPosition() + 1);
                } else if (catergoryclick == 1) {
                    query = "select * from [UserProduct] where [description] like '%" + search + "%' and [categoryid]=" + (spinnercategory.getSelectedItemPosition() + 1);

                } else if (retailerclick == 1) {
                    query = "select * from [UserProduct] where [description] like '%" + search + "%' and [retailerid]=" + (spinnerretailer.getSelectedItemPosition() + 1);

                }

                description.clear();
                proimage.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    description.add(rs.getString("description").toString());
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);
                }


                SearchListAdapter adapter = new SearchListAdapter(this.getActivity(), proimage, description);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {


//#######################
                            final String selecteddescription = description.get(position).toString();
                            int countinventory = 0;
                            int countshoppinglist = 0;
                            String query0 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='" + selecteddescription + "' and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                            Log.d("ReminderService In", query0);
                            PreparedStatement ps0 = con.prepareStatement(query0);
                            ResultSet rs0 = ps0.executeQuery();
                            while (rs0.next()) {

                                countshoppinglist = countshoppinglist + Integer.parseInt(rs0.getString("quantity").toString());

                            }
                            String query1 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='" + selecteddescription + "' and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                            Log.d("ReminderService In", query1);
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            while (rs1.next()) {
                                countinventory = countinventory + Integer.parseInt(rs1.getString("quantity").toString());

                            }
                            //===========

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                            Log.d("ReminderService In", selecteddescription + "\nInventory:" + String.valueOf(countinventory) + " & Shopping:" + String.valueOf(countshoppinglist));

                            TextView title = new TextView(rootView.getContext());
                            title.setPadding(10, 10, 10, 10);
                            title.setText(selecteddescription + "\nInventory:" + String.valueOf(countinventory) + " & Shopping:" + String.valueOf(countshoppinglist));
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(getResources().getColor(R.color.colorPrimary));
                            builder.setCustomTitle(title);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (items[which].equals("Shopping List")) {
                                        try {


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
                                    }else if (items[which].equals("Member Allergens")) {

                                        try {

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

                                                String query1 = "    select a.Allergies,up.name,up.id from UserProduct up\n" +
                                                        "  inner join ProductAllergies pa on pa.ProductId=up.id\n" +
                                                        "  inner join Allergies a on a.Id=pa.AllergiesId" +
                                                        "  where up.id='" + rs.getString("id").toString() + "'";
                                                Log.d("ReminderService In", query);
                                                PreparedStatement ps1 = con.prepareStatement(query1);
                                                ResultSet rs1 = ps.executeQuery();
                                                String allergy="";
                                                rs1.next();
                                                if(rs1.getRow()!=0){
                                                    Log.d("ReminderService In", "AddProductFragAllergy clickke");
                                                    AddProductFragAllergy fragment = new AddProductFragAllergy();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                                }else{
                                                    Toast.makeText(rootView.getContext(), "No Allergy for this Product", Toast.LENGTH_LONG).show();
                                                    AddProductFragAllergy fragment = new AddProductFragAllergy();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                                }


                                            }



                                        } catch (Exception ex) {

                                        }

                                    }
                                    else if (items[which].equals("Product Insight")) {
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


                                                ProductInsightFrag fragment = new ProductInsightFrag();
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

    public void FillDataOrderByCategory() {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {
                //Co-user login
                String query = "select top 2 * from [UserProduct]";// where [categoryid]="+(spinnercategory.getSelectedItemPosition()+1);
                search_product = edtsearch.getText().toString();
                if (!search_product.equals("")) {
                    Log.d("ReminderService In", "Serach not empty");
                    if (catergoryclick == 1 && retailerclick == 1) {
                        query = "select * from [UserProduct] where  contains([description] ,'" + search_product + "')  and [categoryid]=" + (spinnercategory.getSelectedItemPosition() + 1) + " and [retailerid]=" + (spinnerretailer.getSelectedItemPosition() + 1);
                    } else if (catergoryclick == 1) {
                        query = "select * from [UserProduct] where contains([description] ,'" + search_product + "')  and [categoryid]=" + (spinnercategory.getSelectedItemPosition() + 1);

                    } else if (retailerclick == 1) {
                        query = "select * from [UserProduct] where contains([description] ,'" + search_product + "')  and [retailerid]=" + (spinnerretailer.getSelectedItemPosition() + 1);

                    }
                } else {
                    Log.d("ReminderService In", "Serach empty");

                    if (catergoryclick == 1 && retailerclick == 1) {
                        Log.d("ReminderService In", "1-1");
                        query = "select * from [UserProduct] where   [categoryid]='" + (spinnercategory.getSelectedItemPosition() + 1) + "' and [retailerid]=" + (spinnerretailer.getSelectedItemPosition() + 1);
                    } else if (catergoryclick == 1) {
                        Log.d("ReminderService In", "1-0");
                        query = "select * from [UserProduct] where  [categoryid]='" + (spinnercategory.getSelectedItemPosition() + 1) + "'";

                    } else if (retailerclick == 1) {
                        Log.d("ReminderService In", "0-1");
                        query = "select * from [UserProduct] where  [retailerid]='" + (spinnerretailer.getSelectedItemPosition() + 1) + "'";

                    }
                }


                description.clear();
                proimage.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    try {
                        description.add(rs.getString("description").toString());
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Log.d("ReminderService In", rs.getString("name") + decodeString.length);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        proimage.add(decodebitmap);
                    } catch (OutOfMemoryError ex) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Log.d("ReminderService In", rs.getString("name") + decodeString.length + "######");

                    }

                }


                SearchListAdapter adapter = new SearchListAdapter(this.getActivity(), proimage, description);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {


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

                                    }else if (items[which].equals("Member Allergens")) {

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

                                                String query1 = "    select a.Allergies,up.name,up.id from UserProduct up\n" +
                                                        "  inner join ProductAllergies pa on pa.ProductId=up.id\n" +
                                                        "  inner join Allergies a on a.Id=pa.AllergiesId" +
                                                        "  where up.id='" + rs.getString("id").toString() + "'";
                                                Log.d("ReminderService In", query);
                                                PreparedStatement ps1 = con.prepareStatement(query1);
                                                ResultSet rs1 = ps.executeQuery();
                                                String allergy="";
                                                rs1.next();
                                                if(rs1.getRow()!=0){
                                                    Log.d("ReminderService In", "AddProductFragAllergy clickke");
                                                    AddProductFragAllergy fragment = new AddProductFragAllergy();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                                }else{
                                                    Toast.makeText(rootView.getContext(), "No Allergy for this Product", Toast.LENGTH_LONG).show();
                                                    AddProductFragAllergy fragment = new AddProductFragAllergy();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                                }


                                            }



                                        } catch (Exception ex) {

                                        }

                                    }
                                    else if (items[which].equals("GroRequest")) {
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
            //   Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }


}


