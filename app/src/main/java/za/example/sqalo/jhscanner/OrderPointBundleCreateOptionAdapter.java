package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;

public class OrderPointBundleCreateOptionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemname;



    Connection con;
    String un, pass, db, ip;

    public OrderPointBundleCreateOptionAdapter(Activity context, ArrayList<Bitmap> image, ArrayList<String> name) {
        super(context, R.layout.lsttemplateorderpointseachoption, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemname = name;



        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateorderpointseachoption, null, true);

        ImageView proimage;
        TextView name,option;

        proimage = (ImageView) rootView.findViewById(R.id.lblproimage);
        name = (TextView) rootView.findViewById(R.id.lblname);



        try {


            name.setText(itemname.get(position).toString());
            proimage.setImageBitmap(itemimage.get(position));


        } catch (Exception ex) {
//            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}