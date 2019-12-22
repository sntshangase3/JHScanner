package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
public class SearchListIngredientFrag extends Fragment implements AdapterView.OnItemSelectedListener {
    ArrayList <String> ProdIDingredients = new ArrayList<String>();
    ArrayList <String> ProdDescriptions= new ArrayList<String>();
    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;

    final CharSequence[] items = {"Shopping List", "GroRequest", "Recipe Idea"};
    ListView lstgross;
    String currentid;
    ImageView productimage;
    int userid;
    Button btn_done;
    int catergoryclick = 0;
    int retailerclick = 0;
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> description = new ArrayList<String>();


    ArrayAdapter adapter1;
    Spinner spinnerretailer, spinnercategory;


    ImageButton search, btn_category, btn_retailer;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    EditText edtsearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_ingredient, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        search = (ImageButton) rootView.findViewById(R.id.search);
        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        btn_category = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_retailer = (ImageButton) rootView.findViewById(R.id.filterretailer);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ArrayList<ItemData> list = new ArrayList<>();
        // list.add(new ItemData("Select Retailer",R.drawable.blackproduct));
        list.add(new ItemData("Checkers", R.drawable.logocheckers));
        list.add(new ItemData("OK Food", R.drawable.logook));
        list.add(new ItemData("Pick n Pay", R.drawable.logopicknpay));
        list.add(new ItemData("Shoprite", R.drawable.logoshoprite));
        list.add(new ItemData("Spar", R.drawable.logospar));
        list.add(new ItemData("USave", R.drawable.logousaveshoprite));

        list.add(new ItemData("Makro", R.drawable.logomakro));
        list.add(new ItemData("Food Lovers", R.drawable.logofoodlovers));
        list.add(new ItemData("Woolworth Food", R.drawable.logowoolworth));
        list.add(new ItemData("Boxer", R.drawable.logoboxer));
        list.add(new ItemData("Game", R.drawable.logogame));
        list.add(new ItemData("Fruits & Veg", R.drawable.logofruitsandveg));
        list.add(new ItemData("Other Retailers", R.drawable.logootherretailers));


        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        adapter = new SpinnerAdapter(this.getActivity(), R.layout.spinner_layout, R.id.txt, list);
        spinnerretailer.setAdapter(adapter);
        spinnerretailer.setOnItemSelectedListener(this);

        bundle = this.getArguments();

        FillCategoryData();
        spinnercategory.setOnItemSelectedListener(this);


        try {
            if (activity.edthidenuserid.getText().toString().equals("")) {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                String query11 = "select * from [AppUserAssistance] where [id]='" + activity.edthidenuserrole.getText().toString() + "'";
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                rs11.next();

                userid = Integer.parseInt(rs11.getString("userid").toString());

                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
            }

        } catch (Exception ex) {

        }

        try{
            if(bundle != null) {
                if (!bundle.getString("chefname").equals("")) {
               bundles.putAll(bundle);

                }
            }
        }catch (Exception ex){

        }
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //bundle.clear();
                    String[] outputStrArr = new String[ProdIDingredients.size()];
                String[] outputStrArr1 = new String[ProdDescriptions.size()];

                    for (int i = 0; i < ProdIDingredients.size(); i++) {
                        outputStrArr[i] = ProdIDingredients.get(i);
                        outputStrArr1[i] = ProdDescriptions.get(i);

                    }
                    bundles.putStringArray("outputStrArr",outputStrArr);
                bundles.putStringArray("outputStrArr1",outputStrArr1);

                    RegisterChefPartnershipFrag fragment = new RegisterChefPartnershipFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();



            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                if (!search_product.equals("")) {
                    FillDataOrderByStatus(search_product);
                } else {
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Toast.makeText(rootView.getContext(), "Please enter search value!!!", Toast.LENGTH_LONG).show();
                    edtsearch.setBackground(errorbg);

                }


            }
        });

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catergoryclick = 1;
                spinnercategory.performClick();
                // FillDataOrderByCategory();

            }
        });
        btn_retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerclick = 1;
                spinnerretailer.performClick();
                // FillDataOrderByCategory();

            }
        });


        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
        //Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString() , Toast.LENGTH_LONG).show();



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    public void FillCategoryData() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                String query = "select * from [Category]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

               /* category.add("Food");
                category.add("Status");
                category.add("Retailer");*/
                while (rs.next()) {

                    category.add(rs.getString("productCategory"));
                }
                adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnercategory.setAdapter(adapter1);


            }


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    private View currentSelectedView;
    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {


                //Co-user login
                String query = "select * from [UserProduct] where  contains([description] ,'" + search + "')";

                description.clear();
                proimage.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    description.add(rs.getString("description").toString());
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);
                }


                SearchListAdapter adapter = new SearchListAdapter(this.getActivity(), proimage, description);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {


//#######################
                            String selecteddescription = description.get(position).toString();
                            String query = "select * from [UserProduct] where [description]='" + selecteddescription + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {


                                final String productId=rs.getString("id").toString();
                                bundles.putString("id", rs.getString("id").toString());
                                bundles.putString("bestbefore", rs.getString("bestbefore").toString());
                                bundles.putString("name", rs.getString("name").toString());

                                bundles.putString("barcode", rs.getString("Barcode").toString());
                                bundles.putString("MylistFrag", "MylistFrag");
                                bundles.putString("website", rs.getString("website").toString());

                                bundles.putString("description", rs.getString("description").toString());
                                bundles.putString("bestbeforeStatus", rs.getString("bestbeforeStatus").toString());
                                bundles.putString("size", rs.getString("size").toString());
                                bundles.putString("price", rs.getString("price").toString());
                                bundles.putString("storage", rs.getString("storage").toString());
                                bundles.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                bundles.putString("quantity", rs.getString("quantity").toString());

                                bundles.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                bundles.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                bundles.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                bundles.putString("isscanned", rs.getString("isscanned").toString());
                                bundles.putString("image", rs.getString("image").toString());
                                bundles.putString("categoryid", rs.getString("categoryid").toString());
                                bundles.putString("retailerid", rs.getString("retailerid").toString());


                                    if(ProdIDingredients.contains(rs.getString("id").toString())){
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from Ingredient?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // m_Text_donate = input.getText().toString();
                                                    ProdIDingredients.remove(productId);
                                                    ProdDescriptions.remove(description.get(position).toString());



                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.show();





                                        } catch (Exception ex) {
                                            Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                        }

                                    }else {

                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("New Ingredient");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.add));
                                            builder.setMessage("Add Product to Ingredient?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    ProdIDingredients.add(productId);
                                                    ProdDescriptions.add(description.get(position).toString());

                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.show();



                                        } catch (Exception ex) {
                                            Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view ) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);






                            }


                        } catch (Exception ex) {
                            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString() + " NGOAllD");
                        }
                    }
                });


            }
        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }
    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor( R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }



}


