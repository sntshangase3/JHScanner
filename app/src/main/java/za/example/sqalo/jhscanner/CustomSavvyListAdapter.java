package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomSavvyListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemprice;
    private final  ArrayList <String> itemquantity;


    Connection con;
    String un, pass, db, ip;

    public CustomSavvyListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name, ArrayList <String> price, ArrayList <String> quantity) {
        super(context, R.layout.lsttemplategrosssavvy, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;
        this.itemprice=price;
        this.itemquantity=quantity;


        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplategrosssavvy, null,true);

        ImageView productimage;
        TextView name,price,quantity;
        CheckBox isshop;



        productimage =  (ImageView) rootView.findViewById(R.id.lblproductimage);
        name = (TextView) rootView. findViewById(R.id.lblname);
        price = (TextView) rootView. findViewById(R.id.lblprice);
        quantity = (TextView) rootView. findViewById(R.id.lblquantity);
       isshop = (CheckBox) rootView. findViewById(R.id.chkisshopping);



try{
    productimage.setImageBitmap(itemimage.get(position));

    name.setText(itemname.get(position));
   price.setText("Price: R"+itemprice.get(position));
   quantity.setText("Qty: "+itemquantity.get(position));
    //price.setText(itemprice.get(position));
   // quantity.setText(itemquantity.get(position));


            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            String query = "select * from [UserProduct] where [description] = '" +itemname.get(position).toString() + "' and [totatitemvalue] = '" + itemprice.get(position).toString() + "' and [quantity] = '" + itemquantity.get(position).toString() + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();



            while (rs.next()) {



                //Check if not reach R.O.P
                int ropvalue=Integer.parseInt(rs.getString("reorderpoint").toString());
                int quantityvalue=Integer.parseInt(rs.getString("quantity"));
                if(quantityvalue<=ropvalue){
                    if(rs.getString("isscanned").equals("Yes") ){
                        isshop.setChecked(true);
                        Log.d("ReminderService In", "scn yes ");
                    }
                  //  isshop.setBackground(rootView.getResources().getDrawable(R.drawable.status_expire));
                }else{
                    if(rs.getString("isscanned").equals("Yes") ){
                        isshop.setChecked(true);

                    }
                  // isshop.setBackground(rootView.getResources().getDrawable(R.drawable.status_fresh));

                }
            }












}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
}


        return rootView;

    };
}