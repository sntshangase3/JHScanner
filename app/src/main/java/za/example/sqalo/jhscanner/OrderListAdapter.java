package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemquantity;
    private final  ArrayList <String> itemorderdate;
    private final  ArrayList <String> itemchefname;

    public OrderListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name, ArrayList <String> quantity, ArrayList <String> orderdate, ArrayList <String> chefname) {
        super(context, R.layout.lsttemplategross, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;
        this.itemquantity=quantity;
        this.itemorderdate=orderdate;
        this.itemchefname=chefname;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateorder, null,true);

        ImageView productimage;
        TextView name,quantity,orderdate,chefname;



        productimage =  (ImageView) rootView.findViewById(R.id.lblproductimage);;
          name = (TextView) rootView. findViewById(R.id.lblname);
        quantity = (TextView) rootView. findViewById(R.id.lblquantity);
        orderdate = (TextView) rootView. findViewById(R.id.lblstatus);
        chefname = (TextView) rootView. findViewById(R.id.lblrop);


try{
    productimage.setImageBitmap(itemimage.get(position));
    name.setText(itemname.get(position));
    orderdate.setText(itemorderdate.get(position));
    quantity.setText(itemquantity.get(position));
    chefname.setText(itemchefname.get(position));

}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
}


        return rootView;

    };
}