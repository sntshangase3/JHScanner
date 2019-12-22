package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
public class ChefListInventoryAllFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    String currentid;
    ImageView productimage, retailerimage;
    TextView txttotal, selecttxt;
    double total = 0;
    int catergoryclick = 0;
    int statusclick = 0;
    int shoppingclick = 0;

    String filterstatusselect, filtershoppingselect;
    String TotalDescribtion = "";
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<Bitmap> retimage = new ArrayList<Bitmap>();

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();

    ArrayList<String> schedule = new ArrayList<String>();
    ArrayList<String> scheduleqty = new ArrayList<String>();
    String m_Text_donate = "";
    Bundle bundles = new Bundle();
    Bundle bundless = new Bundle();

    ArrayAdapter adapter1;
    Spinner spinnercategory;

    ImageButton btn_category, btn_status, btn_shopping,donestatus;
    Button btn_schedule, btn_search;
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    ArrayList<Bitmap> proimages = new ArrayList<Bitmap>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();
    ListView lstgross,lstdish;
    LinearLayout layoutdishlist,layoutlistgross;
    String dishfound="No";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.inventory_all_list, container, false);

        layoutdishlist = (LinearLayout) rootView.findViewById(R.id.dishlist);
        layoutlistgross = (LinearLayout) rootView.findViewById(R.id.layoutlistgross);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstgross.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lstgross.setSelector(R.color.colorPrimary);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        txttotal = (TextView) rootView.findViewById(R.id.total);
        selecttxt = (TextView) rootView.findViewById(R.id.selecttxt);


        btn_category = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_status = (ImageButton) rootView.findViewById(R.id.filterstatus);
        btn_shopping = (ImageButton) rootView.findViewById(R.id.filtershopping);

        btn_schedule = (Button) rootView.findViewById(R.id.btn_schedule);
        btn_search = (Button) rootView.findViewById(R.id.btn_search);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();
        try {
            if (bundle != null) {
                if (!bundle.getString("ProdIDingredients").equals("")) {
                    btn_schedule.setVisibility(View.VISIBLE);
                    selecttxt.setVisibility(View.VISIBLE);
                }/*else{
                    btn_schedule.setVisibility(View.GONE);
                    selecttxt.setVisibility(View.GONE);
                }*/

            }
        } catch (Exception ex) {

        }
        FillCategoryData();
        spinnercategory.setOnItemSelectedListener(this);

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catergoryclick = 1;
                spinnercategory.performClick();

            }
        });

        donestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {

                    schedule.clear();
                    scheduleqty.clear();
                    Fragment fragment = new HomeDishCollectionFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                }
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* bundle.clear();
                String[] outputStrArr = new String[ProdIDingredients.size()];
                String[] outputStrArrqty = new String[scheduleqty.size()];
                for (int i = 0; i < ProdIDingredients.size(); i++) {
                    outputStrArr[i] = ProdIDingredients.get(i);
                    outputStrArrqty[i] = scheduleqty.get(i);
                }*/
                FillDataOrderBySearch();


            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

        FillDataOrderBy(spinnercategory.getSelectedItemPosition() + 1);


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


    public void FillDataOrderBy(int orderbyId) {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {

                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                //for loop build in condition
                String query ;
               if(catergoryclick==1){
                   query = "select * from [UserProduct] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [isscanned]='No' and [categoryid] =" + orderbyId + " order by [categoryid] asc";
               }else {
                   query = "select * from [UserProduct] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [isscanned]='No'  order by [categoryid] asc";
               }
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());


                    if (Integer.parseInt(rs.getString("retailerid").toString()) == 1) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logocheckers);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 2) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logook);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 3) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logopicknpay);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 4) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logoshoprite);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 5) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logospar);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 6) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logousaveshoprite);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 7) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logomakro);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 8) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logofoodlovers);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 9) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logowoolworth);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 10) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logoboxer);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 11) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logogame);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 12) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logofruitsandveg);
                        retimage.add(im);
                    } else if (Integer.parseInt(rs.getString("retailerid").toString()) == 13) {
                        Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.logootherretailers);
                        retimage.add(im);
                    }

                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);


                }

                CustomListAdapterAll adapter = new CustomListAdapterAll(this.getActivity(), proimage, name, quantity, status, retimage);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub


                        try {

                            String selectedname = name.get(position);
                            String selectedquantity = quantity.get(position);
                            String selectedstatus = status.get(position);
                            String query = "select * from [UserProduct] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [name]='" + selectedname + "'  and [quantity]='" + selectedquantity + "'  and [bestbeforeStatus]='" + selectedstatus + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();

                            while (rs.next()) {

                                Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();


                                final String productId = rs.getString("id").toString();
                                bundles.putString("id", rs.getString("id").toString());
                                bundles.putString("bestbefore", rs.getString("bestbefore").toString());
                                bundles.putString("name", selectedname);
                                bundles.putString("description", rs.getString("description").toString());
                                bundles.putString("bestbeforeStatus", selectedstatus);
                                bundles.putString("size", rs.getString("size").toString());
                                bundles.putString("price", rs.getString("price").toString());
                                bundles.putString("storage", rs.getString("storage").toString());
                                bundles.putString("preferbestbeforedays", rs.getString("preferbestbeforedays").toString());
                                bundles.putString("quantity", selectedquantity);

                                bundles.putString("quantityperbulk", rs.getString("quantityperbulk").toString());
                                bundles.putString("reorderpoint", rs.getString("reorderpoint").toString());
                                bundles.putString("totatitemvalue", rs.getString("totatitemvalue").toString());
                                bundles.putString("isscanned", rs.getString("isscanned").toString());
                                bundles.putString("image", rs.getString("image").toString());
                                bundles.putString("categoryid", rs.getString("categoryid").toString());
                                bundles.putString("retailerid", rs.getString("retailerid").toString());

                                if (schedule.size() != 5) {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from dish search idea?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // m_Text_donate = input.getText().toString();
                                                    schedule.remove(productId);
                                                    scheduleqty.remove(m_Text_donate);


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
                                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                                        }

                                    } else {

                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("New Product");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.add));
                                            builder.setMessage("Add Product to search dish idea?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    //  scheduleqty.add(m_Text_donate);
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
                                            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);
                                } else {
                                    Toast.makeText(rootView.getContext(), "Maximum of 5 items reached!!!", Toast.LENGTH_LONG).show();
                                }


                            }

                        } catch (Exception ex) {
                           // Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                txttotal.setText("Total R" + String.valueOf(total));
            }
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
//===========

    }
    public void FillDataOrderBySearch() {
        //==============Initialize list=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {

                names.clear();
                keyingredient.clear();
                recipe.clear();
                proimages.clear();
                //for loop build in condition

                for (int i = 0; i < schedule.size(); i++) {
                    int orderbyId = Integer.parseInt(schedule.get(i));
                    //#########

                    String query = "select * from [UserProduct] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [id] ='" + orderbyId + "' order by [categoryid] asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    total = 0;
                    while (rs.next()) {

                        //+++++++++++++++++++++++++++++++
                        String query5 = "select * from [Dish]  order by [id] asc";
                        PreparedStatement ps5 = con.prepareStatement(query5);
                        ResultSet rs5 = ps5.executeQuery();

                        while (rs5.next()) {
                            if(rs5.getString("keyingredient").toString().toLowerCase().contains(rs.getString("name").toString().toLowerCase())){

                                names.add(rs5.getString("name").toString());
                                keyingredient.add(rs5.getString("keyingredient").toString());
                                recipe.add(rs5.getString("recipe").toString());

                                byte[] decodeString = Base64.decode(rs5.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                proimages.add(decodebitmap);
                                dishfound="Yes";

                            }

                        }
                        //+++++++++++++++++++++++++++++++++++



                    }
                    //#########
                }
                DishListAdapter adapter = new DishListAdapter(this.getActivity(), proimages, names, keyingredient, recipe);
                lstdish.setAdapter(adapter);
                lstdish.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub
                        try {


                             String selectedname = names.get(position);
                            String selectedkeyingredient = keyingredient.get(position);
                             String selectedrecipe = recipe.get(position);

                            String query = "select * from [Dish] where [name]='"+selectedname+ "'   and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();

                            while (rs.next()) {

                                Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

                                bundless.putString("id", rs.getString("id").toString());
                                bundless.putString("useridchef", rs.getString("userid").toString());
                                bundless.putString("name", selectedname);
                                bundless.putString("keyingredient", rs.getString("keyingredient").toString());
                                bundless.putString("image", rs.getString("image").toString());


                                ChefDishDetailsFragment fragment = new ChefDishDetailsFragment();
                                fragment.setArguments(bundless);
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




                            }

                        } catch (Exception ex) {
                            Log.d("ReminderService In", "An error occurred:1 " + ex.getMessage());
                        }



                    }
                });

if(dishfound.equals("Yes")){
    layoutdishlist.setVisibility(View.VISIBLE);
    layoutlistgross.setVisibility(View.GONE);
}else{
    Toast.makeText(rootView.getContext(), "Sorry No Matching dish found!!!", Toast.LENGTH_LONG).show();
    layoutlistgross.setVisibility(View.VISIBLE);
    layoutdishlist.setVisibility(View.GONE);
}


                txttotal.setText("Total R" + String.valueOf(total));
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred:1 " + ex.getMessage());
        }
//===========

    }

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


