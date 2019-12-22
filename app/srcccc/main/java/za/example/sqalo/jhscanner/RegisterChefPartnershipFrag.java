package za.example.sqalo.jhscanner;

import android.app.AlertDialog;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;





/**
 * Created by sibusison on 2017/07/30.
 */
public class RegisterChefPartnershipFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;

    ListView lstdish;
    String currentid;
    ImageView productimage;
    TextView txtTheKitchen, txtselect,txtdishnumber,edtpremiumstatus;
    int total = 0;
    String ingredientProductID ="";
    String ingredientProductDescription="";
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();

  //  Bundle bundles = new Bundle();

    ArrayAdapter adapter1;
    EditText edtuserid, edtname,edtstreet,edtsuburb,edtcity,edtpostalcode, edttelephone, edtwebsite, edtabout, edtnamedish, edtkeyingredient, edtrecipe,edtpreptime,edtcost;
    CheckBox chkpremium,chkcookmealorders, chkmealkitorders, chkprivatechefservices, chkcateringservices, chkrecipedesigns;
    ImageView dishImage,b2;
    Button btnsignup, btndelete;
    ImageButton btnupdate, btnadddish;


    byte[] byteArray;
    String encodedImage;
    ArrayList<String> services = new ArrayList<String>();
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle=new Bundle();
    LinearLayout layoutdishlist,layout_prem;

    int PLACE_PICKER_REQUEST = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String API_KEY = "AIzaSyA1RX5FgK6qKIuHOkQP6D40uEEaL5eLi0w";
    String lonlat="";
    String signedupdishcheck="";
    String address="";
String isActive="0";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

     try {

            rootView = inflater.inflate(R.layout.register_chef_service_partnership, container, false);

            layoutdishlist = (LinearLayout) rootView.findViewById(R.id.dishlist);
         layout_prem = (LinearLayout) rootView.findViewById(R.id.layout_prem);

            lstdish = (ListView) rootView.findViewById(R.id.lstdish);

         edtpremiumstatus = (TextView) rootView.findViewById(R.id.edtpremiumstatus);
            edtuserid = (EditText) rootView.findViewById(R.id.edtuserid);
            edtname = (EditText) rootView.findViewById(R.id.edtname);

         edtstreet = (EditText) rootView.findViewById(R.id.edtstreet); edtsuburb = (EditText) rootView.findViewById(R.id.edtsuburb);
         edtcity = (EditText) rootView.findViewById(R.id.edtcity);  edtpostalcode = (EditText) rootView.findViewById(R.id.edtpostalcode);


            edttelephone = (EditText) rootView.findViewById(R.id.edttelephone);
            edtwebsite = (EditText) rootView.findViewById(R.id.edtwebsite);
            edtabout = (EditText) rootView.findViewById(R.id.edtaboutyou);

            edtnamedish = (EditText) rootView.findViewById(R.id.edtnamedish);
            edtkeyingredient = (EditText) rootView.findViewById(R.id.edtkeyingredients);
            edtrecipe = (EditText) rootView.findViewById(R.id.edtrecipe);
         edtpreptime = (EditText) rootView.findViewById(R.id.edtpreptime);
         edtcost = (EditText) rootView.findViewById(R.id.edtcost);

         chkpremium = (CheckBox) rootView.findViewById(R.id.chkpremium);
            chkcookmealorders = (CheckBox) rootView.findViewById(R.id.chkcookmealorders);
            chkmealkitorders = (CheckBox) rootView.findViewById(R.id.chkmealkitorders);
            chkcateringservices = (CheckBox) rootView.findViewById(R.id.chkcateringservices);
            chkrecipedesigns = (CheckBox) rootView.findViewById(R.id.chkrecipedesigns);
            chkprivatechefservices = (CheckBox) rootView.findViewById(R.id.chkprivatechefservices);
         txtselect = (TextView) rootView.findViewById(R.id.txtseehow);
            dishImage = (ImageView) rootView.findViewById(R.id.dishprofileImage);

            btnadddish = (ImageButton) rootView.findViewById(R.id.btn_addish);

            btnsignup = (Button) rootView.findViewById(R.id.btn_signup);
            btnupdate = (ImageButton) rootView.findViewById(R.id.btn_update);
            btndelete = (Button) rootView.findViewById(R.id.btn_delete);

         txtdishnumber = (TextView) rootView.findViewById(R.id.txtdishnumber);
         b2 = (ImageView) rootView.findViewById(R.id.b2);
           // edtrecipe.setHint("e.g. 1. Put flour,salt and baking powder into a bowl and mix.\n2. Stir in the sugar \n 3. Pour in the warm mil");

            fragmentManager = getFragmentManager();
            // Declaring Server ip, username, database name and password
            ip = "winsqls01.cpt.wa.co.za";
            db = "JHShopper";
            un = "sqaloits";
            pass = "422q5mfQzU";

            edtuserid.setText(activity.edthidenuserid.getText().toString());

            FillDataUser();
            try{
                FillDataDish();
            }catch (Exception ex){

            }

            bundle = this.getArguments();


         try {
             if (bundle != null) {
                 if (!bundle.getStringArray("outputStrArr").equals("")) {
                    String[] outputStrArr = bundle.getStringArray("outputStrArr");
                     String[] outputStrArr1 = bundle.getStringArray("outputStrArr1");
                    ingredientProductID = Arrays.toString(outputStrArr).replace("[","").replace("]","");
                     ingredientProductDescription = Arrays.toString(outputStrArr1).replace("[","").replace("]","");

                     edtkeyingredient.setText(ingredientProductDescription);

                     edtname.setText(bundle.getString("chefname"));
                     edtstreet.setText(bundle.getString("street"));
                     edtsuburb.setText(bundle.getString("suburb"));
                     edtcity.setText(bundle.getString("city"));
                     edtpostalcode.setText(bundle.getString("postal"));

                     edttelephone.setText(bundle.getString("telephone"));
                     edtwebsite.setText(bundle.getString("website"));
                     edtabout.setText(bundle.getString("about"));

                     edtnamedish.setText(bundle.getString("namedish"));
                     byte[] decodeString = Base64.decode(bundle.getString("encodedImage"), Base64.DEFAULT);
                     Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                     dishImage.setImageBitmap(decodebitmap);
                     encodedImage=bundle.getString("encodedImage");
                 }
             }

         } catch (Exception ex) {

         }

         txtselect.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(chkpremium.isChecked()){
                     chkpremium.setChecked(false);
                 }else{
                     chkpremium.setChecked(true);
                 }
                 /*Fragment frag = new TermsFrag();
                 FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.beginTransaction()
                         .replace(R.id.mainFrame, frag).commit();*/


             }
         });
            edtname.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    try {
                        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                        if ((s.length() != 0)) {
                            layoutdishlist.setVisibility(View.VISIBLE);

                        } else {
                            layoutdishlist.setVisibility(View.GONE);
                        }

                    } catch (Exception ex) {

                    }

                }
            });
         edtkeyingredient.addTextChangedListener(new TextWatcher() {

             @Override
             public void afterTextChanged(Editable s) {

             }

             @Override
             public void beforeTextChanged(CharSequence s, int start,
                                           int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start,
                                       int before, int count) {

                 try {

                     if ((s.length() != 0)) {

                         if(ingredientProductID.equals("")){
                             AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                             builder.setTitle("Ingredients");
                             builder.setMessage("Please Select Ingredient?");
                             builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));

                             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     Fragment frag = new SearchListIngredientFrag();
                                     String name = edtname.getText().toString();
                                     String street = edtstreet.getText().toString();
                                     String suburb = edtsuburb.getText().toString();
                                     String city = edtcity.getText().toString();
                                     String postal = edtpostalcode.getText().toString();


                                     String telephone = edttelephone.getText().toString();
                                     String website = edtwebsite.getText().toString();
                                     String about = edtabout.getText().toString();
                                     String namedish = edtnamedish.getText().toString();
                                     Bundle bundle = new Bundle();
                                     bundle.putString("chefname", name); bundle.putString("street",street );
                                     bundle.putString("suburb",suburb ); bundle.putString("city",city );
                                     bundle.putString("postal",postal ); bundle.putString("telephone",telephone );
                                     bundle.putString("website",website ); bundle.putString("about", about);
                                     bundle.putString("namedish", namedish);bundle.putString("encodedImage",encodedImage);

                                     frag.setArguments(bundle);
                                     FragmentManager fragmentManager = getFragmentManager();
                                     fragmentManager.beginTransaction()
                                             .replace(R.id.mainFrame, frag).commit();

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

                     }

                 } catch (Exception ex) {

                 }

             }
         });
         b2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Toast.makeText(rootView.getContext(), "Loading Please Wait...", Toast.LENGTH_LONG).show();
                 Fragment frag = new ChefDishesFrag();
                 Bundle bundle = new Bundle();
                 bundle.putString("useridchef", activity.edthidenuserid.getText().toString());
                 frag.setArguments(bundle);
                 FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.beginTransaction()
                         .replace(R.id.mainFrame, frag).commit();


             }
         });
         txtdishnumber.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(rootView.getContext(), "Loading Please Wait...", Toast.LENGTH_LONG).show();
                 Fragment frag = new ChefDishesFrag();
                 Bundle bundle = new Bundle();
                 bundle.putString("useridchef", activity.edthidenuserid.getText().toString());
                 frag.setArguments(bundle);
                 FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.beginTransaction()
                         .replace(R.id.mainFrame, frag).commit();


             }
         });
            btnadddish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddDish addDish = new AddDish();
                    addDish.execute("");


                }
            });
            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(signedupdishcheck.equals("yes")){
                        ChefSignUp chefSignUp = new ChefSignUp();
                        chefSignUp.execute("");
                    }else{
                        Toast.makeText(rootView.getContext(), "Add At least one Dish!!", Toast.LENGTH_LONG).show();
                    }

                }
            });
            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateChefProfile updateChefProfile = new UpdateChefProfile();
                    updateChefProfile.execute("");
                }
            });
            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteChefProfile deleteChefProfile = new DeleteChefProfile();
                    deleteChefProfile.execute("");
                }
            });


            chkcookmealorders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        services.add("Cook Meal Orders");
                    } else {
                        services.remove("Cook Meal Orders");
                    }
                }
            });
            chkmealkitorders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        services.add("Meal Kit Orders");
                    } else {
                        services.remove("Meal Kit Orders");
                    }
                }
            });
            chkcateringservices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        services.add("Catering Services");
                    } else {
                        services.remove("Catering Services");
                    }
                }
            });
            chkrecipedesigns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        services.add("Recipe Designs");
                    } else {
                        services.remove("Recipe Designs");
                    }
                }
            });
            chkprivatechefservices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        services.add("Private Chef Services");
                    } else {
                        services.remove("Private Chef Services");
                    }
                }
            });

            dishImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });


        } catch (Exception ex) {

           // System.exit(0);
       }


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
                        dishImage.setImageBitmap(bitmap);
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

                    dishImage.setImageBitmap(thumbnail);
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

        // spinnercategory.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();

        //FillDataOrderBy(spinnercategory.getSelectedItemPosition()+1);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    public void FillDataUser() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                //AppUser already Chef
                String query = "select * from [Chef] where [userid]='" + activity.edthidenuserid.getText().toString() + "' order by [id] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (rs.getRow() != 0) {


                    Date deactivation_date = null;
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                    String todaydate = date_format.format(today);
                    try {
                        deactivation_date = new Date(date_format.parse(rs.getString("deactivation_date").toString()).getTime());
                        today = new Date(date_format.parse(todaydate).getTime());


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long daysleft = getDateDiff(today, deactivation_date, TimeUnit.DAYS);

                    if(Integer.parseInt(rs.getString("isActive").toString())==1){
                         chkpremium.setChecked(true);
                        edtpremiumstatus.setText(String.valueOf(daysleft)+" Days Left");
                        layout_prem.setVisibility(View.VISIBLE);
                    }else {
                        layout_prem.setVisibility(View.GONE);
                    }

                    btnsignup.setEnabled(false);
                    edtuserid.setText(rs.getString("userid"));
                    edtname.setText(rs.getString("name"));

                  address = rs.getString("address").trim();
                    edtstreet.setText(address.substring(0,address.indexOf(',')));
                    address=address.substring(address.indexOf(',')+1).trim();
                    edtsuburb.setText(address.substring(0,address.indexOf(',')));
                    address=address.substring(address.indexOf(',')+1).trim();
                    edtcity.setText(address.substring(0,address.indexOf(',')));
                    address=address.substring(address.indexOf(',')+1).trim();
                    edtpostalcode.setText(address.substring(0,address.indexOf(',')));


                    lonlat=rs.getString("lonlat");
                    edttelephone.setText(rs.getString("telephone"));
                    edtwebsite.setText(rs.getString("website"));
                    edtabout.setText(rs.getString("about"));
                    isActive=rs.getString("isActive");


                    if (rs.getString("services").contains("Cook Meal Orders")) {
                        chkcookmealorders.setChecked(true);
                        services.add("Meal Kit Orders");
                    }
                    if (rs.getString("services").contains("Meal Kit Orders")) {
                        chkmealkitorders.setChecked(true);
                        services.add("Meal Kit Orders");
                    }
                    if (rs.getString("services").contains("Catering Services")) {
                        chkcateringservices.setChecked(true);
                        services.add("Catering Services");
                    }
                    if (rs.getString("services").contains("Recipe Designs")) {
                        chkrecipedesigns.setChecked(true);
                        services.add("Recipe Designs");
                    }
                    if (rs.getString("services").contains("Private Chef Services")) {
                        chkprivatechefservices.setChecked(true);
                        services.add("Private Chef Services");
                    }

                }


            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here", Toast.LENGTH_LONG).show();
        }

    }

    public void FillDataDish() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String query = "select * from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' order by [id] asc";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                name.clear();
                keyingredient.clear();
                recipe.clear();
                proimage.clear();
                total = 0;
                Log.d("ReminderService In", query);
                while (rs.next()) {
                    Log.d("ReminderService In", "####IN");
                    name.add(rs.getString("name").toString());
                    keyingredient.add(rs.getString("keyingredient").toString());
                    recipe.add(rs.getString("recipe").toString());

                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    proimage.add(decodebitmap);
                    total = total + 1;
                    signedupdishcheck="yes";
                }
                txtdishnumber.setText(String.valueOf(total));
                if (total >= 10) {
                    if(isActive.equals("0")){
                        btnadddish.setVisibility(View.GONE);
                    }

                } else {
                    btnadddish.setVisibility(View.VISIBLE);
                }
                DishListAdapter adapter = new DishListAdapter(this.getActivity(), proimage, name, keyingredient, recipe);
                lstdish.setAdapter(adapter);
                lstdish.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub


                        try {
                            final String selectedname = name.get(position);
                            final String selectedkeyingredient = keyingredient.get(position);
                            final String selectedrecipe = recipe.get(position);
                            String query1 = "select top 1 * from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [name]='" + selectedname + "'  and [keyingredient]='" + selectedkeyingredient + "'  and [recipe]='" + selectedrecipe + "' order by [id] asc";
                            PreparedStatement ps = con.prepareStatement(query1);
                            final ResultSet rs = ps.executeQuery();
                            rs.next();
                            try {

                                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                TextView title = new TextView(rootView.getContext());
                                title.setPadding(10, 10, 10, 10);
                                title.setText("Remove");
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(getResources().getColor(R.color.colorPrimary));
                                builder.setCustomTitle(title);
                                builder.setMessage("Remove Dish " + selectedname + " ?");
                                builder.setIcon(rootView.getResources().getDrawable(R.drawable.removedish));
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            //Get top 1 if details are the same


                                            String query = "delete from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [id]='" + rs.getString("id").toString() + "'";
                                            PreparedStatement preparedStatement = con.prepareStatement(query);
                                            preparedStatement.executeUpdate();
                                            FillDataDish();
                                            //=========
                                            try {
                                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                                //String to = "jabun@ngobeniholdings.co.za";
                                                // String to = "sntshangase3@gmail.com";
                                                // m.setFrom("Info@ngobeniholdings.co.za");
                                                // String to = "SibusisoN@sqaloitsolutions.co.za";
                                                String to = "SibusisoN@sqaloitsolutions.co.za";
                                                String from = "info@goingdots.com";
                                                String subject = "Item removed";
                                                String message = "Dear Sibusiso\nDish removed" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                                                String[] toArr = {to};
                                                m.setTo(toArr);
                                                m.setFrom(from);
                                                m.setSubject(subject);
                                                m.setBody(message);

                                                m.send();


                                            } catch (Exception e) {


                                            }
                                            //==========
                                        } catch (Exception ex) {
                                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here3", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();


                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here2", Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception ex) {
                            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here1", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here0", Toast.LENGTH_LONG).show();
        }
//===========

    }

    public class ChefSignUp extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String name = edtname.getText().toString();
        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();


        String telephone = edttelephone.getText().toString();
        String website = edtwebsite.getText().toString();
        String about = edtabout.getText().toString();
        String service = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();


                Fragment frag = new HomePremiumFragment();
                Bundle bundle = new Bundle();
                bundle.putString("couser", "couser");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            } else {
                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);

                if ((edtname.getText().toString().trim().equals(""))) {
                    edtname.setBackground(errorbg);
                } else {
                    edtname.setBackground(bg);
                }

                if ((edtstreet.getText().toString().trim().equals(""))) {
                    edtstreet.setBackground(errorbg);
                } else {
                    edtstreet.setBackground(bg);
                }
                if ((edtsuburb.getText().toString().trim().equals(""))) {
                    edtsuburb.setBackground(errorbg);
                } else {
                    edtsuburb.setBackground(bg);
                }
                if ((edtcity.getText().toString().trim().equals(""))) {
                    edtcity.setBackground(errorbg);
                } else {
                    edtcity.setBackground(bg);
                }
                if ((edtpostalcode.getText().toString().trim().equals(""))) {
                    edtpostalcode.setBackground(errorbg);
                } else {
                    edtpostalcode.setBackground(bg);
                }





                if ((edttelephone.getText().toString().trim().equals(""))) {
                    edttelephone.setBackground(errorbg);
                } else {
                    edttelephone.setBackground(bg);
                }

                if ((edtabout.getText().toString().trim().equals(""))) {
                    edtabout.setBackground(errorbg);
                } else {
                    edtabout.setBackground(bg);
                }
                if (service.trim().equals("")) {
                    chkcookmealorders.setBackground(errorbg);
                    chkcateringservices.setBackground(errorbg);
                    chkmealkitorders.setBackground(errorbg);
                    chkprivatechefservices.setBackground(errorbg);
                    chkrecipedesigns.setBackground(errorbg);
                } else {
                    chkcookmealorders.setBackground(bg);
                    chkcateringservices.setBackground(bg);
                    chkmealkitorders.setBackground(bg);
                    chkprivatechefservices.setBackground(bg);
                    chkrecipedesigns.setBackground(bg);
                }


            }

        }

        @Override
        protected String doInBackground(String... params) {

            for (String temp : services) {
                service = service + temp;
            }
            if (name.trim().equals("")  || telephone.trim().equals("") || about.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals(""))
                z = "Please fill in all details...";
            else {
                if (service.trim().equals("")) {
                    z = "Tick Service...";
                } else {
                    try {

                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        if (con == null) {
                            z = "Check your network connection!!";
                        } else {

                                    address = street+", "+suburb+", "+city+", "+postal+", South Africa";
                                    LatLon(address);
                                    if(website.equals("")){
                                        website="http://www.goingdots.com/";
                                    }
                            int isActive=0;
                            Date expiry = new Date();
                            Date today = new Date();
                            String todaydate="";String expdate="";

                            if(chkpremium.isChecked()){
                                isActive=1;

                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                todaydate = date_format.format(today);
                                try {

                                    today = new Date(date_format.parse(todaydate).getTime());

                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(today);
                                    cal.add(Calendar.YEAR, 1);
                                    expiry = cal.getTime();
                                    expdate=date_format.format(expiry);
                                    Log.d("ReminderService In", todaydate+"  "+expdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                isActive=0;

                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                todaydate = date_format.format(today);
                                try {

                                    today = new Date(date_format.parse(todaydate).getTime());

                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(today);
                                    cal.add(Calendar.YEAR, 1);
                                    expiry = cal.getTime();
                                    expdate=date_format.format(expiry);
                                    Log.d("ReminderService In", todaydate+"  "+expdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                                if(!lonlat.equals("")){
                                    String query = "insert into [Chef]([name],[address],[lonlat],[telephone],[website],[about],[services],[userid],[isActive],[activation_date],[deactivation_date]) " +
                                            "values ('" + name + "','" + address + "','" + lonlat + "','" + telephone + "','" + website + "','" + about + "','" + service + "','" + activity.edthidenuserid.getText().toString() + "','" + isActive + "','" + todaydate + "','" + expdate + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query);
                                    preparedStatement.executeUpdate();
                                    z = "Signed In Successfully";
                                    isSuccess = true;
                                }

                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        //  z = "Check your network connection!!";
                        z= ex.getMessage().toString();
                        edtabout.setText(z);
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }

            }
            return z;
        }
    }

    public class AddDish extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;

        int userid = Integer.parseInt(activity.edthidenuserid.getText().toString());
        String namedish = edtnamedish.getText().toString();
        String keyingredient = edtkeyingredient.getText().toString();
        String recipe = edtrecipe.getText().toString();
        String preptime = edtpreptime.getText().toString();
        String cost = edtcost.getText().toString();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                FillDataDish();
                layoutdishlist.setVisibility(View.VISIBLE);
                edtnamedish.setText("");
                edtkeyingredient.setText("");
                edtrecipe.setText("");
                edtpreptime.setText("");
                edtcost.setText("");
                encodedImage = "";
                dishImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));
                signedupdishcheck="yes";


            } else {
                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edtnamedish.getText().toString().trim().equals(""))) {
                    edtnamedish.setBackground(errorbg);
                } else {
                    edtnamedish.setBackground(bg);
                }

                if ((edtkeyingredient.getText().toString().trim().equals(""))) {
                    edtkeyingredient.setBackground(errorbg);
                } else {
                    edtkeyingredient.setBackground(bg);
                }

                if ((edtpreptime.getText().toString().trim().equals(""))) {
                    edtpreptime.setBackground(errorbg);
                } else {
                    edtpreptime.setBackground(bg);
                }

                if ((edtcost.getText().toString().trim().equals(""))) {
                    edtcost.setBackground(errorbg);
                } else {
                    edtcost.setBackground(bg);
                }


            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (namedish.trim().equals("") || keyingredient.trim().equals("") || encodedImage.trim().equals("")|| preptime.trim().equals("")|| cost.trim().equals("")) {

                    if(encodedImage.trim().equals("")){
                        z = "Upload Dish Photo";
                    }else{
                        z = "Please fill in required details...";
                    }
                } else {
                    try {
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        if (con == null) {
                            z = "Check your network connection!!";
                        } else {

                            if(recipe.trim().equals("")){
                                recipe="Not Specified";
                            }
                            if(!cost.contains("R")){
                                cost="R"+cost;
                            }

                            String query = "insert into [Dish]([name],[image],[keyingredient],[recipe],[preptime],[cost],[userid],[ingredientProductID]) " +
                                    "values ('" + namedish + "','" + encodedImage + "','" + keyingredient + "','" + recipe + "','" + preptime + "','" + cost + "','" + activity.edthidenuserid.getText().toString() + "','" + ingredientProductID + "')";
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Dish Added Successfully";
                            isSuccess = true;
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Check your network connection!!";

                       // z = ex.getMessage();
                    }
                }

            } catch (Exception ex) {
                z = "Upload Dish Photo";
//                Toast.makeText(rootView.getContext(), "Upload Dish Image", Toast.LENGTH_LONG).show();
            }

            return z;
        }
    }

    public class UpdateChefProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;



        String name = edtname.getText().toString();

        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();

        String telephone = edttelephone.getText().toString();
        String website = edtwebsite.getText().toString();
        String about = edtabout.getText().toString();
        String service = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();

                Fragment frag = new HomePremiumFragment();
                Bundle bundle = new Bundle();
                bundle.putString("couser", "couser");
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();

            } else {
               // Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                edtabout.setText("Too long text");
                edtabout.setBackground(errorbg);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            for (String temp : services) {
                service = service + temp;
            }
            if (name.trim().equals("")  || telephone.trim().equals("") || about.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals(""))
                z = "Please fill in all details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {

                                address = street+", "+suburb+", "+city+", "+postal+", South Africa";
                                LatLon(address);
                        if(website.equals("")){
                            website="http://www.goingdots.com";
                        }
                        int isActive=0;
                        Date expiry = new Date();
                        Date today = new Date();
                        String todaydate="";String expdate="";

                        if(chkpremium.isChecked()){
                            isActive=1;

                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                           todaydate = date_format.format(today);
                            try {

                                today = new Date(date_format.parse(todaydate).getTime());

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(today);
                                cal.add(Calendar.YEAR, 1);
                                expiry = cal.getTime();
                                expdate=date_format.format(expiry);
                                Log.d("ReminderService In", todaydate+"  "+expdate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            isActive=0;

                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            todaydate = date_format.format(today);
                            try {

                                today = new Date(date_format.parse(todaydate).getTime());

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(today);
                                cal.add(Calendar.YEAR, 1);
                                expiry = cal.getTime();
                                expdate=date_format.format(expiry);
                                Log.d("ReminderService In", todaydate+"  "+expdate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if(!lonlat.equals("")){
                            String query = "Update [Chef] set [name]='" + name + "',[address]='" + address + "',[lonlat]='" + lonlat + "',[telephone]='" + telephone + "',[website]='" + website + "',[about]='" + about + "',[services]='" + service + "' ,[isActive]='" + isActive + "',[activation_date]='" + todaydate + "',[deactivation_date]='" + expdate + "'  where [userid]=" + activity.edthidenuserid.getText().toString();
                            query.replace("'"," ");
                           Log.d("Check Query", query);
                            //=========
                            try {
                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                //String to = "jabun@ngobeniholdings.co.za";
                                // String to = "sntshangase3@gmail.com";
                                // m.setFrom("Info@ngobeniholdings.co.za");
                                // String to = "SibusisoN@sqaloitsolutions.co.za";
                                String to = "SibusisoN@sqaloitsolutions.co.za";
                                String from = "info@goingdots.com";
                                String subject = "Testing";
                                String message = "Dear \nChef updating \n" +query+ "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                                String[] toArr = {to};
                                m.setTo(toArr);
                                m.setFrom(from);
                                m.setSubject(subject);
                                m.setBody(message);

                                m.send();


                            } catch (Exception e) {

                                Log.d("Check Query", e.getMessage());
                            }
                            //==========
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Updated Successfully";
                            isSuccess = true;
                        }


                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    //  z = "Check your network connection!!";
                    z= ex.getMessage().toString();
                    //=========
                    try {
                        Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                        //String to = "jabun@ngobeniholdings.co.za";
                        // String to = "sntshangase3@gmail.com";
                        // m.setFrom("Info@ngobeniholdings.co.za");
                        // String to = "SibusisoN@sqaloitsolutions.co.za";
                        String to = "SibusisoN@sqaloitsolutions.co.za";
                        String from = "info@goingdots.com";
                        String subject = "Testing";
                        String message = "Dear \nChef updating \n" +z+ "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                        String[] toArr = {to};
                        m.setTo(toArr);
                        m.setFrom(from);
                        m.setSubject(subject);
                        m.setBody(message);

                        m.send();


                    } catch (Exception e) {


                    }
                    //==========
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
            return z;
        }
    }
    public void LatLon(String address){
        try{
            Geocoder geocoder;
            geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
            double Lat = geocoder.getFromLocationName(address, 1).get(0).getLatitude();
            double Lon = geocoder.getFromLocationName(address, 1).get(0).getLongitude();
            lonlat="lat/lng: ("+String.valueOf(Lat)+","+String.valueOf(Lon)+")";
            Log.d("Check LatLon", lonlat);

        }catch (Exception e){
            Toast.makeText(rootView.getContext(), "Slow network connection,wait...!!", Toast.LENGTH_LONG).show();
        }

    }

    public class DeleteChefProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;



        String name = edtname.getText().toString();

        String telephone = edttelephone.getText().toString();
        String website = edtwebsite.getText().toString();
        String about = edtabout.getText().toString();
        String service = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                    Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();
                    Fragment frag = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    frag.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);
                if (con == null) {
                    z = "Check your network connection!!";
                } else {

                    String query = "delete from [Chef]  where [userid]=" + activity.edthidenuserid.getText().toString();
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    String query1 = "delete from [Dish]  where [userid]=" + activity.edthidenuserid.getText().toString();
                    PreparedStatement preparedStatement1 = con.prepareStatement(query1);
                    preparedStatement1.executeUpdate();

                    z = "Profile Deleted Successfully";

                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                // z = "Exceptions";
                z = "Check your network connection!!";
                //z = ex.getMessage();
            }

            return z;

        }
    }
}


