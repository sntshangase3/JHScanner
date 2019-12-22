package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleRecipeFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip, price,name,recipename, ordertype,instructions,ingredients,image;
    String firstname = "";

    Calendar date;
    int userid,  bundleid, selectedquantity;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    ListView lstgross;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    Date bestbefore = null;
    Bundle bundles = new Bundle();

    LinearLayout loginlayout;
    Calendar c;
    TextView edtrecipename, txtback,edtingredient,edtinstruction;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundlerecipe, container, false);
        edtrecipename = (TextView) rootView.findViewById(R.id.edtrecipename);
        txtback = (TextView) rootView.findViewById(R.id.txtback);
        loginlayout = (LinearLayout) rootView.findViewById(R.id.layout);
        edtingredient = (TextView) rootView.findViewById(R.id.edtingredient);
        edtinstruction = (TextView) rootView.findViewById(R.id.edtinstruction);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        bundle = this.getArguments();


        try {
            bundleid = bundle.getInt("bundleid");
            selectedquantity = bundle.getInt("qty");
            price = bundle.getString("price");
            ordertype = bundle.getString("ordertype");
            name = bundle.getString("name");
            recipename = bundle.getString("recipename");
            ingredients = bundle.getString("ingredients");
            instructions = bundle.getString("instructions");
            image = bundle.getString("image");
            FillScheduleData();




                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");



        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }


        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {




                        bundles.putInt("id", bundleid);
                        bundles.putString("name",name);
                        bundles.putString("price", price);
                        bundles.putInt("quantity", selectedquantity);

                    OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
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




    public void FillScheduleData() {
        //==============Fill Data=FillScheduleData

        try {

                edtrecipename.setText(recipename);
                String newing="\u2022";
                for(int i=0;i<ingredients.length();i++){
                    if(ingredients.charAt(i)=='.'){
                        newing=newing+ "\n\u2022";
                    }else {
                        newing=newing+ ingredients.charAt(i);
                    }
                }
            edtingredient.setText(newing);
                newing="\u2022";
            for(int i=0;i<instructions.length();i++){
                if(instructions.charAt(i)=='.'){
                    newing=newing+ "\n\u2022";
                }else {
                    newing=newing+ instructions.charAt(i);
                }
            }
            edtinstruction.setText(newing);


            byte[] decodeString1 = Base64.decode(image, 0);
            Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);

            //Set rounded corner
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap1);
            roundedBitmapDrawable.setCornerRadius(100.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            loginlayout.setBackground(roundedBitmapDrawable);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }
}


