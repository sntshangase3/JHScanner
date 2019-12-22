package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.graphics.Bitmap;
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
import java.util.ArrayList;

public class SearchListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemname;

    Connection con;
    String un, pass, db, ip;

    public SearchListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList<String> name) {
        super(context, R.layout.lsttemplateseach, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemname = name;

        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateseach, null, true);

        ImageView logoimage;
        TextView name;
        logoimage = (ImageView) rootView.findViewById(R.id.lbllogoimage);
        name = (TextView) rootView.findViewById(R.id.lblname);


        try {

            String displayname = itemname.get(position).toString();
            name.setText(displayname);
            logoimage.setImageBitmap(itemimage.get(position));



        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}