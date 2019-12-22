package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.CheckBox;
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


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyListSavvyFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    int ngouserid;
    ListView lstgross;
    String currentid;
    ImageView productimage, retailerimage;
    TextView txttotal, selecttxt, txtbudget, txtpurchase, txtlisttotal;
    double total = 0;
    double totalpurchases = 0;
    double totalbudget = 0;
    int catergoryclick = 0;
    int statusclick = 0;
    int shoppingclick = 0;
    int ropclick = 0;
    String filterstatusselect, filtershoppingselect, filterropselect;
    String TotalDescribtion = "";
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> isshopping = new ArrayList<String>();

    ArrayList<String> mylistselected = new ArrayList<String>();
    ArrayList<String> scheduleqty = new ArrayList<String>();
    String m_Text_donate = "";
    Bundle bundles = new Bundle();

    ArrayAdapter adapter1;
    SpinnerAdapterImage adapter = null;
    Spinner spinnercategory, spinnerretailer;


    ImageButton btn_budget,btn_purchase;
    Button btn_done, btn_add;
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    String donationpinter = "No";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mylistsavy, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstgross.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstgross.setSelector(R.color.colorPrimary);


        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        txttotal = (TextView) rootView.findViewById(R.id.total);
        selecttxt = (TextView) rootView.findViewById(R.id.selecttxt);

        txtbudget = (TextView) rootView.findViewById(R.id.budget);
        txtpurchase = (TextView) rootView.findViewById(R.id.purchase);
        txtlisttotal = (TextView) rootView.findViewById(R.id.listtotal);


        btn_budget = (ImageButton) rootView.findViewById(R.id.filterbudget);
        btn_purchase = (ImageButton) rootView.findViewById(R.id.filterpurchase);

        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);

        fragmentManager = getFragmentManager();

        ArrayList<ItemDataImage> list = new ArrayList<>();
        // list.add(new ItemData("Select Retailer",R.drawable.blackproduct));
        list.add(new ItemDataImage( R.drawable.logocheckers));
        list.add(new ItemDataImage( R.drawable.logook));
        list.add(new ItemDataImage( R.drawable.logopicknpay));
        list.add(new ItemDataImage(R.drawable.logoshoprite));
        list.add(new ItemDataImage(R.drawable.logospar));
        list.add(new ItemDataImage(R.drawable.logousaveshoprite));

        list.add(new ItemDataImage( R.drawable.logomakro));
        list.add(new ItemDataImage( R.drawable.logofoodlovers));
        list.add(new ItemDataImage( R.drawable.logowoolworth));
        list.add(new ItemDataImage(R.drawable.logoboxer));
        list.add(new ItemDataImage(R.drawable.logogame));
        list.add(new ItemDataImage( R.drawable.logofruitsandveg));
        list.add(new ItemDataImage( R.drawable.logootherretailers));


        spinnerretailer = (Spinner) rootView.findViewById(R.id.spinnerretailer);
        adapter = new SpinnerAdapterImage(this.getActivity(), R.layout.spinner_layout, R.id.txt, list);
        spinnerretailer.setAdapter(adapter);
        spinnerretailer.setOnItemSelectedListener(this);

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        // FillDataOrderByStatus("");

        bundle = this.getArguments();


        FillCategoryData();
        spinnercategory.setOnItemSelectedListener(this);
        //  FillDataOrderBy(3);


        btn_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(rootView.getContext());

                builder1.setTitle("Budget");
                builder1.setMessage("Update Budget:");
                final EditText input = new EditText(rootView.getContext());
                input.setGravity(Gravity.CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                input.setHint("Enter Amount");
                input.setHintTextColor(Color.GRAY);
                builder1.setView(input);

                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text_donate = input.getText().toString();
                        try {
                            ConnectionClass cn = new ConnectionClass();
                            con = cn.connectionclass(un, pass, db, ip);
                            String query = "Update [AppUser] set [budget]='" + m_Text_donate + "' where [id]=" + activity.edthidenuserid.getText().toString();
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            Toast.makeText(rootView.getContext(), "Budget Updated Successfully", Toast.LENGTH_LONG).show();
                            Fragment frag = new MyListSavvyFrag();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                            //  FillDataOrderBy(3);

                        } catch (Exception ex) {
                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage());
                        }

                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder1.show();

            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    TextView lblname, lblprice, lblquantity;
                    CheckBox chkisshopping;
                    for (int i = 0; i < lstgross.getCount(); i++) {
                        lblname = (TextView) lstgross.getChildAt(i).findViewById(R.id.lblname);
                        lblprice = (TextView) lstgross.getChildAt(i).findViewById(R.id.lblprice);
                        lblquantity = (TextView) lstgross.getChildAt(i).findViewById(R.id.lblquantity);

                        chkisshopping = (CheckBox) lstgross.getChildAt(i).findViewById(R.id.chkisshopping);

                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);

                        String isscanned = "";

                        if (chkisshopping.isChecked()) {
                            isscanned = "No";// Change all selected items to be Inventory
                            String commands = "update [UserProduct] set [isscanned]='" + isscanned + "' where [description]='" + lblname.getText().toString() + "' and [totatitemvalue]='" + Double.parseDouble(lblprice.getText().toString().substring(8)) + "' and [quantity]='" + Integer.parseInt(lblquantity.getText().toString().substring(5)) + "'";
                           PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();

                            totalbudget=totalbudget-Double.parseDouble(lblprice.getText().toString().substring(8));
                            double newbudget=totalbudget;
                            String query = "Update [AppUser] set [budget]='" + String.valueOf(newbudget) + "' where [id]=" + activity.edthidenuserid.getText().toString();
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            Log.d("ReminderService In", lblname.getText().toString() + " new budget="+newbudget+" Is scann" + isscanned);
                        }else{
                            isscanned = "Yes";// Change all selected items to be Inventory
                            String commands = "update [UserProduct] set [isscanned]='" + isscanned + "' where [description]='" + lblname.getText().toString() + "' and [totatitemvalue]='" + Double.parseDouble(lblprice.getText().toString().substring(8)) + "' and [quantity]='" + Integer.parseInt(lblquantity.getText().toString().substring(5)) + "'";
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }




                    }
                    Toast.makeText(rootView.getContext(), "Transferred to Inventory Successfully", Toast.LENGTH_LONG).show();
                    Fragment frag = new MyListSavvyFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage());
                }

                // FillDataOrderBy(3);

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag =   new SearchListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        ItemDataImage c = adapter.getItem(position);
        // spinnerretailer.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + c.getText()+String.valueOf(spinnerretailer.getSelectedItemPosition()+1) , Toast.LENGTH_LONG).show();

        FillDataOrderBy(spinnerretailer.getSelectedItemPosition() + 1);


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

                String query = "select [description],[quantity],[totatitemvalue],[isscanned],[image] from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [retailerid]=" + orderbyId + " and  [isscanned]='Yes' order by [retailerid] asc";

               // String query = "select * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [retailerid]=" + orderbyId + " order by [retailerid] asc";
               /* if(catergoryclick==1){
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString()+" and [retailerid]="+orderbyId+" order by [retailerid] asc";
                }else{
                    query = "select * from [UserProduct] where [userid]="+activity.edthidenuserid.getText().toString();
                }*/

                name.clear();
                quantity.clear();
                price.clear();
                isshopping.clear();
                proimage.clear();


                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                total = 0;
                totalpurchases = 0;
                while (rs.next()) {

                    name.add(rs.getString("description").toString());
                    quantity.add(rs.getString("quantity").toString());
                    price.add(rs.getString("totatitemvalue").toString());
                    isshopping.add(rs.getString("isscanned").toString());


                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);


                }


                CustomSavvyListAdapter adapter = new CustomSavvyListAdapter(this.getActivity(), proimage, name, price, quantity);
                lstgross.setAdapter(adapter);

                query = "select [totatitemvalue] from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString() + " and [retailerid]=" + orderbyId + " and  [isscanned]='Yes' order by [retailerid] asc";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                        totalpurchases = totalpurchases + Double.parseDouble(rs.getString("totatitemvalue").toString());


                }
                txtpurchase.setText("R" + String.valueOf(totalpurchases));

                query = "select [totatitemvalue] from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString()+" and  [isscanned]='Yes'" ;
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {

                        total = total + Double.parseDouble(rs.getString("totatitemvalue").toString());

                }

                txtlisttotal.setText("R" + String.valueOf(total));

                String query1 = "select [budget] from [AppUser] where [id]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    totalbudget = Double.parseDouble(rs1.getString("budget").toString());
                }
                txtbudget.setText("R" + String.valueOf(totalbudget));
                double budgetstatus = totalbudget - totalpurchases;
                if (budgetstatus > 1000) {
                    txtpurchase.setTextColor(rootView.getResources().getColor(R.color.colorGreen));
                    Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.budget1);
                    btn_purchase.setImageBitmap(im);
                } else if (budgetstatus <= 1000 && budgetstatus >= 100) {
                    txtpurchase.setTextColor(rootView.getResources().getColor(R.color.colorYellow));
                    Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.budget1);
                    btn_purchase.setImageBitmap(im);
                } else {
                    txtpurchase.setTextColor(rootView.getResources().getColor(R.color.colorRed));
                    Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.budget2);
                    btn_purchase.setImageBitmap(im);
                }

            }
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
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


