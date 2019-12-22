package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefIdeasListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;


    ListView lstdish;
    String currentid;
    ImageView productimage;
    TextView txtTheKitchen, txtselect;
    double total = 0;

    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();

    ArrayAdapter adapter1;


    ImageButton donestatus;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundles = new Bundle();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.mylistideas, container, false);

        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        txtTheKitchen = (TextView)rootView.  findViewById(R.id.txtTheKitchen);
        txtTheKitchen.setText("The Kitchen - Dish Idea Inspirations");
        txtselect = (TextView)rootView.  findViewById(R.id.txtselect);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



       // bundles = this.getArguments();

        FillDataDish();

        try{
            if (!activity.edthidenuserid.getText().toString().equals("")) {

               // donestatus.setVisibility(View.GONE);

            }

        }catch (Exception ex){

        }
        donestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {

                    // donestatus.setVisibility(View.GONE);
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                }else {
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("assist", "assistant");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
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




    public void FillDataDish() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String query = "select * from [DishIdeas] where [userid]='" + activity.edthidenuserid.getText().toString() + "' order by [id] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                name.clear();
                keyingredient.clear();
                recipe.clear();
                proimage.clear();
                total = 0;
                while (rs.next()) {

                    name.add(rs.getString("name").toString());
                    keyingredient.add(rs.getString("keyingredient").toString());
                    recipe.add(rs.getString("recipe").toString());

                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);
                    total = total + 1;

                }

                DishListAdapter adapter = new DishListAdapter(this.getActivity(), proimage, name, keyingredient, recipe);
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
                            String query1 = "select top 1 * from [DishIdeas] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [name]='" + selectedname + "'  and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query1);
                            final ResultSet rs = ps.executeQuery();
                            rs.next();
                            try {

                                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                TextView title = new TextView(rootView.getContext());
                                title.setPadding(10, 10, 10, 10);
                                title.setText("Dish Selected");
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                builder.setCustomTitle(title);
                                builder.setMessage( selectedname + " ?");
                                builder.setIcon(rootView.getResources().getDrawable(R.drawable.removedish));
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            //Get top 1 if details are the same


                                            String query = "delete from [DishIdeas] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [id]='" + rs.getString("id").toString() + "'";
                                            PreparedStatement preparedStatement = con.prepareStatement(query);
                                            preparedStatement.executeUpdate();
                                            FillDataDish();
                                            //=========
                                            try {
                                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                                //String to = "jabun@ngobeniholdings.co.za";
                                                // String to = "sntshangase3@gmail.com";
                                                // m.setFrom("Info@ngobeniholdings.co.za");
                                                // String to = "SibusisoN@sqaloitsolutions.co.za";
                                                String to = "SibusisoN@sqaloitsolutions.co.za";
                                                String from = "info@goingdots.com";
                                                String subject = "Item removed";
                                                String message = "Dear Sibusiso\nDish Idea removed" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                                                String[] toArr = {to};
                                                m.setTo(toArr);
                                                m.setFrom(from);
                                                m.setSubject(subject);
                                                m.setBody(message);

                                                m.send();


                                            } catch (Exception e) {


                                            }
                                            //==========
                                        } catch (Exception ex) {
                                         //   Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here3", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                                builder.setNegativeButton("View", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {


                                            final String selectedname = name.get(position);
                                            final String selectedkeyingredient = keyingredient.get(position);
                                            final String selectedrecipe = recipe.get(position);

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
                                        }
                                       // dialog.cancel();
                                    }
                                });
                                builder.show();


                            } catch (Exception ex) {
                              Log.d("ReminderService In", "An error occurred:2 " + ex.getMessage());
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


