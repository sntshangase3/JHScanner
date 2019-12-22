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
import android.widget.Button;
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
import java.util.concurrent.TimeUnit;


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
    int userid,qty,id;

    ArrayList<Bitmap> addcart = new ArrayList<Bitmap>();
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();


    ImageButton search,filtercategory;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    Button btn_schedule,btn_create,btn_update;
    EditText edtsearch,edtbundlename,edtquantity,edtprice;
TextView txtorderno;
    byte[] byteArray;
    String encodedImage;

    final CharSequence[] items = {"Delivery Areas", "View/Order","Recipe Idea"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundlelist, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        edtbundlename = (EditText) rootView.findViewById(R.id.edtbundlename);
        edtquantity = (EditText) rootView.findViewById(R.id.edtquantity);
        edtprice = (EditText) rootView.findViewById(R.id.edtprice);
        edtbundlename = (EditText) rootView.findViewById(R.id.edtbundlename);
        bundleprofileImage = (ImageView) rootView.findViewById(R.id.bundleprofileImage);
        txtorderno = (TextView) rootView.findViewById(R.id.txtorderno);

        search = (ImageButton) rootView.findViewById(R.id.search);
        filtercategory = (ImageButton) rootView.findViewById(R.id.filtercategory);
        btn_schedule = (Button) rootView.findViewById(R.id.btn_schedule);
        btn_create = (Button) rootView.findViewById(R.id.btn_create);
        btn_update = (Button) rootView.findViewById(R.id.btn_update);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }



        try {
            FillDataOrderByStatus(search_product);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");

        }

        try {
            if (activity.edthidenuserid.getText().toString().equals("")) {

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
                if(userid!=3){
                    btn_schedule.setVisibility(View.GONE);
                }

            } else {
                userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
                String query = "select * from [AppUser] where [id]=" + userid;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("firstname");
                if(userid!=3){
                    btn_schedule.setVisibility(View.GONE);
                }
            }

        } catch (Exception ex) {

        }



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                    FillDataOrderByStatus(search_product);
            }
        });


        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new OrderPointBundleCreateFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            }
        });
        filtercategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    OrderPointBundleListAllOrdersFrag fragment = new OrderPointBundleListAllOrdersFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(rootView.getContext(), "You've selected " + name.get(position) , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    private View currentSelectedView;
    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {

                //Co-user login
            String query1 = "select  * from [BundleOrder] where [userid]="+activity.edthidenuserid.getText().toString();
            PreparedStatement ps1 = con.prepareStatement(query1);
            ResultSet rs1 = ps1.executeQuery();
            int total=0;
            while (rs1.next()) {
                total=total+1;
            }
            txtorderno.setText(String.valueOf(total));
                String query = "select * from [Bundle]";
                if (!search.equals("")) {
                    query = "select * from [Bundle] where [name] like '%" + search + "%'";
                }
            Log.d("ReminderService In",  query);
                name.clear();
                proimage.clear();
                addcart.clear();
                price.clear();
                quantity.clear();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    name.add(rs.getString("name").toString());
                    quantity.add(rs.getString("quantity").toString()+" Left");
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    Log.d("ReminderService In",String.valueOf(decodebitmap.getHeight()));
                    if(decodebitmap.getHeight()>=500)
                    decodebitmap=Bitmap.createScaledBitmap(decodebitmap,decodebitmap.getWidth(),500,false);
                    proimage.add(decodebitmap);
                    Bitmap cart = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.addcart);
                    addcart.add(cart);
                    price.add("R"+rs.getString("price").toString());
                }


                OrderPointBundleListAdapter adapter = new OrderPointBundleListAdapter(this.getActivity(), proimage,addcart, name,quantity,price);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub

                        try {

                            String selectedname = name.get(position);
                            String query = "select * from [Bundle] where [name]='" + selectedname + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            Log.d("ReminderService In",query);
                            while (rs.next()) {


                                bundles.putString("id", rs.getString("id").toString());
                                bundles.putString("name", selectedname);
                                bundles.putString("price", rs.getString("price").toString());
                                bundles.putString("quantity", rs.getString("quantity").toString());




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
                                                    OrderPointBundleListAllOrdersFrag fragment = new OrderPointBundleListAllOrdersFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

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
                                            }  else if (items[which].equals("Recipe Idea")) {
                                                try {


                                                    ChefIdeasListSearchedFrag fragment = new ChefIdeasListSearchedFrag();
                                                    fragment.setArguments(bundles);
                                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                                } catch (Exception ex) {

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
                        } catch(Exception ex) {
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
    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


