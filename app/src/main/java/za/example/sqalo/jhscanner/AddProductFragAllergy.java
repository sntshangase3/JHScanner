package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sibusison on 2017/07/30.
 */
public class AddProductFragAllergy extends Fragment {

    View rootView;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    private CompoundButton autoFocus;
    private CompoundButton useFlash;

    TextView txtAllergy, txtmembername, txtproductname, txtmemberallallergies, txtintolerance, txtback;
    String daysleftmessage_pass = "";
    FragmentManager fragmentManager;
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;
    Bitmap decodebitmap1;
    Button btnscan;
    Bundle bundle;
    String extractdate, extractformat;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "AddProductFragOcr";
    public long daysleft;
    ImageButton search_idea,btn_select_action;
    ImageView productimage, memberprofileImage, search, prev, next;
    final CharSequence[] items = {"Product Insight", "Shopping List", "GroRequest", "Recipe Idea", "Edit Product"};
    Bundle bundles = new Bundle();
    String m_Text_donate = "";
    String firstname = "";
    int userid;
    MainActivity activity = MainActivity.instance;
    int count = 0;
    int countshoppinglist = 0;
    String allergy = "", intolerance = "";
    ArrayList<RoundedBitmapDrawable> proimageRoundedBitmapDrawable = new ArrayList<RoundedBitmapDrawable>();
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> membyerallerg = new ArrayList<String>();
    ArrayList<String> memberintolerance = new ArrayList<String>();
    LinearLayout layoutmemberdetails;
    String description="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.addproductallery, container, false);
        fragmentManager = getFragmentManager();

        prev = (ImageView) rootView.findViewById(R.id.previous);
        next = (ImageView) rootView.findViewById(R.id.next);

        txtback = (TextView) rootView.findViewById(R.id.txtback);
        txtproductname = (TextView) rootView.findViewById(R.id.txtproductname);
        txtmembername = (TextView) rootView.findViewById(R.id.txtmembername);
        txtAllergy = (TextView) rootView.findViewById(R.id.txtAllergy);
        txtmemberallallergies = (TextView) rootView.findViewById(R.id.txtmemberallallergies);
        txtintolerance = (TextView) rootView.findViewById(R.id.txtintolerance);

        productimage = (ImageView) rootView.findViewById(R.id.productimage);
        memberprofileImage = (ImageView) rootView.findViewById(R.id.memberprofileImage);

        search = (ImageView) rootView.findViewById(R.id.search);
        layoutmemberdetails = (LinearLayout) rootView.findViewById(R.id.layoutmemberdetails);
        btn_select_action = (ImageButton) rootView. findViewById(R.id.btn_select_action);


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        bundle = this.getArguments();

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

        reload();

        btn_select_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


//#######################

                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    TextView title=new TextView(rootView.getContext());
                    title.setPadding(10,10,10,10);
                    title.setText(description);
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

                            } else if (items[which].equals("Recipe Idea")){
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
                            } else {
                                try {


                                    AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


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
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MyListFrag fragment = new MyListFrag();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SearchListFrag();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }
        });


        prev.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                return false;
            }
        });
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
                return false;
            }
        });


        return rootView;
    }
public void reload(){
    try {
        if (bundle != null) {

            if (!bundle.getString("id").toString().equals("")) {

                description=bundle.getString("description").toString();

                byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                productimage.setImageBitmap(decodebitmap);

                String query1 = "  select a.Allergies,i.Intolerance,up.name,up.id,up.description from UserProduct up\n" +
                        "                             join ProductAllergies pa on pa.ProductId=up.id\n" +
                        "                           join Allergies a on a.Id=pa.AllergiesId\n" +
                        " left join Intolerance i on i.Id=pa.IntoleranceId" +
                        "  where up.id='" + bundle.getString("id").toString() + "'";
                Log.d("ReminderService In", query1);
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();

                while (rs1.next()) {


                    if (rs1.getString("Allergies").toString() != null) {
                        allergy = allergy + rs1.getString("Allergies").toString() + ",";
                    }

                    try {
                        if (rs1.getString("Intolerance").toString() != null) {
                            intolerance = intolerance + rs1.getString("Intolerance").toString() + ",";
                        }

                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                    bundle.putString("id", rs1.getString("id").toString());
                    bundle.putString("name", rs1.getString("name").toString());
                    bundle.putString("description", rs1.getString("description").toString());
                }
                if (!allergy.equals("")) {
                    txtAllergy.setText(allergy.substring(0,allergy.length()-1));
                } else {
                    txtAllergy.setText("None");
                }
                if (!intolerance.equals("")) {
                    txtproductname.setText(intolerance.substring(0,intolerance.length()-1));
                } else {
                    txtproductname.setText("None");
                }
                Log.d("ReminderService In", allergy+"####"+intolerance);
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");
            }
        }
    } catch (Exception ex) {

        Log.d("ReminderService In", ex.getMessage().toString());
    }

}

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;
        // Integer.parseInt(edtappuserId.getText().toString());


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {
                setProfile(count);

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {
                    z = "Check your network connection!!";

                } else {
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;

                //  z = "Check your network connection!!";
                z = ex.getMessage().toString();
                Log.d("ReminderService In", ex.getMessage().toString());

            }

            return z;
        }
    }

    public void setProfile(int position) {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {



                String query = "select * from [AllergyMember] where userid='" + userid + "'";
                Log.d("ReminderService In", query);
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                proimage.clear();
                name.clear();
                membyerallerg.clear();
                memberintolerance.clear();
                while (rs.next()) {
                    if (rs.getString("allergy").toString().contains(allergy)) {
                        Log.d("ReminderService In", "Allegery:"+rs.getString("allergy").toString() + " " + allergy);
                        byte[] decodeString1 = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                        proimage.add(decodebitmap1);
                        name.add(rs.getString("name").toString());
                        membyerallerg.add(rs.getString("allergy").toString());
                        memberintolerance.add(rs.getString("intolerate").toString());

                    }else if ( rs.getString("intolerate").toString().contains(intolerance)) {
                        Log.d("ReminderService In", "Intolerance:"+rs.getString("intolerate").toString() + " " + intolerance);
                        byte[] decodeString1 = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                        proimage.add(decodebitmap1);
                        name.add(rs.getString("name").toString());
                        membyerallerg.add(rs.getString("allergy").toString());
                        memberintolerance.add(rs.getString("intolerate").toString());
                    }

                }

                if (proimage.size() > 0) {
                    layoutmemberdetails.setVisibility(View.VISIBLE);
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), proimage.get(position));
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    memberprofileImage.setImageDrawable(roundedBitmapDrawable);
                    txtmembername.setText(name.get(position));
                    txtmemberallallergies.setText(membyerallerg.get(position));
                    txtintolerance.setText(memberintolerance.get(position));
                    if (count != proimage.size()) {
                        count = count + 1;
                    }
                }else{
                    layoutmemberdetails.setVisibility(View.GONE);
                }


            }
            // con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurredDDDDDD: " + ex.getMessage());
            count = 0;
            UpdateProfile updatePro = new UpdateProfile();
            updatePro.execute("");
        }

    }

}
