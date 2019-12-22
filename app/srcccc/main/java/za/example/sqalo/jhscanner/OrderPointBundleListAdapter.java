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

public class OrderPointBundleListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemname;
    private final ArrayList<String> itemquantity;
    private final ArrayList<Bitmap> itemcart ;
    private final ArrayList<String> itemprice ;

    Connection con;
    String un, pass, db, ip;

    public OrderPointBundleListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList<Bitmap> cart, ArrayList<String> name,ArrayList<String> qty, ArrayList<String> price) {
        super(context, R.layout.lsttemplateorderpointbundle, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemcart = cart;
        this.itemname = name;
        this.itemquantity=qty;
        this.itemprice = price;

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateorderpointbundle, null, true);

        ImageView cart;
        TextView name,qty,price;

        cart = (ImageView) rootView.findViewById(R.id.lblproimage);
        name = (TextView) rootView.findViewById(R.id.lblname);
        qty = (TextView) rootView.findViewById(R.id.lblqty);
        price = (TextView) rootView.findViewById(R.id.lblprice);

        try {


            name.setText(itemname.get(position).toString());
            price.setText(itemprice.get(position).toString());
            qty.setText(itemquantity.get(position).toString());
            cart.setImageBitmap(itemcart.get(position));
            Bitmap back=itemimage.get(position);

            Drawable profile =new BitmapDrawable(rootView.getContext().getResources(),back);


          rootView.setBackground(profile);

        } catch (Exception ex) {
//            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}