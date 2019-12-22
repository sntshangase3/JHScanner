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
import android.graphics.drawable.Drawable;
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
import android.view.MotionEvent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class RegisterPremiumFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ConnectionClass connectionClass;
    EditText edtassistanceid, edtcomanagerid, edtfamilymembers, edtcomanagername, edtcomanagerdaypasscode, edtcomanagercontactno, edtassistancename, edtassistancedaypasscode, edtassistancecontactno;
    ImageView comanagerprofileImage, assistanceprofileImage,familyprofileImage,addmember,updatemember;
    Button btncreate ;
ImageButton btnupdate;

    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String encodedImage,encodedImage1="";
    String comanageremail;
    String assistancepassword;
    Spinner spinnermember,spinnerallery,spinnerintolerant;
    ArrayAdapter adapter,adapter1,adapter2;
String option="",familyprofileImageClick="";
    String allergy="", intolerant="";
    EditText edtmembername,edtallergy,edtintolerant;
    LinearLayout layoutallergymembers;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.register_premium, container, false);

        edtassistanceid = (EditText) rootView.findViewById(R.id.edtassistanceid);
        edtcomanagerid = (EditText) rootView.findViewById(R.id.edtcomanagerid);
        edtfamilymembers = (EditText) rootView.findViewById(R.id.edtfamilymembers);
        edtcomanagername = (EditText) rootView.findViewById(R.id.edtcomanagername);
        edtcomanagerdaypasscode = (EditText) rootView.findViewById(R.id.edtcomanagerdaypasscode);
        edtcomanagercontactno = (EditText) rootView.findViewById(R.id.edtcomanagercontactno);
        edtassistancename = (EditText) rootView.findViewById(R.id.edtassistancename);
        edtassistancedaypasscode = (EditText) rootView.findViewById(R.id.edtassistancedaypasscode);
        edtassistancecontactno = (EditText) rootView.findViewById(R.id.edtassistancecontactno);

        edtmembername = (EditText) rootView.findViewById(R.id.edtmembername);
        edtallergy = (EditText) rootView.findViewById(R.id.edtallergy);
        edtintolerant = (EditText) rootView.findViewById(R.id.edtintolerant);

        layoutallergymembers = (LinearLayout) rootView.findViewById(R.id.layoutallergymembers);

        edtassistanceid.setVisibility(View.GONE);
        edtcomanagerid.setVisibility(View.GONE);

        btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnupdate = (ImageButton) rootView.findViewById(R.id.btn_update);
        comanagerprofileImage = (ImageView) rootView.findViewById(R.id.comanagerprofileImage);
        assistanceprofileImage = (ImageView) rootView.findViewById(R.id.assistanceprofileImage);
        familyprofileImage = (ImageView) rootView.findViewById(R.id.familyprofileImage);

        addmember = (ImageView) rootView.findViewById(R.id.addmember);
        updatemember = (ImageView) rootView.findViewById(R.id.updatemember);

        spinnermember = (Spinner) rootView. findViewById(R.id.spinnermember);
        spinnerallery = (Spinner) rootView. findViewById(R.id.spinnerallery);
        spinnerintolerant = (Spinner) rootView. findViewById(R.id.spinnerintolerant);

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

        spinnermember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
              if(!spinnermember.getSelectedItem().toString().equals("Select Member")){
                  FillAllergiesMemberData(spinnermember.getSelectedItem().toString());
                  btncreate.setVisibility(View.INVISIBLE);
                  btnupdate.setVisibility(View.INVISIBLE);
                  layoutallergymembers.setVisibility(View.VISIBLE);
              }else{
                  edtmembername.setText("");
                  edtallergy.setText("");
                  edtintolerant.setText("");
                  familyprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

              }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinnerallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
               // item.toString()
                Log.d("ReminderService In", spinnerallery.getSelectedItem().toString());

                allergy=allergy+spinnerallery.getSelectedItem().toString()+",";
                allergy = allergy.replace("Select Allergies,", "");
                edtallergy.setText(allergy);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        spinnerintolerant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                Log.d("ReminderService In", spinnerintolerant.getSelectedItem().toString());

                intolerant=intolerant+spinnerintolerant.getSelectedItem().toString()+",";
                intolerant = intolerant.replace("Select Intolerance,", "");
                edtintolerant.setText(intolerant);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });




        FillData();
        FillAllergiesMemberData();
        FillAllergiesData();
        FillIntolData();


        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    CreatePremiumProfile addPro = new CreatePremiumProfile();
                    addPro.execute("");



            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });

        assistanceprofileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);

                if ((edtmembername.getText().toString().trim().equals(""))) {
                    edtmembername.setBackground(errorbg);
                } else if ((spinnerallery.getSelectedItem().toString().trim().equals("Select Allergy"))) {
                    spinnerallery.setBackground(errorbg);
                } else if ((spinnerintolerant.getSelectedItem().toString().trim().equals("Select Intolerance"))) {
                    spinnerintolerant.setBackground(errorbg);
                }else {

                    try {

                        allergy = allergy.replace("Select Allergies,", "");
                        intolerant = intolerant.replace("Select Intolerance,", "");
                        Log.d("ReminderService In", "Value " + allergy + " " + intolerant);
                        String command = "insert into [AllergyMember]([name],[image],[allergy],[intolerate],[userid]) " +
                                "values ('" + edtmembername.getText().toString() + "','" + encodedImage1 + "','" + allergy.substring(0, allergy.length() - 1) + "','" + intolerant.substring(0, intolerant.length() - 1) + "','" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "')";

                        Log.d("ReminderService In", "An error occurred: " + command);
                        PreparedStatement preparedStatement = con.prepareStatement(command);
                        preparedStatement.executeUpdate();
                        Toast.makeText(rootView.getContext(), "Member Added Successfully", Toast.LENGTH_LONG).show();

                    } catch (Exception ex) {
                        Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
                    }
                }
            }

        });

        updatemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);

                if ((edtmembername.getText().toString().trim().equals(""))) {
                    edtmembername.setBackground(errorbg);
                }else if ((spinnermember.getSelectedItem().toString().trim().equals("Select Member"))) {
                    spinnermember.setBackground(errorbg);
                    addmember.setBackground(errorbg);
                } else {

                    try {

                        Log.d("ReminderService In", "Value " + allergy+" "+intolerant);
                        String command = "update [AllergyMember] set [name]='" + edtmembername.getText().toString() + "',[image]='" +encodedImage1 + "',[allergy]='" + edtallergy.getText().toString().substring(0,edtallergy.getText().toString().length()-1) + "'," +
                                "[intolerate]='" + edtintolerant.getText().toString().substring(0,edtintolerant.getText().toString().length()-1) + "'" +
                                " where [userid]='" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "' and [name]='" +spinnermember.getSelectedItem() + "'";

                        Log.d("ReminderService In", "An error occurred: " + command);
                        PreparedStatement preparedStatement = con.prepareStatement(command);
                        preparedStatement.executeUpdate();
                        Toast.makeText(rootView.getContext(), "Member Updated Successfully", Toast.LENGTH_LONG).show();

                    } catch (Exception ex) {
                        Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
                    }
                }

            }
        });

        familyprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    familyprofileImageClick="familyprofileImage";
                    selectImage();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }



            }
        });

        edtmembername.addTextChangedListener(new TextWatcher() {

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
                    if((s.length() != 0)){
                        btncreate.setVisibility(View.INVISIBLE);
                        btnupdate.setVisibility(View.INVISIBLE);
                    } else{
                        btncreate.setVisibility(View.VISIBLE);
                        btnupdate.setVisibility(View.VISIBLE);
                    }
                }catch (Exception ex){

                }

            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

       /* Log.d("ReminderService In", option);
if(option.equals("intolerant")){
    intolerant=intolerant+spinnerintolerant.getSelectedItem().toString()+",";
}else if(option.equals("allergy")){
    allergy=allergy+spinnerallery.getSelectedItem().toString()+",";
}*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void FillAllergiesMemberData() {
        //==============Fill Data=
        try {

            String query = "select [name] from [AllergyMember] where [userid]="+Integer.parseInt(activity.edthidenuserid.getText().toString());
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Member");
            while (rs.next()) {
                category.add(rs.getString("name"));
            }
            adapter2 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnermember.setAdapter(adapter2);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public void FillAllergiesMemberData(String name) {
        //==============Fill Data=
        try {

            String query = "select * from [AllergyMember] where [name]='"+name+"' and [userid]="+Integer.parseInt(activity.edthidenuserid.getText().toString());
            Log.d("ReminderService In", query);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Member");

            while (rs.next()) {
                if(rs.getRow()!=0){
                    edtmembername.setText(rs.getString("name"));
                    edtallergy.setText(rs.getString("allergy"));
                    edtintolerant.setText(rs.getString("intolerate"));
                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        familyprofileImage.setImageBitmap(decodebitmap);
                        encodedImage1=rs.getString("image");
                    } else {
                        familyprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));
                    }
                }else{

                }

            }


        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public void FillAllergiesData() {
        //==============Fill Data=
        try {

            String query = "select * from [Allergies]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Allergies");
            while (rs.next()) {
                category.add(rs.getString("Allergies"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerallery.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public void FillIntolData() {
        //==============Fill Data=
        try {

            String query = "select * from [Intolerance]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Intolerance");
            while (rs.next()) {
                category.add(rs.getString("Intolerance"));
            }
            adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerintolerant.setAdapter(adapter1);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    //===Upload profile
    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        encodedImage="";

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
                        if(!edtmembername.getText().toString().equals("")){
                            familyprofileImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();
                            encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }else{
                            assistanceprofileImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();
                            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }



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
                thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), thumbnail.getHeight(), true);
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



                if(!edtmembername.getText().toString().equals("")){
                    familyprofileImage.setImageBitmap(thumbnail);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }else{
                    assistanceprofileImage.setImageBitmap(thumbnail);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

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

                String query = "select * from [AppUser] where [id]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                edtcomanagerid.setText(rs.getString("id"));
                edtcomanagername.setText(rs.getString("firstname"));
                edtcomanagercontactno.setText(rs.getString("contact"));
                comanageremail=rs.getString("email");

                if (rs.getString("image") != null) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    comanagerprofileImage.setImageBitmap(decodebitmap);
                } else {
                    comanagerprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                }
                //Check if AppUser info already subcribes and populates accordingly
                String query1 = "select * from [AppUserAssistance] where [userid]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();
                //generate next assistance password (assist+rowcount)
                assistancepassword ="a"+String.valueOf(rs1.getRow());
                 if (rs1.getRow() != 0) {                //
                     edtassistanceid.setText(rs1.getString("id"));
                     edtfamilymembers.setText(rs1.getString("familymembers"));
                     edtcomanagerdaypasscode.setText(rs1.getString("comanagerdaypasscode"));
                     edtassistancename.setText(rs1.getString("assistancename"));
                     edtassistancedaypasscode.setText(rs1.getString("assistancedaypasscode"));
                     edtassistancecontactno.setText(rs1.getString("assistancecontactno"));
                     if (rs1.getString("image") != null) {
                         byte[] decodeString = Base64.decode(rs1.getString("image"), Base64.DEFAULT);
                         Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                         assistanceprofileImage.setImageBitmap(decodebitmap);
                         encodedImage=rs1.getString("image");
                     } else {
                         assistanceprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));
                     }

                }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }

    public class CreatePremiumProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;

        int comanagerid = Integer.parseInt(edtcomanagerid.getText().toString());
        String familymembers = edtfamilymembers.getText().toString();
        String comanagername = edtcomanagername.getText().toString();
        String comanagerdaypasscode = edtcomanagerdaypasscode.getText().toString();
        String comanagercontactno = edtcomanagercontactno.getText().toString();

        String assistancename = edtassistancename.getText().toString();
        String assistancedaypasscode = edtassistancedaypasscode.getText().toString();
        String assistancecontactno = edtassistancecontactno.getText().toString();

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
                Toast.makeText(rootView.getContext(), "Assistant login sent to email", Toast.LENGTH_LONG).show();

                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                    //String to = "jabun@ngobeniholdings.co.za";
                    // String to = "sntshangase3@gmail.com";
                    // m.setFrom("Info@ngobeniholdings.co.za");
                    // String to = "SibusisoN@sqaloitsolutions.co.za";
                    String to = comanageremail;
                    String from = "info@goingdots.com";
                    String subject = "Assistance Login";
                    String message = "Dear Co-User \n Your assistant Login:\nEmail: "+to+"\nPassword: "+assistancepassword+"\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

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
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edtfamilymembers.getText().toString().trim().equals(""))) {
                    edtfamilymembers.setBackground(errorbg);
                } else {
                    edtfamilymembers.setBackground(bg);
                }

                if ((edtcomanagerdaypasscode.getText().toString().trim().equals(""))) {
                    edtcomanagerdaypasscode.setBackground(errorbg);
                } else {
                    edtcomanagerdaypasscode.setBackground(bg);
                }

                if ((edtassistancename.getText().toString().trim().equals(""))) {
                    edtassistancename.setBackground(errorbg);
                } else {
                    edtassistancename.setBackground(bg);
                }

                if ((edtassistancedaypasscode.getText().toString().trim().equals(""))) {
                    edtassistancedaypasscode.setBackground(errorbg);
                } else {
                    edtassistancedaypasscode.setBackground(bg);
                }
                if ((edtassistancecontactno.getText().toString().trim().equals(""))) {
                    edtassistancecontactno.setBackground(errorbg);
                } else {
                    edtassistancecontactno.setBackground(bg);
                }




            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (familymembers.trim().equals("") || comanagerdaypasscode.trim().equals("") || assistancename.trim().equals("") || assistancedaypasscode.trim().equals("")|| assistancecontactno.trim().equals(""))
                z = "Please fill in all assistance details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {


                        String query = "insert into [AppUserAssistance]([familymembers],[comanagerdaypasscode],[assistancename],[assistancedaypasscode],[assistancecontactno],[email],[password],[image],[isdaypasscodesent],[userid]) " +
                                "values ('" + familymembers + "','" + comanagerdaypasscode + "','" + assistancename + "','" + assistancedaypasscode + "','" + assistancecontactno + "','" + comanageremail + "','" + assistancepassword + "','" + encodedImage + "','Yes','" + comanagerid + "')";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Subcribed to Premium Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    // z=ex.getMessage();
                }
            }
            return z;
        }
    }

    //===================

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        int assistanceid = Integer.parseInt(edtassistanceid.getText().toString());
        String familymembers = edtfamilymembers.getText().toString();
        String comanagername = edtcomanagername.getText().toString();
        String comanagerdaypasscode = edtcomanagerdaypasscode.getText().toString();
        String comanagercontactno = edtcomanagercontactno.getText().toString();

        String assistancename = edtassistancename.getText().toString();
        String assistancedaypasscode = edtassistancedaypasscode.getText().toString();
        String assistancecontactno = edtassistancecontactno.getText().toString();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {
                if (isSuccess == true) {
                    CharSequence text = z;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                    toast.show();
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (familymembers.trim().equals("") || comanagerdaypasscode.trim().equals("") || assistancename.trim().equals("") || assistancedaypasscode.trim().equals("")|| assistancecontactno.trim().equals(""))
                z = "Please fill in all details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {

                        String query = "Update [AppUserAssistance] set [familymembers]='" + familymembers + "',[comanagerdaypasscode]='" + comanagerdaypasscode + "' ,[assistancename]='" + assistancename + "',[assistancedaypasscode]='" + assistancedaypasscode + "',[assistancecontactno]='" + assistancecontactno + "',[image]='" + encodedImage + "'  where [id]=" + assistanceid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                        z = "Updated Successfully";

                        isSuccess = true;
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


    //==================

}



