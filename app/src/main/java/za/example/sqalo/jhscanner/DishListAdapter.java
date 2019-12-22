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

public class DishListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemname;
    private final ArrayList<String> itemkeyingredient;
    private final ArrayList<String> itemrecipe;


    public DishListAdapter(Activity context, ArrayList<Bitmap> image, ArrayList<String> name, ArrayList<String> keyingredient, ArrayList<String> recipe) {
        super(context, R.layout.lsttemplatedish, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemname = name;
        this.itemkeyingredient = keyingredient;
        this.itemrecipe = recipe;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplatedish, null, true);

        ImageView productimage;
        TextView name, keyingredient, recipe;

        productimage = (ImageView) rootView.findViewById(R.id.lblproductimage);
         name = (TextView) rootView.findViewById(R.id.lblname);
        keyingredient = (TextView) rootView.findViewById(R.id.lblkeyingredient);
        recipe = (TextView) rootView.findViewById(R.id.lblrecipe);


        try {
            productimage.setImageBitmap(itemimage.get(position));
            name.setText(itemname.get(position));
            keyingredient.setText(itemkeyingredient.get(position));
            recipe.setText(itemrecipe.get(position));

        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
        }


        return rootView;

    }


}