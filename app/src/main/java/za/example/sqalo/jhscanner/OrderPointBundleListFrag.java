package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleListFrag extends Fragment implements AdapterView.OnItemSelectedListener {


    View rootView;
    String m_Text_donate = "";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid;
    ImageView bundleprofileImage;
    int userid, qty;
    int bundleid;
    ArrayList<Bitmap> addcart = new ArrayList<Bitmap>();
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    Bundle bundles = new Bundle();
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;

    String encodedImage;
    final CharSequence[] items = {"Delivery Areas", "View/Order", "Add Optional Items", "Serving Suggestions"};
    Spinner spinnerdeliveryaddres;
    ArrayAdapter adapter1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundlelist, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        bundleprofileImage = (ImageView) rootView.findViewById(R.id.bundleprofileImage);
        spinnerdeliveryaddres = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerdeliveryaddres.setOnItemSelectedListener(this);
        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        try {
            userid = Integer.parseInt(activity.edthidenuserid.getText().toString());

        } catch (Exception ex) {

        }
        try {
            FillDataOrderByStatus();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
/*
        try {
           final String  fulladdress_search = spinnerrecipe.getSelectedItem().toString();
            if (!fulladdress_search.equals("Select Area")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setTitle(fulladdress_search);
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.map_search));
                builder.setMessage("Go?");
                builder.setPositiveButton("Navigate To", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isGoogleMapsInstalled()) {
                            try {

                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                getActivity().startActivity(mapIntent);


                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), "Enable Device GPS/Address/Google Maps Not Found......", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setMessage("Install Google Maps");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Install", getGoogleMapsListener());
                            AlertDialog dialog1 = builder.create();
                            dialog1.show();
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
            }else{
               // spinnerrecipe.performClick();
            }




        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Enable Device GPS/ Google Maps Not Found......", Toast.LENGTH_LONG).show();

        }*/


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    private View currentSelectedView;

    public void FillAddressData(int bundleid) {
        //==============Fill Data=
        try {

            String query = "select * from [BundleDeliveryAddress] where [bundleid]='" + bundleid + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Area");
            while (rs.next()) {
                category.add(rs.getString("address"));
            }
            adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerdeliveryaddres.setAdapter(adapter1);

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }

    public void FillDataOrderByStatus() {
        //==============Initialize list=
        try {

            //Co-user login
            String query = "select * from [Bundle]";
            name.clear();
            proimage.clear();
            addcart.clear();
            price.clear();
            quantity.clear();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name.add(rs.getString("name").toString());
                quantity.add(rs.getString("quantity").toString() + " Left");
                byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                // if(decodebitmap.getHeight()>700)
                // decodebitmap=Bitmap.createScaledBitmap(decodebitmap,decodebitmap.getWidth(),700,false);
                proimage.add(decodebitmap);
                Bitmap cart = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.addcart);
                addcart.add(cart);
                price.add("R" + rs.getString("price").toString());
            }


            OrderPointBundleListAdapter adapter = new OrderPointBundleListAdapter(this.getActivity(), proimage, addcart, name, quantity, price);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String selectedname = name.get(position);
                        String query = "select * from [Bundle] where [name]='" + selectedname + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        final ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {

                            bundleid = rs.getInt("id");
                            bundles.putInt("id", bundleid);
                            bundles.putString("name", selectedname);
                            bundles.putString("price", rs.getString("price").toString());
                            bundles.putString("quantity", rs.getString("quantity").toString());
                            bundles.putString("extras", "extras");

                            String person = rs.getString("days").toString();
                            String family = rs.getString("family").toString();
                            String time = rs.getString("ServingTime").toString();
                            final String serving = "One-Person:    " + person + " Days\nFamily:              " + family + " people\nServing-Time:\t" + time;
                            Log.d("ReminderService In", person + " " + family + " " + time + "  ### " + serving);

                            FillAddressData(bundleid);

                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                //builder.setMessage("Inventory:" + countinventory + " & Shopping:"+countshoppinglist);
                                Log.d("ReminderService In", selectedname);

                                TextView title = new TextView(rootView.getContext());
                                title.setPadding(10, 10, 10, 10);
                                title.setText(selectedname);
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                builder.setCustomTitle(title);
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (items[which].equals("Delivery Areas")) {
                                            try {

                                                spinnerdeliveryaddres.performClick();


                                            } catch (Exception ex) {

                                            }
                                        } else if (items[which].equals("View/Order")) {
                                            try {

                                                OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                                                fragment.setArguments(bundles);
                                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                                            } catch (Exception ex) {
                                                Log.d("ReminderService In", ex.getMessage());
                                            }
                                        } else if (items[which].equals("Add Optional Items")) {
                                            try {
                                                if (userid == 3) {
                                                    OrderPointBundleCreateFrag fragment = new OrderPointBundleCreateFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                                } else {
                                                    Toast ToastMessage = Toast.makeText(rootView.getContext(), "Admin Only!!!", Toast.LENGTH_LONG);
                                                    View toastView = ToastMessage.getView();
                                                    toastView.setBackgroundResource(R.drawable.toast_bground);
                                                    ToastMessage.show();
                                                }


                                            } catch (Exception ex) {
                                                Log.d("ReminderService In", ex.getMessage());
                                            }
                                        } else if (items[which].equals("Serving Suggestions")) {

                                            try {


                                                final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                                builder.setTitle("SERVING SUGGESTIONS\n" + selectedname);
                                                builder.setIcon(rootView.getResources().getDrawable(R.drawable.drop));
                                                final EditText input = new EditText(rootView.getContext());
                                                input.setBackground(rootView.getResources().getDrawable(R.drawable.edittext_bground_login));
                                                input.setTextColor(Color.BLACK);
                                                if (userid != 3) {
                                                    input.setKeyListener(null);
                                                    input.setEnabled(false);
                                                }
                                                input.setText(serving);
                                                builder.setView(input);
                                                builder.setPositiveButton("Order Now", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                                                        fragment.setArguments(bundles);
                                                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                                                    }
                                                });
                                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        String value = input.getText().toString();
                                                        String regex = "(\\d+)";
                                                        Matcher matcher = Pattern.compile(regex).matcher(value);
                                                        int i = 1;
                                                        String person = "", family = "", time = "";
                                                        time = value.substring(value.lastIndexOf(":") + 1).trim();
                                                        while (matcher.find()) {
                                                            if (i == 1) {
                                                                person = matcher.group();
                                                            } else {
                                                                family = matcher.group();
                                                            }
                                                            i++;

                                                        }
                                                        try {
                                                            //Update [UserProduct] qty and total
                                                            String commands = "update [Bundle] set [days]='" + person + "',[family]='" + family + "',[ServingTime]='" + time + "' where [id]='" + bundleid + "'";
                                                            PreparedStatement preStmt = con.prepareStatement(commands);
                                                            preStmt.executeUpdate();
                                                            //Toast.makeText(rootView.getContext(), "Bundle Updated Successfully", Toast.LENGTH_LONG).show();
                                                        } catch (Exception ex) {
                                                            Log.d("ReminderService In", ex.getMessage().toString());
                                                        }

                                                        // dialog.cancel();
                                                    }
                                                });
                                                builder.show();
                                            } catch (Exception ex) {
                                                Log.d("ReminderService In", ex.getMessage().toString());
                                            }


                                        }/* else {
                                                Fragment frag = new OrderPointBundleCreateFrag();
                                                FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.mainFrame, frag).commit();
                                            }*/
                                        // Toast.makeText(rootView.getContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();

                                    }

                                });
                                builder.show();


                            } catch (Exception ex) {
                                Log.d("ReminderService In", ex.getMessage().toString());
                            }

                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);

                        }
                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }


            });

        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


