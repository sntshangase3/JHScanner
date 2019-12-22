package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;

public class OrderPointBundleConfirmAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;

    private final ArrayList<String> itemid;
    private final ArrayList<String> itemdeliveryday;
    private final ArrayList<String> itemhourrange;
      private final ArrayList<String> itemcost ;
    private final ArrayList<Bitmap> itemtimer ;

    Connection con;
    String un, pass, db, ip;

    public OrderPointBundleConfirmAdapter(Activity context, ArrayList<String> id, ArrayList<String> day, ArrayList<String> hour, ArrayList<String> cost,ArrayList<Bitmap> time) {
        super(context, R.layout.lsttemplateorderpointbundledeliveryschedule, id);
        // TODO Auto-generated constructor stub

        this.context = context;

        this.itemid = id;
        this.itemdeliveryday = day;
        this.itemhourrange=hour;
        this.itemcost = cost;
        this.itemtimer = time;

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateorderpointbundledeliveryschedule, null, true);

        ImageView cart;
        TextView id,name,qty,price;

        id = (TextView) rootView.findViewById(R.id.lblid);
        name = (TextView) rootView.findViewById(R.id.lblday);
        qty = (TextView) rootView.findViewById(R.id.lbltime);
        price = (TextView) rootView.findViewById(R.id.lblcost);
        cart = (ImageView) rootView.findViewById(R.id.lblproductimage);

        try {


            name.setText(itemdeliveryday.get(position).toString());
            price.setText(itemcost.get(position).toString());
            qty.setText(itemhourrange.get(position).toString());
            id.setText(itemid.get(position));
            cart.setImageBitmap(itemtimer.get(position));


        } catch (Exception ex) {
//            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}