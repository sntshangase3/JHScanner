package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefIdeasListSearchedFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    ListView lstdish;
    String currentid;
    ImageView productimage;
    EditText edtsearch;
    double total = 0;

    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();

    ArrayAdapter adapter1;

    ImageButton search;
    String search_product = "";

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundles = new Bundle();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mylistideas_searched, container, false);

        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);

        productimage = (ImageView) rootView.findViewById(R.id.productImage);
        search = (ImageButton) rootView.findViewById(R.id.search);
        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";


        bundles = this.getArguments();


        try {
            if (!bundles.getString("description").equals("")) {
                edtsearch.setText(bundles.getString("description"));
                search_product = edtsearch.getText().toString();
                byte[] decodeString = Base64.decode(bundles.getString("image"), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                productimage.setImageBitmap(decodebitmap);
                FillDataDish(search_product);
                // Log.d("ReminderService In", search_product);
                // donestatus.setVisibility(View.GONE);

            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                if (!search_product.equals("")) {
                    byte[] decodeString = Base64.decode(bundles.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    productimage.setImageBitmap(decodebitmap);
                    FillDataDish(search_product);
                } else {
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Toast.makeText(rootView.getContext(), "Please enter search value!!!", Toast.LENGTH_LONG).show();
                    edtsearch.setBackground(errorbg);

                }


            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        // spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

        //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }


    public void FillDataDish(String name_Or_ingredients) {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String[] words = name_Or_ingredients.split("\\W+");
                name.clear();
                keyingredient.clear();
                recipe.clear();
                proimage.clear();
                total = 0;
int status=0;

                String ingredients=name_Or_ingredients.toString().replace(", ",",");
                StringTokenizer tokenizer = new StringTokenizer(ingredients, ",");
                while (tokenizer.hasMoreTokens()) {
                    String descriptions = tokenizer.nextToken();
                   // String query = "select * from [Dish] where contains([name],\"'"+descriptions+"'\") or contains([keyingredient],\"'"+descriptions+"'\")";
                     String query = "select * from [Dish] where [name] like '%" + descriptions + "%' or [keyingredient] like '%" + descriptions + "%'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    Log.d("ReminderService In", query);
                    while (rs.next()) {
                        status=1;
                        name.add(rs.getString("name").toString());
                        keyingredient.add(rs.getString("keyingredient").toString());
                        recipe.add(rs.getString("recipe").toString());

                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        proimage.add(decodebitmap);
                        total = total + 1;
                        // break outer;
                    }


                }
                if(status==0){
                    Toast.makeText(rootView.getContext(), "Sorry, no recipe idea for this ingredient now!!!", Toast.LENGTH_LONG).show();
                    Fragment fragment = new SearchListFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }



                DishListSearchedAdapter adapter = new DishListSearchedAdapter(this.getActivity(), proimage, name, keyingredient, recipe);
                lstdish.setAdapter(adapter);
                lstdish.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub
                       /* try {


                             String selectedname = name.get(position);
                            String selectedkeyingredient = keyingredient.get(position);
                             String selectedrecipe = recipe.get(position);

                            String query = "select * from [Dish] where [name]='"+selectedname+ "'   and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();

                            while (rs.next()) {

                                Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                bundles.putString("id", rs.getString("id").toString());
                                bundles.putString("useridchef", rs.getString("userid").toString());
                                bundles.putString("name", selectedname);
                                bundles.putString("keyingredient", rs.getString("keyingredient").toString());
                                bundles.putString("image", rs.getString("image").toString());


                                ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                fragment.setArguments(bundles);
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




                            }

                        } catch (Exception ex) {
                            Log.d("ReminderService In", "An error occurred:1 " + ex.getMessage());
                        }*/


                        try {
                            final String selectedname = name.get(position);
                            final String selectedkeyingredient = keyingredient.get(position);
                            final String selectedrecipe = recipe.get(position);
                            /*String query1 = "select top 1 * from [Dish] where  [name]='" + selectedname + "'  and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            final ResultSet rs1 = ps1.executeQuery();
                            rs1.next();*/

                            try {


                                String query = "select * from [Dish] where [name]='" + selectedname + "'   and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                                PreparedStatement ps = con.prepareStatement(query);
                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {

                                    Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                    bundles.putString("id", rs.getString("id").toString());
                                    bundles.putString("useridchef", rs.getString("userid").toString());
                                    bundles.putString("name", selectedname);
                                    bundles.putString("keyingredient", rs.getString("keyingredient").toString());
                                    bundles.putString("image", rs.getString("image").toString());


                                    ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                }

                            } catch (Exception ex) {
                                Log.d("ReminderService In", "An error occurred:1 " + ex.getMessage());
                            }

                        } catch (Exception ex) {
                            Log.d("ReminderService In", "An error occurred:3 " + ex.getMessage());
                        }
                    }
                });
            }


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred:4 " + ex.getMessage());
        }
//===========

    }


}


