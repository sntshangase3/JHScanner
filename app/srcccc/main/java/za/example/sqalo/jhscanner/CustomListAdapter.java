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

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemquantity;
    private final  ArrayList <String> itemstatus;
    private final ArrayList<Bitmap> retailerimage;
    private final  ArrayList <String> itemrop;

    public CustomListAdapter(Activity context,  ArrayList<Bitmap> image,ArrayList <String> name,ArrayList <String> quantity,ArrayList <String> status,  ArrayList<Bitmap> retailimage,ArrayList <String> rop) {
        super(context, R.layout.lsttemplategross, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;
        this.itemquantity=quantity;
        this.itemstatus=status;
        this.retailerimage=retailimage;
        this.itemrop=rop;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplategross, null,true);

        ImageView productimage,retimage;
        TextView name,quantity,status,rop;



        productimage =  (ImageView) rootView.findViewById(R.id.lblproductimage);;
        retimage = (ImageView) rootView. findViewById(R.id.lblretailerimage);

        name = (TextView) rootView. findViewById(R.id.lblname);
        quantity = (TextView) rootView. findViewById(R.id.lblquantity);
       status = (TextView) rootView. findViewById(R.id.lblstatus);
        rop = (TextView) rootView. findViewById(R.id.lblrop);


try{
    productimage.setImageBitmap(itemimage.get(position));
    retimage.setImageBitmap(retailerimage.get(position));

    name.setText(itemname.get(position));
    int days=Integer.parseInt(itemstatus.get(position).toString().substring(0,itemstatus.get(position).toString().indexOf(" ")));
    int ropvalue=Integer.parseInt(itemrop.get(position).toString());
    int quantityvalue=Integer.parseInt(itemquantity.get(position));
    if(days==0){
        status.setBackground(rootView.getResources().getDrawable(R.drawable.status_expire));
    }else if(days>0 && days<=7){
        status.setBackground(rootView.getResources().getDrawable(R.drawable.status_prime));
    }else if(days>7){
        status.setBackground(rootView.getResources().getDrawable(R.drawable.status_fresh));
    }

    //Check if not reach R.O.P
    if(quantityvalue<=ropvalue){
        rop.setBackground(rootView.getResources().getDrawable(R.drawable.status_expire));
    }else{
        rop.setBackground(rootView.getResources().getDrawable(R.drawable.status_fresh));
    }


    status.setText(itemstatus.get(position));
    quantity.setText(itemquantity.get(position));
    rop.setText(itemrop.get(position));

}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
}


        return rootView;

    };
}