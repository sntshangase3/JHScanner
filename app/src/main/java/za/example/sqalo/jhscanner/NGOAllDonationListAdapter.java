package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

public class NGOAllDonationListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity =   MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemdistance;
    Connection con;
    String un, pass, db, ip;

    public NGOAllDonationListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name, ArrayList <String> distance) {
        super(context, R.layout.lsttemplatengo, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;
        this.itemdistance=distance;
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplatengo, null,true);

        ImageView logoimage;
        TextView name,distance;
        logoimage =  (ImageView) rootView.findViewById(R.id.lbllogoimage);;
        name = (TextView) rootView. findViewById(R.id.lblname);
        distance = (TextView) rootView. findViewById(R.id.lbldistance);



try{

          // String donationstatus=itemname.get(position).substring(0,itemname.get(position).indexOf(" "));
    String donationstatus=itemname.get(position).toString();
   // String donationstatus=itemname.get(position).toString();
         String displayname=itemname.get(position).substring(itemname.get(position).indexOf(" ")).trim();

   //itemname.set(position,displayname);
    //String donationstatus=itemname.get(position).toString();
    name.setText(displayname);
    distance.setText(itemdistance.get(position));

           /* if(donationstatus.contains("Accepted")){
               name.setBackground(rootView.getResources().getDrawable(R.drawable.status_fresh));
            }else if(donationstatus.contains("Pending")){
                name.setBackground(rootView.getResources().getDrawable(R.drawable.status_prime));
            }else if(donationstatus.contains("Declined")){
                name.setBackground(rootView.getResources().getDrawable(R.drawable.status_expire));
            }*/



    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), itemimage.get(position));
    roundedBitmapDrawable.setCornerRadius(50.0f);
    roundedBitmapDrawable.setAntiAlias(true);
    logoimage.setImageDrawable(roundedBitmapDrawable);


}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
}


        return rootView;

    };
}