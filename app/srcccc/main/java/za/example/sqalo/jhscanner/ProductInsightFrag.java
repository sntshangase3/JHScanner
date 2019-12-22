package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sibusison on 2017/07/30.
 */
public class ProductInsightFrag extends Fragment {

    View rootView;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    TextView txtwastelevelpercent, txtquantity, txtweight, txtvalue,
            txtvalueinventory, txtpercentinventory,
            txtvalueconsumption, txtpercentconsumption,
            txtvaluedonation, txtpercentdonation,
            txtvaluewaste, txtpercentwaste,txt_cancel;
    FragmentManager fragmentManager;
    Bundle bundle;
    ImageView productimage;
    MainActivity activity = MainActivity.instance;
    int quantity=0;
    int weight=0;
    String unit="";

    int yearlyinventory = 0;
    int yearlycosumption = 0;
    int yearlydonation = 0;
    int yearlywaste = 0;

    ImageButton btn_select_action;
    final CharSequence[] items = {"Product Insight","Shopping List", "GroRequest", "Recipe Idea"};
    Bundle bundles = new Bundle();
    String description="";
    String m_Text_donate = "";
    String firstname = "";
    int userid;
    int countinventory=0;
    int countshoppinglist=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.productsinsight, container, false);
        fragmentManager = getFragmentManager();
        txtwastelevelpercent = (TextView) rootView.findViewById(R.id.txtwastelevelpercent);
        txtquantity = (TextView) rootView.findViewById(R.id.txtquantity);
        txtweight = (TextView) rootView.findViewById(R.id.txtweight);
        txtvalue = (TextView) rootView.findViewById(R.id.txtvalue);




        txtvalueinventory = (TextView) rootView.findViewById(R.id.txtvalueinventory);
        txtpercentinventory = (TextView) rootView.findViewById(R.id.txtpercentinventory);

        txtvalueconsumption = (TextView) rootView.findViewById(R.id.txtvalueconsumption);
        txtpercentconsumption = (TextView) rootView.findViewById(R.id.txtpercentconsumption);

        txtvaluedonation = (TextView) rootView.findViewById(R.id.txtvaluedonation);
        txtpercentdonation = (TextView) rootView.findViewById(R.id.txtpercentdonation);

        txtvaluewaste = (TextView) rootView.findViewById(R.id.txtvaluewaste);
        txtpercentwaste = (TextView) rootView.findViewById(R.id.txtpercentwaste);

        productimage = (ImageView) rootView.findViewById(R.id.productimage);
        btn_select_action = (ImageButton) rootView. findViewById(R.id.btn_select_action);
        txt_cancel = (TextView) rootView. findViewById(R.id.txt_cancel);

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

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

        try {
            if (bundle != null) {


                if (!bundle.getString("barcode").toString().equals("")) {


                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    productimage.setImageBitmap(decodebitmap);
                    description=bundle.getString("description").toString();
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);


                    String query0 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='" + description + "' and  [isscanned]='Yes' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query0);
                    PreparedStatement ps0 = con.prepareStatement(query0);
                    ResultSet rs0 = ps0.executeQuery();
                    while (rs0.next()) {

                        countshoppinglist = countshoppinglist + Integer.parseInt(rs0.getString("quantity").toString());

                    }
                    //-------- Yearly Invenstory currently in Product

                    String query1 = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='" + description + "' and  [isscanned]='No' and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query1);
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();
                    while (rs1.next()) {

                        yearlyinventory = yearlyinventory + Integer.parseInt(rs1.getString("totatitemvalue").toString());
                        quantity = quantity + Integer.parseInt(rs1.getString("quantity").toString());
                        countinventory = countinventory + Integer.parseInt(rs1.getString("quantity").toString());
                        String numberOnly= rs1.getString("size").toString().replaceAll("[^0-9]", "");
                        weight = weight + Integer.parseInt(numberOnly);
                        unit = rs1.getString("size").toString().replaceAll("[^A-Za-z]+", "");
                    }
                    Log.d("ReminderService In", yearlyinventory+" "+quantity+ " "+weight+" "+unit);
                    txtvalueinventory.setText("R" + String.valueOf(yearlyinventory));

                    //-------- Yearly Consumption

                    String query2 = "select * from [UserProductConsumption] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='" + description + "'  and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query2);
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        yearlycosumption = yearlycosumption + Integer.parseInt(rs2.getString("totatitemvalue").toString());
                        quantity = quantity + Integer.parseInt(rs2.getString("quantity").toString());
                        String numberOnly= rs2.getString("size").toString().replaceAll("[^0-9]", "");
                        weight = weight + Integer.parseInt(numberOnly);

                    }
                    Log.d("ReminderService In", yearlycosumption+" "+quantity+ " "+weight+" "+unit);
                    txtvalueconsumption.setText("R" + String.valueOf(yearlycosumption));
                    //-------- Yearly Donation

                    String query3 = "select * from [UserProductDonation] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='"+description + "'  and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query3);
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        yearlydonation = yearlydonation + Integer.parseInt(rs3.getString("totatitemvalue").toString());
                        quantity = quantity + Integer.parseInt(rs3.getString("quantity").toString());
                        String numberOnly= rs3.getString("size").toString().replaceAll("[^0-9]", "");
                        weight = weight + Integer.parseInt(numberOnly);
                    }
                    Log.d("ReminderService In", yearlydonation+" "+quantity+ " "+weight+" "+unit);
                    txtvaluedonation.setText("R" + String.valueOf(yearlydonation));

                    String query4 = "select * from [UserProductWaste] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [description]='"+description + "'  and YEAR(Cast([purchasedate] as date))=YEAR(getDate())";
                    Log.d("ReminderService In", query4);

                    PreparedStatement ps4 = con.prepareStatement(query4);
                    ResultSet rs4 = ps4.executeQuery();
                    while (rs4.next()) {
                        yearlywaste = yearlywaste + Integer.parseInt(rs4.getString("totatitemvalue").toString());
                        quantity = quantity + Integer.parseInt(rs4.getString("quantity").toString());
                        String numberOnly= rs4.getString("size").toString().replaceAll("[^0-9]", "");
                        weight = weight + Integer.parseInt(numberOnly);
                    }
                    Log.d("ReminderService In", yearlywaste+" "+quantity+ " "+weight+" "+unit);
                    txtvaluewaste.setText("R" + String.valueOf(yearlywaste));
                    double totalvalue=yearlyinventory+yearlycosumption+yearlydonation+yearlywaste;

                    txtquantity.setText("Q:" + String.valueOf(quantity));
                    txtweight.setText("W:" + String.valueOf(weight)+unit);
                    txtvalue.setText("V:R" + String.valueOf((int)Math.round(totalvalue)));



                    txtwastelevelpercent.setText(String.valueOf((int)Math.round((yearlywaste/totalvalue)*100))+"%");

                    txtpercentinventory.setText(String.valueOf((int)Math.round((yearlyinventory/totalvalue)*100))+"%");
                    txtpercentconsumption.setText(String.valueOf((int)Math.round((yearlycosumption/totalvalue)*100))+"%");
                    txtpercentdonation.setText(String.valueOf((int)Math.round((yearlydonation/totalvalue)*100))+"%");
                    txtpercentwaste.setText(String.valueOf((int)Math.round((yearlywaste/totalvalue)*100))+"%");


                }


            }
        } catch (Exception ex) {

            Log.d("ReminderService In", "Stack"+ex.getStackTrace().toString()+"Loca"+ex.getLocalizedMessage()+ex.getMessage());
        }
        btn_select_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


//#######################

                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    TextView title=new TextView(rootView.getContext());
                    title.setPadding(10,10,10,10);
                    title.setText(description+"\nInventory:" + String.valueOf(countinventory) + " & Shopping:"+String.valueOf(countshoppinglist));
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                    builder.setCustomTitle(title);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (items[which].equals("Shopping List")) {
                                try {
                                    String selecteddescription = description;
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
                            }
                            else if (items[which].equals("Product Insight")) {
                                try {
                                    String selecteddescription = description;
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

                                            String selecteddescription = description;
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
                                    String selecteddescription = description;
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
                //##################
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag =   new SearchListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });

        return rootView;
    }


}
