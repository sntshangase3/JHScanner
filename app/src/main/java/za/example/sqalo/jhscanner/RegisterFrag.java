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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class RegisterFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ConnectionClass connectionClass;
    EditText edtappuserId, edtuserrole, edtfirstname,edtstreet,edtsuburb,edtcity,edtpostalcode,  edtemail, edtpassword, edtcontactno;
    ImageView edtprofileImage;
    Button btncreate, btnsave;
    Spinner spinnerbirthyear, spinnergender;

    ListView lstproduct;
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    Calendar c;
    //---------con--------

    int PLACE_PICKER_REQUEST = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String API_KEY = "AIzaSyA1RX5FgK6qKIuHOkQP6D40uEEaL5eLi0w";
    Calendar cal;
    int hour;
    int min = 0;
    int sec;
    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String encodedImage;
    TextView txtterms;
    CheckBox chkterms;
    String address ="";
    String lonlat="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.register, container, false);


        edtappuserId = (EditText) rootView.findViewById(R.id.edtappuserid);
        edtuserrole = (EditText) rootView.findViewById(R.id.edtuserrole);
        edtfirstname = (EditText) rootView.findViewById(R.id.edtfirstname);

        edtstreet = (EditText) rootView.findViewById(R.id.edtstreet); edtsuburb = (EditText) rootView.findViewById(R.id.edtsuburb);
        edtcity = (EditText) rootView.findViewById(R.id.edtcity);  edtpostalcode = (EditText) rootView.findViewById(R.id.edtpostalcode);


        edtemail = (EditText) rootView.findViewById(R.id.edtemail);
        edtpassword = (EditText) rootView.findViewById(R.id.edtpassword);
        edtcontactno = (EditText) rootView.findViewById(R.id.edtcontactno);


        txtterms = (TextView) rootView.findViewById(R.id.txtterms);

        spinnerbirthyear = (Spinner) rootView.findViewById(R.id.spinnerbirthyear);
        spinnergender = (Spinner) rootView.findViewById(R.id.spinnergender);

        edtappuserId.setVisibility(View.GONE);
        edtuserrole.setVisibility(View.GONE);

        btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);
        chkterms = (CheckBox) rootView.findViewById(R.id.chkterms);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.gender_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnergender.setAdapter(adapter);

        ArrayList<String> years = new ArrayList<String>();
        int thisyear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Select here");
        for (int i = 1938; i <= thisyear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, years);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerbirthyear.setAdapter(adapter1);


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";


        FillData();
        Bundle bundle = this.getArguments();
        try {
            if (bundle != null) {

                if (bundle.getString("termsread").toString().equals("termsread")) {
                    chkterms.setChecked(true);
                } else {
                    chkterms.setChecked(false);
                }

            }
        } catch (Exception ex) {

        }
        edtfirstname.addTextChangedListener(new TextWatcher() {

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

                try{
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                    if((s.length() != 0)&&!chkterms.isChecked()){
                        txtterms.setBackground(errorbg);
                        Toast.makeText(rootView.getContext(), "Please read terms,scroll up to the bottom", Toast.LENGTH_LONG).show();
                    } else{
                        txtterms.setBackground(bg);
                    }

                }catch (Exception ex){

                }

            }
        });
        edtemail.addTextChangedListener(new TextWatcher() {

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

                try{
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                    if((s.length() != 0) && (emailValidator(edtemail.getText().toString()))){
                        edtemail.setBackground(bg);
                    } else{
                        edtemail.setBackground(errorbg);
                    }
                }catch (Exception ex){

                }

            }
        });

        txtterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new TermsFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        chkterms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Fragment frag = new TermsFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, frag).commit();
                } else {
                    Toast.makeText(rootView.getContext(), "Please read terms,scroll up to the bottom", Toast.LENGTH_LONG).show();
                }
            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkterms.isChecked()) {
                    CreateProfile addPro = new CreateProfile();
                    addPro.execute("");
                } else {
                    Toast.makeText(rootView.getContext(), "Please read terms,scroll up to the bottom", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });
        edtprofileImage.setOnClickListener(new View.OnClickListener() {

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

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

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
                        edtprofileImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


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


                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, 100, 100, true);
                try {
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

                } catch (IOException io) {

                }


                edtprofileImage.setImageBitmap(thumbnail);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }

        }

    }

    //======
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void FillData() {
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


                if (activity.edthidenuserid.getText().toString().equals("")) {
                    edtuserrole.setText("2");
                    // btnsave.setVisibility(View.GONE);
                    // btncreate.setVisibility(View.VISIBLE);
                } else {
                    edtappuserId.setText(activity.edthidenuserid.getText().toString());
                    edtuserrole.setText("2");
//btncreate.setVisibility(View.GONE);
                    // btnsave.setVisibility(View.VISIBLE);
                    String query = "select * from [AppUser] where [id]=" + Integer.parseInt(edtappuserId.getText().toString());
                 PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();


                    if (rs.getRow() != 0) {

                        edtfirstname.setText(rs.getString("firstname"));

                        address = rs.getString("location").trim();
                        edtstreet.setText(address.substring(0,address.indexOf(',')));
                        address=address.substring(address.indexOf(',')+1).trim();
                        edtsuburb.setText(address.substring(0,address.indexOf(',')));
                        address=address.substring(address.indexOf(',')+1).trim();
                        edtcity.setText(address.substring(0,address.indexOf(',')));
                        address=address.substring(address.indexOf(',')+1).trim();
                        edtpostalcode.setText(address.substring(0,address.indexOf(',')));


                        edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtcontactno.setText(rs.getString("contact"));

                        edtuserrole.setText(rs.getString("userRole"));

                        if (rs.getString("image") != null) {
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            edtprofileImage.setImageBitmap(decodebitmap);
                            encodedImage = rs.getString("image");
                        } else {

                            edtprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                        }


                        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.gender_arrays, R.layout.spinner_item);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnergender.setAdapter(adapter);
                        spinnergender.setSelection(adapter.getPosition(rs.getString("gender")));

                        ArrayList<String> years = new ArrayList<String>();
                        int thisyear = Calendar.getInstance().get(Calendar.YEAR);
                        years.add("Select here");
                        for (int i = 1918; i <= thisyear; i++) {
                            years.add(Integer.toString(i));
                        }
                        ArrayAdapter adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, years);
                        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerbirthyear.setAdapter(adapter1);
                        spinnerbirthyear.setSelection(adapter1.getPosition(rs.getString("birthyear")));
                        // btncreate.setVisibility(View.GONE);
                    }
                }


            }


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String firstname = edtfirstname.getText().toString();

        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();


        String email = edtemail.getText().toString();
        String password = edtpassword.getText().toString();
        String contact = edtcontactno.getText().toString();

        int userrole = Integer.parseInt(edtuserrole.getText().toString());

        // int birthyear =Integer.parseInt( spinnerbirthyear.getSelectedItem().toString());
        // String gender = spinnergender.getSelectedItem().toString();
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                CharSequence text = z;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();

                Intent i = new Intent(rootView.getContext(), MainActivity.class);
                startActivity(i);

            } else {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edtfirstname.getText().toString().trim().equals(""))) {
                    edtfirstname.setBackground(errorbg);
                } else {
                    edtfirstname.setBackground(bg);
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

                if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                if ((edtpassword.getText().toString().trim().equals(""))) {
                    edtpassword.setBackground(errorbg);
                } else {
                    edtpassword.setBackground(bg);
                }

                if ((edtcontactno.getText().toString().trim().equals(""))) {
                    edtcontactno.setBackground(errorbg);
                } else {
                    edtcontactno.setBackground(bg);
                }

                if ((spinnerbirthyear.getSelectedItem().toString().trim().equals("Select here"))) {
                    spinnerbirthyear.setBackground(errorbg);
                } else {
                    spinnerbirthyear.setBackground(bg);
                }

                if ((spinnergender.getSelectedItem().toString().trim().equals("Select here"))) {
                    spinnergender.setBackground(errorbg);
                } else {
                    spinnergender.setBackground(bg);
                }
                if ((z.equals("Email Address Already Exist"))) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_LONG).show();
            }

        }



        @Override
        protected String doInBackground(String... params) {
            if (firstname.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals("") || email.trim().equals("") || password.trim().equals("") || contact.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {

                        if (!emailValidator(email)) {
                            z = "InValid Email Address";
                        }else{
                            String query1 = "select * from [AppUser] where [email]='" + email+"'";
                            PreparedStatement ps = con.prepareStatement(query1);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            if (rs.getRow() != 0) {
                               z = "Email Address Already Exist";
                           }else{
                               address = street+", "+suburb+", "+city+", "+postal+", South Africa";
                               LatLon(address);
                               if(!lonlat.equals("")){
                                   String query = "insert into [AppUser]([firstname],[location],[email],[password],[contact],[provice],[birthyear],[gender],[userRole],[image]) " +
                                           "values ('" + firstname + "','" + address + "','" + email + "','" + password + "','" + contact + "','" + lonlat + "','" + Integer.parseInt(spinnerbirthyear.getSelectedItem().toString()) + "','" + spinnergender.getSelectedItem().toString() + "','" + userrole + "','" + encodedImage + "')";
                                   PreparedStatement preparedStatement = con.prepareStatement(query);
                                   preparedStatement.executeUpdate();
                                   z = "Profile Created,please Login";
                                   isSuccess = true;
                           }


                            }


                        }


                    }
                } catch (Exception ex) {
                    isSuccess = false;
                   // z = "Check your network connection!!";
                     z=ex.getMessage();
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
            return z;
        }
    }

    //===================

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String firstname = edtfirstname.getText().toString();

        String street = edtstreet.getText().toString();
        String suburb = edtsuburb.getText().toString();
        String city = edtcity.getText().toString();
        String postal = edtpostalcode.getText().toString();

        String email = edtemail.getText().toString();
        String password = edtpassword.getText().toString();
        String contact = edtcontactno.getText().toString();

        int userrole = Integer.parseInt(edtuserrole.getText().toString());
        int birthyear = Integer.parseInt(spinnerbirthyear.getSelectedItem().toString());
        String gender = spinnergender.getSelectedItem().toString();

        int userid = Integer.parseInt(edtappuserId.getText().toString());


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {



                if (isSuccess == true) {
                    CharSequence text = z;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                    toast.show();
                } else {
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                    if ((edtfirstname.getText().toString().trim().equals(""))) {
                        edtfirstname.setBackground(errorbg);
                    } else {
                        edtfirstname.setBackground(bg);
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

                    if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                        edtemail.setBackground(errorbg);
                    } else {
                        edtemail.setBackground(bg);
                    }

                    if ((edtpassword.getText().toString().trim().equals(""))) {
                        edtpassword.setBackground(errorbg);
                    } else {
                        edtpassword.setBackground(bg);
                    }

                    if ((edtcontactno.getText().toString().trim().equals(""))) {
                        edtcontactno.setBackground(errorbg);
                    } else {
                        edtcontactno.setBackground(bg);
                    }

                    if ((spinnerbirthyear.getSelectedItem().toString().trim().equals("Select here"))) {
                        spinnerbirthyear.setBackground(errorbg);
                    } else {
                        spinnerbirthyear.setBackground(bg);
                    }

                    if ((spinnergender.getSelectedItem().toString().trim().equals("Select here"))) {
                        spinnergender.setBackground(errorbg);
                    } else {
                        spinnergender.setBackground(bg);
                    }


                    Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
                }


        }

        @Override
        protected String doInBackground(String... params) {
            if (firstname.trim().equals("")|| street.trim().equals("")|| suburb.trim().equals("")|| city.trim().equals("")|| postal.trim().equals("") || email.trim().equals("") || password.trim().equals("") || contact.trim().equals(""))
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
                        if(!lonlat.equals("")){
                            String query = "Update [AppUser] set [firstname]='" + firstname + "',[location]='" + address + "' ,[email]='" + email + "',[password]='" + password + "',[contact]='" + contact + "',[provice]='" + lonlat + "' ,[birthyear]='" + birthyear + "',[image]='" + encodedImage + "' ,[gender]='" + gender + "' where [id]=" + userid;
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Updated Successfully";
                            isSuccess = true;
                        }

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    // z = "Exceptions";
                    z = "Check your network connection!!";
                    //  z=ex.getMessage();
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

}



