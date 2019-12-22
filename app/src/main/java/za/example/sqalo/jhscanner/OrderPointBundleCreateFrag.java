package za.example.sqalo.jhscanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class OrderPointBundleCreateFrag extends Fragment implements AdapterView.OnItemSelectedListener {


    View rootView;
    String m_Text_donate = "";
    String status="No";
    String firstname = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid;
    ImageView bundleprofileImage;
    int userid,qty,id;


    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> description = new ArrayList<String>();


    ArrayList<String> schedule = new ArrayList<String>();
    ArrayList<String> scheduleqty = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();

    ArrayList<String> optionalid = new ArrayList<String>();
    ArrayList<String> optionalqty = new ArrayList<String>();
    ArrayList<String> mainorder = new ArrayList<String>();


    ImageButton search;
    String search_product = "";
    Bundle bundles = new Bundle();

    ArrayAdapter adapter;
    ArrayAdapter adapter1, adapter2;
   TextView txtselect;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    Button btn_schedule,btn_create;
    EditText edtsearch,edtbundlename,edtquantity,edtprice,edtpriceoptinal,edtdays,edtfamily;
    Spinner spinnerservingtime;
int count=1;
    byte[] byteArray;
    String encodedImage;
    LinearLayout layout_bundle_details,layoutlist;
    private static final int REQUEST = 112;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.orderpointbundlecreate, container, false);
        layout_bundle_details = (LinearLayout) rootView. findViewById(R.id.layout_bundle_details);
        layoutlist = (LinearLayout) rootView. findViewById(R.id.layoutlist);

        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        edtbundlename = (EditText) rootView.findViewById(R.id.edtbundlename);
        edtquantity = (EditText) rootView.findViewById(R.id.edtquantity);
        edtprice = (EditText) rootView.findViewById(R.id.edtprice);
        edtpriceoptinal = (EditText) rootView.findViewById(R.id.edtpriceoptinal);

        edtdays = (EditText) rootView.findViewById(R.id.edtdays);
        edtfamily = (EditText) rootView.findViewById(R.id.edtfamily);
        spinnerservingtime = (Spinner) rootView. findViewById(R.id.spinnerservingtime);

        edtbundlename = (EditText) rootView.findViewById(R.id.edtbundlename);
        bundleprofileImage = (ImageView) rootView.findViewById(R.id.bundleprofileImage);
        txtselect = (TextView) rootView. findViewById(R.id.txtselect);
        search = (ImageButton) rootView.findViewById(R.id.search);
        btn_schedule = (Button) rootView.findViewById(R.id.btn_schedule);
        btn_create = (Button) rootView.findViewById(R.id.btn_create);



      //  btn_update = (Button) rootView.findViewById(R.id.btn_update);

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


        bundle = this.getArguments();
        FillDataOrderByStatus(search_product);
        FillServingTimeData();
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

        spinnerservingtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                    FillDataOrderByStatus(search_product);
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Delivery Addresses");
                    builder.setIcon(rootView.getResources().getDrawable(R.drawable.map_search));
                    final EditText input = new EditText(rootView.getContext());
                    input.setGravity(Gravity.CENTER);
                    input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                    input.setHint("Enter Delivery Address:" + String.valueOf(count));
                    input.setHintTextColor(Color.GRAY);
                    builder.setView(input);
                    builder.setMessage("Add Address?");
                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text_donate = input.getText().toString();
                            address.add(m_Text_donate);
                            count++;

                        }
                    });
                    builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(address.size()>0){
                                layoutlist.setVisibility(View.VISIBLE);
                                layout_bundle_details.setVisibility(View.GONE);
                                btn_schedule.setVisibility(View.VISIBLE);
                                String[] outputStrArr = new String[schedule.size()];
                                String[] outputStrArrqty = new String[scheduleqty.size()];
                                for (int i = 0; i < schedule.size(); i++) {
                                    outputStrArr[i] = schedule.get(i);
                                    outputStrArrqty[i] = scheduleqty.get(i);
                                }

                                String[] outputStrArrOptional = new String[optionalid.size()];
                                String[] outputStrArrqtyOptional = new String[optionalqty.size()];
                                for (int i = 0; i < optionalid.size(); i++) {
                                    outputStrArrOptional[i] = optionalid.get(i);
                                    outputStrArrqtyOptional[i] = optionalqty.get(i);
                                }

                                try {
                                    String name = edtbundlename.getText().toString();
                                    String price = edtprice.getText().toString();
                                    String priceoptional = edtpriceoptinal.getText().toString();
                                    String qtybundle = edtquantity.getText().toString();
                                    String days = edtdays.getText().toString();
                                    String fmaily = edtfamily.getText().toString();


                                    if (name.trim().equals("") || price.trim().equals("") || fmaily.trim().equals("") || days.trim().equals("") || qtybundle.trim().equals("") ||  spinnerservingtime.getSelectedItem().toString().equals("Select Serving Time")) {
                                        Toast.makeText(rootView.getContext(), "Fill in all details and picture...", Toast.LENGTH_LONG).show();
                                    } else {

                                            String   command = "insert into [Bundle]([name],[price],[priceoptional],[quantity],[image],[userid],[days],[family],[ServingTime]) " +
                                                    "values ('" + name + "','" + Double.parseDouble(price) + "','" + Double.parseDouble(priceoptional) + "','" + Integer.parseInt(qtybundle) + "','" + encodedImage + "','" + activity.edthidenuserid.getText().toString() + "','" + days + "','" + fmaily + "','" + spinnerservingtime.getSelectedItem().toString() + "')";

                                    PreparedStatement preparedStatement = con.prepareStatement(command);
                                        preparedStatement.executeUpdate();

                                        String query1 = "select *  from [Bundle] where [id]in( select MAX([id]) as id  from [Bundle]);";
                                        PreparedStatement ps1 = con.prepareStatement(query1);
                                        ResultSet rs1 = ps1.executeQuery();
                                        rs1.next();
                                        id = Integer.parseInt(rs1.getString("id"));
                                        Log.d("ReminderService In", id +" EEE ");
                                        for (int i = 0; i < address.size(); i++) {
                                            try {
                                                Log.d("ReminderService In", id +" EEE "+address.get(i));
                                                String commands = "insert into [BundleDeliveryAddress] ([address],[bundleid])" +
                                                        "values ('" + address.get(i) + "','" + id + "')";
                                                PreparedStatement preStmt = con.prepareStatement(commands);
                                                preStmt.executeUpdate();
                                            } catch (Exception ex) {
                                                Log.d("ReminderService In", ex.getMessage().toString());
                                            }
                                        }

                                            //================


                                            for (int j = 0; j < outputStrArr.length; j++) {
                                               // for (int i = 0; i < mainorder.size(); i++) {
                                                String query = "select * from [UserProduct] where [id]='" + outputStrArr[j].toString() + "'";
                                                PreparedStatement ps = con.prepareStatement(query);
                                                ResultSet rs = ps.executeQuery();
                                                rs.next();
                                                Log.d("ReminderService In","%%%%%% "+outputStrArr[j].toString());
                                               // if (mainorder.get(j).equals("Yes")) {
                                                    qty = Integer.parseInt(outputStrArrqty[j].toString());
                                                    String fulldescription = qty + " X " + rs.getString("description").toString();
                                                    String commands = "insert into [BundleItems] ([description],[image],[bundleid])" +
                                                            "values ('" + fulldescription + "','" + rs.getString("image").toString() + "','" + id + "')";
                                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                                    preStmt.executeUpdate();

                                               // }
                                            }
                                            for (int j = 0; j < outputStrArrOptional.length; j++) {
                                                String query = "select * from [UserProduct] where [id]='" + outputStrArrOptional[j].toString() + "'";
                                                PreparedStatement ps = con.prepareStatement(query);
                                                ResultSet rs = ps.executeQuery();
                                                rs.next();

                                                    qty = Integer.parseInt(outputStrArrqtyOptional[j].toString());
                                                    String fulldescription = qty + " X " + rs.getString("description").toString();
                                                    String commands = "insert into [BundleItemsOptional] ([description],[image],[bundleid])" +
                                                            "values ('" + fulldescription + "','" + rs.getString("image").toString() + "','" + id + "')";
                                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                                    preStmt.executeUpdate();

                                            }




                                        Toast.makeText(rootView.getContext(), "Bundle created Successfully", Toast.LENGTH_LONG).show();
                                        Fragment frag = new HomeFragmentOrderPoint();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.mainFrame, frag).commit();
                                    }

                                } catch (Exception ex) {
                                    Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
                                }
                            }else{
                                Toast.makeText(rootView.getContext(), "Please Add at least one address", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    builder.show();
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }



            }
        });

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bundle.getString("extras").equals("")) {
                    id = bundle.getInt("id");
                    try{
                        Log.d("ReminderService In", "ADDING EXTRAS");

                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        builder.setTitle("Bundle Optional Items");
                        builder.setIcon(rootView.getResources().getDrawable(R.drawable.add));
                        final EditText input = new EditText(rootView.getContext());
                        input.setGravity(Gravity.CENTER);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setBackground(rootView.getResources().getDrawable(R.drawable.toasttext_bground));
                        input.setHint("Enter Optional Price");
                        input.setHintTextColor(Color.GRAY);
                        builder.setView(input);
                        builder.setMessage("Optional Price");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 m_Text_donate = input.getText().toString();
                                 try{
                                     String commands = "update [Bundle] set [priceoptional]='" + m_Text_donate + "' where [id]='" + id + "'";
                                     PreparedStatement preStmt = con.prepareStatement(commands);
                                     preStmt.executeUpdate();
                                 }catch (Exception ex){

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




                        String[] outputStrArrOptional = new String[optionalid.size()];
                        String[] outputStrArrqtyOptional = new String[optionalqty.size()];
                        for (int i = 0; i < optionalid.size(); i++) {
                            outputStrArrOptional[i] = optionalid.get(i);
                            outputStrArrqtyOptional[i] = optionalqty.get(i);
                        }

                        for (int i = 0; i < outputStrArrOptional.length; i++) {


                            String query = "select * from [UserProduct] where [id]='" + outputStrArrOptional[i].toString() + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            rs.next();

                            qty = Integer.parseInt(outputStrArrqtyOptional[i].toString());
                            String fulldescription = qty + " X " + rs.getString("description").toString();
                            String commands = "insert into [BundleItemsOptional] ([description],[image],[bundleid])" +
                                    "values ('" + fulldescription + "','" + rs.getString("image").toString() + "','" + id + "')";

                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }
                        Toast.makeText(rootView.getContext(), "Bundle Optional Items Added!!!", Toast.LENGTH_LONG).show();
                        Fragment frag = new HomeFragmentOrderPoint();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }catch (Exception ex){
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                } else {
                    btn_schedule.setVisibility(View.GONE);
                    layoutlist.setVisibility(View.GONE);
                    txtselect.setVisibility(View.GONE);
                    layout_bundle_details.setVisibility(View.VISIBLE);
                }

            }
        });

        bundleprofileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return rootView;
    }
    public void FillServingTimeData() {
        //==============Fill Data=
        try {

            String query = "select * from [ServingTime]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Serving Time");
            while (rs.next()) {
                category.add(rs.getString("ServingTime"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerservingtime.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }

    //===Upload profile
    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(rootView.getContext(), "You grant write external storage permission", Toast.LENGTH_LONG).show();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), "Reopen app and allow permission.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                    try {
                        ExifInterface ei = new ExifInterface(f.getAbsolutePath());
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap, 90);
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap, 90);
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap, 270);

                        }

                    } catch (IOException io) {

                    }


                    try {
                        bundleprofileImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        // encodedImage= byteArray.toString();


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = rootView.getContext().getApplicationContext().getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();



                try {
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), thumbnail.getHeight(), true);
                    ExifInterface ei = new ExifInterface(picturePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            thumbnail = rotateImage(thumbnail, 90);
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            thumbnail = rotateImage(thumbnail, 90);
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            thumbnail = rotateImage(thumbnail, 270);


                    }

                    bundleprofileImage.setImageBitmap(thumbnail);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // encodedImage= byteArray.toString();
                } catch (IOException io) {

                }




            }

        }

    }



    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(rootView.getContext(), "You've selected " + description.get(position) , Toast.LENGTH_SHORT).show();

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
                String query = "select top 25 * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
                if (!search.equals("")) {
                    query = "select * from [UserProduct] where [description] like '%" + search + "%'";
                }

                description.clear();
                proimage.clear();

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    description.add(rs.getString("description").toString());
                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        proimage.add(decodebitmap);
                    }


                }


                OrderPointBundleCreateAdapter adapter = new OrderPointBundleCreateAdapter(this.getActivity(), proimage, description);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub

                        try {

                            String selecteddescription = description.get(position);

                            String query = "select * from [UserProduct] where [description]='" + selecteddescription + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {

                              //  Toast.makeText(rootView.getContext(), selecteddescription, Toast.LENGTH_SHORT).show();
                            final String productId = rs.getString("id").toString();
                            Log.d("ReminderService In", productId);
                            if (schedule.contains(productId)) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    builder.setTitle("Product exist");
                                    builder.setMessage("Remove Product from Bundle?");
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
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }

                            } else {
                                Log.d("ReminderService In", "ELSE HERE"+bundle.getString("extras"));
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

                                    builder.setMessage("Add Product to Bundle?");
                                    if(!bundle.getString("extras").equals("")){
                                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                optionalid.add(productId);
                                                optionalqty.add(m_Text_donate);
                                                mainorder.add("No");
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                             dialog.cancel();
                                            }
                                        });
                                    }else{
                                        builder.setPositiveButton("Main Order", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                schedule.add(productId);
                                                scheduleqty.add(m_Text_donate);
                                                mainorder.add("Yes");
                                            }
                                        });
                                        builder.setNegativeButton("Optional", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text_donate = input.getText().toString();
                                                optionalid.add(productId);
                                                optionalqty.add(m_Text_donate);
                                                mainorder.add("No");
                                            }
                                        });
                                    }

                                    builder.show();


                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }
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


