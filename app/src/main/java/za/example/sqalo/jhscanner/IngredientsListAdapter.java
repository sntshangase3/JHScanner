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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class IngredientsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemdescription;
    private final ArrayList<String> itemqty;

    Connection con;
    String un, pass, db, ip;

    public IngredientsListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList<String> description,ArrayList<String> qty) {
        super(context, R.layout.lsttemplateingredientschecklist, description);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemdescription = description;
        this.itemqty=qty;

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateingredientschecklist, null, true);

        ImageView logoimage;
        TextView description,qty;
        logoimage = (ImageView) rootView.findViewById(R.id.lbllogoimage);
        description = (TextView) rootView.findViewById(R.id.lbldescribtion);
        qty = (TextView) rootView.findViewById(R.id.lblquantity);


        try {


            description.setText(itemdescription.get(position).toString());
            qty.setText(itemqty.get(position).toString());
            logoimage.setImageBitmap(itemimage.get(position));

           /* ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            String query = "select * from [UserProduct] where [description] = '" +itemdescription.get(position).toString() + "' and [quantity] = '" + itemqty.get(position).toString() + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            }*/

            int quantityvalue=Integer.parseInt(itemqty.get(position));
            if(quantityvalue>0){
                qty.setBackground(rootView.getResources().getDrawable(R.drawable.button_bground_login2));
            }else{
                qty.setBackground(rootView.getResources().getDrawable(R.drawable.button_bground_login1));
            }



        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}