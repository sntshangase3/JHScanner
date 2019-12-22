package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleOrderFrag extends Fragment implements AdapterView.OnItemSelectedListener {


    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid,price;
    ImageView bundleprofileImage;
    int userid,qty,bundleid;


    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> description = new ArrayList<String>();



    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;

    Button btn_order,btn_cancel;
    TextView edtbundlename,edtquantity,edtprice;

    byte[] byteArray;
    String encodedImage;

    Bundle bundles = new Bundle();
    Bundle bundle;
    String selected_bundle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundleoder, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);


        edtbundlename = (TextView) rootView.findViewById(R.id.edtbundlename);
        edtquantity = (TextView) rootView.findViewById(R.id.edtquantity);
        edtprice = (TextView) rootView.findViewById(R.id.edtprice);


        bundleprofileImage = (ImageView) rootView.findViewById(R.id.bundleprofileImage);

        btn_order = (Button) rootView.findViewById(R.id.btn_order);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);


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
            Log.d("ReminderService In", ex.getMessage() + "######");
        }



        try {
            if (bundle != null) {
                if (!bundle.getString("name").equals("")) {
                    selected_bundle = bundle.getString("name");
                    FillDataOrderByStatus(selected_bundle);
                }
                if (!bundle.getString("bundlefinal").equals("")) {
                    btn_order.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.GONE);
                }else{
                    btn_order.setVisibility(View.VISIBLE);
                    btn_cancel.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");

        }

       
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {



                    bundles.putInt("bundleid", bundleid);
                    bundles.putInt("userid", userid);
                    bundles.putInt("qty", qty);
                    bundles.putString("price", price);

                    OrderPointBundleConfirmFrag fragment = new OrderPointBundleConfirmFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
                }


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new HomeFragmentOrderPoint();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });


        return rootView;
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(rootView.getContext(), "You've selected " + description.get(position) , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {

                //Co-user login
                String query = "SELECT b.id as b_id,name,price,quantity,b.image as b_image,description,bi.image as bi_image " +
                        "  FROM [Bundle] b " +
                        "  inner join [BundleItems] bi on bi.bundleid=b.id where [name]='" + search +"'";
                description.clear();
                proimage.clear();
            Log.d("ReminderService In",  query);
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    bundleid= rs.getInt("b_id");
                    qty=rs.getInt("quantity");
                    price=rs.getString("price");
                    byte[] decodeString1 = Base64.decode(rs.getString("b_image"), Base64.DEFAULT);
                    Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                    bundleprofileImage.setImageBitmap(decodebitmap1);
                    edtbundlename.setText(rs.getString("name"));
                    edtprice.setText("R"+rs.getString("price"));
                    edtquantity.setText(rs.getString("quantity")+" items");

                    description.add(rs.getString("description").toString());
                    byte[] decodeString = Base64.decode(rs.getString("bi_image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);

                }


                OrderPointBundleCreateAdapter adapter = new OrderPointBundleCreateAdapter(this.getActivity(), proimage, description);
                lstgross.setAdapter(adapter);


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


