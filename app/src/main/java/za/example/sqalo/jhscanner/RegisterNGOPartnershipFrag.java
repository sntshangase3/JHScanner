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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class RegisterNGOPartnershipFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;

    ListView lstdish;
    String currentid;
    ImageView productimage;
    TextView txtTheKitchen, txtselect;
    double total = 0;
    int catergoryclick = 0;
    ArrayList<Bitmap> proimage = new ArrayList<Bitmap>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> keyingredient = new ArrayList<String>();
    ArrayList<String> recipe = new ArrayList<String>();

    ArrayList<String> category_acceptable = new ArrayList<String>();
    Bundle bundles = new Bundle();

    ArrayAdapter adapter1;
    EditText edtuserid,edtregno, edtname,edtstreet,edtsuburb,edtcity,edtpostalcode, edttelephone, edtwebsite,edtcontactperson, edtabout,edtemail,edtacceptable;

    ImageView profileImage,logoImage;
    Button btnsignup, btndelete,btn_category;
    ImageButton btnupdate;


    byte[] byteArray;
    String encodedImage1,encodedImage2;


    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    LinearLayout layoutdishlist;

    int PLACE_PICKER_REQUEST = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String API_KEY = "AIzaSyA1RX5FgK6qKIuHOkQP6D40uEEaL5eLi0w";
    String lonlat="";
    String signedupdishcheck="";
    String address="";

    Spinner spinnercategory;

    String checkifpremiumuser="No";
    String imagetype="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

     try {

            rootView = inflater.inflate(R.layout.register_ngo_service_partnership, container, false);



            edtuserid = (EditText) rootView.findViewById(R.id.edtuserid);
         edtregno = (EditText) rootView.findViewById(R.id.edtregno);
            edtname = (EditText) rootView.findViewById(R.id.edtname);
         edtemail = (EditText) rootView.findViewById(R.id.edtemail);

         edtstreet = (EditText) rootView.findViewById(R.id.edtstreet); edtsuburb = (EditText) rootView.findViewById(R.id.edtsuburb);
         edtcity = (EditText) rootView.findViewById(R.id.edtcity);  edtpostalcode = (EditText) rootView.findViewById(R.id.edtpostalcode);


            edttelephone = (EditText) rootView.findViewById(R.id.edttelephone);
            edtwebsite = (EditText) rootView.findViewById(R.id.edtwebsite);
         edtcontactperson = (EditText) rootView.findViewById(R.id.edtcontactperson);
            edtabout = (EditText) rootView.findViewById(R.id.edtaboutyou);
         edtacceptable = (EditText) rootView.findViewById(R.id.edtacceptable);



         profileImage = (ImageView) rootView.findViewById(R.id.profileImage);
         logoImage = (ImageView) rootView.findViewById(R.id.logoImage);



            btnsignup = (Button) rootView.findViewById(R.id.btn_signup);
            btnupdate = (ImageButton) rootView.findViewById(R.id.btn_update);
            btndelete = (Button) rootView.findViewById(R.id.btn_delete);
         btn_category = (Button) rootView.findViewById(R.id.btn_category);
           // edtrecipe.setHint("e.g. 1. Put flour,salt and baking powder into a bowl and mix.\n2. Stir in the sugar \n 3. Pour in the warm mil");

            fragmentManager = getFragmentManager();
         spinnercategory = (Spinner) rootView. findViewById(R.id.spinnercategory);
         // Declaring Server ip, username, database name and password
         ip = "winsqls01.cpt.wa.co.za";
         db = "JHShopper";
         un = "sqaloits";
         pass = "422q5mfQzU";
         //startTimer();

         FillCategoryData();
         spinnercategory.setOnItemSelectedListener(this);

         btn_category.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 catergoryclick = 1;
                 spinnercategory.performClick();

             }
         });

            edtuserid.setText(activity.edthidenuserid.getText().toString());

            FillDataUser();


            bundle = this.getArguments();





            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        NgoSignUp chefSignUp = new NgoSignUp();
                        chefSignUp.execute("");


                }
            });
            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateNgoProfile updateChefProfile = new UpdateNgoProfile();
                    updateChefProfile.execute("");
                }
            });
            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteNgoProfile deleteChefProfile = new DeleteNgoProfile();
                    deleteChefProfile.execute("");
                }
            });


         profileImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    imagetype="profile";
                    selectImage();
                }
            });
        logoImage.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 imagetype="logo";
                 selectImage();
             }
         });

        } catch (Exception ex) {
         Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here", Toast.LENGTH_LONG).show();
         Log.d("ReminderService In", ex.getMessage().toString());
           // System.exit(0);
       }


        return rootView;
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
             Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
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
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), 500, true);

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
                        if(imagetype.equals("profile")){
                            profileImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byteArray = stream.toByteArray();
                            encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }else{
                            logoImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byteArray = stream.toByteArray();
                            encodedImage2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }


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
                    thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), 500, true);
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


                    if(imagetype.equals("profile")){
                        profileImage.setImageBitmap(thumbnail);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }else{
                        logoImage.setImageBitmap(thumbnail);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        encodedImage2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }

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

        spinnercategory.setSelection(position);
         //Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();
        if(catergoryclick ==1){
            if (category_acceptable.size() != 5) {
                if (category_acceptable.contains(spinnercategory.getSelectedItem().toString())) {
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        builder.setTitle("Category exist");
                        builder.setMessage("Remove Category from acceptable donation?");
                        builder.setIcon(rootView.getResources().getDrawable(R.drawable.remove));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // m_Text_donate = input.getText().toString();
                                category_acceptable.remove(spinnercategory.getSelectedItem().toString());



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
                        builder.setTitle("New Category");
                        builder.setIcon(rootView.getResources().getDrawable(R.drawable.add));
                        builder.setMessage("Add Category to acceptable donation?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // m_Text_donate = input.getText().toString();
                                category_acceptable.add(spinnercategory.getSelectedItem().toString());
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

            } else {
                Toast.makeText(rootView.getContext(), "Maximum of 5 items reached!!!", Toast.LENGTH_LONG).show();
            }
        }


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
                String query = "select * from [Ngo] where [userid]=" + Integer.parseInt(edtuserid.getText().toString());
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (rs.getRow() != 0) {
                    btnsignup.setEnabled(false);
                    edtuserid.setText(rs.getString("userid"));
                    edtregno.setText(rs.getString("regno"));
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
                    edtcontactperson.setText(rs.getString("contactperson"));
                    edtabout.setText(rs.getString("about"));
                    edtemail.setText(rs.getString("email"));
                    edtacceptable.setText(rs.getString("categoryacceptable"));
                    if (rs.getString("imageprofile") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("imageprofile"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        profileImage.setImageBitmap(decodebitmap);
                        encodedImage1=rs.getString("imageprofile");
                    } else {
                        profileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));
                    }
                    if (rs.getString("imagelogo") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("imagelogo"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        logoImage.setImageBitmap(decodebitmap);
                        encodedImage2=rs.getString("imagelogo");
                    } else {
                        logoImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));
                    }



                }


            }


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here", Toast.LENGTH_LONG).show();
        }

    }



    public class NgoSignUp extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;

        int userid = Integer.parseInt(edtuserid.getText().toString());
        String regno = edtregno.getText().toString();
        String name = edtname.getText().toString();
        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();


        String telephone = edttelephone.getText().toString();
        String website = edtwebsite.getText().toString();
        String contactperson = edtcontactperson.getText().toString();
        String about = edtabout.getText().toString();
        String email = edtemail.getText().toString();

        String service = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();

                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    //String to = "jabun@ngobeniholdings.co.za";
                    // String to = "sntshangase3@gmail.com";
                    // m.setFrom("Info@ngobeniholdings.co.za");
                    // String to = "SibusisoN@sqaloitsolutions.co.za";
                    String to = "SibusisoN@sqaloitsolutions.co.za";
                    String from = "info@goingdots.com";
                    String subject = "Ngo Signed Up";
                    String message = "Dear Sibusiso\nNew NGO signed up" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);

                    m.send();


                } catch (Exception e) {


                }
                //==========
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
                if ((edtregno.getText().toString().trim().equals(""))) {
                    edtregno.setBackground(errorbg);
                } else {
                    edtregno.setBackground(bg);
                }
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

                if ((edtcontactperson.getText().toString().trim().equals(""))) {
                    edtcontactperson.setBackground(errorbg);
                } else {
                    edtcontactperson.setBackground(bg);
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
                    spinnercategory.setBackground(errorbg);

                } else {
                    spinnercategory.setBackground(bg);

                }


            }

        }

        @Override
        protected String doInBackground(String... params) {

            for (String temp : category_acceptable) {
                service = service + temp;
            }
            if (regno.trim().equals("")  ||name.trim().equals("")  || telephone.trim().equals("") || contactperson.trim().equals("")|| about.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals(""))
                z = "Please fill in all details...";
            else {
                if (service.trim().equals("")) {
                    z = "Select acceptable Category...";
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

                                if(!lonlat.equals("")){
                                    String query = "insert into [Ngo]([regno],[name],[address],[lonlat],[telephone],[website],[contactperson],[about],[categoryacceptable],[imageprofile],[imagelogo],[userid],[email]) " +
                                            "values ('" + regno + "','" + name + "','" + address + "','"+lonlat+"','" + telephone + "','" + website + "','" + contactperson + "','" + about + "','" + service + "','" + encodedImage1 + "','" + encodedImage2 + "','" + userid + "','" + email + "')";
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
                        edtcity.setText(z);
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }

            }
            return z;
        }






















































    }



    public class UpdateNgoProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        int userid = Integer.parseInt(edtuserid.getText().toString());
        String regno = edtregno.getText().toString();
        String name = edtname.getText().toString();
        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();


        String telephone = edttelephone.getText().toString();
        String website = edtwebsite.getText().toString();
        String contactperson = edtcontactperson.getText().toString();
        String about = edtabout.getText().toString();
        String acceptable=edtcontactperson.getText().toString();
        String email = edtemail.getText().toString();



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
                edtabout.setText("Too long text");
                edtabout.setBackground(errorbg);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            if (regno.trim().equals("")  ||name.trim().equals("") ||acceptable.trim().equals("")  || telephone.trim().equals("") || contactperson.trim().equals("")|| about.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals(""))
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

                        if(!lonlat.equals("")){
                            String query = "Update [Ngo] set [regno]='" + regno + "',[name]='" + name + "',[address]='" + address + "',[lonlat]='" + lonlat + "' ,[telephone]='" + telephone + "',[website]='" + website + "',[contactperson]='" + contactperson + "',[about]='" + about + "',[email]='" + email + "',[categoryacceptable]='" + acceptable + "',[imageprofile]='" + encodedImage1 + "' ,[imagelogo]='" + encodedImage2 + "' where [userid]=" + userid;
                            query.replace("'"," ");

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
                                String message = "Dear \nNgo updating \n" +query+ "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                                String[] toArr = {to};
                                m.setTo(toArr);
                                m.setFrom(from);
                                m.setSubject(subject);
                                m.setBody(message);

                                m.send();


                            } catch (Exception e) {


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
                        String message = "Dear \nNgo updating \n" +z+ "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

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

        }catch (Exception e){
            Toast.makeText(rootView.getContext(), "Slow network connection,wait...!!", Toast.LENGTH_LONG).show();
        }

    }

    public class DeleteNgoProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        int userid = Integer.parseInt(edtuserid.getText().toString());





        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {
                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    //String to = "jabun@ngobeniholdings.co.za";
                    // String to = "sntshangase3@gmail.com";
                    // m.setFrom("Info@ngobeniholdings.co.za");
                    // String to = "SibusisoN@sqaloitsolutions.co.za";
                    String to = "SibusisoN@sqaloitsolutions.co.za";
                    String from = "info@goingdots.com";
                    String subject = "Ngo deleted";
                    String message = "Dear Sibusiso\nNgo removed profile" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);

                    m.send();


                } catch (Exception e) {


                }
                //==========
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

                    String query = "delete from [Ngo]  where [userid]=" + userid;
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

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


