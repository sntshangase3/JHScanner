package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    int ngouserid;
    ListView lstgross;
    String currentid;
    ImageView productimage, retailerimage;
    TextView txttotal, selecttxt;
    double total = 0;
    int catergoryclick = 0;
    int statusclick = 0;
    int shoppingclick = 0;
    int ropclick = 0;
    String filterstatusselect, filtershoppingselect, filterropselect;
    String TotalDescribtion = "";
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<Bitmap> retimage = new ArrayList<Bitmap>();

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<String> rop = new ArrayList<String>();
    ArrayList<String> schedule = new ArrayList<String>();
    ArrayList<String> scheduleqty = new ArrayList<String>();
    String m_Text_donate = "";
    String m_Text_waste="";
    Bundle bundles = new Bundle();

    ArrayAdapter adapter1, adapter2;
    Spinner spinnercategory;

    ImageButton btn_category, btn_status, btn_shopping, btn_rop;
    Button btn_schedule;
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    String donationpinter = "No";

    final CharSequence[] items = {"Product Insight", "Consumption", "Donation", "Grocery Waste", "Recipe Idea", "Edit Product"};
    final CharSequence[]s = {"Expiration Date", "Food Spoilage", "Badly Prepared", "Badly Cooked", "Poor Quality", "Partially Used"};

    String edtproductId, edtname, edtdescription, edtbestbeforeStatus, edtbestbeforedate, edtsize, edtprice, edtquantity, edtquantityperbulk, edtquantitybulktotal, edtreorderpoint, edttotal;
    String edtpreferbestbeforedays, edtbarcode, edtstorage, edtisscanned, edtuserid, edtcategoryid, edtretailerid, edtweb;


    String encodedImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mylist, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstgross.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lstgross.setSelector(R.color.colorPrimary);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);

        txttotal = (TextView) rootView.findViewById(R.id.total);
        selecttxt = (TextView) rootView.findViewById(R.id.selecttxt);


        btn_category = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_status = (ImageButton) rootView.findViewById(R.id.filterstatus);
        btn_shopping = (ImageButton) rootView.findViewById(R.id.filtershopping);
        btn_rop = (ImageButton) rootView.findViewById(R.id.filterreturnorderpoint);
        btn_schedule = (Button) rootView.findViewById(R.id.btn_schedule);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        FillDataOrderByStatus("");

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


        try {
            if (bundle != null) {
                if (!bundle.getString("donationpinter").equals("")) {
                    ngouserid = bundle.getInt("ngouserid");
                    donationpinter = "Yes";
                    btn_schedule.setText("Donate");
                    filtershoppingselect = "Inventory";
                    FillDataOrderByInventoryForDonation();
                    Toast.makeText(rootView.getContext(), filtershoppingselect + " items.", Toast.LENGTH_LONG).show();
                }

            }
        } catch (Exception ex) {

        }

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catergoryclick = 1;
                spinnercategory.performClick();

            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusclick == 0) {
                    filterstatusselect = "Fresh";
                    statusclick = 1;
                } else if (statusclick == 1) {
                    filterstatusselect = "Prime";
                    statusclick = 2;
                } else if (statusclick == 2) {
                    filterstatusselect = "Expired";
                    statusclick = 3;
                } else {
                    filterstatusselect = "All";
                    statusclick = 0;
                }

                FillDataOrderByStatus(filterstatusselect);
                Toast.makeText(rootView.getContext(), filterstatusselect + " items.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoppingclick == 0) {
                    filtershoppingselect = "Shopping";
                    shoppingclick = 1;
                } else if (shoppingclick == 1) {
                    filtershoppingselect = "Inventory";
                    shoppingclick = 2;
                } else {
                    filtershoppingselect = "All";
                    shoppingclick = 0;
                }

                FillDataOrderByShopping(filtershoppingselect);
                Toast.makeText(rootView.getContext(), filtershoppingselect + " items.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_rop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ropclick == 0) {
                    filterropselect = "R.o.p";
                    ropclick = 1;
                } else if (ropclick == 1) {
                    filterropselect = "Non-R.o.p";
                    ropclick = 2;
                } else {
                    filterropselect = "All";
                    ropclick = 0;
                }

                FillDataOrderByROP(filterropselect);
                Toast.makeText(rootView.getContext(), filterropselect + " items.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (donationpinter.equals("Yes")) {
                    bundle.clear();
                    String[] outputStrArr = new String[schedule.size()];
                    String[] outputStrArrqty = new String[scheduleqty.size()];
                    for (int i = 0; i < schedule.size(); i++) {
                        outputStrArr[i] = schedule.get(i);
                        outputStrArrqty[i] = scheduleqty.get(i);
                    }
                    bundles.putStringArray("outputStrArr", outputStrArr);
                    bundles.putStringArray("outputStrArrqty", outputStrArrqty);

                    bundles.putString("couser", "couser");
                    bundles.putString("donationpinter", "donationpinter");
                    bundles.putInt("ngouserid", ngouserid);
                 /* Intent i = new Intent(rootView.getContext(), ReminderEditScheduleFrag.class);
                  i.putExtras(bundles);
                startActivity(i);*/
                    NGODonatesFrag fragment = new NGODonatesFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } else {
                    bundle.clear();
                    String[] outputStrArr = new String[schedule.size()];
                    String[] outputStrArrqty = new String[scheduleqty.size()];
                    for (int i = 0; i < schedule.size(); i++) {
                        outputStrArr[i] = schedule.get(i);
                        outputStrArrqty[i] = scheduleqty.get(i);
                    }
                    bundles.putStringArray("outputStrArr", outputStrArr);
                    bundles.putStringArray("outputStrArrqty", outputStrArrqty);
                 /* Intent i = new Intent(rootView.getContext(), ReminderEditScheduleFrag.class);
                  i.putExtras(bundles);
                startActivity(i);*/
                    ReminderEditScheduleFrag fragment = new ReminderEditScheduleFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }


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


                String query;
                if (catergoryclick == 1) {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [categoryid]=" + orderbyId + " order by [categoryid] asc";
                } else {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                }

                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                rop.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    rop.add(rs.getString("reorderpoint").toString());

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

                CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), proimage, name, quantity, status, retimage, rop);
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

                                bundles.putString("barcode", rs.getString("Barcode").toString());
                                bundles.putString("MylistFrag", "MylistFrag");
                                bundles.putString("website", rs.getString("website").toString());

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

                                //#########################
                                edtproductId = rs.getString("id").toString();
                                edtname = selectedname;
                                edtdescription = rs.getString("description").toString();
                                edtbestbeforeStatus = selectedstatus;
                                edtbestbeforedate = rs.getString("bestbefore").toString();
                                edtsize = rs.getString("size").toString();
                                edtprice = rs.getString("price").toString();
                                edtquantity = selectedquantity;
                                edtquantityperbulk = rs.getString("quantityperbulk").toString();
                                edtreorderpoint = rs.getString("reorderpoint").toString();
                                edttotal = rs.getString("totatitemvalue").toString();
                                edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                                edtbarcode = rs.getString("Barcode").toString();
                                edtstorage = rs.getString("storage").toString();
                                edtisscanned = rs.getString("isscanned").toString();
                                edtuserid = activity.edthidenuserid.getText().toString();
                                edtcategoryid = rs.getString("categoryid").toString();
                                edtretailerid = rs.getString("retailerid").toString();
                                edtweb = rs.getString("website").toString();
                                encodedImage = rs.getString("image");


                                if (btn_schedule.getVisibility() == View.GONE) {
                                    //Not from ProdIDingredients
                                   /* AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

                                    //===========
                                    final String selecteddescription = rs.getString("description").toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                    Log.d("ReminderService In", selecteddescription);

                                    TextView title = new TextView(rootView.getContext());
                                    title.setPadding(10, 10, 10, 10);
                                    title.setText(selecteddescription);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    builder.setCustomTitle(title);
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (items[which].equals("Consumption")) {
                                                try {

                                                    ConsumePro();

                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Donation")) {
                                                try {

                                                    DonatePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Grocery Waste")) {
                                                try {


                                                    WastePro();


                                                } catch (Exception ex) {
                                                    Log.d("ReminderService In", ex.getMessage());
                                                }
                                            } else if (items[which].equals("Product Insight")) {
                                                try {


                                                    ProductInsightFrag fragment = new ProductInsightFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else {
                                                try {


                                                    AddProductFrag fragment = new AddProductFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            }
                                            // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                        }

                                    });
                                    builder.show();

                                    //#########################


                                } else {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from request?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));


                                            /* final EditText input = new EditText(rootView.getContext());
                                             input.setGravity(Gravity.CENTER);
                                             input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                             input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                             input.setHint("Enter Quantity");
                                             input.setHintTextColor(Color.GRAY);
                                             builder.setView(input);*/


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
                                            final EditText input = new EditText(rootView.getContext());
                                            input.setGravity(Gravity.CENTER);
                                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                            input.setHint("Enter Quantity");
                                            input.setHintTextColor(Color.GRAY);
                                            builder.setView(input);

                                            builder.setMessage("Add Product to request?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    scheduleqty.add(m_Text_donate);
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
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);


                                }


                            }

                        } catch (Exception ex) {
                            //      Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }
                    }
                });
                txttotal.setText("Total R" + String.valueOf(total));
            }
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }

    public void FillDataOrderByInventoryForDonation() {
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


                String query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No'";
                TotalDescribtion = "Total Inventory: R";


                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                rop.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    rop.add(rs.getString("reorderpoint").toString());

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

                CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), proimage, name, quantity, status, retimage, rop);
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
                                bundle.putString("website", rs.getString("website").toString());
                                bundles.putString("name", selectedname);
                                bundle.putString("barcode", rs.getString("Barcode").toString());
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
                                bundles.putString("userid", rs.getString("userid").toString());

                                //#########################
                                edtproductId = rs.getString("id").toString();
                                edtname = selectedname;
                                edtdescription = rs.getString("description").toString();
                                edtbestbeforeStatus = selectedstatus;
                                edtbestbeforedate = rs.getString("bestbefore").toString();
                                edtsize = rs.getString("size").toString();
                                edtprice = rs.getString("price").toString();
                                edtquantity = selectedquantity;
                                edtquantityperbulk = rs.getString("quantityperbulk").toString();
                                edtreorderpoint = rs.getString("reorderpoint").toString();
                                edttotal = rs.getString("totatitemvalue").toString();
                                edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                                edtbarcode = rs.getString("Barcode").toString();
                                edtstorage = rs.getString("storage").toString();
                                edtisscanned = rs.getString("isscanned").toString();
                                edtuserid = activity.edthidenuserid.getText().toString();
                                edtcategoryid = rs.getString("categoryid").toString();
                                edtretailerid = rs.getString("retailerid").toString();
                                edtweb = rs.getString("website").toString();
                                encodedImage = rs.getString("image");


                                if (btn_schedule.getVisibility() == View.GONE) {
                                    //Not from ProdIDingredients
                                   /* AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

                                    //===========
                                    final String selecteddescription = rs.getString("description").toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                    Log.d("ReminderService In", selecteddescription);

                                    TextView title = new TextView(rootView.getContext());
                                    title.setPadding(10, 10, 10, 10);
                                    title.setText(selecteddescription);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    builder.setCustomTitle(title);
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (items[which].equals("Consumption")) {
                                                try {

                                                    ConsumePro();

                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Donation")) {
                                                try {

                                                    DonatePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Grocery Waste")) {
                                                try {

                                                    WastePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Product Insight")) {
                                                try {


                                                    ProductInsightFrag fragment = new ProductInsightFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else {
                                                try {


                                                    AddProductFrag fragment = new AddProductFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            }
                                            // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                        }

                                    });
                                    builder.show();

                                    //#########################
                                } else {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from request?");
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
                                            final EditText input = new EditText(rootView.getContext());
                                            input.setGravity(Gravity.CENTER);
                                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                            input.setHint("Enter Quantity");
                                            input.setHintTextColor(Color.GRAY);
                                            builder.setView(input);

                                            builder.setMessage("Add Product to request?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    scheduleqty.add(m_Text_donate);
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
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);


                                }


                            }

                        } catch (Exception ex) {
                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
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

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }

    public void FillDataOrderByStatus(String filterstatus) {
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


                String query;
               /* if(filterstatus.equals("Fresh")){
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>7";
                TotalDescribtion="Total Fresh: R";
                }else if(filterstatus.equals("Prime")){
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))>=1 and Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=7";
                    TotalDescribtion="Total Prime: R";
                }else if(filterstatus.equals("Expired")){
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and  Datediff(D,convert(varchar,getDate(),112),Cast([bestbefore] as date))<=0";
                    TotalDescribtion="Total Expired: R";
                }else{
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString();
                    TotalDescribtion="Total All: R";
                }*/
                query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                rop.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    rop.add(rs.getString("reorderpoint").toString());

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

                CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), proimage, name, quantity, status, retimage, rop);
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

                                bundles.putString("barcode", rs.getString("Barcode").toString());
                                bundles.putString("MylistFrag", "MylistFrag");
                                bundles.putString("website", rs.getString("website").toString());

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

                                //#########################
                                edtproductId = rs.getString("id").toString();
                                edtname = selectedname;
                                edtdescription = rs.getString("description").toString();
                                edtbestbeforeStatus = selectedstatus;
                                edtbestbeforedate = rs.getString("bestbefore").toString();
                                edtsize = rs.getString("size").toString();
                                edtprice = rs.getString("price").toString();
                                edtquantity = selectedquantity;
                                edtquantityperbulk = rs.getString("quantityperbulk").toString();
                                edtreorderpoint = rs.getString("reorderpoint").toString();
                                edttotal = rs.getString("totatitemvalue").toString();
                                edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                                edtbarcode = rs.getString("Barcode").toString();
                                edtstorage = rs.getString("storage").toString();
                                edtisscanned = rs.getString("isscanned").toString();
                                edtuserid = activity.edthidenuserid.getText().toString();
                                edtcategoryid = rs.getString("categoryid").toString();
                                edtretailerid = rs.getString("retailerid").toString();
                                edtweb = rs.getString("website").toString();
                                encodedImage = rs.getString("image");


                                if (btn_schedule.getVisibility() == View.GONE) {
                                    //Not from ProdIDingredients
                                   /* AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

                                    //===========
                                    final String selecteddescription = rs.getString("description").toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                    Log.d("ReminderService In", selecteddescription);

                                    TextView title = new TextView(rootView.getContext());
                                    title.setPadding(10, 10, 10, 10);
                                    title.setText(selecteddescription);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    builder.setCustomTitle(title);
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (items[which].equals("Consumption")) {
                                                try {

                                                    ConsumePro();

                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Donation")) {
                                                try {

                                                    DonatePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Grocery Waste")) {
                                                try {

                                                    WastePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Product Insight")) {
                                                try {


                                                    ProductInsightFrag fragment = new ProductInsightFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else {
                                                try {


                                                    AddProductFrag fragment = new AddProductFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            }
                                            // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                        }

                                    });
                                    builder.show();

                                    //#########################


                                } else {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from request?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));


                                            /* final EditText input = new EditText(rootView.getContext());
                                             input.setGravity(Gravity.CENTER);
                                             input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                             input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                             input.setHint("Enter Quantity");
                                             input.setHintTextColor(Color.GRAY);
                                             builder.setView(input);*/


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
                                            final EditText input = new EditText(rootView.getContext());
                                            input.setGravity(Gravity.CENTER);
                                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                            input.setHint("Enter Quantity");
                                            input.setHintTextColor(Color.GRAY);
                                            builder.setView(input);

                                            builder.setMessage("Add Product to request?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    scheduleqty.add(m_Text_donate);
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
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);


                                }


                            }

                        } catch (Exception ex) {
                            //      Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }
                    }
                });
                txttotal.setText("Total R" + String.valueOf(total));
            }
        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }

    public void FillDataOrderByShopping(String filterstatus) {
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


                String query;
                if (filterstatus.equals("Shopping")) {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='Yes'";
                    TotalDescribtion = "Total Shopping: R";
                } else if (filterstatus.equals("Inventory")) {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [isscanned]='No'";
                    TotalDescribtion = "Total Inventory: R";
                } else {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                    TotalDescribtion = "Total All: R";
                }

                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                rop.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    rop.add(rs.getString("reorderpoint").toString());

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

                CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), proimage, name, quantity, status, retimage, rop);
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

                                bundles.putString("barcode", rs.getString("Barcode").toString());
                                bundles.putString("MylistFrag", "MylistFrag");
                                bundles.putString("website", rs.getString("website").toString());

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

                                //#########################
                                edtproductId = rs.getString("id").toString();
                                edtname = selectedname;
                                edtdescription = rs.getString("description").toString();
                                edtbestbeforeStatus = selectedstatus;
                                edtbestbeforedate = rs.getString("bestbefore").toString();
                                edtsize = rs.getString("size").toString();
                                edtprice = rs.getString("price").toString();
                                edtquantity = selectedquantity;
                                edtquantityperbulk = rs.getString("quantityperbulk").toString();
                                edtreorderpoint = rs.getString("reorderpoint").toString();
                                edttotal = rs.getString("totatitemvalue").toString();
                                edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                                edtbarcode = rs.getString("Barcode").toString();
                                edtstorage = rs.getString("storage").toString();
                                edtisscanned = rs.getString("isscanned").toString();
                                edtuserid = activity.edthidenuserid.getText().toString();
                                edtcategoryid = rs.getString("categoryid").toString();
                                edtretailerid = rs.getString("retailerid").toString();
                                edtweb = rs.getString("website").toString();
                                encodedImage = rs.getString("image");


                                if (btn_schedule.getVisibility() == View.GONE) {
                                    //Not from ProdIDingredients
                                   /* AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

                                    //===========
                                    final String selecteddescription = rs.getString("description").toString();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                    Log.d("ReminderService In", selecteddescription);

                                    TextView title = new TextView(rootView.getContext());
                                    title.setPadding(10, 10, 10, 10);
                                    title.setText(selecteddescription);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    builder.setCustomTitle(title);
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (items[which].equals("Consumption")) {
                                                try {

                                                    ConsumePro();

                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Donation")) {
                                                try {

                                                    DonatePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Grocery Waste")) {
                                                try {

                                                    WastePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Product Insight")) {
                                                try {


                                                    ProductInsightFrag fragment = new ProductInsightFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else {
                                                try {


                                                    AddProductFrag fragment = new AddProductFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            }
                                            // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                        }

                                    });
                                    builder.show();

                                    //#########################


                                } else {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from request?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));


                                            /* final EditText input = new EditText(rootView.getContext());
                                             input.setGravity(Gravity.CENTER);
                                             input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                             input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                             input.setHint("Enter Quantity");
                                             input.setHintTextColor(Color.GRAY);
                                             builder.setView(input);*/


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
                                            final EditText input = new EditText(rootView.getContext());
                                            input.setGravity(Gravity.CENTER);
                                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                            input.setHint("Enter Quantity");
                                            input.setHintTextColor(Color.GRAY);
                                            builder.setView(input);

                                            builder.setMessage("Add Product to request?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    scheduleqty.add(m_Text_donate);
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
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);


                                }


                            }

                        } catch (Exception ex) {
                            //      Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
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

    public void FillDataOrderByROP(String filterrop) {
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


                String query;
                if (filterrop.equals("R.o.p")) {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [quantity]<=[reorderpoint]";
                    TotalDescribtion = "Total R.O.Point: R";
                } else if (filterrop.equals("Non-R.o.p")) {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and  [reorderpoint]=0";
                    TotalDescribtion = "Total Non-R.O.Point: R";
                } else {
                    query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                    TotalDescribtion = "Total All: R";
                }

                name.clear();
                quantity.clear();
                status.clear();
                retimage.clear();
                proimage.clear();
                rop.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    rop.add(rs.getString("reorderpoint").toString());

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

                CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), proimage, name, quantity, status, retimage, rop);
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

                                bundles.putString("barcode", rs.getString("Barcode").toString());
                                bundles.putString("MylistFrag", "MylistFrag");
                                bundles.putString("website", rs.getString("website").toString());

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

                                //#########################
                                edtproductId = rs.getString("id").toString();
                                edtname = selectedname;
                                edtdescription = rs.getString("description").toString();
                                edtbestbeforeStatus = selectedstatus;
                                edtbestbeforedate = rs.getString("bestbefore").toString();
                                edtsize = rs.getString("size").toString();
                                edtprice = rs.getString("price").toString();
                                edtquantity = selectedquantity;
                                edtquantityperbulk = rs.getString("quantityperbulk").toString();
                                edtreorderpoint = rs.getString("reorderpoint").toString();
                                edttotal = rs.getString("totatitemvalue").toString();
                                edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                                edtbarcode = rs.getString("Barcode").toString();
                                edtstorage = rs.getString("storage").toString();
                                edtisscanned = rs.getString("isscanned").toString();
                                edtuserid = activity.edthidenuserid.getText().toString();
                                edtcategoryid = rs.getString("categoryid").toString();
                                edtretailerid = rs.getString("retailerid").toString();
                                edtweb = rs.getString("website").toString();
                                encodedImage = rs.getString("image");


                                if (btn_schedule.getVisibility() == View.GONE) {
                                    //Not from ProdIDingredients
                                   /* AddProductFrag fragment = new AddProductFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/

                                    //===========
                                    final String selecteddescription = rs.getString("description").toString();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                    Log.d("ReminderService In", selecteddescription);

                                    TextView title = new TextView(rootView.getContext());
                                    title.setPadding(10, 10, 10, 10);
                                    title.setText(selecteddescription);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    builder.setCustomTitle(title);
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (items[which].equals("Consumption")) {
                                                try {

                                                    ConsumePro();

                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Donation")) {
                                                try {

                                                    DonatePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Grocery Waste")) {
                                                try {

                                                    WastePro();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Product Insight")) {
                                                try {


                                                    ProductInsightFrag fragment = new ProductInsightFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            } else {
                                                try {


                                                    AddProductFrag fragment = new AddProductFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

                                                }
                                            }
                                            // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                        }

                                    });
                                    builder.show();

                                    //#########################


                                } else {
                                    if (schedule.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from request?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));


                                            /* final EditText input = new EditText(rootView.getContext());
                                             input.setGravity(Gravity.CENTER);
                                             input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                             input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                             input.setHint("Enter Quantity");
                                             input.setHintTextColor(Color.GRAY);
                                             builder.setView(input);*/


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
                                            final EditText input = new EditText(rootView.getContext());
                                            input.setGravity(Gravity.CENTER);
                                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                                            input.setHint("Enter Quantity");
                                            input.setHintTextColor(Color.GRAY);
                                            builder.setView(input);

                                            builder.setMessage("Add Product to request?");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    m_Text_donate = input.getText().toString();
                                                    schedule.add(productId);
                                                    scheduleqty.add(m_Text_donate);
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
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                    }
                                    if (currentSelectedView != null && currentSelectedView != view) {
                                        unhighlightCurrentRow(currentSelectedView);
                                    }
                                    currentSelectedView = view;
                                    highlightCurrentRow(currentSelectedView);


                                }


                            }

                        } catch (Exception ex) {
                            //      Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
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

    public void ConsumePro() {

        try {
            Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
            //String to = "jabun@ngobeniholdings.co.za";
            // String to = "sntshangase3@gmail.com";
            // m.setFrom("Info@ngobeniholdings.co.za");
            // String to = "SibusisoN@sqaloitsolutions.co.za";
            String to = "SibusisoN@sqaloitsolutions.co.za";
            String from = "info@goingdots.com";
            String subject = "Item consumed" + edtproductId;
            String message = "Dear Sibusiso\nItem Consumed" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========
        ConsumeConfirmFrag fragment = new ConsumeConfirmFrag();
        fragment.setArguments(bundles);
        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
    }

    public void DonatePro() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
              /* TextView title=new TextView(rootView.getContext());
               // title.setPadding(10,10,10,10);
                title.setText("Donation");
                title.setGravity(Gravity.CENTER);
                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                builder.setCustomTitle("Donation");*/

                builder.setTitle("Donation");
                builder.setMessage("Thank you for Donating:");
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.yearlydonation));
                final EditText input = new EditText(rootView.getContext());
                input.setGravity(Gravity.CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                input.setHint("Enter Quantity");
                input.setHintTextColor(Color.GRAY);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text_donate = input.getText().toString();
                        try {

                            if (Integer.parseInt(m_Text_donate) == Integer.parseInt(edtquantity)) {
                                //Donate all
                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductDonation] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate + "','" + edtname + "','" + edtdescription + "','" +
                                        edtbestbeforeStatus + "','" + edtsize + "','" + Double.parseDouble(edtprice) + "','" +
                                        edtstorage + "','" + Integer.parseInt(edtpreferbestbeforedays) + "','" + Integer.parseInt(edtquantity) + "','" + Integer.parseInt(edtquantityperbulk) + "','" + Integer.parseInt(edtreorderpoint) + "','" + Double.parseDouble(edttotal) +
                                        "','" + edtisscanned + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid) + "','" + Integer.parseInt(edtcategoryid) + "','" + Integer.parseInt(edtretailerid) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (edtreorderpoint.trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have 0 " + edtname + " left";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                //Remove completely from [UserProduct]
                                String commands = "delete from [UserProduct]  where [id]='" + edtproductId + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();
                                if (!activity.edthidenuserid.equals("")) {
                                    Fragment frag = new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                } else {
                                    Fragment frag = new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }

                                Toast.makeText(rootView.getContext(), "Product Donated Successfully", Toast.LENGTH_LONG).show();
                            } else if (Integer.parseInt(m_Text_donate) < Integer.parseInt(edtquantity)) {
                                //Donate not all

                                int diff;
                                double total;
                                double totaldonation;

                                if (!edtquantityperbulk.equals("0")) {

                                    diff = Integer.parseInt(edtquantity) - Integer.parseInt(m_Text_donate);
                                    total = diff * (Double.valueOf(edtprice) / Integer.valueOf(edtquantityperbulk));
                                    totaldonation = Integer.parseInt(m_Text_donate) * (Double.valueOf(edtprice) / Integer.valueOf(edtquantityperbulk));
                                } else {
                                    diff = Integer.parseInt(edtquantity) - Integer.parseInt(m_Text_donate);
                                    total = diff * Double.parseDouble(edtprice);
                                    totaldonation = Integer.parseInt(m_Text_donate) * Double.parseDouble(edtprice);
                                }

                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                String commandsd = "insert into [UserProductDonation] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid])" +
                                        "values ('" + edtbestbeforedate + "','" + edtname + "','" + edtdescription + "','" +
                                        edtbestbeforeStatus + "','" + edtsize + "','" + Double.parseDouble(edtprice) + "','" +
                                        edtstorage + "','" + Integer.parseInt(edtpreferbestbeforedays) + "','" + Integer.parseInt(m_Text_donate) + "','" + Integer.parseInt(edtquantityperbulk) + "','" + Integer.parseInt(edtreorderpoint) + "','" + totaldonation +
                                        "','" + edtisscanned + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid) + "','" + Integer.parseInt(edtcategoryid) + "','" + Integer.parseInt(edtretailerid) + "')";
                                // encodedImage which is the Base64 String
                                PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                preStmtd.executeUpdate();

                                //Update [UserProduct] qty and total
                                String commands = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + edtproductId + "'";
                                PreparedStatement preStmt = con.prepareStatement(commands);
                                preStmt.executeUpdate();

                                //Check if need notifications as R.O.P
                                if (diff <= Integer.valueOf(edtreorderpoint) && !edtreorderpoint.trim().equals("0")) {
                                    //trigger notification
                                    //**********
                                    try {
                                        String notificationbody = "You have " + String.valueOf(diff) + " " + edtname + " left";
                                        byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                        NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                        Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builderd.setContentIntent(contentIntent);
                                        builderd.setAutoCancel(false);
                                        // Add as notification
                                        builderd.build();

                                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                        Notification myNotication;

                                        myNotication = builderd.getNotification();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        manager.notify(0, myNotication);
                                    } catch (Exception ex) {
                                        //   Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    }
                                    //**********
                                }

                                if (!activity.edthidenuserid.equals("")) {
                                    Fragment frag = new MyListFrag();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                } else {
                                    Fragment frag = new HomePremiumFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.mainFrame, frag).commit();
                                }
                                Toast.makeText(rootView.getContext(), "Partially Donation Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                // Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)
                                Toast.makeText(rootView.getContext(), "Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here2",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Invalid data input!", Toast.LENGTH_LONG).show();
        }
        //=========
        try {
            Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
            //String to = "jabun@ngobeniholdings.co.za";
            // String to = "sntshangase3@gmail.com";
            // m.setFrom("Info@ngobeniholdings.co.za");
            // String to = "SibusisoN@sqaloitsolutions.co.za";
            String to = "SibusisoN@sqaloitsolutions.co.za";
            String from = "info@goingdots.com";
            String subject = "Item donated" + edtproductId;
            String message = "Dear Sibusiso\nItem donated" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========

    }

    public void WastePro() {
        //=========
        try {
            Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
            //String to = "jabun@ngobeniholdings.co.za";
            // String to = "sntshangase3@gmail.com";
            // m.setFrom("Info@ngobeniholdings.co.za");
            // String to = "SibusisoN@sqaloitsolutions.co.za";
            String to = "SibusisoN@sqaloitsolutions.co.za";
            String from = "info@goingdots.com";
            String subject = "Item waste" + edtproductId;
            ;
            String message = "Dear Sibusiso\nItem wasted" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

            String[] toArr = {to};
            m.setTo(toArr);
            m.setFrom(from);
            m.setSubject(subject);
            m.setBody(message);

            m.send();


        } catch (Exception e) {


        }
        //==========
        WasteConfirmFrag fragment = new WasteConfirmFrag();
        fragment.setArguments(bundles);
        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

    }


}


