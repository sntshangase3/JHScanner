package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
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
public class NGODonationPerUserFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    String currentid;
    ImageView productimage, retailerimage;
    TextView txttotal, selecttxt,txtdonatedate;
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
    Button btn_accept,btn_receive,btn_postpone,btn_decline,btn_schedule, btn_search;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ngo_donation_per_user, container, false);

        layoutdishlist = (LinearLayout) rootView.findViewById(R.id.dishlist);
        layoutlistgross = (LinearLayout) rootView.findViewById(R.id.layoutlistgross);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstgross.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lstgross.setSelector(R.color.colorPrimary);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        txttotal = (TextView) rootView.findViewById(R.id.total);
        txtdonatedate = (TextView) rootView.findViewById(R.id.txtdonatedate);
        selecttxt = (TextView) rootView.findViewById(R.id.selecttxt);


        btn_category = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_status = (ImageButton) rootView.findViewById(R.id.filterstatus);
        btn_shopping = (ImageButton) rootView.findViewById(R.id.filtershopping);

        btn_schedule = (Button) rootView.findViewById(R.id.btn_schedule);
        btn_search = (Button) rootView.findViewById(R.id.btn_search);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);
        lstdish = (ListView) rootView.findViewById(R.id.lstdish);

        btn_accept = (Button) rootView. findViewById(R.id.btn_accept);
        btn_receive = (Button) rootView. findViewById(R.id.btn_receive);
        btn_postpone = (Button) rootView. findViewById(R.id.btn_postpone);
        btn_decline = (Button) rootView. findViewById(R.id.btn_decline);

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
        FillDataOrderBy();
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
                    Fragment fragment = new HomeFragmentDonationPointer();
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



            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accept();
            }
        });
        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Receive();
            }
        });
        btn_postpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postpone();
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Decline();
            }
        });
        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

      //  FillDataOrderBy(spinnercategory.getSelectedItemPosition() + 1);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }




    private View currentSelectedView;

    public void Accept() {
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [NgoProductDonation] set [donationstatus]='Accepted', [isread]='Yes' where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [userid]='" + bundle.getString("userid") + "'  and [isread]='No'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Donation has been accepted", Toast.LENGTH_LONG).show();
            }
            con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void Receive() {
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [NgoProductDonation] set [donationstatus]='Received', [isread]='Yes' where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [userid]='" + bundle.getString("userid") + "'  and [isread]='No'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Donation has been Received", Toast.LENGTH_LONG).show();
            }
            con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void Postpone() {
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [NgoProductDonation] set [donationstatus]='Postponed', [isread]='Yes' where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [userid]='" + bundle.getString("userid") + "'  and [isread]='No'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Donation been Postponed", Toast.LENGTH_LONG).show();
            }
            con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void Decline() {
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [NgoProductDonation] set [donationstatus]='Declined', [isread]='Yes' where [ngouserid]='"+activity.edthidenuserid.getText().toString()+"' and [userid]='" + bundle.getString("userid") + "'  and [isread]='No'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Donation has been Declined", Toast.LENGTH_LONG).show();
            }
            con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }

    public void FillDataOrderBy() {
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

                if (bundle.getString("image") != null) {
                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), decodebitmap);
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    btn_category.setImageDrawable(roundedBitmapDrawable);

                }

                String query = "select * from [NgoProductDonation] where [userid]='" + bundle.getString("userid") + "'  and [isread]='No'  order by [categoryid] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                while (rs.next()) {
                    total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString());
                    status.add(rs.getString("bestbeforeStatus").toString());
                    txtdonatedate.setText(rs.getString("donationdate").toString());

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
                            String query = "select * from [NgoProductDonation] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [name]='" + selectedname + "'  and [quantity]='" + selectedquantity + "'  and [bestbeforeStatus]='" + selectedstatus + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();

                            while (rs.next()) {

                                Toast.makeText(rootView.getContext(), selectedname, Toast.LENGTH_SHORT).show();

/*
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

                                if (ProdIDingredients.size() != 5) {
                                    if (ProdIDingredients.contains(rs.getString("id").toString())) {
                                        try {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                            builder.setTitle("Product exist");
                                            builder.setMessage("Remove Product from dish search idea?");
                                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // m_Text_donate = input.getText().toString();
                                                    ProdIDingredients.remove(productId);
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
                                                    ProdIDingredients.add(productId);
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
*/

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


    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


