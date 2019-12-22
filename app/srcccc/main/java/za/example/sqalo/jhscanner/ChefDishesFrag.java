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
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefDishesFrag extends Fragment implements AdapterView.OnItemSelectedListener {

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
    int useridchef;

    ImageButton donestatus;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.chefdishes, container, false);

        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        txtTheKitchen = (TextView)rootView.  findViewById(R.id.txtTheKitchen);
        txtTheKitchen.setText("The Kitchen - Dishes");
        txtselect = (TextView)rootView.  findViewById(R.id.txtselect);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



        bundle = this.getArguments();

        try{
            if(bundle != null){
                useridchef=Integer.parseInt(bundle.getString("useridchef"));
             FillDataDish();
            }
        }catch (Exception ex){

        }



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

                String query = "select * from [Dish] where [userid]='" + useridchef + "' order by [id] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                name.clear();
                keyingredient.clear();
                recipe.clear();
                proimage.clear();
                total = 0;
                Log.d("ReminderService In", "####"+query);
                while (rs.next()) {
                    Log.d("ReminderService In", "####IN");
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


                        try {
                            final String selectedname = name.get(position);
                            final String selectedkeyingredient = keyingredient.get(position);
                            final String selectedrecipe = recipe.get(position);
                            String query1 = "select top 1 * from [Dish] where [userid]='" + useridchef + "' and [name]='" + selectedname + "'  and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query1);
                            final ResultSet rs = ps.executeQuery();
                            rs.next();
                           final String image=rs.getString("image").toString();
                            try {

                                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                TextView title = new TextView(rootView.getContext());
                                title.setPadding(10, 10, 10, 10);
                                title.setText("Dish");
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                builder.setCustomTitle(title);
                                builder.setMessage("   Selected Dish: " + selectedname);
                               builder.setIcon(rootView.getResources().getDrawable(R.drawable.dish));
                                builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                        bundle.putString("useridchef", String.valueOf(useridchef));
                                        bundle.putString("name", selectedname);
                                        bundle.putString("keyingredient", selectedkeyingredient);
                                        bundle.putString("image", image);
                                        fragment.setArguments(bundle);
                                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                                    }
                                });
                                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            //Get top 1 if details are the same


                                            String query = "delete from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [id]='" + rs.getString("id").toString() + "'";
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
                                                String message = "Dear Sibusiso\nDish removed" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

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
                                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here3", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                builder.show();


                            } catch (Exception ex) {
                              //  Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception ex) {
                          //  Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


        } catch (Exception ex) {
          //  Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here0", Toast.LENGTH_LONG).show();
        }
//===========

    }




}


