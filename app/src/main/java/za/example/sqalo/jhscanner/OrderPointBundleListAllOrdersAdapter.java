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

import java.util.ArrayList;

public class OrderPointBundleListAllOrdersAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemquantity;
    private final  ArrayList <String> itemorderdeliverydate;
    private final  ArrayList <String> itemorderstatus;
    //private final  ArrayList <String> itemordercost;


    public OrderPointBundleListAllOrdersAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name, ArrayList <String> quantity,
                                                ArrayList <String> orderdate,ArrayList <String> status) {
        super(context, R.layout.lsttemplateorderpointbundlelistallorders, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;
        this.itemquantity=quantity;
        this.itemorderdeliverydate=orderdate;
        this.itemorderstatus=status;
       // this.itemordercost=cost;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateorderpointbundlelistallorders, null,true);

        ImageView productimage;
        TextView name,quantity,orderdate,status,cost;

        productimage =  (ImageView) rootView.findViewById(R.id.lblproductimage);;
          name = (TextView) rootView. findViewById(R.id.lblname);
        quantity = (TextView) rootView. findViewById(R.id.lblquantity);
        orderdate = (TextView) rootView. findViewById(R.id.lblorderdate);
        status = (TextView) rootView. findViewById(R.id.lblstatus);
      //  cost = (TextView) rootView. findViewById(R.id.lblcost);


try{
    productimage.setImageBitmap(itemimage.get(position));
    name.setText(itemname.get(position));
    orderdate.setText(itemorderdeliverydate.get(position));
    quantity.setText(itemquantity.get(position));
    status.setText(itemorderstatus.get(position));
   // cost.setText(itemordercost.get(position));

}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", ex.getMessage().toString());
}
     return rootView;

    };
}