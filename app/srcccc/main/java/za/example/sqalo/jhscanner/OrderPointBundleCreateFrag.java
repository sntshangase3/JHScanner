package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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



    ImageButton search;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    Button btn_schedule,btn_create,btn_update;
    EditText edtsearch,edtbundlename,edtquantity,edtprice;

    byte[] byteArray;
    String encodedImage;
    LinearLayout layout_bundle_details,layoutlist;

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
        edtbundlename = (EditText) rootView.findViewById(R.id.edtbundlename);
        bundleprofileImage = (ImageView) rootView.findViewById(R.id.bundleprofileImage);

        search = (ImageButton) rootView.findViewById(R.id.search);
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


        bundle = this.getArguments();
        FillDataOrderByStatus(search_product);

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

        try {
            if (bundle != null) {
                if (!bundle.getString("name").equals("")) {
                    Log.d("ReminderService In", bundle.getString("name") + "######");
                    edtsearch.setText(bundle.getString("name"));
                    search_product = edtsearch.getText().toString();
                    FillDataOrderByStatus(search_product);
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
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutlist.setVisibility(View.VISIBLE);
                layout_bundle_details.setVisibility(View.GONE);
                btn_schedule.setVisibility(View.VISIBLE);
                String[] outputStrArr = new String[schedule.size()];
                String[] outputStrArrqty = new String[scheduleqty.size()];
                for (int i = 0; i < schedule.size(); i++) {
                    outputStrArr[i] = schedule.get(i);
                    outputStrArrqty[i] = scheduleqty.get(i);
                }


                try {
                    String name = edtbundlename.getText().toString();
                    String price = edtprice.getText().toString();
                    String qtybundle = edtquantity.getText().toString();

                    if (name.trim().equals("")  || price.trim().equals("") || qtybundle.trim().equals("")|| encodedImage.trim().equals("")){
                        Toast.makeText(rootView.getContext(), "Please fill in all details...", Toast.LENGTH_LONG).show();
                    }
                    else {
                        String command = "insert into [Bundle]([name],[price],[quantity],[image],[userid]) " +
                                "values ('" + name + "','" + Double.parseDouble(price) + "','" + Integer.parseInt(qtybundle) + "','" + encodedImage + "','" + activity.edthidenuserid.getText().toString() + "')";
                        PreparedStatement preparedStatement = con.prepareStatement(command);
                        preparedStatement.executeUpdate();

                        String query1 = "select *  from [Bundle] where [id]in( select MAX([id]) as id  from [Bundle]);";
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        id = Integer.parseInt(rs1.getString("id"));

                        for (int i = 0; i < outputStrArr.length; i++) {
                            //================

                            Date today = new Date();
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            String todaydate = date_format.format(today);

                            String query = "select * from [UserProduct] where [id]='" + outputStrArr[i].toString() + "'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            rs.next();

                            qty = Integer.parseInt(outputStrArrqty[i].toString());
                            String fulldescription=qty+" X "+rs.getString("description").toString();
                            String commands = "insert into [BundleItems] ([description],[image],[bundleid])" +
                                    "values ('" +fulldescription + "','" + rs.getString("image").toString() + "','" + id + "')";
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();

//------------------
                            //Update [UserProduct] qty and total
                            //#########No longer in needed
                                           /* String commands1 = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + outputStrArr[i].toString() + "'";
                                            PreparedStatement preStmt1 = con.prepareStatement(commands1);
                                            preStmt1.executeUpdate();*/
                            //Update [AppUserAssistance] isdaypasscode no
                                        /*
                                        String commands1 = "update [AppUserAssistance] set [isdaypasscodesent]='No' where [assistancename]='" + spinnerassistant.getSelectedItem().toString()+ "'";
                                        PreparedStatement preStmt1 = con.prepareStatement(commands1);
                                        preStmt1.executeUpdate();

                                        productID=outputStrArr[i].toString();
                                        //Check if need notifications as R.O.P
                                        if (diff <= Integer.valueOf(rs.getString("reorderpoint").toString()) && !rs.getString("reorderpoint").toString().trim().equals("0")) {
                                            //trigger notification
                                            //**********
                                            try {
                                                String notificationbody = "You have " + String.valueOf(diff) + " " + rs.getString("name").toString() + " left";

                                                //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                                NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                                Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                                PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                builderd.setContentIntent(contentIntent);
                                                builderd.setAutoCancel(false);
                                                // Add as notification
                                                builderd.build();

                                                NotificationManager manager = (NotificationManager) rootView.getContext().getSystemService(NOTIFICATION_SERVICE);

                                                Notification myNotication;

                                                myNotication = builderd.getNotification();
                                                myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                                myNotication.defaults |= Notification.DEFAULT_SOUND;
                                                myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                                manager.notify(0, myNotication);
                                            } catch (Exception ex) {
                                                Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                                            }
                                            //**********
                                        }


                                        //--------
                                      */


                        }
                        Toast.makeText(rootView.getContext(), "Bundle created Successfully", Toast.LENGTH_LONG).show();
                        Fragment frag = new HomePremiumFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, frag).commit();
                    }
                } catch (Exception ex) {
                    Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
                }


            }
        });

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_schedule.setVisibility(View.GONE);
                layoutlist.setVisibility(View.GONE);
                layout_bundle_details.setVisibility(View.VISIBLE);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

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
                String query = "select top 20 * from [UserProduct] where [userid]=" + activity.edthidenuserid.getText().toString();
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


