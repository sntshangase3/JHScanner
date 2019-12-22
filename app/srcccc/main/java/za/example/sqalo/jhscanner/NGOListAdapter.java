package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NGOListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemdistance;
    Connection con;
    String un, pass, db, ip;
    LinearLayout llayout;
    public NGOListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name, ArrayList <String> distance) {
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
    logoimage.setImageBitmap(itemimage.get(position));
    name.setText(itemname.get(position));
    distance.setText(itemdistance.get(position));
    ConnectionClass cn = new ConnectionClass();
    con = cn.connectionclass(un, pass, db, ip);
    if (con == null) {
        Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
    } else {

        // Co-user login
        String selectedname = itemname.get(position);
        String query = "select * from [Ngo] where [name]='"+selectedname+ "'";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
try{
    if (rs.getString("imageprofile") != null) {
        byte[] decodeString = Base64.decode(rs.getString("imageprofile"), Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Drawable profile =new BitmapDrawable(rootView.getContext().getResources(),decodebitmap);
//rootView.setMinimumHeight(200);

        rootView.setBackground(profile);

    }
}catch (Exception ex){
    
}


//rootView.setBackground();
        }
    }


}catch (Exception ex){
    Log.d("ReminderService In", ex.getMessage().toString());
  //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
}


        return rootView;

    };
}