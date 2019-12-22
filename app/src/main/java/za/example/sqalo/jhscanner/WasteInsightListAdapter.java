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

public class WasteInsightListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    ArrayList<Bitmap> itemimage;
    ArrayList<String> itemwastevalue;


    ArrayList<String> itemexpirationc;
    ArrayList<String> itemspoilagec ;
    ArrayList<String> itempreparationc ;
    ArrayList<String> itemcookedc ;
    ArrayList<String> itemqualityc;
    ArrayList<String> itemusedc ;

    Connection con;
    String un, pass, db, ip;

    public WasteInsightListAdapter(Activity context, ArrayList<Bitmap> image,
                                   ArrayList<String> itemwastevalue,
                                   ArrayList<String> itemexpirationc ,
                                   ArrayList<String> itemspoilagec,
                                   ArrayList<String> itempreparationc ,
                                   ArrayList<String> itemcookedc,
                                   ArrayList<String> itemqualityc,
                                   ArrayList<String> itemusedc) {
        super(context, R.layout.lsttemplatewasteinsight, itemwastevalue);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemwastevalue=itemwastevalue;

        this.itemexpirationc = itemexpirationc;
        this.itemspoilagec=itemspoilagec;
        this.itempreparationc=itempreparationc;
        this.itemcookedc=itemcookedc;
        this.itemqualityc=itemqualityc;
        this.itemusedc=itemusedc;

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplatewasteinsight, null, true);

        ImageView productimage= (ImageView) rootView.findViewById(R.id.lblproimage);
        TextView wastevalue=(TextView) rootView.findViewById(R.id.lblwastevalue);
        TextView expirationc=(TextView) rootView.findViewById(R.id.lblexpirationc);
        TextView spoilagec=(TextView) rootView.findViewById(R.id.lblspoilagec);
        TextView preparedc=(TextView) rootView.findViewById(R.id.lblpreparationc);
        TextView cookingc=(TextView) rootView.findViewById(R.id.lblcookedc);
        TextView qualityc=(TextView) rootView.findViewById(R.id.lblqualityc);
        TextView usedc=(TextView) rootView.findViewById(R.id.lblusedc);

        TextView expiration=(TextView) rootView.findViewById(R.id.lblexpire);
        TextView spoilage=(TextView) rootView.findViewById(R.id.lblspoil);
        TextView prepared=(TextView) rootView.findViewById(R.id.lblprepare);
        TextView cooking=(TextView) rootView.findViewById(R.id.lblcook);
        TextView quality=(TextView) rootView.findViewById(R.id.lblquality);
        TextView used=(TextView) rootView.findViewById(R.id.lblused);




        try {

            productimage.setImageBitmap(itemimage.get(position));
            wastevalue.setText(itemwastevalue.get(position).toString());
            expirationc.setText(itemexpirationc.get(position).toString());
            if(itemexpirationc.get(position).toString().equals("\t\t0 Counts")){
               expirationc.setVisibility(View.GONE);
                expiration.setVisibility(View.GONE);
            }
            spoilagec.setText(itemspoilagec.get(position).toString());
            if(itemspoilagec.get(position).toString().equals("\t\t0 Counts")){
               spoilagec.setVisibility(View.GONE);
                spoilage.setVisibility(View.GONE);
            }
            preparedc.setText(itempreparationc.get(position).toString());
            if(itempreparationc.get(position).toString().equals("\t\t0 Counts")){
                preparedc.setVisibility(View.GONE);
                prepared.setVisibility(View.GONE);
            }
            cookingc.setText(itemcookedc.get(position).toString());
            if(itemcookedc.get(position).toString().equals("\t\t0 Counts")){
                cookingc.setVisibility(View.GONE);
                cooking.setVisibility(View.GONE);
            }
            qualityc.setText(itemqualityc.get(position).toString());
            if(itemqualityc.get(position).toString().equals("\t\t0 Counts")){
                qualityc.setVisibility(View.GONE);
                quality.setVisibility(View.GONE);
            }
            usedc.setText(itemusedc.get(position).toString());
            if(itemusedc.get(position).toString().equals("\t\t0 Counts")){
                usedc.setVisibility(View.GONE);
                used.setVisibility(View.GONE);
            }



        } catch (Exception ex) {
         Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }


}