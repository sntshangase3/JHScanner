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

public class AddProductFragAllergyAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final  ArrayList <String> itemname;

    public AddProductFragAllergyAdapter(Activity context, ArrayList<Bitmap> image, ArrayList <String> name ) {
        super(context, R.layout.lsttemplateallergy, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemimage=image;
        this.itemname=name;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateallergy, null,true);

        ImageView productimage;
        TextView name;
        productimage =  (ImageView) rootView.findViewById(R.id.lblproductimage);
        name = (TextView) rootView. findViewById(R.id.lblname);


try{
    productimage.setImageBitmap(itemimage.get(position));
    name.setText(itemname.get(position));

}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
}


        return rootView;

    };
}