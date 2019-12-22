package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class OrderPointBundleOrderOptionalFrag extends Fragment implements OnItemSelectedListener {
    MainActivity activity = MainActivity.instance;
    Button btn_delete;
    Button btn_order;
    Bundle bundle;
    int bundleid;
    Bundle bundles = new Bundle();
    byte[] byteArray;
    Connection con;
    private View currentSelectedView;
    String currentid;
    String db;
    ArrayList<String> description = new ArrayList<>();
    TextView edtbundlename;
    TextView edtprice;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.orderpointbundleoderoptional, container, false);
        this.lstgross = (ListView) this.rootView.findViewById(R.id.lstgross);
        this.edtbundlename = (TextView) this.rootView.findViewById(R.id.edtbundlename);
        this.edtquantity = (TextView) this.rootView.findViewById(R.id.edtquantity);
        this.edtprice = (TextView) this.rootView.findViewById(R.id.edtprice);
        this.loginlayout = (LinearLayout) this.rootView.findViewById(R.id.layout);
        this.btn_order = (Button) this.rootView.findViewById(R.id.btn_order);
        this.btn_delete = (Button) this.rootView.findViewById(R.id.btn_delete);
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
                    OrderPointBundleOrderOptionalFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderOptionalFrag.this.bundleid);
                    OrderPointBundleOrderOptionalFrag.this.bundles.putInt("userid", OrderPointBundleOrderOptionalFrag.this.userid);
                    OrderPointBundleOrderOptionalFrag.this.bundles.putInt("qty", OrderPointBundleOrderOptionalFrag.this.qty);
                    OrderPointBundleOrderOptionalFrag.this.bundles.putString("price", price);
                    OrderPointBundleOrderOptionalFrag.this.bundles.putString("name", OrderPointBundleOrderOptionalFrag.this.selected_bundle);
                    OrderPointBundleOrderOptionalFrag.this.bundles.putString("ordertype","optional");
                    OrderPointBundleConfirmFrag fragment = new OrderPointBundleConfirmFrag();
                    fragment.setArguments(OrderPointBundleOrderOptionalFrag.this.bundles);
                    OrderPointBundleOrderOptionalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                } catch (Exception ex) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("An error occurred: ");
                    sb.append(ex.getMessage());
                    Log.d("ReminderService In", sb.toString());
                }
            }
        });
        this.btn_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OrderPointBundleOrderOptionalFrag.this.bundles.putInt("bundleid", OrderPointBundleOrderOptionalFrag.this.bundleid);
                OrderPointBundleOrderOptionalFrag.this.bundles.putInt("userid", OrderPointBundleOrderOptionalFrag.this.userid);
                OrderPointBundleOrderOptionalFrag.this.bundles.putInt("qty", OrderPointBundleOrderOptionalFrag.this.qty);
                OrderPointBundleOrderOptionalFrag.this.bundles.putString("price", OrderPointBundleOrderOptionalFrag.this.price);
                OrderPointBundleOrderOptionalFrag.this.bundles.putString("name", OrderPointBundleOrderOptionalFrag.this.selected_bundle);
                OrderPointBundleOrderFrag fragment = new OrderPointBundleOrderFrag();
                fragment.setArguments(OrderPointBundleOrderOptionalFrag.this.bundles);
                OrderPointBundleOrderOptionalFrag.this.fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }
        });
        return this.rootView;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void FillDataOrderByStatus(String search) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT b.id as b_id,name,price,priceoptional,quantity,b.image as b_image,description,bi.image as bi_image   FROM [Bundle] b   inner join [BundleItemsOptional] bi on bi.bundleid=b.id where [name]='");
            sb.append(search);
            sb.append("'");
            String query = sb.toString();
            this.description.clear();
            this.proimage.clear();
            Log.d("ReminderService In", query);
            ResultSet rs = this.con.prepareStatement(query).executeQuery();
            while (rs.next()) {

                this.bundleid = rs.getInt("b_id");
                this.qty = rs.getInt("quantity");
                this.price = rs.getString("priceoptional");
                byte[] decodeString1 = Base64.decode(rs.getString("b_image"), 0);
                Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                this.loginlayout.setBackground(new BitmapDrawable(getResources(), decodebitmap1));
                this.edtbundlename.setText(rs.getString("name"));
                this.edtprice.setTop(decodebitmap1.getHeight());
                TextView textView = this.edtprice;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("R");
                sb2.append(rs.getString("priceoptional"));
                textView.setText(sb2.toString());
                TextView textView2 = this.edtquantity;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" ");
                sb3.append(rs.getString("quantity"));
                sb3.append(" items");
                textView2.setText(sb3.toString());
                this.description.add(rs.getString("description").toString());
                byte[] decodeString = Base64.decode(rs.getString("bi_image"), 0);
                this.proimage.add(BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length));
            }
            this.lstgross.setAdapter(new OrderPointBundleCreateAdapter(getActivity(), this.proimage, this.description));
        } catch (Exception ex) {
            Log.d("ReminderService In","9999999"+ ex.getMessage().toString());
        }
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
}
