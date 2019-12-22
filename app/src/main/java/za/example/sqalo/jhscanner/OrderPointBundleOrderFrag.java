package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class OrderPointBundleOrderFrag extends Fragment implements OnItemSelectedListener {
    MainActivity activity = MainActivity.instance;

    Button btn_order,btn_orderfull,btn_delete;
    Bundle bundle;
    int bundleid;
    String bundlename;
    Bundle bundles = new Bundle();
    byte[] byteArray;
    int click_count = 0;
    Connection con;
    /* access modifiers changed from: private */
    public View currentSelectedView;
    String currentid;
    String db;
    ArrayList<String> description = new ArrayList<>();

    TextView edtbundlename;
    TextView edtprice,edtcountoptional,edtrecipe;
    TextView edtquantity;
    String encodedImage;
    String firstname = "";
    FragmentManager fragmentManager;
    String ip;
    LinearLayout loginlayout;
    ListView lstgross;
    String m_Text_donate = "";
    String pass;
    String price;
    ArrayList<Bitmap> proimage = new ArrayList<>();
    int qty;
    View rootView;
    String selected_bundle;
    String un;
    int userid;
    ImageView optional,recipe;
    int bundleoptional=0;
    int bundleoptionalprice=0;
    final CharSequence[] items = {"Assign Recipe", "View Recipe"};
    Spinner spinnerrecipe;
    ArrayAdapter adapter1;
    Spinner spinnerrecipe1;
    ArrayAdapter adapter2;
    String status="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.orderpointbundleoder, container, false);
        this.lstgross = (ListView) this.rootView.findViewById(R.id.lstgross);
        this.edtbundlename = (TextView) this.rootView.findViewById(R.id.edtbundlename);
        this.edtquantity = (TextView) this.rootView.findViewById(R.id.edtquantity);
        this.edtcountoptional = (TextView) this.rootView.findViewById(R.id.edtcountoptional);
        this.edtrecipe = (TextView) this.rootView.findViewById(R.id.edtrecipe);
        this.edtprice = (TextView) this.rootView.findViewById(R.id.edtprice);
        this.loginlayout = (LinearLayout) this.rootView.findViewById(R.id.layout);
        this.btn_order = (Button) this.rootView.findViewById(R.id.btn_order);
        this.btn_orderfull = (Button) this.rootView.findViewById(R.id.btn_orderfull);
        this.btn_delete = (Button) this.rootView.findViewById(R.id.btn_delete);
        optional = (ImageView) rootView.findViewById(R.id.optional);
        recipe = (ImageView) rootView.findViewById(R.id.recipe);

        spinnerrecipe = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerrecipe.setOnItemSelectedListener(this);
        spinnerrecipe1 = (Spinner) rootView.findViewById(R.id.spinnercategory1);
        spinnerrecipe1.setOnItemSelectedListener(this);
      
        this.fragmentManager = getFragmentManager();
        this.ip = "winsqls01.cpt.wa.co.za";
        this.db = "JHShopper";
        this.un = "sqaloits";
        this.pass = "422q5mfQzU";
        this.con = new ConnectionClass().connectionclass(this.un, this.pass, this.db, this.ip);
        if (this.con == null) {
            Toast.makeText(this.rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        this.bundle = getArguments();
        try {

            if (this.activity.edthidenuserid.getText().toString().equals("")) {
                this.userid = Integer.parseInt(this.activity.edthidenuserrole.getText().toString());
            } else {
                this.userid = Integer.parseInt(this.activity.edthidenuserid.getText().toString());
                if (this.userid == 3) {
                    this.btn_delete.setVisibility(View.VISIBLE);
                }else{
                    this.btn_delete.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append(ex.getMessage());
            sb.append("######");
            Log.d("ReminderService In", sb.toString());
        }
        try {
            if (this.bundle != null && !this.bundle.getString("name").equals("")) {
                this.selected_bundle = this.bundle.getString("name");
                FillDataOrderByStatus(this.selected_bundle);


            }
        } catch (Exception ex2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(ex2.getMessage());
            sb2.append("######");
            Log.d("ReminderService In", sb2.toString());
        }
        this.btn_order.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleOrderFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderFrag.this.bundleid);
                    OrderPointBundleOrderFrag.this.bundles.putInt("userid", OrderPointBundleOrderFrag.this.userid);
                    OrderPointBundleOrderFrag.this.bundles.putInt("qty", OrderPointBundleOrderFrag.this.qty);
                    OrderPointBundleOrderFrag.this.bundles.putString("price", OrderPointBundleOrderFrag.this.price);
                    OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.selected_bundle);
                    OrderPointBundleOrderFrag.this.bundles.putString("ordertype", "main");
                    OrderPointBundleConfirmFrag fragment = new OrderPointBundleConfirmFrag();
                    fragment.setArguments(OrderPointBundleOrderFrag.this.bundles);
                    OrderPointBundleOrderFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("An error occurred: ");
                    sb.append(ex.getMessage());
                    Log.d("ReminderService In", sb.toString());
                }
            }
        });
        this.btn_orderfull.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    OrderPointBundleOrderFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderFrag.this.bundleid);
                    OrderPointBundleOrderFrag.this.bundles.putInt("userid", OrderPointBundleOrderFrag.this.userid);
                    OrderPointBundleOrderFrag.this.bundles.putInt("qty", OrderPointBundleOrderFrag.this.qty);
                    OrderPointBundleOrderFrag.this.bundles.putString("price",String.valueOf(Integer.parseInt(price)+bundleoptionalprice));
                    OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.selected_bundle);
                    OrderPointBundleOrderFrag.this.bundles.putString("ordertype", "full");
                    OrderPointBundleConfirmFrag fragment = new OrderPointBundleConfirmFrag();
                    fragment.setArguments(OrderPointBundleOrderFrag.this.bundles);
                    OrderPointBundleOrderFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("An error occurred: ");
                    sb.append(ex.getMessage());
                    Log.d("ReminderService In", sb.toString());
                }
            }
        });
        this.optional.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {

                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SELECT b.id as b_id,name,price,priceoptional,quantity,b.image as b_image,description,bi.image as bi_image   FROM [Bundle] b   inner join [BundleItemsOptional] bi on bi.bundleid=b.id where [name]='");
                    sb2.append(OrderPointBundleOrderFrag.this.bundlename);
                    sb2.append("'");
                    ResultSet rs1 = OrderPointBundleOrderFrag.this.con.prepareStatement(sb2.toString()).executeQuery();
                    rs1.next();
                    if (rs1.getRow() != 0) {
                        btn_orderfull.setVisibility(View.GONE);
                        Log.d("ReminderService In", "OPTIONAL TRUE");
                        try {
                            Builder builder = new Builder(OrderPointBundleOrderFrag.this.rootView.getContext());
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Optional Items @ R");
                            sb3.append(rs1.getString("priceoptional"));
                            price = rs1.getString("priceoptional");

                            OrderPointBundleOrderFrag.this.bundles.putInt("id", OrderPointBundleOrderFrag.this.bundleid);
                            OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.bundlename);
                            OrderPointBundleOrderFrag.this.bundles.putString("price", price);

                            builder.setMessage(sb3.toString());
                            builder.setPositiveButton("View Items", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    OrderPointBundleOrderOptionalFrag fragment = new OrderPointBundleOrderOptionalFrag();
                                    fragment.setArguments(OrderPointBundleOrderFrag.this.bundles);
                                    OrderPointBundleOrderFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                }
                            });
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } catch (Exception ex) {
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }
                    } else {
                        Log.d("ReminderService In", "OPTIONAL FALSE");
                        Context context = OrderPointBundleOrderFrag.this.rootView.getContext();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(OrderPointBundleOrderFrag.this.bundlename);
                        sb4.append(" has no optional items!!!");
                        Toast.makeText(context, sb4.toString(), Toast.LENGTH_LONG).show();
                    }
                    if (!(OrderPointBundleOrderFrag.this.currentSelectedView == null || OrderPointBundleOrderFrag.this.currentSelectedView == view)) {
                        OrderPointBundleOrderFrag.this.unhighlightCurrentRow(OrderPointBundleOrderFrag.this.currentSelectedView);
                    }
                    OrderPointBundleOrderFrag.this.currentSelectedView = view;
                    OrderPointBundleOrderFrag.this.highlightCurrentRow(OrderPointBundleOrderFrag.this.currentSelectedView);

                } catch (Exception ex2) {
                    Log.d("ReminderService In", ex2.getMessage().toString());
                }
            }
        });
     
        this.btn_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OrderPointBundleOrderFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderFrag.this.bundleid);
                OrderPointBundleOrderFrag.this.bundles.putInt("userid", OrderPointBundleOrderFrag.this.userid);
                OrderPointBundleOrderFrag.this.bundles.putInt("qty", OrderPointBundleOrderFrag.this.qty);
                OrderPointBundleOrderFrag.this.bundles.putString("price", OrderPointBundleOrderFrag.this.price);
                OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.selected_bundle);
                Builder builder = new Builder(OrderPointBundleOrderFrag.this.rootView.getContext());
                builder.setTitle(OrderPointBundleOrderFrag.this.selected_bundle);
                builder.setMessage("Delete Bundle?");
                builder.setIcon(OrderPointBundleOrderFrag.this.rootView.getResources().getDrawable(R.drawable.remove));
                builder.setPositiveButton(IntentIntegrator.DEFAULT_YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Log.d("ReminderService In","Bundle Id"+ bundleid);
                            StringBuilder sb = new StringBuilder();
                            sb.append("delete from [Bundle] where [id]='");
                            sb.append(bundleid);
                            sb.append("'");
                            OrderPointBundleOrderFrag.this.con.prepareStatement(sb.toString()).executeUpdate();
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("delete from [BundleItems] where [bundleid]='");
                            sb2.append(bundleid);
                            sb2.append("'");
                            OrderPointBundleOrderFrag.this.con.prepareStatement(sb2.toString()).executeUpdate();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("delete from [BundleItemsOptional] where [bundleid]='");
                            sb3.append(bundleid);
                            sb3.append("'");
                            OrderPointBundleOrderFrag.this.con.prepareStatement(sb3.toString()).executeUpdate();
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("delete from [BundleDeliveryAddress] where [bundleid]='");
                            sb4.append(OrderPointBundleOrderFrag.this.bundleid);
                            sb4.append("'");
                            OrderPointBundleOrderFrag.this.con.prepareStatement(sb4.toString()).executeUpdate();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("delete from [BundleOrder] where [bundleid]='");
                            sb5.append(OrderPointBundleOrderFrag.this.bundleid);
                            sb5.append("'");
                            OrderPointBundleOrderFrag.this.con.prepareStatement(sb5.toString()).executeUpdate();
                            Toast.makeText(OrderPointBundleOrderFrag.this.rootView.getContext(), "Bundle Deleted Successfully", Toast.LENGTH_LONG).show();
                            Fragment frag = new OrderPointBundleListFrag();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                        } catch (Exception ex) {
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }
                    }
                });
                builder.setNegativeButton(IntentIntegrator.DEFAULT_NO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        this.recipe.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                try {

                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Select Option");
                    builder.setPositiveButton("Assign Recipe", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            status="assign";
                            spinnerrecipe.performClick();
                        }
                    });
                    builder.setNegativeButton("View Recipe", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            status="view";
                            spinnerrecipe1.performClick();

                        }
                    });
                    builder.show();



                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

    }
});

        return this.rootView;
    }
   /* int GetTotalPrice(int bundleid){
        int total=0;
        try{
            String query11 = "select SUM(up.price* SUBSTRING(bo.description, 1,CHARINDEX('X',bo.description)-1)) as Total from UserProduct up\n" +
                    "inner join BundleItemsOptional bo on   SUBSTRING(bo.description, 5,LEN(bo.description))  = up.description\n" +
                    "where bo.bundleid='" + bundleid + "'";
            PreparedStatement ps11 = con.prepareStatement(query11);
            ResultSet rs11 = ps11.executeQuery();
            rs11.next();
            total=rs11.getInt("Total");
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
        return total;
    }*/


    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
       String recipe = "";
        if(!spinnerrecipe.getSelectedItem().toString().equals("Select Recipe")){
recipe=spinnerrecipe.getSelectedItem().toString();
        }else if(!spinnerrecipe1.getSelectedItem().toString().equals("Select Recipe")){
            recipe=spinnerrecipe1.getSelectedItem().toString();
        }
        Log.d("ReminderService In", status+" "+userid+" "+bundleid+" "+recipe);
        if(!status.equals("") && !recipe.equals("Select Recipe")){
            if(status.equals("view")){
                //go to RecipeFrag if ordered

                try {
                    String query1 = "select * from [BundleOrder] where [userid]='" +userid + "' and [bundleid]='" + bundleid + "'";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1= ps1.executeQuery();
                    String found="";
                    while (rs1.next()) {
                        found="yes";
                    }
                    Log.d("ReminderService In", found);
                    if(found.equals("yes")){
                        String query = "select * from [BundleRecipe] where [name]='" +recipe + "' and [bundleid]='" + bundleid + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            bundles.putString("name", bundle.getString("name"));
                            bundles.putString("ingredients", rs.getString("ingredients").toString());
                            bundles.putString("instructions", rs.getString("instructions").toString());
                            bundles.putString("image", rs.getString("image").toString());
                        }
                        OrderPointBundleRecipeFrag fragment = new OrderPointBundleRecipeFrag();
                        fragment.setArguments(bundles);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }else{
                        Toast.makeText(OrderPointBundleOrderFrag.this.rootView.getContext(), "Place an Order First!!!", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }else{
                //assign admin only
                if(userid==3){
                    try {
                        String query1 = "select * from [BundleRecipe] where [name]='" +recipe + "' and [bundleid]='" + bundleid + "'";
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1= ps1.executeQuery();
                        String found="";
                        while (rs1.next()) {
                            found="yes";
                        }
                        if(found.equals("yes")){
                            Toast.makeText(OrderPointBundleOrderFrag.this.rootView.getContext(), "Recipe Already Exist", Toast.LENGTH_LONG).show();
                        }else {
                            String query = "INSERT INTO BundleRecipe (name, ingredients, instructions,image,bundleid)\n" +
                                    "SELECT name, ingredients, instructions,image,'"+bundleid+"'" +
                                    " FROM BundleRecipe\n" +
                                    "WHERE [name]='" +recipe + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ps.executeUpdate();
                            Log.d("ReminderService In", query);
                            OrderPointBundleOrderFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderFrag.this.bundleid);
                            OrderPointBundleOrderFrag.this.bundles.putInt("userid", OrderPointBundleOrderFrag.this.userid);
                            OrderPointBundleOrderFrag.this.bundles.putInt("qty", OrderPointBundleOrderFrag.this.qty);
                            OrderPointBundleOrderFrag.this.bundles.putString("price", OrderPointBundleOrderFrag.this.price);
                            OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.selected_bundle);
                            OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                            Toast.makeText(OrderPointBundleOrderFrag.this.rootView.getContext(), "Recipe Assigned Successfully", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }else{
                    Toast.makeText(OrderPointBundleOrderFrag.this.rootView.getContext(), "Admin ONLY!!!", Toast.LENGTH_LONG).show();
                }

            }
        }

    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void FillDataOrderByStatus(String search) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT b.id as b_id,name,price,quantity,b.image as b_image,bi.description,bi.image as bi_image   FROM [Bundle] b   inner join [BundleItems] bi on bi.bundleid=b.id where [name]='");
            sb.append(search);
            sb.append("'");
            String query = sb.toString();
            this.description.clear();
            this.proimage.clear();
            Log.d("ReminderService In", query);
            ResultSet rs = this.con.prepareStatement(query).executeQuery();
            while (rs.next()) {
                this.bundleid = rs.getInt("b_id");
                this.bundlename = rs.getString("name");
                this.qty = rs.getInt("quantity");
                this.price = rs.getString("price");
                byte[] decodeString1 = Base64.decode(rs.getString("b_image"), 0);
                Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                this.loginlayout.setBackground(new BitmapDrawable(getResources(), decodebitmap1));
                this.edtbundlename.setText(this.bundlename);
                this.edtprice.setTop(decodebitmap1.getHeight());
                TextView textView = this.edtprice;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("R");
                sb2.append(this.price);
                textView.setText(sb2.toString());
                TextView textView2 = this.edtquantity;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" ");
                sb3.append(this.qty);
                sb3.append(" items");
                textView2.setText(sb3.toString());
                this.description.add(rs.getString("description").toString());
                byte[] decodeString = Base64.decode(rs.getString("bi_image"), 0);
                this.proimage.add(BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length));



            }
            //Poulate all recipe
            try {

                String query1 = "select distinct name from [BundleRecipe]";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                ArrayList<String> category = new ArrayList<String>();
                category.add("Select Recipe");
                while (rs1.next()) {
                    category.add(rs1.getString("name"));

                }
                adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerrecipe.setAdapter(adapter1);

            } catch (Exception ex) {
                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
            }
            try {

                String query1 = "select name from [BundleRecipe] where  [bundleid]='" + bundleid + "'";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                ArrayList<String> category = new ArrayList<String>();
                category.add("Select Recipe");
                while (rs1.next()) {
                    category.add(rs1.getString("name"));

                }
                adapter2 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerrecipe1.setAdapter(adapter2);

            } catch (Exception ex) {
                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
            }
            // Fill optional
            StringBuilder srb = new StringBuilder();
            srb.append("SELECT id  FROM  [BundleItemsOptional]  where bundleid='");
            srb.append(OrderPointBundleOrderFrag.this.bundleid);
            srb.append("'");
            ResultSet rs1 = OrderPointBundleOrderFrag.this.con.prepareStatement(srb.toString()).executeQuery();

            while( rs1.next()){
                bundleoptional=bundleoptional+1;
            }
            edtcountoptional.setText(String.valueOf(bundleoptional));

            // Fill Recipe
            bundleoptional=0;
            StringBuilder srb1 = new StringBuilder();
            srb1.append("SELECT id  FROM  [BundleRecipe]  where bundleid='");
            srb1.append(OrderPointBundleOrderFrag.this.bundleid);
            srb1.append("'");
            ResultSet rs11 = OrderPointBundleOrderFrag.this.con.prepareStatement(srb1.toString()).executeQuery();

            while( rs11.next()){
                bundleoptional=bundleoptional+1;
            }
            edtrecipe.setText(String.valueOf(bundleoptional));

            StringBuilder srb2 = new StringBuilder();
            srb2.append("SELECT priceoptional   FROM [Bundle] b   inner join [BundleItemsOptional] bi on bi.bundleid=b.id where b.id='");
            srb2.append(OrderPointBundleOrderFrag.this.bundleid);
            srb2.append("'");
            ResultSet rs2 = OrderPointBundleOrderFrag.this.con.prepareStatement(srb2.toString()).executeQuery();

            while( rs2.next()){
                bundleoptionalprice=Integer.parseInt(rs2.getString("priceoptional"));
            }

            this.lstgross.setAdapter(new OrderPointBundleCreateOptionAdapter(getActivity(), this.proimage, this.description));
            this.lstgross.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                    try {

                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("SELECT b.id as b_id,name,price,priceoptional,quantity,b.image as b_image,description,bi.image as bi_image   FROM [Bundle] b   inner join [BundleItemsOptional] bi on bi.bundleid=b.id where [name]='");
                            sb2.append(OrderPointBundleOrderFrag.this.bundlename);
                            sb2.append("'");
                            ResultSet rs1 = OrderPointBundleOrderFrag.this.con.prepareStatement(sb2.toString()).executeQuery();
                            rs1.next();
                            if (rs1.getRow() != 0) {
                                btn_orderfull.setVisibility(View.GONE);
                                Log.d("ReminderService In", "OPTIONAL TRUE");
                                try {
                                    Builder builder = new Builder(OrderPointBundleOrderFrag.this.rootView.getContext());
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Optional Order @ R");
                                    sb3.append(rs1.getString("priceoptional"));
                                    price = rs1.getString("priceoptional");

                                    OrderPointBundleOrderFrag.this.bundles.putInt("id", OrderPointBundleOrderFrag.this.bundleid);
                                    OrderPointBundleOrderFrag.this.bundles.putString("name", OrderPointBundleOrderFrag.this.bundlename);
                                    OrderPointBundleOrderFrag.this.bundles.putString("price", price);

                                    builder.setMessage(sb3.toString());
                                    builder.setPositiveButton("View Items", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            OrderPointBundleOrderOptionalFrag fragment = new OrderPointBundleOrderOptionalFrag();
                                            fragment.setArguments(OrderPointBundleOrderFrag.this.bundles);
                                            OrderPointBundleOrderFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                        }
                                    });
                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }
                            } else {
                                Log.d("ReminderService In", "OPTIONAL FALSE");
                                Context context = OrderPointBundleOrderFrag.this.rootView.getContext();
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(OrderPointBundleOrderFrag.this.bundlename);
                                sb4.append(" has no optional items!!!");
                                Toast.makeText(context, sb4.toString(), Toast.LENGTH_LONG).show();
                            }
                            if (!(OrderPointBundleOrderFrag.this.currentSelectedView == null || OrderPointBundleOrderFrag.this.currentSelectedView == view)) {
                                OrderPointBundleOrderFrag.this.unhighlightCurrentRow(OrderPointBundleOrderFrag.this.currentSelectedView);
                            }
                            OrderPointBundleOrderFrag.this.currentSelectedView = view;
                            OrderPointBundleOrderFrag.this.highlightCurrentRow(OrderPointBundleOrderFrag.this.currentSelectedView);

                    } catch (Exception ex2) {
                        Log.d("ReminderService In", ex2.getMessage().toString());
                    }
                }
            });
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
    }

    /* access modifiers changed from: private */
    public void highlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(getResources().getColor(R.color.colorGray));
    }

    /* access modifiers changed from: private */
    public void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(View.GONE);
    }
}
